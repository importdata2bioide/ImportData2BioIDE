package org.big.service;

import java.lang.reflect.InvocationTargetException;

public interface ToolService {
	/**
	 * 
	 * title: ToolService.java
	 * @param line
	 * @param expression
	 * @param newChar
	 * @return
	 * @author ZXY
	 */
	public String replaceAllChar(String line,String expression,String newChar) ;
	/**
	 * target在line中存在的个数
	 * title: ToolService.java
	 * @param line
	 * @param target
	 * @return
	 * @author ZXY
	 */
	public int countTargetStr(String line,String target);
	/**
	 * 通过反射机制，更改属性值
	 * title: PlantEncyclopediaServiceImpl.java
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
	public void reflectChangeValue(Object model, String oldChar, String newChar)
			throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, InstantiationException ;
	/**
	 * 
	 * title: ToolService.java
	 * @param model
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @author ZXY
	 */
	public void printEntity(Object model) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException;
	/**
	 * 
	 * title: ToolService.java
	 * @param model
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @author ZXY
	 */
	public boolean EntityAttrNull(Object model) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException;

}
