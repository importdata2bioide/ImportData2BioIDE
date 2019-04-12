package org.big.service;

import java.util.List;

import org.big.entity.Citation;
import org.big.entity.Commonname;
import org.big.entity.Description;
import org.big.entity.Distributiondata;
import org.big.entity.Taxon;
import org.big.entity.TaxonHasTaxtree;
/**
 * 
 * @Description 数据库连接参数需要增加rewriteBatchedStatements=true
 * @author ZXY
 */
public interface BatchInsertService {
	/**
	 * 
	 * @Description 基础信息
	 * @param records
	 * @param inputtimeStr
	 * @throws Exception
	 * @author ZXY
	 */
	public void batchInsertTaxon(List<Taxon> records,String inputtimeStr)throws Exception  ;
	/**
	 * 
	 * @Description 引证
	 * @param records
	 * @param inputtimeStr
	 * @throws Exception
	 * @author ZXY
	 */
	public void batchInsertCitation(List<Citation> records,String inputtimeStr) throws Exception ;
	/**
	 * 
	 * @Description 更新引证表完整引证和参考文献字段
	 * @param records
	 * @author ZXY
	 */
	public void batchUpdateCitation(List<Citation> records);
	
	/**
	 * 
	 * @Description 描述
	 * @param records
	 * @param inputtimeStr
	 * @throws Exception
	 * @author ZXY
	 */
	public void batchInsertDescription(List<Description> records,String inputtimeStr) throws Exception ;
	/**
	 * 
	 * @Description 分布
	 * @param records
	 * @param inputtimeStr
	 * @throws Exception
	 * @author ZXY
	 */
	public void batchInsertDistributiondata(List<Distributiondata> records,String inputtimeStr) throws Exception ;
	/**
	 * 
	 * @Description 俗名
	 * @param records
	 * @author ZXY
	 */
	public void batchInsertCommonname (List<Commonname> records);
	/**
	 * 
	 * @Description 
	 * @param records
	 * @author ZXY
	 */
	public void batchInsertTaxonHasTaxtree(List<TaxonHasTaxtree> records);
	
	public void batchUpdateTaxonOrderNumById(List<Taxon> records);
	
	/**
	 * 
	 * @Description 
	 * @param records
	 * @author ZXY
	 */
	public void batchUpdateTaxonAuthorstrById(List<Taxon> records);
	/**
	 * 
	 * @Description 根据主键更新authorship、citationstr、remark字段
	 * @param records
	 * @author ZXY
	 */
	public void batchUpdateCitationById(List<Citation> records);
	
	/**
	 * 
	 * @Description 根据主键更新authorship，citationstr，refjson，remark字段
	 * @param records
	 * @author ZXY
	 */
	public void batchUpdateCitationFourById(List<Citation> records);
	
	/**
	 * 
	 * @Description 根据主键更新authorship、sciname字段
	 * @param records
	 * @author ZXY
	 */
	public void batchUpdateNameAndAuthorById(List<Citation> records);
	/**
	 * 
	 * @Description 更新TaxonHasTaxtree.preTaxon
	 * @param list
	 * @author ZXY
	 */
	public void updateTaxonHasTaxtree(List<TaxonHasTaxtree> list);

}
