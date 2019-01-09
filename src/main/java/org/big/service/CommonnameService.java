package org.big.service;

import org.big.entity.Commonname;


/**
 *<p><b>Commonname的Service类接口</b></p>
 *<p> Commonname的Service类接口，与Commonname有关的业务逻辑方法</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/6 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
public interface CommonnameService {

    /**
     *<b>根据id查找一个实体</b>
     *<p> 据id查找一个实体</p>
     * @author WangTianshan (王天山)
     * @param ID 实体的id
     * @return org.big.entity.Commonname
     */
    Commonname findbyID(String ID);

    /**
     *<b>根据id删除一个实体</b>
     *<p> 据id删除一个实体</p>
     * @author WangTianshan (王天山)
     * @param ID 实体的id
     * @return void
     */
    void removeOne(String ID);

    /**
     *<b>保存一个实体</b>
     *<p> 保存一个实体</p>
     * @author WangTianshan (王天山)
     * @param thisPerson 实体
     * @return void
     */
    void saveOne(Commonname thisPerson);

   /**
    * 
    * title: CommonnameService.java
    * @param thisPerson
    * @author ZXY
    */
	void save(Commonname thisPerson);
	
}
