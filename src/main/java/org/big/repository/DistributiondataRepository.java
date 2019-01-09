package org.big.repository;

import java.util.Date;
import java.util.List;

import org.big.entity.Distributiondata;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *<p><b>Distributiondata的DAO类接口</b></p>
 *<p> Distributiondata的DAO类接口，与User有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/06/13 10:59</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface DistributiondataRepository extends BaseRepository<Distributiondata, String> {
	/**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author BINZI
     * @param findText 条件关键词，这里是模糊匹配
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.Distributiondata>
     */
	@Query(value = "Select dd from Distributiondata dd where ("
			+ "dd.inputer like %?1% or "
			+ "dd.inputtime like %?1% or "
			+ "dd.synchdate like %?1%) and "
			+ "dd.status = 1")
	Page<Distributiondata> searchInfo(String searchText, Pageable pageable);

	/**
     *<b>通过Id查找一个实体</b>
     *<p> 通过Id查找一个实体</p>
     * @author BINZI
     * @param id
     * @return org.big.entity.Distributiondata
     */
	@Query(value = "Select dd from Distributiondata dd where dd.id = ?1")
	Distributiondata findOneById(String id);

	/**
     *<b>通过Id删除一个实体</b>
     *<p> 通过Id删除一个实体</p>
     * @author BINZI
     * @param distributiondataId
     */
	@Modifying
	@Transactional
	@Query("Delete Distributiondata d where d.id =?1")
	void deleteOneById(String distributiondataId);
	
	/**
     *<b>Distributiondata的select列表</b>
     *<p> Distributiondata的select检索列表</p>
     * @author BINZI
     * @param findText
     * @param pageable
     * @return com.alibaba.fastjson.JSON
     */
	@Query(value = "Select d from Distributiondata d where (d.discontent like %?1%) and status = 1")
	Page<Distributiondata> searchByDescriptionInfo(String findText, Pageable pageable);
	
	/**
     *<b>通过TaxonId查询Taxon下的所有Distributiondata实体</b>
     *<p> 通过TaxonId查询Taxon下的所有Distributiondata实体</p>
     * @author BINZI
     * @param taxonId
     */
	@Query(value = "Select d from Distributiondata d Where d.taxon.id = ?1 and status = 1")
	Page<Distributiondata> searchDistributiondatasByTaxonId(Pageable pageable, String taxonId);

	/**
	 * <b>根据taxonId修改Taxon下的Descriptions</b>
	 * <p> 根据taxonId修改Taxon下的Descriptions</p>
	 * @param request
	 * @return
	 */
	@Query(value = "Select d from Distributiondata d Where d.taxon.id = ?1 and status = 1")
	List<Distributiondata> findDistributiondataListByTaxonId(String taxonId);
	//zxy
	@Modifying
	@Transactional
	@Query("Delete Distributiondata c where c.inputtime =?1 and c.sourcesid = ?2")
	void deleteByInputtimeAndSourcesid(Date inputtime,String sourcesid);
	
	
	@Query(value = "Select d from Distributiondata d Where d.geojson like %?1% ")
	List<Distributiondata> findDistrByLikeGeojson(String geojson);
	
	@Transactional
	@Modifying
	@Query(value = "delete from distributiondata where taxon_id  in (select id from taxon where taxaset_id = (select id from taxaset where tsname = ?1 and dataset_id = (select id  from dataset where dsname = ?2 and team_id = (select id from team where name = ?3))))", nativeQuery = true)
	void delDistributiondata(String tsname, String dsname, String teamName);

	@Transactional
	@Modifying
	@Query(value = "delete from distributiondata where taxon_id  in (select id from taxon where taxaset_id = ?1)", nativeQuery = true)
	void delDistributiondataByTaxaSetId(String tsId);
}
