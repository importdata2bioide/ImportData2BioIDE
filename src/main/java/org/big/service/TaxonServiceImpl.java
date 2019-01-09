package org.big.service;

import java.util.List;

import org.big.entity.Taxon;
import org.big.repository.TaxonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class TaxonServiceImpl implements TaxonService {
	
	@Autowired
	private TaxonRepository taxonRepository;
	@Override
	public void saveOne(Taxon taxon) {
		taxonRepository.save(taxon);
		
	}

	@Override
	public Taxon findOneById(String id) {
		return taxonRepository.findOneById(id);
	}

	@Override
	public List<Taxon> findTaxonByTaxasetId(String taxasetId) {
		return taxonRepository.findByTaxaset(taxasetId);
	}

}
