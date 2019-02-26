package org.big.service;


import org.big.entityVO.BaseParamsForm;

public interface FourService {

	void handleTxt(BaseParamsForm baseParamsForm) throws Exception;
	/**
	 * 
	 * @Description 分布描述转换成分布数据
	 * @param baseParamsForm
	 * @author ZXY
	 */
	void turnDescToDistribution(String teamId,boolean save);
	

}
