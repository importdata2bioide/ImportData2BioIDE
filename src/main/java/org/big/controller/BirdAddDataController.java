package org.big.controller;

import javax.servlet.http.HttpServletResponse;

import org.big.service.BirdAddData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 * 
 * @Description 2019鸟数据补充
 * @author ZXY
 */
@Controller
public class BirdAddDataController {
	@Autowired
	private  BirdAddData birdAddData;
	
	/**
	 * 
	 * @Description http://localhost/importByExcel
	 * @param response
	 * @throws Exception
	 * @author ZXY
	 */
	@RequestMapping(value = "/importByExcel",method = RequestMethod.GET)
	public String importByExcel(HttpServletResponse response) throws Exception {
		birdAddData.importByExcel();
		return "index";
	}
	
	@RequestMapping(value = "/birdAddDataController_countCitationByTaxon",method = RequestMethod.GET)
	public String countCitationByTaxon(HttpServletResponse response) throws Exception {
		birdAddData.countCitationByTaxon(response);
		return "index";
	}
	
	/**
	 * 
	 * @Description 更新2019鸟类名录引证：根据引证原文生成参考文献，没有引证的从旧采集系统导入
	 * @param response
	 * @return
	 * @throws Exception
	 * @author ZXY
	 */
	@RequestMapping(value = "/birdAddDataController_updateCitationStrBySciName",method = RequestMethod.GET)
	public String updateCitationStrBySciName(HttpServletResponse response) throws Exception {
		birdAddData.updateCitationStrBySciName(response);
		return "index";
	}


}
