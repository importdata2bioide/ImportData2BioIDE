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
	
//	>1）亚属拉丁名应该是斜体，外面的括号是正体。（目前亚属的拉丁名和括号都是正体）
//	>2）拉丁名中作者带括号的，括号前面应该有一个空格。（例如，
//		>Gnathopogon taeniellus(Nichols，1925)，应该为
//		>Gnathopogon taeniellus (Nichols，1925)）
//	3）分布的内容如果长于一行，第二行应该缩进。

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
