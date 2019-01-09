package org.big.service;


import org.big.entity.License;

public interface LicenseService {
	
	
	/**
	 * <b>根据id查询License实体</b>
	 * <p> 根据id查询License实体</p>
	 * @param id
	 * @return
	 */
	License findOneById(String id);

	
}
