package org.big.service;

import javax.servlet.http.HttpServletRequest;

import org.big.entity.Distributiondata;


public interface DistributiondataService {
	
	/**
	 *<b>根据id删除一个实体</b>
     *<p> 根据id删除一个实体</p>
	 * @param request
	 * @return
	 */
	boolean deleteOne(HttpServletRequest request);
	
	Distributiondata saveOne(Distributiondata record);
	
	
}
