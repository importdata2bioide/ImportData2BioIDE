package org.big.repository;

import org.big.entity.Rank;
import org.big.entity.Taxaset;
import org.big.entity.Taxon;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 *<p><b>Taxon的DAO类接口</b></p>
 *<p> Taxon的DAO类接口，与User有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/06/13 10:59</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface TaxonRepository extends BaseRepository<Taxon, String> {
    /**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author BINZI
     * @param searchText 条件关键词，这里是模糊匹配
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.Taxon>
     */
    @Query(value = "Select t from Taxon t where ("
    		+ "t.scientificname like %?1% or "
    		+ "t.authorstr like %?1% or "
    		+ "t.inputer like %?1% or "
    		+ "t.synchdate like %?1%) and t.taxaset.id =?2 and t.status = 1")
	Page<Taxon> searchInfo(String searchText, Pageable pageable, String taxasetId);

    /**
     *<b>根据TaxonId查找一个Taxon实体</b>
     *<p> 据id查找一个实体</p>
     * @author BINZI
     * @param Id 实体的id
     * @return org.big.entity.Taxon
     */
	@Query(value = "Select t From Taxon t Where t.id = ?1")
	Taxon findOneById(String id);
	/**
     *<b>Taxon的select列表</b>
     *<p> 当前Taxkey下的Taxon的select检索列表</p>
     * @author BINZI
     * @param findText
     * @param pageable
     * @return com.alibaba.fastjson.JSON
     */
    @Query(value = "select t from Taxon t where (t.scientificname like %?1%) and t.status = 1 and t.taxaset.id = ?2")
	Page<Taxon> searchByTaxonInfo(String findText, Pageable pageable, String taxonsetId);

	/**
	 *<b>根据Rank和TaxonSet返回TaxonList </b>
	 *<p> 根据Rank和TaxonSet返回TaxonList</p>
	 * @author WangTianshan (王天山)
	 * @param thisTaxaset TaxonSet
	 * @param thisRank Rank
	 * @return org.springframework.data.domain.Page<org.big.entity.Taxon>
	 */
	List<Taxon> findTaxonByTaxasetAndRankAndStatus(Taxaset thisTaxaset, Rank thisRank,int status);


	/**
	 *<b>根据Rank和TaxonSet和关键名字返回TaxonList </b>
	 *<p> 根据Rank和TaxonSet和关键名字返回TaxonList</p>
	 * @author WangTianshan (王天山)
	 * @param thisTaxaset TaxonSet
	 * @param thisRank Rank
	 * @param taxonName 关键名字
	 * @return org.springframework.data.domain.Page<org.big.entity.Taxon>
	 */
	@Query(value = "select t from Taxon t where " +
			"((t.scientificname like %?3%) or (t.chname like %?3%))" +
			" and t.status = ?4" +
			" and t.rank.id = ?2" +
			" and t.taxaset.id = ?1")
	List<Taxon> searchTaxonByTaxasetAndRankAndTaxonNameAndStatus(String thisTaxaset, String thisRank,String taxonName,int status);

	/**
	 *<b>根据dataset统计符合条件的taxon</b>
	 *<p> 根据dataset统计符合条件的taxon</p>
	 * @author WangTianshan (王天山)
	 * @param status 状态
	 * @param id Dataset的id
	 * @return long
	 */
	long countByStatusAndTaxaset_Dataset_Id(int status,String id);

	@Query(value = "select t from Taxon t where t.taxaset.id = ?1")
	List<Taxon> findTaxonByTaxasetId(String taxasetId);
	
	@Query(value = "select t from Taxon t where t.sourcesid = ?1")
	List<Taxon>  findBySourcesidAll(String sourcesid);
	
	@Query(value = "select t from Taxon t where t.id = ?1 and t.sourcesid = ?2")
	Taxon findByTaxonOldId(String id, String sourcesid);
	
	@Query(value = "select t from Taxon t where t.scientificname like ?1 and t.sourcesid = '46f17bc0-c158-4952-bbdb-becd269b88e7'")
	List<Taxon> findLikeScientificname(String scientificname);
	
	//zxy
	@Transactional
	@Modifying
	@Query(value = "delete from taxon where taxaset_id = (select id from taxaset where tsname = ?1 and dataset_id = (select id  from dataset where dsname = ?2 and team_id = (select id from team where name = ?3)))", nativeQuery = true)
	void deleteTaxon(String tsname, String dsname, String teamName);
	
	@Modifying
	@Transactional
	@Query("Delete Taxon c where c.inputtime =?1 and c.sourcesid = ?2 and c.taxaset.id =?3")
	void deleteByInputtimeAndSourcesidAndTsId(Date inputtime,String sourcesid,String taxasetId);
	
	@Modifying
	@Transactional
	@Query(value = "update  taxon c set  c.chname =?1 where c.id = ?2",nativeQuery = true)
	void updateByCnameAndId(String chname,String id);
	
	/**
	 * 根据学名和阶元查询😚
	 * @param scientificname 模糊查询
	 * @param rankid 精确查询
	 * @return
	 */
	@Query(value = "select t.* from taxon t where t.scientificname like %?1% and t.rank_id = ?2  and t.taxaset_id in (select d.id from taxaset d where d.dataset_id = ?3) and t.status = 1",nativeQuery = true)
	List<Taxon> findByTaxonSciNameAndRankAndDSid(String scientificname, String rankid,String datasetId);
	
	
	@Query(value = "select t from Taxon t where t.taxaset.id = ?1")
	List<Taxon> findByTaxaset(String taxasetId);

	@Modifying
	@Transactional
	@Query("Delete Taxon c where  c.taxaset.id =?1")
	void deleteTaxonByTaxaSetId(String tsId);
	
	
}
