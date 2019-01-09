package org.big.service;

import javax.servlet.http.HttpServletRequest;

import org.big.entity.Multimedia;


public interface MultimediaService {
	


	/**
	 *<b>根据id删除一个已添加的实体</b>
     *<p> 根据id删除一个已添加的实体</p>
	 * @param request
	 * @return
	 */
	boolean deleteOne(HttpServletRequest request);
	
	
	/**
	 *<b>存储Multimedia实体</b>
     *<p> 存储Multimedia实体</p>
	 * @param taxonId
	 * @param thisMultimedia
	 */
	void saveMultimedia(String taxonId, Multimedia thisMultimedia);


}
