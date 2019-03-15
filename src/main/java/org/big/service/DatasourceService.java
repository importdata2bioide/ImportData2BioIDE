package org.big.service;



import java.util.List;

import org.big.entity.Datasource;


public interface DatasourceService {
	
	
   
	/**
	 * <b>根据id查询Datasource实体</b>
	 * <p> 根据id查询Datasource实体</p>
	 * @param id
	 * @return
	 */
	Datasource findOneById(String id);
	/**
	 * 
	 * title: DatasourceService.java
	 * @param title
	 * @param inputer
	 * @return
	 * @author ZXY
	 */
	Datasource findOneByTitleAndInputer(String title,String inputer);
	
	
	
	/**
	 * 根据title和inputerId查询一个Datasource，结果为空，则insert
	 * title: DatasourceService.java
	 * @param title
	 * @param inputerId
	 * @param expertId
	 * @return
	 * @author ZXY
	 */
	Datasource insertDSIfNotExist(String title,String inputerId,String expertId);
	
}
