package org.big.service;

import java.util.List;

import org.big.entity.Taxon;


public interface TaxonService {
	/**
	 * 
	 * title: TaxonService.java
	 * @param taxon
	 * @author ZXY
	 */
	void saveOne(Taxon taxon);

   
	// 自定义Taxon增删改查
	 /**
     *<b>根据TaxonId查找一个Taxon实体</b>
     *<p> 据id查找一个实体</p>
     * @author BINZI
     * @param id 实体的id
     * @return org.big.entity.Taxon
     */
	Taxon findOneById(String id);

	
	/**
	 *<b>根据分类单元id查taxon列表</b>
	 *<p> 根据分类单元id查taxon列表</p>
	 * @author  BINZI
	 * @param taxasetId
	 * @return
	 */
	List<Taxon> findTaxonByTaxasetId(String taxasetId);

}
