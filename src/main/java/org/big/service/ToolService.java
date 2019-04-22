package org.big.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.big.entityVO.SpeciesCatalogueEnum;

public interface ToolService {
	void asy(int i);

	/**
	 * 
	 * title: ToolService.java
	 * 
	 * @param line
	 * @param expression
	 * @param newChar
	 * @return
	 * @author ZXY
	 */
	String replaceAllChar(String line, String expression, String newChar);

	/**
	 * target在line中存在的个数 title: ToolService.java
	 * 
	 * @param line
	 * @param target
	 * @return
	 * @author ZXY
	 */
	int countTargetStr(String line, String target);

	/**
	 * 通过反射机制，更改属性值 title: PlantEncyclopediaServiceImpl.java
	 * 
	 * @param model
	 * @param oldChar
	 * @param newChar
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 * @author ZXY
	 */
	void reflectChangeValue(Object model, String oldChar, String newChar)
			throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, InstantiationException;

	/**
	 * 
	 * title: ToolService.java
	 * 
	 * @param model
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @author ZXY
	 */
	void printEntity(Object model) throws Exception;

	/**
	 * 
	 * title: ToolService.java
	 * 
	 * @param model
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @author ZXY
	 */
	boolean EntityAttrNull(Object model) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException;

	/**
	 * 
	 * @Description
	 * @param line
	 * @param rowNum
	 * @return
	 * @author ZXY
	 */
	SpeciesCatalogueEnum judgeIsWhat(String line, int rowNum);

	/**
	 * 
	 * @Description 获取类注解
	 * @param cla
	 * @param annotationClass
	 * @return
	 * @author ZXY
	 */
	@SuppressWarnings("rawtypes")
	Annotation getClassAnnotation(Class<?> cla, Class annotationClass);

	/**
	 * 
	 * @Description 获取学名
	 * @param line
	 * @param count
	 * @return
	 * @author ZXY
	 */
	String getSciNameFromCitation(String line, int count);

	/**
	 * 
	 * @Description 返回年份
	 * @param line
	 * @return
	 * @author ZXY
	 */
	String getYear(String line);
	

	/**
	 * 
	 * @Description 统计字符串中大写字母的个数
	 * @param str
	 * @return
	 * @author ZXY
	 */
	int getUpperCaseCount(String str);

	/**
	 * 
	 * @Description 获取第二个大写字母的位置
	 * @param str
	 * @return
	 * @author ZXY
	 */
	int getSecondUpperCaseIndex(String str);
	/**
	 * 
	 * @Description 获取第point个大写字母的位置
	 * @param str
	 * @param point
	 * @return
	 * @author ZXY
	 */
	int getPointUpperCaseIndex(String str,int point);

	/**
	 * 
	 * @Description 读取doc文件内容
	 * @param path
	 * @return
	 * @throws SQLException
	 * @author ZXY
	 */
	List<String> readDoc(String path) throws SQLException;
	
	/**
	 * 
	 * @Description 
	 * @param line 格式：学名 命名信息 其他
	 * @return
	 * @author ZXY
	 */
	Map<String,String> parseSciName(String line);
	
	/**
	 * 
	 * @Description 第一个中文字符的位置
	 * @param line
	 * @return
	 * @author ZXY
	 */
	int IndexOfFirstChinese(String line);
	/**
	 * 
	 * @Description 第一个英文字符的位置
	 * @param line
	 * @return
	 * @author ZXY
	 */
	int IndexOfFirstEng(String line);
	
	/**
	 * 
	 * @Description 第一个数字字符的位置
	 * @param line
	 * @return
	 * @author ZXY
	 */
	int IndexOfFirstNum(String line);
	
	public void initregExlist();
	/**
	 * 
	 * @Description 第point个大写字母的位置，括号内的大写字母不算
	 * @param line
	 * @return
	 * @author ZXY
	 */
	public int indexOfPointUpperCaseWithoutBrackets(String line,int point);
	/**
	 * 
	 * @Description 
	 * @param line
	 * @return
	 * @author ZXY
	 */
	int getYearStart(String line);
	/**
	 * 
	 * @Description 默认第一行为标题
	 * @param path 文件路径
	 * @param t 实体类
	 * @return
	 * @author ZXY
	 */
	<T> List<T> readExcel(String path, Class<T> t);

}
