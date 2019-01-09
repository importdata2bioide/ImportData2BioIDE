package org.big.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.big.entity.Dataset;
import org.big.entity.Taxaset;
import org.big.entity.Team;
import org.big.service.DeleteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * delete 删除数据
 * 
 * @author BIGIOZ
 *
 */
@Controller
@RequestMapping(value = "guest")
public class DeleteController {
	
	private final static Logger logger = LoggerFactory.getLogger(DeleteController.class);
	@Autowired
	private DeleteService deleteService;
	
	@RequestMapping(value = "deleteController_goDelete")
	public ModelAndView jumptoDeleteHtml() throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		List<Team> teamList = deleteService.findAllTeams();
		modelAndView.addObject("teamList", teamList);
		modelAndView.setViewName("delete");
		return modelAndView;
	}
	
	@RequestMapping(value = "findAllDataSetByTeam",method = RequestMethod.POST)
	@ResponseBody
	public Object findAllDataSetByTeam(String team) throws Exception {
		System.out.println("enter..."+team);
		List<Dataset> datasetList = deleteService.findAllDataSetByTeam(team);
		JSONArray array = new JSONArray();
		for (Dataset dataset : datasetList) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", dataset.getId());
			jsonObject.put("dsname", dataset.getDsname());
			array.add(jsonObject);
		}
		System.out.println(array.toJSONString());
		return array.toJSONString();
		
	}
	
	
	@RequestMapping(value = "findAllTaxaSetByDS",method = RequestMethod.POST)
	@ResponseBody
	public Object findAllTaxaSetByDS(String dataset) throws Exception {
		System.out.println(dataset);
		List<Taxaset> list = deleteService.findAllTaxaSetByDS(dataset);
		JSONArray array = new JSONArray();
		for (Taxaset taxaset : list) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", taxaset.getId());
			jsonObject.put("tsname", taxaset.getTsname());
			array.add(jsonObject);
		}
		System.out.println(array.toJSONString());
		return array.toJSONString();
		
	}

	@RequestMapping(value = "hello")
	public String hello(HttpServletRequest request) {
		System.out.println(deleteService.hashCode());
		System.out.println(this.hashCode());
		logger.info("hello ---- 输出日志");
		return "index";
	}
	
	
	@RequestMapping(value = "/deleteByTaxtreeId",method = RequestMethod.POST)
	@ResponseBody
	public String deleteByTaxtreeId(String mTaxtreeId) {
		try {
			return deleteService.deleteByTaxtreeId(mTaxtreeId);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	@RequestMapping(value = "/deleteByTeam")
	@ResponseBody
	public String deleteByTeam(HttpServletRequest request) {
		try {
			return deleteService.deleteByTeam(request);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	
	/**
	 * 根据团队、数据集、分类单元集，删除分类单元集下的所有数据
	 * title: DeleteController.java
	 * @param request
	 * @return
	 * @author ZXY
	 */
	@RequestMapping(value = "/deleteDataByTaxaSet")
	@ResponseBody
	public String deleteByTaxaSet(HttpServletRequest request) {
		try {
			return deleteService.deleteByTaxaSet(request);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	/**
	 * 根据团队、数据集、分类树名称删除所有描述信息
	 * title: DeleteController.java
	 * @param request
	 * @return
	 * @author ZXY
	 */
	@RequestMapping(value = "/deleteDescription")
	@ResponseBody
	public String deleteDescription(HttpServletRequest request) {
		
		try {
			return deleteService.deleteDescription(request);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	/**
	 * 根据团队、数据集、分类树名称删除分类树所有节点
	 * title: DeleteController.java
	 * @param request
	 * @return
	 * @author ZXY
	 */
	@RequestMapping(value = "/deleteTreeByTaxaSet")
	@ResponseBody
	public String deleteTreeByTaxaSet(HttpServletRequest request) {
		try {
			return deleteService.deleteTreeByTaxaSet(request);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	/**
	 * 
	 * title: DeleteController.java
	 * @param request
	 * @return
	 * @author ZXY
	 */
	@RequestMapping(value = "/findNodeAndAllChildren")
	@ResponseBody
	public String findNodeAndAllChildren(HttpServletRequest request) {
		try {
			return deleteService.findNodeAndAllChildren(request);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		
	}


}
