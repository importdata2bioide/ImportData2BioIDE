package org.big.repository;

import java.util.List;

import org.big.entity.Taxaset;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *<p><b>Taxaset的DAO类接口</b></p>
 *<p> Taxaset的DAO类接口，与Taxaset有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/05/21 08:45</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface TaxasetRepository extends BaseRepository<Taxaset, String> {
	// 自定义条件查询 | 修改 | 删除
    /**
     *<b>根据id删除一个实体</b>
     *<p> 据id删除一个实体</p>
     * @author BINZI
     * @param ID 实体的id
     * @return void
     */
	@Modifying
	@Transactional
	@Query(value = "Delete From Taxaset ts Where ts.id = ?1")
	void deleteOneById(String id);
	
    /**
     *<b>根据TaxasetId查找一个Taxaset实体</b>
     *<p> 据id查找一个实体</p>
     * @author BINZI
     * @param Id 实体的id
     * @return org.big.entity.Taxaset
     */
	@Query(value = "Select ts From Taxaset ts Where ts.id = ?1")
	Taxaset findOneById(String Id);
	
	/**
     *<b>根据tsName查找一个实体</b>
     *<p> 据tsName查找一个实体</p>
     * @author BINZI
     * @param name 实体的name
     * @return org.big.entity.Taxaset
     */
	@Query(value = "Select ts From Taxaset ts Where ts.tsname = ?1")
	Taxaset findOneByTsname(String tsname);
	
    /**
     *<b>查找指定Dataset下的所有Taxaset</b>
     *<p> 查找指定Dataset下的所有Taxaset</p>
     * @author BINZI
     * @param id
     * @return java.util.List 
     */
	@Query(value = "Select ts From Taxaset ts Where ts.dataset.id = ?1")
	List<Taxaset> findTaxasetsByDatasetId(String dsId);
	
	/**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author WangTianshan (王天山)
     * @param findText 条件关键词，这里是模糊匹配
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.Taxaset>
     */
    @Query(value = "Select ts from Taxaset ts where (ts.tsname like %?1% or ts.tsinfo like %?1% or ts.createdDate like %?1%) and ts.status = 1 and ts.dataset.id = ?2")
    Page<Taxaset> searchInfo(String findText, Pageable pageable, String id);
    
    
    /**
     *<b>Taxaset的select列表</b>
     *<p> 当前Dataset下的Taxaset的select检索列表</p>
     * @author BINZI
     * @param findText
     * @param dsId
     * @param pageable
     * @return com.alibaba.fastjson.JSON
     */
    @Query(value = "Select ts from Taxaset ts where (ts.tsname like %?1%) and ts.dataset.id = ?2 and ts.status = 1")
    Page<Taxaset> searchByTsname(String findText, String dsId, Pageable pageable);
    
    
    @Query(value = "Select ts From Taxaset ts Where ts.tsname = ?1 and ts.dataset.id = ?2")
	Taxaset findOneByTsnameAndDataset(String tsname,String dataset);
}
