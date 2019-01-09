package org.big.repository;

import org.big.entity.Keyitem;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *<p><b>Keyitem的DAO类接口</b></p>
 *<p> Keyitem的DAO类接口，与Keyitem有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/06/19 15:15</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface KeyitemRepository extends BaseRepository<Keyitem, String> {
	/**
	 *<b>带排序的查询</b>
	 *<p> 带排序的查询</p>
	 * @author WangTianshan（王天山）
	 * @param taxkeyId
	 * @param keytype
	 * @return List<org.big.entity.Keyitem>
	 */
	@Query(value = "Select ki from Keyitem ki where ki.taxkey.id = ?1 and ki.keytype = ?2 order by ki.orderid asc, ki.innerorder asc")
	List<Keyitem> searchInfoSort(String taxkeyId, String keytype);
	/**
     *<b>通过外键Id删除一个实体</b>
     *<p> 通过外键Id删除一个实体</p>
     * @author BINZI
     * @param taxkeyId
     */
	@Modifying
	@Transactional
	@Query("Delete Keyitem ki where ki.taxkey.id =?1")
	void deleteByFk(String taxkeyId);
	
	/**
	 *<b>通过keyitemId查找一个Keyitem实体</b>
     *<p> 通过keyitemId查找一个Keyitem实体</p> 
	 * @param keyitemId
	 * @return org.big.entity.Keyitem
	 */
	Keyitem findOneById(String keyitemId);
	
	/**
	 * <b>带分页的keyitem下的特征图片列表查询</b>
     *<p> 带分页的keyitem下的特征图片列表查询</p>
	 * @author BINZI
	 * @param pageable
	 * @param keyitemId
	 * @return
	 */
	@Query(value = "Select ki from Keyitem ki where ki.id = ?1")
	Page<Keyitem> findFeatureImgList(Pageable pageable, String keyitemId);
	
	
	/**
     *<b>通过外键Id删除一个实体</b>
     *<p> 通过外键Id删除一个实体</p>
     * @author BINZI
     * @param keytemId
     */
	@Modifying
	@Transactional
	@Query("Delete Keyitem ki where ki.id =?1")
	void delOne(String keyitemId);
	
	@Modifying
	@Transactional
	@Query("Delete Keyitem ki where ki.taxkey.id  in (:taxkeyIds)")
	void deleteBytaxkeyId(List<String> taxkeyIds);
	
	@Transactional
	@Modifying
	@Query(value = "delete from keyitem where taxkey_id in (select id from taxkey where taxon_id in (select id  from taxon where taxaset_id = (select id from taxaset where tsname = ?1 and dataset_id = (select id  from dataset where dsname = ?2 and team_id = (select id from team where name = ?3 )))) )", nativeQuery = true)
	void deleteKeyitem(String tsname, String dsname, String teamName);
	
	
	@Transactional
	@Modifying
	@Query(value = "delete from keyitem where taxkey_id in (select id from taxkey where taxon_id in (select id  from taxon where taxaset_id = ?1) )", nativeQuery = true)
	void deleteKeyitemByTaxaSetId(String tsId);
	

}
