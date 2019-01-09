package org.big.repository;

import java.util.List;

import org.big.entity.Protection;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 *<p><b>Protection的DAO类接口</b></p>
 *<p> Protection的DAO类接口，与User有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/06/13 10:59</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
public interface ProtectionRepository extends BaseRepository<Protection, String> {
	/**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author BINZI
     * @param findText 条件关键词，这里是模糊匹配
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.Distributiondata>
     */
	@Query(value = "Select p from Protection p where ("
			+ "p.protlevel like %?1% or "
			+ "p.proassessment like %?1% or "
			+ "p.inputer like %?1% or "
			+ "p.inputtime like %?1% or "
			+ "p.synchdate like %?1%) and "
			+ "p.status = 1")
	Page<Protection> searchInfo(String searchText, Pageable pageable);

	/**
     *<b>通过Id查找一个实体</b>
     *<p> 通过Id查找一个实体</p>
     * @author BINZI
     * @param id
     * @return org.big.entity.Protection
     */
	@Query(value = "Select p from Protection p where p.id = ?1")
	Protection findOneById(String id);
	
	/**
     *<b>Proteciton的select列表</b>
     *<p> 当前Taxon下的Proteciton的select检索列表</p>
     * @author BINZI
     * @param findText
     * @param pageable
     * @return com.alibaba.fastjson.JSON
     */
	@Query(value = "Select p from Protection p where (p.protlevel like %?1%) and p.status = 1")
	Page<Protection> searchByProtlevel(String findText, Pageable pageable);
	
	/**
     *<b>通过Id删除一个实体</b>
     *<p> 通过Id删除一个实体</p>
     * @author BINZI
     * @param protectionId
     */
	@Modifying
	@Transactional
	@Query("Delete Protection p where p.id =?1")
	void deleteOneById(String protectionId);

	/**
     *<b>通过TaxonId查询Taxon下的所有Protection实体</b>
     *<p> 通过TaxonId查询Taxon下的所有Protection实体</p>
     * @author BINZI
     * @param taxonId
     */
	@Query(value = "Select p from Protection p Where p.taxon.id = ?1 and p.status = 1")
	Page<Protection> searchProtectionsByTaxonId(Pageable pageable, String taxonId);

	/**
	 * <b>根据taxonId修改Taxon下的Protections</b>
	 * <p> 根据taxonId修改Taxon下的Protections</p>
	 * @param request
	 * @return
	 */
	@Query(value = "Select p from Protection p Where p.taxon.id = ?1 and p.status = 1")
	List<Protection> findProtectionListByTaxonId(String taxonId);

}
