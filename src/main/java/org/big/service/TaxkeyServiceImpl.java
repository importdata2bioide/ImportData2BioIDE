package org.big.service;


import org.big.entity.Taxkey;
import org.big.repository.TaxkeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class TaxkeyServiceImpl implements TaxkeyService {
	@Autowired
	private TaxkeyRepository taxkeyRepository;


	@Override
	public boolean deleteOneById(String id) {
		if (null != this.taxkeyRepository.findOneById(id)) {
			this.taxkeyRepository.deleteOneById(id);
			return true;
		}
		return false;
	}


	
	@Override
	public Taxkey findOneById(String taxkeyId) {
		return this.taxkeyRepository.findOneById(taxkeyId);
	}

	
	@Override
	public void deleteTaxkey(String tsname, String dsname, String teamName) {
		taxkeyRepository.deleteTaxkey(tsname, dsname, teamName);
		
	}

	@Override
	public void deleteTaxkeyByTaxaSetId(String tsId) {
		taxkeyRepository.deleteTaxkeyByTaxaSetId(tsId);
		
	}
}
