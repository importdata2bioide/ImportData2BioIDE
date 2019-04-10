package org.big.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public interface BirdAddData {

	void importByExcel() throws Exception;
	/**
	 * 
	 * @Description 统计属、种阶元接受名和异名引证的数量
	 * @param response
	 * @author ZXY
	 */
	void countCitationByTaxon(HttpServletResponse response);
	
	void updateCitationStrBySciName(HttpServletResponse response) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;
	/**
	 * 
	 * @Description 使用正则表达式解析引证原文中的页码
	 * @param citationstr
	 * @return
	 * @author ZXY
	 */
	Map<String, String> getPageFromCitationStr(String citationstr);
	
	void initRegExPagelist();

}
