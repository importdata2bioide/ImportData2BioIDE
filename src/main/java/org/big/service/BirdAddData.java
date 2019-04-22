package org.big.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.big.entityVO.ExcelUntilF;
import org.big.entityVO.NametypeEnum;

public interface BirdAddData {

	void importByExcel() throws Exception;
	/**
	 * 
	 * @Description 统计属、种阶元接受名和异名引证的数量
	 * @param response
	 * @author ZXY
	 */
	void countCitationByTaxon(HttpServletResponse response);
	
	void updateCitationStrBySciName(HttpServletResponse response) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, Exception;
	/**
	 * 
	 * @Description 使用正则表达式解析引证原文中的页码
	 * @param citationstr
	 * @return
	 * @author ZXY
	 */
	Map<String, String> getPageFromCitationStr(String citationstr);
	
	void initRegExPagelist();
	/**
	 * 
	 * @Description 2019鸟引证数据检验
	 * @author ZXY
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws Exception 
	 */
	void validateCitation() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, Exception;
	
	/**
	 * 
	 * @Description 查询某个数据集下所有的引证信息
	 * @param nametype 名称类型
	 * @param datasetId 数据集id
	 * @return
	 * @author ZXY
	 */
	List<ExcelUntilF> exportCitationExcelOfDataSet(NametypeEnum nametype, String datasetId);
	/**
	 * 
	 * @Description 接受名和异名 名称相同，命名信息相同，删除异名
	 * @author ZXY
	 */
	void deleteCitationOfSameSciname(String datasetId);
	/**
	 * 
	 * @Description 根据旧采集系统（动物志）补充异名引证的完整引证（citationstr）字段
	 * @param datasetId
	 * @author ZXY
	 * @throws Exception 
	 */
	void perfectCitationStr(String datasetId) throws Exception;
	/**
	 * 
	 * @Description 完善的命名信息（有命名人没有命名时间）
	 * @author ZXY
	 */
	void perfectAuthorFromExcel();
	/**
	 * 
	 * @Description 根据引证原文解析命名信息
	 * @author ZXY
	 */
	void perfectAuthorByCitationStr();
	/**
	 * 
	 * @Description 打印没有接受名引证的taxon
	 * @author ZXY
	 */
	void printDontHasAcceptCitationTaxon();
	
	void countCitationByDs(String datasetId);
	/**
	 * 
	 * @Description 2019鸟类名录数据转换成word文档
	 * @author ZXY
	 * @throws Exception 
	 */
	void exportToWord() throws Exception;
	/**
	 * 
	 * @Description 从2018鸟类名录补充俗名
	 * @author ZXY
	 */
	void addCommonName();

}
