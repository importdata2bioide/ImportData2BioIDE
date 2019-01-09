package org.big.service;

import javax.servlet.http.HttpServletRequest;


public interface OccurrenceService {
	
	/**
	 *<b>根据id删除一个已添加的实体</b>
     *<p> 根据id删除一个已添加的实体</p>
	 * @param request
	 * @return
	 */
	boolean deleteOne(HttpServletRequest request);

}
