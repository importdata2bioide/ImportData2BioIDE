package org.big.service;

import java.util.List;

import org.big.entity.Citation;

public interface CitationService {
	
	public void save(Citation entity);
	
	public List<Citation> findCitationListByTaxonId(String taxonId);

	

}
