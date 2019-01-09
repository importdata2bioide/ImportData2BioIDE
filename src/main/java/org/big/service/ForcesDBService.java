package org.big.service;

import java.sql.SQLException;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ForcesDBService {
	
	public String selectAll(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	public String createAll(HttpServletRequest request, HttpServletResponse response) throws Exception;
	/**
	 * 根据配置的变量录入数据
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String insertDSAll(HttpServletRequest request, HttpServletResponse response) throws Exception;
	/**
	 * insertDSAllByXml 读取xml文件录入数据
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String insertDSAllByXml(HttpServletRequest request, HttpServletResponse response) throws Exception;
	/**
	 * 录入引证
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 */
	public String insertCitation(HttpServletRequest request) throws Exception;
	/**
	 * 录入检索表
	 * @param request
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 */
	public String insertKeyitem(HttpServletRequest request) throws SQLException, ParseException ;
	
	/**
	 * 录入taxon
	 * @param request
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 */
	public String insertTaxon(HttpServletRequest request) throws SQLException, ParseException;
	/**
	 * 录入多媒体
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void insertMultimedia(HttpServletRequest request, HttpServletResponse response) throws Exception;
	/**
	 * 录入分类树
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String insertTree(HttpServletRequest request) throws Exception;
	/**
	 * 录入分布数据
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String insertDistribution(HttpServletRequest request) throws Exception ;
	/**
	 * 录入俗名
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	public String insertCommName(HttpServletRequest request) throws SQLException;
	/**
	 * 录入参考文献
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 */
	public String insertRefs() throws SQLException, ParseException ;
	/**
	 * 录入描述信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String insertDesc(HttpServletRequest request) throws Exception;
	/**
	 * 录入标本
	 * @return
	 * @throws Exception
	 */
	public String insertSpecimen(HttpServletRequest request) throws Exception;

}
