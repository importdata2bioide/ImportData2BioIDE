package org.big.service;

import java.util.List;

import org.big.repository.TaxonHasTaxtreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 
 * @author ZXY
 *
 */
@Service
public class CodeFactoryServiceImpl implements CodeFactoryService {
	 @Autowired
	 private TaxonHasTaxtreeRepository taxonHasTaxtreeRepository;
	 
	 public List<String> findAllTable () {
		 return taxonHasTaxtreeRepository.findAllTable("biodata");
	 }

	@Override
	public String batchExecuteCode(String[] tableNames) {
		
		
		for (String tableName : tableNames) {
			System.out.println("-----"+tableName);
		}
		return null;
	}

	
}
