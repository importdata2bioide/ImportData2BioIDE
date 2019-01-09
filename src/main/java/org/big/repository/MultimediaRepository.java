package org.big.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.big.entity.Multimedia;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
/**
 *<p><b>Multimedia的DAO类接口</b></p>
 *<p> Multimedia的DAO类接口，与Multimedia有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/7/13 14:25</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */

@Repository
public interface MultimediaRepository extends BaseRepository<Multimedia, String> {
	/**
     *<b>根据用户id查询Multimedia列表</b>
     *<p> 根据用户id查询Multimedia列表，即根据用户id查找其所属的Multimedia列表</p>
     * @param searchText
	 * @param pageable
     * @return
     */
	@Query("Select m from Multimedia m where (m.title like %?1% or m.synchdate like %?1%) and m.status = 1")
	Page<Multimedia> searchInfo(String searchText, Pageable pageable);
	
    /**
     *<b>根据MultimediaId查找一个Multimedia实体</b>
     *<p> 据MultimediaId查找一个Multimedia实体</p>
     * @author BINZI
     * @param Id 实体的id
     * @return org.big.entity.Multimedia
     */
	@Query(value = "Select m From Multimedia m Where m.id = ?1")
	Multimedia findOneById(String id);

	/**
     *<b>通过Id删除一个实体</b>
     *<p> 通过Id删除一个实体</p>
     * @author BINZI
     * @param multimediaId
     */
	@Modifying
	@Transactional
	@Query("Delete Multimedia m where m.id =?1")
	void deleteOneById(String multimediaId);

	/**
     *<b>通过TaxonId查询Taxon下的所有Multimedia实体</b>
     *<p> 通过TaxonId查询Taxon下的所有Multimedia实体</p>
     * @author BINZI
	 * @param pageable
	 * @param taxonId
	 * @return
     */
	@Query(value = "Select m from Multimedia m Where m.taxon.id = ?1 and m.status = 1")
	Page<Multimedia> searchMultimediasByTaxonId(Pageable pageable, String taxonId);

	/**
     *<b>通过TaxonId修改Taxon下的所有Multimedia实体</b>
     *<p> 通过TaxonId修改Taxon下的所有Multimedia实体</p>
     * @author BINZI
	 * @param taxonId
	 * @return
     */
	@Query(value = "Select m from Multimedia m Where m.taxon.id = ?1 and m.status = 1")
	List<Multimedia> findMultimediaListByTaxonId(String taxonId);
	
	//zxy
    @Transactional
    @Modifying
    @Query(value = "delete  from multimedia where taxon_id in (select id from taxon where taxaset_id = (select id from taxaset where tsname = ?1 and dataset_id = (select id  from dataset where dsname = ?2 and team_id = (select id from team where name = ?3))))",nativeQuery = true)
    void delMultimedia(String tsname,String dsname,String teamName);
    
    @Transactional
    @Modifying
    @Query(value = "delete  from multimedia where taxon_id in (select id from taxon where taxaset_id = ?1)",nativeQuery = true)
	void delMultimediaByTaxaSetId(String tsId);

}
