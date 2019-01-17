package org.big.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.big.common.JDBCTools;
import org.big.repository.TaxonHasTaxtreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author ZXY
 *
 */
@Service
public class CodeFactoryServiceImpl implements CodeFactoryService {
	@Autowired
	private TaxonHasTaxtreeRepository taxonHasTaxtreeRepository;
	final String mysqlDriver = "com.mysql.jdbc.Driver";
	final String tableSchema = "biodata";

	public List<String> findAllTable() {
		return taxonHasTaxtreeRepository.findAllTable(tableSchema);
	}

	@Override
	public String batchExecuteCode(String[] tableNames) {
		StringBuffer codeStr = new StringBuffer();
		for (String tableName : tableNames) {
			List<Object[]> list = taxonHasTaxtreeRepository.findColumnByTable(tableName, tableSchema);
			String codes = createMethods(list);
			codeStr.append("\n" + codes);
		}
		return codeStr.toString();
	}

	private String createMethods(List<Object[]> list) {
		if (list == null || list.size() <= 0) {
			return null;
		}
		Object tableName = list.get(0)[0];
		StringBuffer methodInsert = new StringBuffer();
		methodInsert.append("public void insert" + tableName + "() throws SQLException{\n");
		methodInsert.append("	Connection connection = null;\n");
		methodInsert.append("	PreparedStatement pstmt = null;\n");
		methodInsert.append("	try {\n");
		methodInsert.append("		// 连接数据库,字段个数"+list.size()+"\n");
		methodInsert.append("		connection=连接数据库;\n");
		StringBuffer columnNames = new StringBuffer();// 字段名称
		StringBuffer placeholder = new StringBuffer();// 占位符
		StringBuffer params = new StringBuffer();// 参数
		columnNames.append("INSERT INTO " + tableName + " (");
		placeholder.append(" VALUES (");
		for (Object[] obj : list) {
			// TABLE_NAME,COLUMN_NAME,DATA_TYPE,IS_NULLABLE,COLUMN_COMMENT
			Object columnName = obj[1];
			columnNames.append("" + columnName + ",");
			placeholder.append("?,");
		}
		columnNames.append(")");
		placeholder.append(")");
		columnNames.append(placeholder);
		methodInsert.append("		String insertSql = \" " + columnNames.toString().replace(",)", ")") + " \";\n");
		methodInsert.append("		pstmt = connection.prepareStatement(insertSql);\n");
		// 设置字段值
		for (int i = 0;i<list.size();i++) {
			int j = i+1;
			Object[] obj = list.get(i);
			String dataType = String.valueOf(obj[2]);
			switch (dataType) {
			case "varchar":
				methodInsert.append("		pstmt.setString("+j+", null);\n");
				break;
			case "json":
				methodInsert.append("		pstmt.setString("+j+", null);\n");
				break;
			case "int":
				methodInsert.append("		pstmt.setInt("+j+", 1);\n");
				break;
			case "tinyint":
				methodInsert.append("		pstmt.setInt("+j+", 1);\n");
				break;
			case "tinyblob"://
				methodInsert.append("		pstmt.setBytes("+j+", null);\n");
				break;
			case "bigint":
				methodInsert.append("		pstmt.setInt("+j+", 1);\n");
				break;
			case "datetime":
				methodInsert.append("		pstmt.setTimestamp("+j+", new Timestamp(System.currentTimeMillis()));\n");
				break;
			case "date":
				methodInsert.append("		pstmt.setDate("+j+", new Date(System.currentTimeMillis()));\n");
				break;
			case "timestamp":
				methodInsert.append("		pstmt.setTimestamp("+j+", new java.sql.Timestamp(new java.util.Date().getTime()));\n");
				break;
			case "longtext":
				methodInsert.append("		pstmt.setString("+j+", null);\n");
				break;
			case "double":
				methodInsert.append("		pstmt.setDouble("+j+", 0.0);\n");
				break;
			case "text":
				methodInsert.append("		pstmt.setString("+j+", null);\n");
				break;
			case "char":
				methodInsert.append("		pstmt.setString("+j+", null);\n");
				break;
			default:
				methodInsert.append("		pstmt.setString("+j+", \"未定义的dataType\");\n");
				System.out.println("未定义的dataType : "+dataType);
				break;
			}
			
		}
		methodInsert.append("		pstmt.execute();\n");
		methodInsert.append("	} catch (Exception e) {\n");
		methodInsert.append("		e.printStackTrace();\n");
		methodInsert.append("	}finally{\n");
		methodInsert.append("		//do something\n");
		methodInsert.append("		if(pstmt!=null){\n");
		methodInsert.append("			pstmt.close();\n");
		methodInsert.append("		}\n");
		methodInsert.append("		if(connection!=null){\n");
		methodInsert.append("			connection.close();\n");
		methodInsert.append("		}\n");
		methodInsert.append("	}\n");
		methodInsert.append("}\n");// 方法结束
		methodInsert.append("\n");

		return methodInsert.toString();
	}

	@Override
	public Connection connDB(HttpServletRequest requst) throws Exception {
		String parameter = requst.getParameter("dbParams");
		if (StringUtils.isEmpty(parameter)) {
			throw new Exception("没有获取到参数");
		}
		String[] split = parameter.split("&");
		if (split.length != 3) {
			throw new Exception("参数不合法");
		}
		String url = split[0];
		String username = split[1];
		String password = split[2];
		return JDBCTools.getConnection(mysqlDriver, url, username, password);

	}

	@Override
	public List<String> findAllTable(HttpServletRequest request) throws Exception {
		List<String> list = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			list = new ArrayList<>();
			connection = connDB(request);
			String parameter = request.getParameter("dbParams");
			String url = parameter.split("&")[0];
			String tableSchema = StringUtils.substring(url, url.lastIndexOf("3306") + 5);
			if (tableSchema.contains("?")) {
				tableSchema = tableSchema.substring(0, tableSchema.indexOf("?"));
			}
			String sql = "select table_name from information_schema.tables where table_schema=? and table_type='base table' order by table_name asc";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, tableSchema);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				list.add(rs.getString("table_name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.closeConnection(connection, preparedStatement);
		}
		return list;
	}

}
