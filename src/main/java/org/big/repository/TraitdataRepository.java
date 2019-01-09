package org.big.repository;

import java.util.List;

import org.big.entity.Traitdata;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TraitdataRepository extends BaseRepository<Traitdata, String> {
	/**
     *<b>通过Id查找一个实体</b>
     *<p> 通过Id查找一个实体</p>
     * @author BINZI
     * @param traitdataId
     * @return org.big.entity.Traitdata
     */
	@Query(value = "Select td from Traitdata td where td.id = ?1")
	Traitdata findOneById(String traitdataId);
	
	/**
     *<b>通过Id删除一个实体</b>
     *<p> 通过Id删除一个实体</p>
     * @author BINZI
     * @param traitdataId
     */
	@Modifying
	@Transactional
	@Query("Delete Traitdata td where td.id =?1")
	void deleteOneById(String traitdataId);
	
	/**
     *<b>通过TaxonId查询Taxon下的所有Traitdata实体</b>
     *<p> 通过TaxonId查询Taxon下的所有Traitdata实体</p>
     * @author BINZI
	 * @param pageable
	 * @param taxonId
	 * @return
     */
	@Query(value = "Select t from Traitdata t Where t.taxon.id = ?1 and t.status = 1")
	Page<Traitdata> searchTraitdatasByTaxonId(Pageable pageable, String taxonId);

	/**
	 * <b>根据taxonId修改Taxon下的Traitdatas</b>
	 * <p> 根据taxonId修改Taxon下的Traitdatas</p>
	 * @param request
	 * @return
	 */
	@Query(value = "Select t from Traitdata t Where t.taxon.id = ?1 and t.status = 1")
	List<Traitdata> findTraitdataListByTaxonId(String taxonId);

}
