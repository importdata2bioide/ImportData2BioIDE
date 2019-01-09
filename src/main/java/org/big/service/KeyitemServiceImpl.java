package org.big.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.big.entity.Resource;
import org.big.repository.KeyitemRepository;
import org.big.repository.ResourceRepository;
import org.big.repository.TaxkeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeyitemServiceImpl implements KeyitemService {

	@Autowired
	private KeyitemRepository keyitemRepository;

	@Autowired
	private ResourceRepository resourceRepository;
	@Autowired
	private TaxkeyRepository taxkeyRepository;

	@Override
	public void deleteBytaxkeyId(List<String> taxkeyIds) {
		keyitemRepository.deleteBytaxkeyId(taxkeyIds);

	}

	@Override
	public void deleteRelaKeyItem(String tsname, String dsname, String teamName) {
		resourceRepository.deleteResource(tsname, dsname, teamName);
		keyitemRepository.deleteKeyitem(tsname, dsname, teamName);
		taxkeyRepository.deleteTaxkey(tsname, dsname, teamName);

	}

	@Override
	public void deleteRelaKeyItemByTaxaSetId(String tsId) {
		resourceRepository.deleteResourceByTaxaSetId(tsId);
		keyitemRepository.deleteKeyitemByTaxaSetId(tsId);
		taxkeyRepository.deleteTaxkeyByTaxaSetId(tsId);
	}

	@Override
	public void batchResourceImages(Resource r, String url, String string, HttpServletRequest request) {
		// TODO Auto-generated method stub

	}

}
