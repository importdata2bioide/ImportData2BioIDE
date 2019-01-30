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
	public void batchInsertTaxon(List<Taxon> records,String inputtimeStr)throws Exception  ;
	public void batchInsertCitation(List<Citation> records,String inputtimeStr) throws Exception ;
	public void batchInsertDescription(List<Description> records,String inputtimeStr) throws Exception ;
	public void batchInsertDistributiondata(List<Distributiondata> records,String inputtimeStr) throws Exception ;
	public void batchInsertTaxonHasTaxtree(List<TaxonHasTaxtree> records);
	public void batchInsertCommonname (List<Commonname> records);
	

}
