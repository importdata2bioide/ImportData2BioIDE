package org.big.service;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.big.entity.Citation;
import org.big.entity.Taxon;
import org.big.entityVO.ProvinceVO;

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
	 * @throws Exception 
	 */
	public String insertKeyitem(HttpServletRequest request) throws SQLException, ParseException, Exception ;
	
	/**
	 * 录入taxon
	 * @param request
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 * @throws Exception 
	 */
	public String insertTaxon(HttpServletRequest request) throws SQLException, ParseException, Exception;
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
	 * @throws Exception 
	 */
	public String insertCommName(HttpServletRequest request) throws SQLException, Exception;
	/**
	 * 录入参考文献
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 * @throws Exception 
	 */
	public String insertRefs() throws SQLException, ParseException, Exception ;
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
	/**
	 * 
	 * @Description 获取指定的异名引证
	 * @param scientificname 接受名
	 * @param sciname 异名引证名称
	 * @param forcesDB_Tree_Id 数据源Id
	 * @param citation 实体类，直接修改此实体类中的数据：引证原文和命名信息
	 * @param nameTypeId:taxa.StatusID  67280F4A-D8D6-4CAD-BCDA-843866010852	accepted name    |  BEDBB69A-139D-45A3-8CD9-CC7D55BF6E7E	synonym
	 * @return 引证是否需要更新
	 * @author ZXY
	 * @throws Exception 
	 */
	public boolean getCitationFromForcesDB(String scientificname, String sciname, String forcesDB_Tree_Id, Citation citation,String nameTypeId) throws Exception;
	/**
	 * 
	 * @Description 
	 * @param taxon
	 * @param forcesDB_Tree_Id_Bird
	 * @return
	 * @author ZXY
	 * @throws Exception 
	 */
	public List<Citation> getAcceptCitationByParams(Taxon taxon, String forcesDB_Tree_Id,String sourcesid,String inputer) throws Exception;

	public List<ProvinceVO> findProvince() throws Exception;

}
