package org.big.repository;

import java.util.List;

import org.big.entity.Taxkey;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *<p><b>Taxkey的DAO类接口</b></p>
 *<p> Taxkey的DAO类接口，与User有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/06/19 15:15</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface TaxkeyRepository extends BaseRepository<Taxkey, String> {
	/**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author BINZI
     * @param findText 条件关键词，这里是模糊匹配
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.Taxkey>
     */
	@Query(value = "Select tk from Taxkey tk where tk.keytitle like %?1%")
	Page<Taxkey> searchInfo(String searchText, Pageable pageable);
	
	/**
     *<b>通过Id查找一个实体</b>
     *<p> 通过Id查找一个实体</p>
     * @author BINZI
     * @param id
     * @return org.big.entity.Taxkey
     */
	@Query(value = "Select tk from Taxkey tk where tk.id = ?1")
	Taxkey findOneById(String taxkeyId);

	/**
     *<b>通过Id删除一个实体</b>
     *<p> 通过Id删除一个实体</p>
     * @author BINZI
     * @param taxkeyId
     */
	@Modifying
	@Transactional
	@Query("Delete Taxkey tk where tk.id =?1")
	void deleteOneById(String taxkeyId);

	/**
     *<b>通过TaxonId查询Taxon下的所有Taxkey实体</b>
     *<p> 通过TaxonId查询Taxon下的所有Taxkey实体</p>
     * @author BINZI
     * @param pageable
	 * @param taxonId
	 * @return
     */
	@Query(value = "Select t from Taxkey t Where t.taxon.id = ?1")
	Page<Taxkey> searchTaxkeysByTaxonId(Pageable pageable, String taxonId);

	/**
     *<b>通过TaxonId修改Taxon下的所有Taxkey实体</b>
     *<p> 通过TaxonId修改Taxon下的所有Taxkey实体</p>
     * @author BINZI
     * @param pageable
	 * @param taxonId
	 * @return
     */
	@Query(value = "Select t from Taxkey t Where t.taxon.id = ?1")
	List<Taxkey> findTaxkeyListByTaxonId(String taxonId);
	
	@Modifying
	@Transactional
	@Query("Delete Taxkey tk where tk.id in (:ids)")
	void deleteByIds(List<String> ids);
	
	
	@Transactional
	@Modifying
	@Query(value = "delete from taxkey where taxon_id in (select id  from taxon where taxaset_id = (select id from taxaset where tsname = ?1 and dataset_id = (select id  from dataset where dsname = ?2 and team_id = (select id from team where name = ?3  ))))", nativeQuery = true)
	void deleteTaxkey(String tsname, String dsname, String teamName);

	@Transactional
	@Modifying
	@Query(value = "delete from taxkey where taxon_id in (select id  from taxon where taxaset_id = ?1)", nativeQuery = true)
	void deleteTaxkeyByTaxaSetId(String tsId);

}
