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
	
	

}
