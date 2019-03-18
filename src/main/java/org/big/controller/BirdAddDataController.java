package org.big.controller;

import javax.servlet.http.HttpServletResponse;

import org.big.service.BirdAddData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
	@RequestMapping(value = "/importByExcel")
	public void importByExcel(HttpServletResponse response) throws Exception {
		birdAddData.importByExcel();
	}


}
