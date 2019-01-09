package org.big.service;


/**
 *<p><b>Resource的Service类接口</b></p>
 *<p> Resource的Service类接口，与Resource有关的业务逻辑方法</p>
 * @author MengMeng (王孟豪)
 *<p>Created date: 2018/08/23 </p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
public interface ResourceService {

	/**
     *<b>删除单个remove</b>
     *<p> 根据remove删除单个</p>
     * @author MengMeng (王孟豪)
 	 * @param id
     * @return boolean
     */
	boolean delOne(String id);
	

}
