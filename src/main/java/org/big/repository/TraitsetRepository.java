package org.big.repository;

import org.big.entity.Traitset;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *<p><b>Traitset的DAO类接口</b></p>
 *<p> Traitset的DAO类接口，与Traitontology有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/06/22 10:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface TraitsetRepository extends BaseRepository<Traitset, String> {
	/**
     *<b>Traitset的select列表</b>
     *<p> Traitset的select检索列表</p>
     * @author BINZI
     * @param findText
     * @param pageable
     * @return com.alibaba.fastjson.JSON
     */
	@Query(value = "Select ts from Traitset ts where (ts.name like %?1%)")
	Page<Traitset> searchByTraitsetInfo(String findText, Pageable pageable);
	
	/**
     *<b>根据id查找一个实体</b>
     *<p> 根据id查找一个实体</p>
     * @author BINZI
     * @param Id 实体的id
     * @return org.big.entity.Descriptiontype
     */
	@Query(value = "Select ts From Traitset ts Where ts.id = ?1")
	Traitset findOneById(String id);
	
	/**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author BINZI
     * @param findText 条件关键词，这里是模糊匹配
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.Traitset>
     */
    @Query(value = "Select ts from Traitset ts where (ts.name like %?1% or ts.inputer like %?1% or ts.inputtime like %?1% or ts.synchdate like %?1%) and ts.status = 1")
	Page<Traitset> searchInfo(String searchText, Pageable pageable);
    /**
     *<b>根据id删除一个实体</b>
     *<p> 据id删除一个实体</p>
     * @author BINZI
     * @param ID 实体的id
     * @return void
     */
	@Transactional
	@Modifying
	@Query(value = "Delete From Traitset ts Where ts.id = ?1")
	void deleteOneById(String id);

}
