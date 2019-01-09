package org.big.repository;

import org.big.entity.Traitproperty;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
/**
 *<p><b>Traitproperty的DAO类接口</b></p>
 *<p> Traitproperty的DAO类接口，与Traitproperty有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/06/22 10:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface TraitpropertyRepository extends BaseRepository<Traitproperty, Integer> {
	/**
     *<b>Traitproperty的select列表</b>
     *<p> Traitproperty的select列表</p>
     * @author BINZI
     * @param findText
     * @param pageable
     * @return com.alibaba.fastjson.JSON
     */
	@Query(value = "Select tp from Traitproperty tp where (tp.cnterm like %?1%)")
	Page<Traitproperty> searchByTraitpropertyInfo(String findText, Pageable pageable);
   
	/**
     *<b>根据Traitproperty查找一个Traitproperty实体</b>
     *<p> 据id查找一个实体</p>
     * @author BINZI
     * @param Id 实体的id
     * @return org.big.entity.Traitproperty
     */
	@Query(value = "Select tp From Traitproperty tp Where tp.id = ?1")
	Traitproperty findOneById(String id);

}
