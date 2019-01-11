package org.big.controller;

import java.util.List;

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
	
	
	@RequestMapping(value = "/codeFactoryController_findAllTables",method = RequestMethod.GET)
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
	 * @param tableNames
	 * @return
	 * @author ZXY
	 */
	@ResponseBody
	@RequestMapping(value = "/codeFactoryController_batchExecuteCode_doGet",method = RequestMethod.POST)
	public String batchExecuteCode(@RequestParam("tableNames")String[] tableNames) {
		System.out.println("enter batchExecuteCode_doGet");
		try {
			codeFactoryService.batchExecuteCode(tableNames);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		modelAndView.setViewName("codeFactory");
		return "OK";
	}

}
