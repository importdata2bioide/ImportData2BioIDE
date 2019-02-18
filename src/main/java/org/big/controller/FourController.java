package org.big.controller;

import javax.servlet.http.HttpServletRequest;

import org.big.entityVO.BaseParamsForm;
import org.big.service.FourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * 
 * @Description 四册版
 * @author ZXY
 */
@Controller
@RequestMapping(value = "/guest")
public class FourController {
	
	@Autowired
	private FourService fourService;
	
	
	@ResponseBody
	@RequestMapping(value = "/fourController_doSave",method = RequestMethod.POST)
	public String doSave(BaseParamsForm baseParamsForm,HttpServletRequest request) {
		try {
			fourService.handleTxt(baseParamsForm);
			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

}
