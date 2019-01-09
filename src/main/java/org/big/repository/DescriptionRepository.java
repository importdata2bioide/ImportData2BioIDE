package org.big.repository;

import java.util.Date;
import java.util.List;

import org.big.entity.Description;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *<p><b>Description的DAO类接口</b></p>
 *<p> Description的DAO类接口，与User有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/06/13 10:59</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface DescriptionRepository extends BaseRepository<Description, String> {
	/**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author BINZI
     * @param findText 条件关键词，这里是模糊匹配
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.Citation>
     */
	@Query(value = "Select d from Description d where ("
			+ "d.destitle like %?1% or "
			+ "d.describer like %?1% or "
			+ "d.desdate like %?1% or "
			+ "d.rightsholder like %?1% or "
			+ "d.language like %?1% or "
			+ "d.destypeid like %?1% or "
			+ "d.inputer like %?1% or "
			+ "d.inputtime like %?1% or "
			+ "d.synchdate like %?1%) and "
			+ "d.status = 1")
	Page<Description> searchInfo(String searchText, Pageable pageable);

	/**
     *<b>通过Id查找一个实体</b>
     *<p> 通过Id查找一个实体</p>
     * @author BINZI
     * @param id
     * @return org.big.entity.Description
     */
	@Query(value = "Select d from Description d where d.id = ?1")
	Description findOneById(String id);
	
	/**
     *<b>通过Id删除一个实体</b>
     *<p> 通过Id删除一个实体</p>
     * @author BINZI
     * @param descriptionId
     */
	@Modifying
	@Transactional
	@Query("Delete Description d where d.id =?1")
	void deleteOneById(String descriptionId);
	
	/**
     *<b>Traitset的select列表</b>
     *<p> Traitset的select检索列表</p>
     * @author BINZI
     * @param findText
     * @param pageable
     * @return com.alibaba.fastjson.JSON
     */
	@Query(value = "Select d from Description d where (d.destitle like %?1%) and d.status = 1")
	Page<Description> searchByDescriptionInfo(String findText, Pageable pageable);
	
	/**
     *<b>通过TaxonId查询Taxon下的所有Description实体</b>
     *<p> 通过TaxonId查询Taxon下的所有Description实体</p>
     * @author BINZI
     * @param taxonId
     */
	@Query(value = "Select d from Description d Where d.taxon.id = ?1 and d.status = 1")
	Page<Description> searchDescriptionsByTaxonId(Pageable pageable, String taxonId);

	/**
	 * <b>根据taxonId修改Taxon下的Descriptions</b>
	 * <p> 根据taxonId修改Taxon下的Descriptions</p>
	 * @param taxonId
	 * @return
	 */
	@Query(value = "Select d from Description d Where d.taxon.id = ?1 and d.status = 1")
	List<Description> findDescriptionListByTaxonId(String taxonId);

	/**
	 *<b>根据dataset统计符合条件的Description</b>
	 *<p> 根据dataset统计符合条件的Description</p>
	 * @author WangTianshan (王天山)
	 * @param status 状态
	 * @param id Dataset的id
	 * @return long
	 */
	long countByStatusAndTaxon_Taxaset_Dataset_Id(int status,String id);

	/**
	 * <b>Taxon下的描述信息实体的关系下拉选</b>
	 * <p> Taxon下的描述信息实体的关系下拉选</p>
	 *  @author BINZI
	 * @param findText
	 * @param taxonId
	 * @param pageable
	 * @return
	 */
	@Query(value = "Select d from Description d Where (d.destitle like %?1%) and d.taxon.id = ?2 and d.status = 1")
	Page<Description> searchDescListByTaxonId(String findText, String taxonId, Pageable pageable);
	//zxy
	@Query(value = "Select d from Description d Where d.sourcesid = ?1")
	List<Description> searchBythisTime(String sourcesid);
	
//	@Query(value = "Select d from Description d Where d.sourcesid = ?1  and zxyoldrelationid is not null")
//	List<Description> searchBythisSources(String sourcesid);
	
//	@Query(value = "Select d from Description d Where d.zxyoldid = ?1 and  d.sourcesid = ?2")
//	Description searchByOldID(String oldRelationId,String sourcesid);
	
	@Modifying
	@Transactional
	@Query("Delete Description c where c.inputtime =?1 and c.sourcesid = ?2")
	void deleteByInputtimeAndSourcesid(Date inputtime,String sourcesid);
	
	
	
	@Transactional
	@Modifying
	@Query(value = "delete from description where taxon_id  in (select id from taxon where taxaset_id = (select id from taxaset where tsname = ?1 and dataset_id = (select id  from dataset where dsname = ?2 and team_id = (select id from team where name = ?3))))", nativeQuery = true)
	void delDescription(String tsname, String dsname, String teamName);

	@Transactional
	@Modifying
	@Query(value = "delete from description where taxon_id  in (select id from taxon where taxaset_id = ?1)", nativeQuery = true)
	void delDescriptionByTaxaSetId(String tsId);
}
