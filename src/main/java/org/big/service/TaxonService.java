package org.big.service;

import java.util.List;

import org.big.entity.Taxon;
import org.big.entityVO.PartTaxonVO;
import org.big.sp2000.entity.Family;
import org.big.sp2000.entity.ScientificName;


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
	/**
	 * 
	 * @Description 查询某个分类单元集下的taxon信息，并转换成名录families表结构
	 * @param taxasetId
	 * @param taxtreeId
	 * @return
	 * @author ZXY
	 */
	List<Family> getFamilyDataByTaxaset(String taxasetId, String taxtreeId);


	List<ScientificName> getScientificNamesByTaxaset(String taxasetId, String taxtreeId);

}
