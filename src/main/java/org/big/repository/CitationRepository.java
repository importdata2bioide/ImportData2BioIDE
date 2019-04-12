package org.big.repository;

import java.util.Date;
import java.util.List;

import org.big.entity.Citation;

import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *<p><b>Citation的DAO类接口</b></p>
 *<p> Citation的DAO类接口，与User有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/06/11 10:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface CitationRepository extends BaseRepository<Citation, String> {
	/**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author BINZI
     * @param findText 条件关键词，这里是模糊匹配
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.Citation>
     */
	@Query(value = "Select c from Citation c where ("
			+ "c.sciname like %?1% or "
			+ "c.authorship like %?1% or "
			+ "c.nametype like %?1% or "
			+ "c.inputer like %?1% or "
			+ "c.inputtime like %?1% or "
			+ "c.synchdate like %?1%) and "
			+ "c.status = 1")
	Page<Citation> searchInfo(String searchText, Pageable pageable);

	/**
	 *<b>根据id查询一个实体</b>
     *<p> 带分页排序的条件查询</p>
	 * @param id
	 * @return
	 */
	@Query(value = "Select c from Citation c Where c.id = ?1")
	Citation findOneById(String id);
	
	/**
     *<b>通过Id删除一个实体</b>
     *<p> 通过Id删除一个实体</p>
     * @author BINZI
     * @param citationId
     */
	@Modifying
	@Transactional
	@Query("Delete Citation c where c.id =?1")
	void deleteOneById(String citationId);
	
	/**
     *<b>通过TaxonId查询Taxon下的所有Citation实体</b>
     *<p> 通过TaxonId查询Taxon下的所有Citation实体</p>
     * @author BINZI
     * @param taxonId
     */
	@Query(value = "Select c from Citation c Where c.taxon.id = ?1 and c.status = 1")
	Page<Citation> searchCitationsByTaxonId(Pageable pageable, String taxonId);
	
	/**
	 * <b>根据taxonId修改Taxon下的Citations</b>
	 * <p> 根据taxonId修改Taxon下的Citations</p>
	 * @param request
	 * @return
	 */
	@Query(value = "Select c from Citation c Where c.taxon.id = ?1")
	List<Citation> findCitationListByTaxonId(String taxonId);
	
	//zxy
	@Modifying
	@Transactional
	@Query("Delete Citation c where c.inputtime =?1 and c.sourcesid = ?2")
	void deleteByInputtimeAndSourcesid(Date inputtime,String sourcesid);
	
	
	@Transactional
	@Modifying
	@Query(value = "delete from citation where taxon_id  in (select id from taxon where taxaset_id = (select id from taxaset where tsname = ?1 and dataset_id = (select id  from dataset where dsname = ?2 and team_id = (select id from team where name = ?3))))", nativeQuery = true)
	void deleteCitation(String tsname, String dsname, String teamName);

	@Transactional
	@Modifying
	@Query(value = "delete from citation where taxon_id  in (select id from taxon where taxaset_id = ?1)", nativeQuery = true)
	void deleteCitationByTaxaSetId(String tsId);
	
	
	
	@Query(value = "select a.id,a.taxon_id,a.sciname,a.authorship,a.nameType,a.citationstr,b.rank_id from citation a left join taxon b on a.taxon_id = b.id where b.taxaset_id = ?1 and a.nametype!=?2 and b.rank_id in (?3)",nativeQuery = true)
	List<Object[]> findByNametypeAndTaxaSetAndRankIn(String taxasetId,int nametypeNotEq,List<String> rankNameIn);
	
	@Query(value = "select c.id,c.sciname,c.authorship,c.nametype,c.taxon_id from citation c left join taxon t on c.taxon_id = t.id left join taxaset b on t.taxaset_id = b.id where b.dataset_id =?1 and c.sciname =?2",nativeQuery = true)
	List<Object[]> findByDatasetAndSciName(String datasetId, String sciname);
	
	@Query(value = "Select c from Citation c Where c.taxon.id = ?1 and c.sciname = ?2")
	Citation findByTaxonIdAndSciname(String taxonId, String acceptName);
	
	//Containing 	like '%xxx%'
	List<Citation> findByAuthorshipContaining(String authorship);
	
	List<Citation> findByScinameEndingWith(String sciname);
	
	
	@Query(value = "select a.id,a.scientificname,b.tsname,e.sl from taxon a left join taxaset b on a.taxaset_id = b.id  left join  (select count(*) as sl,taxon_id,scientificname,rank_id,nametype from ( select a.id as taxon_id ,a.scientificname,a.rank_id,c.id,c.sciname,c.nametype from taxon a left join taxaset b on a.taxaset_id = b.id  left join citation c on c.taxon_id = a.id   where b.dataset_id = :datasetId and a.rank_id in (:rankIds)  and nametype = :nameType  ) re group by taxon_id,scientificname,rank_id,nametype ) e on a.id = e.taxon_id where b.dataset_id = :datasetId and a.rank_id in (:rankIds) ",nativeQuery = true)
	List<Object[]> countByTaxonAndNameType(String datasetId,List<String> rankIds,String nameType);
//	
//	
//	
	@Query(value = "select a.scientificname,c.id,c.sciname,c.authorship,c.nametype,c.citationstr,c.refjson,a.id as taxon_id,c.remark from taxon a  left join citation c on c.taxon_id = a.id   left join taxaset b on a.taxaset_id = b.id   where b.dataset_id = :datasetId and a.rank_id in (:rankIds) order by a.scientificname",nativeQuery = true)
	List<Object[]> findByTaxasetId(String datasetId,List<String> rankIds);
	
	@Query(value = "select  c.*  from citation  c left join taxon t on t.id = c.taxon_id  where t.scientificname = :scientificname and t.taxaset_id = :tsId ",nativeQuery = true)
	List<Citation> findByScientificnameAndtsId(String scientificname,String tsId);

	@Query(value = "select id,sciname from citation",nativeQuery = true)
	List<Object[]>  findAllIdAndSciname();
	
	@Query(value = "select id from citation where taxon_id = :taxonId and nametype = :nametype",nativeQuery = true)
	List<String>  findIdByTaxonIdAndNametype(String taxonId,int nametype);
	
	@Query(value = "select id,sciname,authorship,nametype,citationstr,refjson from citation where taxon_id = :taxonId and nametype = :nametype",nativeQuery = true)
	List<Object[]>  findByTaxonIdAndNametype(String taxonId,int nametype);
	
	
	
	@Query(value = "select  c.*  from citation  c left join taxon t on t.id = c.taxon_id  where t.scientificname = :scientificname and t.taxaset_id = :tsId and c.nametype = :nametype ",nativeQuery = true)
	List<Citation> findByScientificnameAndtsIdAndNameType(String scientificname, String tsId,int nametype);
	
	/**
	 * 
	 * @Description 根据数据集ID和名称类型查询
	 * @param datasetId
	 * @param index
	 * @return
	 * @author ZXY
	 */
	@Query(value = "select ts.tsname,t.scientificname,c.id,c.sciname,c.authorship,c.nametype,c.citationstr,t.chname from citation c  left join taxon  t on t.id = c.taxon_id left join taxaset ts on t.taxaset_id = ts.id where ts.dataset_id =:datasetId and c.nametype = :nameType  order by ts.tsname,t.scientificname",nativeQuery = true)
	List<Object[]> findByDsAndNameType(String datasetId, int nameType);
	
	
	
	@Modifying
	@Transactional
	@Query("delete from Citation c where c.id in (:ids) ")
	int deleteByIds(List<String> ids);

	@Query(value = "select ts.tsname,t.scientificname,c.id,c.sciname,c.authorship,c.nametype,c.citationstr,t.chname from citation c  left join taxon  t on t.id = c.taxon_id left join taxaset ts on t.taxaset_id = ts.id where ts.dataset_id =:datasetId and c.nametype = :nameType  and (c.citationstr is null or c.citationstr = '')",nativeQuery = true)
	List<Object[]> findByDsAndNameTypeAndCitationstrNull(String datasetId, int nameType);
	
}
