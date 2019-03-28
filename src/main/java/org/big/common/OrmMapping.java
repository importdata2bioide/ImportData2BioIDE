package org.big.common;

import java.beans.IntrospectionException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.big.entity.Rank;
import org.big.entity.Taxaset;
import org.big.entity.Taxon;
import org.big.service.BatchSubmitServiceImpl;
import org.big.test.ClassUtil;

public class OrmMapping {
	public final static String entityPackageName = "org.big.entity";
	// 属性和字段之间的对应关系
	private static volatile Map<Class, LinkedHashMap<String, String>> map = new HashMap<>();
	// 实体和表名对应关系
	private static Map<Class, String> tableNameMap = new HashMap<>();
	// 实体属性对应的数据库类型
	private static volatile Map<Class, LinkedHashMap<String, String>> fieldTypeMap = new HashMap<>();
	
	private static Map<Class, String> primaryKeyMap = new HashMap<>();
	// 私有构造
	private OrmMapping() {

	}
	
	


	// 静态工厂方法
	public static Map<Class, LinkedHashMap<String, String>> getInstance() throws Exception {
		synchronized (map) {
			if (map.size() == 0) {
				initMap();
			}
		}
		return map;
	}

	@SuppressWarnings("unused")
	private static void initMap() throws Exception {
		// 初始化map对象
		// 获取特定包下所有的类(包括接口和类)
		List<Class<?>> clsList = ClassUtil.getAllClassByPackageName(entityPackageName);
		for (Class<?> cls : clsList) {
			// 获取table注解
			Annotation annotation = cls.getAnnotation(javax.persistence.Table.class);
			if (annotation == null) {
				continue;
			}
			javax.persistence.Table table = (javax.persistence.Table) annotation;
			String tableName = table.name();
			if (StringUtils.isEmpty(table.name())) {
				System.out.println(cls.getSimpleName());
				continue;
			}
			tableNameMap.put(cls, tableName);
			// 属性和字段之间的关系
			Method[] methods = cls.getMethods();
			Field[] fields = cls.getDeclaredFields();
			// key=属性 ，value=数据库字段
			LinkedHashMap<String, String> dbMap = new LinkedHashMap<>();
			for (Field field : fields) {
				String databaseField = null;
				Type genericType = field.getGenericType();
				String typeName = genericType.getTypeName();
				String name = field.getName();
				// 加了Transient注解的为非数据库字段，跳过
				Transient transientAnnotation = field.getAnnotation(javax.persistence.Transient.class);
				if (transientAnnotation != null) {
					continue;
				}
				// 序列化参数，跳过
				if (name.equals("serialVersionUID")) {
					continue;
				}
				// 一对多映射，非数据库字段,跳过
				if (typeName.contains("java.util.List")) {
					continue;
				}
				Id primarykey = field.getAnnotation(javax.persistence.Id.class);
				if(primarykey!=null) {
					//实体主键
					primaryKeyMap.put(cls, field.getName());
				}
				
				Column column = field.getAnnotation(javax.persistence.Column.class);
				if (column != null && StringUtils.isNotEmpty(column.name())) {
					databaseField = column.name();
				} else if (typeName.contains(entityPackageName)) {
					databaseField = field.getName() + "_id";
				} else {
					// 映射命名规则
					databaseField = apply(field.getName());
				}

				dbMap.put(name, databaseField);
				map.put(cls, dbMap);
			}
			// 类字段对应的数据库类型
			initFieldTypeMap(cls, dbMap);

		}

	}

	/**
	 * 
	 * @Description
	 * @param cls
	 * @param dbMap key=实体属性,value=数据库字段
	 * @throws Exception
	 * @author ZXY
	 */
	private static void initFieldTypeMap(Class cls, LinkedHashMap<String, String> dbMap) throws Exception {
		Map<String, String> queryType = findColumnByTable(tableNameMap.get(cls));
		LinkedHashMap<String, String> typeMap = new LinkedHashMap<>();
		for (Entry<String, String> entry : dbMap.entrySet()) {
			String dateType = queryType.get(entry.getValue());// 数据库中的类型
			String key = entry.getKey();// 实体中的字段
			typeMap.put(key, dateType);
			if (StringUtils.isEmpty(dateType)) {
				throw new Exception(
						"error [" + cls.getName() + "] 的 [" + key + "] 属性对应的数据库字段不是[" + entry.getValue() + "]");
			}
			fieldTypeMap.put(cls, typeMap);
		}
	}

	/**
	 * 
	 * @Description key=字段，value=类型
	 * @param tableName
	 * @return
	 * @throws Exception
	 * @author ZXY
	 */
	private static Map<String, String> findColumnByTable(String tableName) throws Exception {
		Map<String, String> map = null;
		PreparedStatement preStatement = null;
		ResultSet result = null;
		try {
			Connection conn = ConnDB.getConnDB(null);
			String sql = "select TABLE_NAME,COLUMN_NAME,DATA_TYPE,IS_NULLABLE,COLUMN_COMMENT from INFORMATION_SCHEMA.Columns where table_name=?";
			preStatement = conn.prepareStatement(sql);
			preStatement.setString(1, tableName);
			result = preStatement.executeQuery();
			map = new HashMap<>();
			while (result.next()) {
				map.put(result.getString("COLUMN_NAME"), result.getString("DATA_TYPE"));
			}
		} finally {
			if (preStatement != null) {
				preStatement.close();
			}
			if(result != null) {
				result.close();
			}
			ConnDB.closeConnDB();
		}
		return map;
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
	@SuppressWarnings("unchecked")
	private static Method convertGetter(Class cla, String field) {
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
				System.out.println(field + "、" + cla.getName());
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			}
		}
		return getterMethod;
	}

	public static Map<Class, String> gettableNameMap() throws Exception {
		if (tableNameMap.size() == 0) {
			getInstance();
		}
		return tableNameMap;
	}

	@SuppressWarnings("rawtypes")
	public static Map<Class, LinkedHashMap<String, String>> getFieldTypeMap() throws Exception {
		if (fieldTypeMap.size() == 0) {
			getInstance();
		}
		return fieldTypeMap;
	}
	
	
	public static Map<Class, String> getPrimaryKeyMap() throws Exception {
		if (primaryKeyMap.size() == 0) {
			getInstance();
		}
		return primaryKeyMap;
	}



	public static void main(String[] args) throws Exception {
		Map<Class, LinkedHashMap<String, String>> instance = OrmMapping.getInstance();
		HashMap<String, String> map = instance.get(Taxon.class);
		List<Taxon> taxonlist = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Taxon t = new Taxon();
			t.setAuthorstr("作者测试20190312" + i);
			t.setId(UUIDUtils.getUUID32());
			Rank rank = new Rank();
			rank.setId("1");
			t.setRank(rank);
			Taxaset taxaset = new Taxaset();
			taxaset.setId("05313d7bbeb6417b8733cac3f74a830d");
			t.setTaxaset(taxaset);
			taxonlist.add(t);
		}
		BatchSubmitServiceImpl b = new BatchSubmitServiceImpl();
		
		b.saveAll(taxonlist);

	}

	private static String apply(String name) {
		if (name == null) {
			return null;
		}
		// 大写字母前加下划线
		StringBuilder builder = new StringBuilder(name.replace('.', '_'));
		for (int i = 1; i < builder.length() - 1; i++) {
			if (isUnderscoreRequired(builder.charAt(i - 1), builder.charAt(i), builder.charAt(i + 1))) {
				builder.insert(i++, '_');
			}
		}
		// 大写变小写
		name = builder.toString().toLowerCase(Locale.ROOT);
		return name;
	}

	private static boolean isUnderscoreRequired(char before, char current, char after) {
		return Character.isLowerCase(before) && Character.isUpperCase(current) && Character.isLowerCase(after);
	}

}
