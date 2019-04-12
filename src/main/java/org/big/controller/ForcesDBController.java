package org.big.controller;

import java.sql.SQLException;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.big.service.ForcesDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * ForcesDB（旧采集系统）的数据导入新采集系统
 * 
 * @author BIGIOZ
 *
 */
@RestController
public class ForcesDBController {
	//org.slf4j.Logger;  org.slf4j.Logger;
	@SuppressWarnings("unused")
	private final static Logger logger = LoggerFactory.getLogger(ForcesDBController.class);
	
	@Autowired
	private ForcesDBService forcesDBService;

	/**
	 * select 查询导入需要的参数。
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectAll")
	public void selectAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
		forcesDBService.selectAll(request, response);
	}

	/**
	 * tree 根据旧系统tree的名称，insert创建数据集、分类单元集、数据源、分类树
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/createAll")
	public String  createAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return forcesDBService.createAll(request, response);
	}
	/**
	 * 根据定义的参数，在指定数据集中录入taxon、citation等信息
	 * title: ForcesDBController.java
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author ZXY
	 */
	@RequestMapping(value = "/insertDSAll")
	public String insertDSAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return forcesDBService.insertDSAll(request, response);
	}
	/**
	 * 读取xml文件，录入数据集中的信息
	 * title: ForcesDBController.java
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author ZXY
	 */
	@RequestMapping(value = "/insertDSAllByXml")
	public String insertDSAllByXml(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			forcesDBService.insertDSAllByXml(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return "insertDSAllByXml execute finish";
	}
	/**
	 * 录入引证
	 * title: ForcesDBController.java
	 * @return
	 * @throws Exception
	 * @author ZXY
	 */
	@RequestMapping(value = "/birdCitationInsert")
	public String insertCitation(HttpServletRequest request) throws Exception{
		try {
			forcesDBService.insertCitation(request);
		} catch (Exception e) {
			e.printStackTrace();
			return "录入引证出错："+e.getMessage();
		}
		return "引证信息录入完成";
	}

	

	/**
	 * Keyitem 检索表
	 * title: ForcesDBController.java
	 * @param request
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 * @author ZXY
	 */
	@RequestMapping(value = "/birdKeyitemInsert")
	public String insertKeyitem(HttpServletRequest request) throws SQLException, ParseException {
		try {
			forcesDBService.insertKeyitem(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 基础信息
	 * title: ForcesDBController.java
	 * @param request
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 * @author ZXY
	 */
	@RequestMapping(value = "/birdTaxonInsert")
	public String insertTaxon(HttpServletRequest request) throws SQLException, ParseException {
		try {
			forcesDBService.insertTaxon(request);
		} catch (Exception e) {
			e.printStackTrace();
			return "录入taxon出错:"+e.getMessage();
		}
		return "insertTaxon execute finish";
	}

	/**
	 * 多媒体
	 * title: ForcesDBController.java
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author ZXY
	 */
	@RequestMapping(value = "/birdMultimediaInsert")
	public void insertMultimedia(HttpServletRequest request, HttpServletResponse response) throws Exception {
		forcesDBService.insertMultimedia(request, response);
	}

	// 分类树
	@RequestMapping(value = "/birdTreeInsert")
	@ResponseBody
	public String insertTree(HttpServletRequest request) throws Exception {
		forcesDBService.insertTree(request);
		return "insertTree execute finish" ;

	}

	// 分布
	@RequestMapping(value = "/birdDistriInsert")
	public String insertDistribution(HttpServletRequest request) throws Exception {
		forcesDBService.insertDistribution(request);
		return "insertDistribution (分布) finish";

	}

	// 俗名
	@RequestMapping(value = "/birdCommNameInsert")
	public String insertCommName(HttpServletRequest request) throws SQLException {
		try {
			forcesDBService.insertCommName(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "insertCommName execute finish";

	}

	// 描述信息
	@RequestMapping(value = "/birdDescInsert")
	public String insertDesc(HttpServletRequest request) throws Exception {
		forcesDBService.insertDesc(request);
		return "insertDesc execute finish";

	}

	@RequestMapping("/birdSpecimenInsert")
	public String insertSpecimen(HttpServletRequest request) throws Exception {
		forcesDBService.insertSpecimen(request);
		return "insertSpecimen execute finish";
	}
	/**
	 * 参考文献
	 * title: ForcesDBController.java
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 * @author ZXY
	 */
	@RequestMapping(value = "/birdRefsInsert")
	@ResponseBody
	public String insertRefs() throws SQLException, ParseException {
		try {
			return forcesDBService.insertRefs();
		} catch (Exception e) {
			e.printStackTrace();
			return "insertRefs(参考文献)出错,错误信息：" + e.getMessage();
		}

	}

}
