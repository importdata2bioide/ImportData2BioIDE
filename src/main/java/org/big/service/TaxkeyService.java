package org.big.service;


import org.big.entity.Taxkey;


public interface TaxkeyService {
	

	/**
	 *<b>根据id删除一个实体</b>
     *<p> 根据id删除一个实体</p>
	 * @param id
	 * @return
	 */
	boolean deleteOneById(String id);
	

	
	
	 /**
     *<b>根据id查找一个Taxkey实体</b>
     *<p> 据id查找一个实体</p>
     * @author BINZI
     * @param id 实体的id
     * @return org.big.entity.Taxkey
     */
	Taxkey findOneById(String taxkeyId);
	
	
	void deleteTaxkey(String tsname, String dsname, String teamName);

	
	void deleteTaxkeyByTaxaSetId(String tsId);
}
