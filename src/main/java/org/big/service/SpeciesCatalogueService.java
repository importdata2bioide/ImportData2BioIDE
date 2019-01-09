package org.big.service;


import org.big.entity.Citation;
import org.big.entity.Distributiondata;
import org.big.entity.Taxon;
import org.big.entityVO.LineStatus;
import org.big.entityVO.BaseParamsForm;
import org.big.entityVO.SpeciesCatalogueEnum;

public interface SpeciesCatalogueService {
	
	public Taxon savefamilyTaxon(Taxon taxon,BaseParamsForm params) throws Exception;
	
	public String getGeojson(String line);
	
	public Citation handleCitation(LineStatus thisLineStatus,SpeciesCatalogueEnum preTaxon,String line,BaseParamsForm params) throws Exception;
	
	public Distributiondata handleDistribution(LineStatus thisLineStatus,SpeciesCatalogueEnum preTaxon,String line,BaseParamsForm params) throws Exception;
	
//	public List<Taxon> getTaxonList();
//	
//	public List<Distributiondata> getDistributionList();
//	
//	public List<Citation> getCitationList();
	/**
	 * 保存分类树
	 * title: SpeciesCatalogueService.java
	 * @author ZXY
	 */
	public void insertTreeByDataSet(BaseParamsForm params);
	/**
	 * 保存Taxon（基础信息）、Citation（引证）、Distributiondata（分布）
	 * title: SpeciesCatalogueService.java
	 * @throws Exception
	 * @author ZXY
	 */
//	public void saveSpeciesCatalogue() throws Exception;
	
	
}
