package org.big.repository;

import java.util.List;

import org.big.entity.Datasource;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *<p><b>Datasource的DAO类接口</b></p>
 *<p> Datasource的DAO类接口，与User有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/06/11 10:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface DatasourceRepository extends BaseRepository<Datasource, String> {
    /**
     *<b>Datasources的select列表</b>
     *<p> 当前Dataset下的Datasources的select检索列表</p>
     * @author BINZI
     * @param findText
     * @param datasetID
     * @param pageable
     * @return com.alibaba.fastjson.JSON
     */
    @Query(value = "select ds from Datasource ds where (ds.title like %?1%) and ds.status = 1 and ds.inputer = ?2")
	Page<Datasource> searchByDsname(String findText, String inputer, Pageable pageable);
    
    /**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author BINZI
     * @param findText 条件关键词，这里是模糊匹配
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.Taxaset>
     */
    @Query(value = "Select ds from Datasource ds where ("
    		+ "ds.title like %?1% or "
    		+ "ds.inputer like %?1% or "
    		+ "ds.inputtime like %?1% or "
    		+ "ds.versions like %?1%) and "
    		+ "ds.status = 1 and "
    		+ "ds.inputer = ?2")
	Page<Datasource> searchInfo(String searchText, Pageable pageable, String inputer);
   
    /**
     *<b>根据DatasourceId查找一个Datasource实体</b>
     *<p> 根据id查找一个实体</p>
     * @author BINZI
     * @param Id 实体的id
     * @return org.big.entity.Taxaset
     */
	@Query(value = "Select ds From Datasource ds Where ds.id = ?1")
	Datasource findOneById(String id);
    /**
     *<b>根据id删除一个实体</b>
     *<p> 据id删除一个实体</p>
     * @author BINZI
     * @param ID 实体的id
     * @return void
     */
	@Transactional
	@Modifying
	@Query(value = "Delete From Datasource ds Where ds.id = ?1")
	void deleteOneById(String id);
	/**
     *<b>根据用户id查找Datasource集合</b>
     *<p> 根据用户id查找Datasource集合</p>
     * @author BINZI
	 * @param id
	 * @return
	 */
	@Query(value = "Select ds From Datasource ds Where ds.inputer = ?1")
	List<Datasource> findAllByUserId(String uid);
	
	//zxy
	
	@Query(value = "Select ds From Datasource ds Where ds.title = ?1 ")
	Datasource findOneByTitle(String title);
	
	@Query(value = "Select ds From Datasource ds Where ds.title = ?1 and ds.inputer = ?2")
	Datasource findOneByTitleAndInputer(String title,String inputer);

}
