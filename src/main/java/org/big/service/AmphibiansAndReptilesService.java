package org.big.service;

import javax.servlet.http.HttpServletRequest;

import org.big.entityVO.BaseParamsForm;

public interface AmphibiansAndReptilesService {

	String handleAndInsertExcel(BaseParamsForm amphibiansAndReptilesForm,HttpServletRequest request);

	String insertTree(BaseParamsForm baseParamsForm);

}