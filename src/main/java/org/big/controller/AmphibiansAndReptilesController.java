package org.big.controller;

import javax.servlet.http.HttpServletRequest;

import org.big.entityVO.BaseParamsForm;
import org.big.service.AmphibiansAndReptilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * List of Amphibians in China（中国两栖动物名录）
 * @author ZXY
 *
 */
@Controller
@RequestMapping(value = "/guest")
public class AmphibiansAndReptilesController {
	
	@Autowired
	private AmphibiansAndReptilesService amphibiansAndReptilesService;
	
	@ResponseBody
	@RequestMapping(value = "/AmphibiansAndReptilesController_doSave",method = RequestMethod.POST)
	public String doSave(BaseParamsForm baseParamsForm,HttpServletRequest request) {
		try {
			return amphibiansAndReptilesService.handleAndInsertExcel(baseParamsForm,request);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/AmphibiansAndReptilesController_doInsertTree",method = RequestMethod.POST)
	public String doInsertTree(BaseParamsForm baseParamsForm,HttpServletRequest request) {
		try {
			return amphibiansAndReptilesService.insertTree(baseParamsForm);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

}
