package org.big.controller;


import org.big.entityVO.BaseParamsForm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class JumpPageController {
	

	@RequestMapping(value = "JumpPageController_model")
	public String jumptoModelHtml() throws Exception {
		return "model";
	}
	
	@RequestMapping(value = "JumpPageController_index")
	public String jumptoIndexHtml() throws Exception {
		return "index";
	}
	
	@RequestMapping(value = "JumpPageController_birdListComparison")
	public String jumptobirdListComparisonHtml() throws Exception {
		return "birdListComparison";
	}
	
	@RequestMapping(value = "JumpPageController_amphibiansAndReptiles")
	public String jumptoAmphibiansAndReptilesHtml() throws Exception {
		return "amphibiansAndReptiles";
	}
	
	@RequestMapping(value = "JumpPageController_ForcesDB")
	public String jumptoForcesDBHtml() throws Exception {
		return "ForcesDB";
	}
	
	
	
		
	

	@RequestMapping(value = "JumpPageController_SpeciesCatalogue")
	public String jumptoSpeciesCatalogueHtml() throws Exception {
		return "SpeciesCatalogue";
	}
	
	
	@RequestMapping(value = "super/SpeciesCatalogueController_jumptoHtml")
	public String hello() throws Exception {
		return "error";
	}
	
	
	@SuppressWarnings("static-access")
	@ResponseBody
	@RequestMapping(value = "/guest/hello2" ,method=RequestMethod.GET)
	public String hello2() throws Exception {
		Thread.currentThread().sleep(7000);
		return "hello2";
	}
	
	@SuppressWarnings("static-access")
	@ResponseBody
	@RequestMapping(value = "/guest/hello3" ,method = RequestMethod.POST)
	public String hello3(BaseParamsForm params) throws Exception {
		Thread.currentThread().sleep(7000);
		return "hello3";
	}
		
		
}
