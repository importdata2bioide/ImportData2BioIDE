package org.big.service;

import java.util.List;

import org.big.entity.Citation;
import org.big.repository.CitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CitationServiceImpl implements CitationService {
	@Autowired
	private CitationRepository citationRepository;
	@Override
	public void save(Citation entity) {
		citationRepository.save(entity);
		
	}
	@Override
	public List<Citation> findCitationListByTaxonId(String taxonId) {
		return  citationRepository.findCitationListByTaxonId(taxonId);
		
	}
	
}
