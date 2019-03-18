package org.big.service;

import java.util.List;

import javax.persistence.Table;


public interface BatchSubmitService {
	/**
	 * 适用于mysql数据库
	 * 实体类@Table注解必须注明name属性，例如 @Table(name = "geogroup")
	 * @Description 安全
	 * @param entities
	 * @return
	 * @throws Exception
	 * @author ZXY
	 */
	<T>int saveAll(List<T> entities) throws Exception;
	

}
