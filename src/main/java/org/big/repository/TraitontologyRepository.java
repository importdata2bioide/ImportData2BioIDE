package org.big.repository;


import org.big.entity.Traitontology;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
/**
 *<p><b>Traitontology的DAO类接口</b></p>
 *<p> Traitontology的DAO类接口，与Traitontology有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/06/22 10:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface TraitontologyRepository extends BaseRepository<Traitontology, Integer> {
	/**
     *<b>Traitontology的select列表</b>
     *<p> Traitontology的select列表</p>
     * @author BINZI
     * @param findText
     * @param pageable
     * @return com.alibaba.fastjson.JSON
     */
	@Query(value = "Select to from Traitontology to where (to.enterm like %?1% or to.cnterm like %?1%) AND to.id IN "
			+ "(select distinct tht.traitontology.id from TraitsetHasTraitontology AS tht where tht.traitset.id = ?2)")
	Page<Traitontology> searchByTraitontologyInfo(String findText, Pageable pageable, String traitsetId);
	
	/**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author BINZI
     * @param findText 条件关键词，这里是模糊匹配
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.Taxaset>
     */
    @Query(value = "Select to "
    		+ "from Traitontology to "
    		+ "where ("
    		+ "to.enterm like %?1% or "
    		+ "to.cnterm like %?1% or "
    		+ "to.group like %?1% or "
    		+ "to.synchdate like %?1%) and "
    		+ "to.status = 1")
	Page<Traitontology> searchInfo(String searchText, Pageable pageable);
    
    /**
     *<b>根据TraitontologyId查找一个Traitontology实体</b>
     *<p> 据id查找一个实体</p>
     * @author BINZI
     * @param Id 实体的id
     * @return org.big.entity.Traitontology
     */
	@Query(value = "Select to From Traitontology to Where to.id = ?1")
	Traitontology findOneById(String id);

	/*@Query(value = "select t FROM Traitontology as t where t.id in (select tt.traitontology.id from TraitsetHasTraitontology as tt where tt.traitset.id = '?1')")
	List<Traitontology> findBySelect(String trainsetid);*/
	
}
