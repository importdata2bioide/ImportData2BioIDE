package org.big.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.big.service.CodeFactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "guest")
public class CodeFactoryController {

	@Autowired
	private CodeFactoryService codeFactoryService;

	@RequestMapping(value = "/codeFactoryController_findAllTables", method = RequestMethod.GET)
	public ModelAndView findAllTables() {
		ModelAndView modelAndView = new ModelAndView();
		try {

			List<String> allTable = codeFactoryService.findAllTable();
			modelAndView.addObject("allTable", allTable);
		} catch (Exception e) {
			e.printStackTrace();
		}
		modelAndView.setViewName("codeFactory");
		return modelAndView;
	}

	/**
	 * 
	 * title: CodeFactoryController.java
	 * 
	 * @param tableNames
	 * @return
	 * @author ZXY
	 */
	@ResponseBody
	@RequestMapping(value = "/codeFactoryController_batchExecuteCode_doGet", method = RequestMethod.POST)
	public String batchExecuteCode(@RequestParam("tableNames") String[] tableNames) {
		System.out.println("enter batchExecuteCode_doGet");
		try {
			return codeFactoryService.batchExecuteCode(tableNames);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		modelAndView.setViewName("codeFactory");
		return "OK";
	}

	@ResponseBody
	@RequestMapping(value = "/codeFactoryController_connDB", method = RequestMethod.POST)
	public String batchExecuteCode(HttpServletRequest requst) {
		try {
			codeFactoryService.connDB(requst);
		} catch (Exception e) {
			e.printStackTrace();
			return "连接失败"+e.getMessage();
		}       
		return "连接成功";
	}
	
	
	@RequestMapping(value = "/codeFactoryController_quertTables", method = RequestMethod.POST)
	public ModelAndView quertTables(HttpServletRequest requst) {
		ModelAndView modelAndView = new ModelAndView();
		try {

			List<String> allTable = codeFactoryService.findAllTable(requst);
			modelAndView.addObject("OtherDBAllTable", allTable);
		} catch (Exception e) {
			e.printStackTrace();
		}
		modelAndView.addObject("tab","otherDBTab");
		modelAndView.setViewName("codeFactory");
		return modelAndView;
	}
	
	

}
