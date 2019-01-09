package org.big.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.big.entity.Occurrence;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
/**
 *<p><b>Occurrence的DAO类接口</b></p>
 *<p> Occurence的DAO类接口，与Occurence有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/7/13 14:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */

@Repository
public interface OccurrenceRepository extends BaseRepository<Occurrence, String> {
	/**
     *<b>根据用户id查询Occurrence列表</b>
     *<p> 根据用户id查询Occurrence列表，即根据用户id查找其所属的Occurrence列表</p>
     * @param searchText
	 * @param pageable
     * @return
     */
	@Query("Select o from Occurrence o where (o.title like %?1% or o.synchdate like %?1%) and o.status = 1")
	Page<Occurrence> searchInfo(String searchText, Pageable pageable);
	
    /**
     *<b>根据OccurrenceId查找一个Occurrence实体</b>
     *<p> 据OccurrenceId查找一个Occurrence实体</p>
     * @author BINZI
     * @param Id 实体的id
     * @return org.big.entity.Occurrence
     */
	@Query(value = "Select o From Occurrence o Where o.id = ?1")
	Occurrence findOneById(String id);

	/**
     *<b>通过Id删除一个实体</b>
     *<p> 通过Id删除一个实体</p>
     * @author BINZI
     * @param occurrenceId
     */
	@Modifying
	@Transactional
	@Query("Delete Occurrence o where o.id =?1")
	void deleteOneById(String occurrenceId);

	/**
     *<b>通过TaxonId查询Taxon下的所有Occurrence实体</b>
     *<p> 通过TaxonId查询Taxon下的所有Occurrence实体</p>
     * @author BINZI
	 * @param pageable
	 * @param taxonId
	 * @return
     */
	@Query(value = "Select o from Occurrence o Where o.taxon.id = ?1 and o.status = 1")
	Page<Occurrence> searchOccurrencesByTaxonId(Pageable pageable, String taxonId);

	/**
     *<b>通过TaxonId修改Taxon下的所有Occurrence实体</b>
     *<p> 通过TaxonId修改Taxon下的所有Occurrence实体</p>
	 * @param taxonId
	 * @return
	 */
	@Query(value = "Select o from Occurrence o Where o.taxon.id = ?1 and o.status = 1")
	List<Occurrence> findOccurrenceListByTaxonId(String taxonId);
}
