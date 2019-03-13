package org.big.service;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.big.common.ConnDB;
import org.big.common.OrmMapping;
import org.springframework.stereotype.Service;

@Service
public class BatchSubmitServiceImpl implements BatchSubmitService {
	@SuppressWarnings("rawtypes")
	@Override
	public <T> int saveAll(List<T> entities) throws Exception {
		Connection connection = null;
		PreparedStatement pstmt = null;
		if(entities == null || entities.size()==0) {
			return 0;
		}
		Class<? extends Object> entityClass = entities.get(0).getClass();
		try {
			Map<Class, LinkedHashMap<String, String>> instance = OrmMapping.getInstance();
			Map<Class, String> nameMap = OrmMapping.gettableNameMap();//
			Map<Class, LinkedHashMap<String, String>> fieldTypeMap = OrmMapping.getFieldTypeMap();
			String tableName = nameMap.get(entityClass);// 表名
			LinkedHashMap<String, String> fieldMapping = instance.get(entityClass);
			StringBuffer columnNames = new StringBuffer();// 字段名称
			StringBuffer placeholder = new StringBuffer();// 占位符
			// 生成SQL
			Set<Entry<String, String>> fmEntrySet = fieldMapping.entrySet();
			columnNames.append("(");
			placeholder.append("(");
			for (Entry<String, String> entry : fmEntrySet) {
				String dbfield = entry.getValue();
				columnNames.append("" + dbfield + ",");
				placeholder.append("?,");
			}
			columnNames.append(")");
			placeholder.append(")");
			String insertSQL = "INSERT INTO " + tableName + " " + columnNames.toString().replace(",)", ")") + " VALUES"
					+ placeholder.toString().replace(",)", ")");
			connection = ConnDB.getConnDB();// 打开数据库连接
			connection.setAutoCommit(false);// 关闭自动提交
			pstmt = connection.prepareStatement(insertSQL);// 预编译SQL
			for (T t : entities) {
				int parameterIndex = 0;
				for (Entry<String, String> entry : fmEntrySet) {
					parameterIndex++;
					Class<? extends Object> targetClass = t.getClass();
					Method method = targetClass.getMethod(convertGetter(targetClass, entry.getKey()).getName());
					Object value = method.invoke(t);
					Class<?> returnType = method.getReturnType();
					// 给pstmt设置参数值
					LinkedHashMap<String, String> typeMap = fieldTypeMap.get(t.getClass());
					String dataTypeFromDB = typeMap.get(entry.getKey());
					addParameter(pstmt, parameterIndex, returnType, dataTypeFromDB, value);

				}
				pstmt.addBatch();// 添加到同一个批处理中;
			}

			pstmt.executeBatch();// 执行批处理
			connection.commit();// 提交
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
			ConnDB.closeConnDB();
		}
		return entities.size();
	}

	private void addParameter(PreparedStatement pstmt, int parameterIndex, Class<?> returnType, String dataTypeFromDB,
			Object value) throws Exception {
		if (value != null && returnType.getName().contains(OrmMapping.entityPackageName)) {
			Method childMethod = null;
			Class<? extends Object> childObj = value.getClass();
			childMethod = childObj.getMethod(convertGetter(childObj, "id").getName());
			Object childValue = childMethod.invoke(value);
			addParameter(pstmt, parameterIndex, childMethod.getReturnType(), dataTypeFromDB, childValue);
		} else {
			switch (dataTypeFromDB) {
			case "varchar":
				pstmt.setString(parameterIndex, String.valueOf(value));
				break;
			case "json":
				pstmt.setString(parameterIndex, String.valueOf(value));
				break;
			case "int":
				pstmt.setInt(parameterIndex, Integer.parseInt(value.toString()));
				break;
			case "tinyint":
				pstmt.setInt(parameterIndex, Integer.parseInt(value.toString()));
				break;
			case "tinyblob"://
				pstmt.setBytes(parameterIndex, null);
				break;
			case "bigint":
				pstmt.setInt(parameterIndex, Integer.parseInt(value.toString()));
				break;
			case "datetime":
				pstmt.setTimestamp(parameterIndex, new Timestamp(System.currentTimeMillis()));
				break;
			case "date":
				pstmt.setDate(parameterIndex, new Date(System.currentTimeMillis()));
				break;
			case "timestamp":
				pstmt.setTimestamp(parameterIndex, new java.sql.Timestamp(new java.util.Date().getTime()));
				break;
			case "longtext":
				pstmt.setString(parameterIndex, String.valueOf(value));
				break;
			case "double":
				pstmt.setDouble(parameterIndex, (double) value);
				break;
			case "text":
				pstmt.setString(parameterIndex, String.valueOf(value));
				break;
			case "char":
				pstmt.setString(parameterIndex, String.valueOf(value));
				break;
			default:
				throw new Exception("未定义的dataType : " + dataTypeFromDB);
			}

		}

	}

	/**
	 * 
	 * @Description
	 * @param cla
	 * @param field
	 * @param parameterTypes
	 * @return
	 * @author ZXY
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Method convertGetter(Class cla, String field) {
		String str1 = field.substring(0, 1);
		String str2 = field.substring(1, field.length());
		String methodGet = "get" + str1.toUpperCase() + str2;
		Method getterMethod = null;
		try {
			getterMethod = cla.getMethod(methodGet);
		} catch (NoSuchMethodException e) {
			methodGet = "get" + field;
			try {
				getterMethod = cla.getMethod(methodGet);
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			}
		}
		return getterMethod;
	}

	@Override
	public <T> int saveAllValues(List<T> entities) throws Exception {
		Connection connection = null;
		PreparedStatement pstmt = null;
		if(entities == null || entities.size()==0) {
			return 0;
		}
		Class<? extends Object> entityClass = entities.get(0).getClass();
		try {
			Map<Class, LinkedHashMap<String, String>> instance = OrmMapping.getInstance();
			Map<Class, String> nameMap = OrmMapping.gettableNameMap();//
			Map<Class, LinkedHashMap<String, String>> fieldTypeMap = OrmMapping.getFieldTypeMap();
			String tableName = nameMap.get(entityClass);// 表名
			LinkedHashMap<String, String> fieldMapping = instance.get(entityClass);
			StringBuffer prefix = new StringBuffer();// 字段名称
			prefix.append("INSERT INTO " + tableName + " ");
			prefix.append("(");
			// 生成SQL
			Set<Entry<String, String>> fmEntrySet = fieldMapping.entrySet();
			for (Entry<String, String> entry : fmEntrySet) {
				String dbfield = entry.getValue();
				prefix.append("" + dbfield + ",");

			}
			prefix.append(")");
			prefix.append("VALUES ");

			StringBuffer valuesStr = new StringBuffer();// 字段名称
			for (T t : entities) {
				valuesStr.append("(");
				for (Entry<String, String> entry : fmEntrySet) {
					Class<? extends Object> targetClass = t.getClass();
					Method method = targetClass.getMethod(convertGetter(targetClass, entry.getKey()).getName());
					Object value = method.invoke(t);
					value = getEntityValue(value, method.getReturnType());
					String entityAttribute = entry.getKey();// 实体属性
					String dataTypeFromDB = fieldTypeMap.get(t.getClass()).get(entityAttribute);
					if(value!=null) {
						switch (dataTypeFromDB) {
						case "varchar":
							value = "'"+value+"'";
							break;
						case "json":
							value = "'"+value+"'";
							break;
						case "int":
							break;
						case "tinyint":
							break;
						case "tinyblob"://
							break;
						case "bigint":
							break;
						case "datetime":
							value = "'"+value+"'";
							break;
						case "date":
							value = "'"+value+"'";
							break;
						case "timestamp":
							value = "'"+value+"'";
							break;
						case "longtext":
							value = "'"+value+"'";
							break;
						case "double":
							break;
						case "text":
							value = "'"+value+"'";
							break;
						case "char":
							value = "'"+value+"'";
							break;
						default:
							throw new Exception("未定义的dataType : " + dataTypeFromDB);
						}
					}
					valuesStr.append(value + ",");
					
				}
				valuesStr.append("),");

			}
			String finalSql = prefix.append(valuesStr).toString().replace(",)", ")");
			finalSql = finalSql.substring(0, finalSql.length()-1);
			connection = ConnDB.getConnDB();// 打开数据库连接
			connection.setAutoCommit(false);// 设置事务为非自动提交
			pstmt = connection.prepareStatement(finalSql);// 预编译SQL
			pstmt.addBatch();
			pstmt.executeBatch();// 执行批处理
			connection.commit();// 提交
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
			ConnDB.closeConnDB();
		}
		return entities.size();
	}

	private Object getEntityValue(Object value, Class<?> returnType) throws Exception {
		if (value == null) {
			return null;
		}
		if (value != null && returnType.getName().contains(OrmMapping.entityPackageName)) {
			Method childMethod = null;
			Class<? extends Object> childObj = value.getClass();
			childMethod = childObj.getMethod(convertGetter(childObj, "id").getName());
			value = childMethod.invoke(value);
			getEntityValue(value, childMethod.getReturnType());
		} else {
			return value;
		}
		return value;
	}

}
