package org.big.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.big.entity.Distributiondata;
import org.big.sp2000.entity.Distribution;


public interface DistributiondataService {
	
	/**
	 *<b>根据id删除一个实体</b>
     *<p> 根据id删除一个实体</p>
	 * @param request
	 * @return
	 */
	boolean deleteOne(HttpServletRequest request);
	
	Distributiondata saveOne(Distributiondata record);
	/**
	 * 
	 * @Description 查询某个分类单元集下的所有分布信息，并转换成名录Distribution格式
	 * @param taxasetId
	 * @return
	 * @author ZXY
	 */
	List<Distribution> getDistributionByTaxaset(String taxasetId);
	
	
}
