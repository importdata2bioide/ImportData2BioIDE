package org.big.service;


import org.big.entity.Descriptiontype;


public interface DescriptiontypeService {
	
	/**
     *<b>根据Id查找一个实体</b>
     *<p> 根据Id查找一个实体</p>
     * @author BINZI
	 * @param id
     * @return org.big.entity.Descriptiontype
     */
	Descriptiontype findOneById(Integer id);
	/**
	 * 
	 * title: DescriptiontypeService.java
	 * @param name
	 * @return
	 * @author ZXY
	 */
	Descriptiontype findOneByName(String name);
	/**
	 * 
	 * @Description 
	 * @param name
	 * @param style
	 * @return
	 * @author ZXY
	 */
	Descriptiontype insertOneDescType(String name,String style);
	
	Descriptiontype insertOrFind(String name);
}