package org.big.service;

import java.util.List;


public interface BatchSubmitService {
	/**
	 * 
	 * @Description 安全
	 * @param entities
	 * @return
	 * @throws Exception
	 * @author ZXY
	 */
	<T>int saveAll(List<T> entities) throws Exception;
	/**
	 * 
	 * @Description 不安全
	 * @param entities
	 * @return
	 * @throws Exception
	 * @author ZXY
	 */
	<T>int saveAllValues(List<T> entities) throws Exception;

}
