package org.big.service;



import org.big.entity.Geoobject;

public interface GeoobjectService {
	
	
	/**
	 *<b>根据id查找Geoobject实体</b>
	 *<p> 根据id查找Geoobject实体</p>
	 * @author BINZI
	 * @param geoobjectId
	 * @return org.big.entity.Geoobjec
	 */
	Geoobject findOneById(String geoobjectId);
	

}
