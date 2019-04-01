package org.big.service;

import javax.servlet.http.HttpServletResponse;

public interface BirdAddData {

	void importByExcel() throws Exception;
	/**
	 * 
	 * @Description 统计属、种阶元接受名和异名引证的数量
	 * @param response
	 * @author ZXY
	 */
	void countCitationByTaxon(HttpServletResponse response);

}
