package org.big.repository;

import java.sql.Timestamp;
import java.util.List;

import org.big.entity.Commonname;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *<p><b>Commonname的DAO类接口</b></p>
 *<p> Commonname的DAO类接口，与Commonname有关的持久化操作方法</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/6 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface CommonnameRepository extends BaseRepository<Commonname, String> {

    /**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author WangTianshan (王天山)
     * @param findText 条件关键词，这里是模糊匹配
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.Commonname>
     */
    @Query(value = "select c from Commonname c where (c.commonname like %?1% or c.language like %?1%) and c.status = 1")
    Page<Commonname> searchInfo(String findText, Pageable pageable);
  
	/**
     *<b>通过Id查找一个实体</b>
     *<p> 通过Id查找一个实体</p>
     * @author BINZI
     * @param id
     * @return org.big.entity.Commonname
     */
	@Query(value = "Select cn from Commonname cn where cn.id = ?1")
	Commonname findOneById(String commonnameId);

	/**
     *<b>通过Id删除一个实体</b>
     *<p> 通过Id删除一个实体</p>
     * @author BINZI
     * @param taxkeyId
     */
	@Modifying
	@Transactional
	@Query("Delete Commonname cn where cn.id =?1")
	void deleteOneById(String commonnameId);

	/**
     *<b>通过TaxonId查询Taxon下的所有Commonname实体</b>
     *<p> 通过TaxonId查询Taxon下的所有Commonname实体</p>
     * @author BINZI
     * @param taxonId
     */
	@Query(value = "Select c from Commonname c Where c.taxon.id = ?1 and c.status = 1")
	Page<Commonname> searchCommonnamesByTaxonId(Pageable pageable, String taxonId);

	/**
	 * <b>根据taxonId修改Taxon下的Commonnames</b>
	 * <p> 根据taxonId修改Taxon下的Commonnames</p>
	 * @param taxonId
	 * @return
	 */
	@Query(value = "Select c from Commonname c Where c.taxon.id = ?1 and c.status = 1 order by commonname")
	List<Commonname> findCommonnameListByTaxonId(String taxonId);
	//zxy
	
	@Modifying
	@Transactional
	@Query("Delete Commonname c where c.inputtime =?1")
	void deleteByInputtime(Timestamp inputtime);
	
	@Transactional
	@Modifying
	@Query(value = "delete from commonname where taxon_id  in (select id from taxon where taxaset_id = (select id from taxaset where tsname = ?1 and dataset_id = (select id  from dataset where dsname = ?2 and team_id = (select id from team where name = ?3))))", nativeQuery = true)
	void delCommonname(String tsname, String dsname, String teamName);

	@Transactional
	@Modifying
	@Query(value = "delete from commonname where taxon_id  in (select id from taxon where taxaset_id = ?1)", nativeQuery = true)
	void delCommonnameByTaxaSetId(String tsId);
	
	List<Commonname> findByCommonnameContaining(String contains);
	
	@Query("Select c from Commonname c Where c.taxon.id = ?1 ")
	List<Commonname>  findByTaxonId(String taxonId);
	
	
	
	

}
