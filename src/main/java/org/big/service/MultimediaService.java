package org.big.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.big.entity.Multimedia;
import org.big.entity.Taxon;
import org.big.entityVO.BaseParamsForm;


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
	/**
	 * 
	 * @Description 存储图片到本地和数据库
	 * @param taxon
	 * @param baseParamsForm
	 * @param image
	 * @author ZXY
	 * @throws Exception 
	 */
	void saveMultimedia(Taxon taxon,BaseParamsForm baseParamsForm,String image) throws Exception;


}
