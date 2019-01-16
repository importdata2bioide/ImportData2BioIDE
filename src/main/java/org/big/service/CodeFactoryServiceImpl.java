package org.big.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
			codeStr.append(codes);
		}
		return codeStr.toString();
	}

	private String createMethods(List<Object[]> list) {
//		for (Object[] obj : list) {
		// TABLE_NAME,COLUMN_NAME,DATA_TYPE,IS_NULLABLE,COLUMN_COMMENT
//			Object tableName = obj[0];
//			Object columnName = obj[1];
//			Object dataType = obj[2];
//			Object isNullable = obj[3];
//			Object columnComment = obj[4];
//		}
		return null;
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
		return JDBCTools.getConnection(mysqlDriver , url, username, password);

	}

	@Override
	public List<String> findAllTable(HttpServletRequest request) throws Exception {
		List<String> list = new ArrayList<>();
		Connection connection = connDB(request);
		String parameter = request.getParameter("dbParams");
		String url = parameter.split("&")[0];
		String tableSchema = StringUtils.substring(url, url.lastIndexOf("3306")+5);
		if(tableSchema.contains("?")) {
			tableSchema  = tableSchema.substring(0, tableSchema.indexOf("?"));
		}
		String sql = "select table_name from information_schema.tables where table_schema=? and table_type='base table' order by table_name asc";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, tableSchema);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			list.add(rs.getString("table_name"));
		}
		return list;
	}

}
