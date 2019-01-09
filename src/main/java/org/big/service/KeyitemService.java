package org.big.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;


import org.big.entity.Resource;

public interface KeyitemService {
	

	void deleteBytaxkeyId(List<String> taxkeyIds);
	
	
	void deleteRelaKeyItem(String tsname, String dsname, String teamName);

	
	void deleteRelaKeyItemByTaxaSetId(String tsId);


	void batchResourceImages(Resource r, String url, String string, HttpServletRequest request);

}
