package org.big.controller;

import org.big.entityVO.BaseParamsForm;
import org.big.service.ParseWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping(value = "/guest")
public class FishController {
	@Autowired
	private ParseWordService parseWordService;
	/**
	 * 
	 * @Description 2019鱼类名录5个word
	 * @param baseParamsForm
	 * @return
	 * @throws Exception
	 * @author ZXY
	 */
	@ResponseBody
	@RequestMapping(value = "/fishController_addFish",method=RequestMethod.POST)
	public String addFish(BaseParamsForm baseParamsForm) throws Exception {
		try {
			parseWordService.readExcelAndOutputWord(baseParamsForm);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return "success";
	}

}
