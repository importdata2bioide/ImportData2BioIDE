package org.big.service;


import org.big.entity.Description;
import org.big.entity.Descriptiontype;
import org.big.entity.Taxon;
import org.big.entityVO.BaseParamsForm;


public interface DescriptionService {
	
	Description findOneById(String desid);

	void insertDescription(Descriptiontype descriptiontype, String descontent, Taxon taxon, BaseParamsForm params);
	
	
	
}
