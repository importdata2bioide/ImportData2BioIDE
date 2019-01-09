package org.big.service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import org.big.common.CommUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ToolServiceImpl implements ToolService{
	private final static Logger logger = LoggerFactory.getLogger(ToolServiceImpl.class);
	
	
	public String replaceAllChar(String line,String expression,String newChar) {
		line = line.replaceAll(expression, newChar).trim();
		return line;
		
	}
	
	
    public int countTargetStr(String line,String target) {//查找字符串里与指定字符串相同的个数
        int n=0;//计数器
        while(line.indexOf(target)!=-1) {
            int i = line.indexOf(target);
            n++;
            line = line.substring(i+1);
        }
        return n;
    }
	/**
	 * 通过反射机制，更改属性值
	 * title: ToolServiceImpl.java
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
			InvocationTargetException, InstantiationException {
		// 获取实体类的所有属性，返回Field数组
		Field[] field = model.getClass().getDeclaredFields();
		// 获取属性的名字
		String[] modelName = new String[field.length];
		String[] modelType = new String[field.length];
		for (int i = 0; i < field.length; i++) {
			// 获取属性的名字
			String name = field[i].getName();
			modelName[i] = name;
			// 获取属性类型
			String type = field[i].getGenericType().toString();
			modelType[i] = type;
			// 关键.可访问私有变量
			field[i].setAccessible(true);
			// 将属性的首字母大写
			name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
			if (type.equals("class java.lang.String")) {
				// 如果type是类类型，则前面包含"class "，后面跟类名
				Method m = model.getClass().getMethod("get" + name);
				// 调用getter方法获取属性值
				String value = (String) m.invoke(model);
				if (value != null && value.equals(oldChar)) {
					//赋值
					field[i].set(model, field[i].getType().getConstructor(field[i].getType()).newInstance(newChar));
				}

			}
			if (type.equals("class java.lang.Integer")) {
				Method m = model.getClass().getMethod("get" + name);
				Integer value = (Integer) m.invoke(model);
				if (value != null) {
					//do something
				}
			}
			if (type.equals("class java.lang.Short")) {
				Method m = model.getClass().getMethod("get" + name);
				Short value = (Short) m.invoke(model);
				if (value != null) {
					//do something
				}
			}
			if (type.equals("class java.lang.Double")) {
				Method m = model.getClass().getMethod("get" + name);
				Double value = (Double) m.invoke(model);
				if (value != null) {
					//do something
				}
			}
			if (type.equals("class java.lang.Boolean")) {
				Method m = model.getClass().getMethod("get" + name);
				Boolean value = (Boolean) m.invoke(model);
				if (value != null) {
					//do something
				}
			}
			if (type.equals("class java.util.Date")) {
				Method m = model.getClass().getMethod("get" + name);
				Date value = (Date) m.invoke(model);
				if (value != null) {
					//do something
				}
			}
		}
	}

	/**
	 * 打印实体中的所有属性值
	 * title: ToolServiceImpl.java
	 * @param model
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @author ZXY
	 */
	public  void printEntity(Object model) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		StringBuffer sb = new StringBuffer();
		// 获取实体类的所有属性，返回Field数组
		Field[] field = model.getClass().getDeclaredFields();
		// 获取属性的名字
		String[] modelName = new String[field.length];
		String[] modelType = new String[field.length];
		for (int i = 0; i < field.length; i++) {
			// 获取属性的名字
			String name = field[i].getName();
			modelName[i] = name;
			// 获取属性类型
			String type = field[i].getGenericType().toString();
			modelType[i] = type;
			// 关键.可访问私有变量
			field[i].setAccessible(true);
			// 将属性的首字母大写
			name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
			if(name.equals("SerialVersionUID")) {
				continue;
			}
			Method m = model.getClass().getMethod("get" + name);
			// 调用getter方法获取属性值
			Object value = (Object) m.invoke(model);
			if(CommUtils.isStrNotEmpty(String.valueOf(value))) {
				sb.append("["+name+"=" + value + "] ");
			}else {
				sb.append("["+name+"=" + null + "] ");
			}
			
		}
		
		logger.info(sb.toString());
	}
	
	/**
	 * 实体中的属性是否都为空(包含多个空格也算空)，都为空返回true,否则返回false
	 * 无法处理boolean类型
	 * title: CommUtils.java
	 * @param model
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @author ZXY
	 */
	public  boolean EntityAttrNull(Object model) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		boolean entityAttrNull = true;
		// 获取实体类的所有属性，返回Field数组
		Field[] field = model.getClass().getDeclaredFields();
		// 获取属性的名字
		String[] modelName = new String[field.length];
		String[] modelType = new String[field.length];
		for (int i = 0; i < field.length; i++) {
			// 获取属性的名字
			String name = field[i].getName();
			modelName[i] = name;
			// 获取属性类型
			String type = field[i].getGenericType().toString();
			modelType[i] = type;
			// 关键.可访问私有变量
			field[i].setAccessible(true);
			// 将属性的首字母大写
			name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
			if(name.equals("SerialVersionUID")) {
				continue;
			}
			Method m = model.getClass().getMethod("get" + name);
			// 调用getter方法获取属性值
			Object value = (Object) m.invoke(model);
			if(CommUtils.isStrNotEmpty(String.valueOf(value))) {
				entityAttrNull = false;
			}
			
		}
		return entityAttrNull;
	}

}
