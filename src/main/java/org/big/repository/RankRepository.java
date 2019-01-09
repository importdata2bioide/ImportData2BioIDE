package org.big.repository;

import org.big.entity.Rank;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *<p><b>Rank的DAO类接口</b></p>
 *<p> Rank的DAO类接口，与Rank有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/05/21 08:45</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface RankRepository extends BaseRepository<Rank, Integer> {
	/**
     *<b>根据id删除一个实体</b>
     *<p> 据id删除一个实体</p>
     * @author BINZI
     * @param ID 实体的id
     * @return void
     */
	@Transactional
	@Modifying
	@Query(value = "Delete From Rank r Where r.id = ?1")
	void deleteOneById(String Id);
	
	/**
     *<b>保存一个实体</b>
     *<p> 保存一个实体</p>
     * @author BINZI
     * @param thisRank 实体
     * @return void
     */
	@Transactional
	@Modifying
	@Query(value = "Update Rank r Set r.enname = ?1 WHERE id =?2")
	void updateOneById(String enname, String Id);
	
    /**
     *<b>根据rankId查找一个Rank实体</b>
     *<p> 据id查找一个实体</p>
     * @author BINZI
     * @param Id 实体的id
     * @return org.big.entity.Rank
     */
	@Query(value = "Select r from Rank r where r.id = ?1")
    Rank findOneById(String Id);
	
    /**
     *<b>根据EnName查找一个实体</b>
     *<p> 据EnName查找一个实体</p>
     * @author BINZI
     * @param name 实体的name
     * @return org.big.entity.Rank
     */
	@Query(value = "Select r from Rank r where r.enname = ?1")
    Rank findOneByEnname(String EnName);
	/**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author BINZI
     * @param findText 条件关键词，这里是模糊匹配
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.Rank>
     */
	@Query(value = "Select r from Rank r where (r.enname like %?1%)")
	Page<Rank> searchByEnname(String findText, Pageable pageable);
}
