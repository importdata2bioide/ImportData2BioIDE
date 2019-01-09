package org.big.repository;

import java.util.List;

import org.big.entity.Taxtree;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *<p><b>Taxtree的DAO类接口</b></p>
 *<p> Taxtree的DAO类接口，与Taxtree有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/04/12 11:16</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface TaxtreeRepository extends BaseRepository<Taxtree, String> {
    // 自定义条件查询 | 修改 | 删除
    /**
     *<b>根据id删除一个实体</b>
     *<p> 据id删除一个实体</p>
     * @author WangTianshan (王天山)
     * @param id 实体的id
     * @return void
     */
    @Transactional
    @Modifying
    @Query(value = "Delete From Taxtree tt Where tt.id = ?1")
    void deleteOneById(String id);

    /**
     *<b>根据TaxtreeId查找一个Taxtree实体</b>
     *<p> 据id查找一个实体</p>
     * @author WangTianshan (王天山)
     * @param Id 实体的id
     * @return org.big.entity.Taxtree
     */
    @Query(value = "Select tt From Taxtree tt Where tt.id = ?1")
    Taxtree findOneById(String Id);

    /**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author WangTianshan (王天山)
     * @param findText 条件关键词，这里是模糊匹配
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.Taxtree>
     */
    @Query(value = "Select tt from Taxtree tt where (tt.treename like %?1% or tt.treeinfo like %?1%) and tt.status = 1 and tt.dataset.id = ?2")
    Page<Taxtree> searchInfo(String findText,String id,Pageable pageable);


    /**
     *<b>Taxtree的select列表</b>
     *<p> 当前Dataset下的Taxtree的select检索列表</p>
     * @author BINZI
     * @param findText
     * @param dsId
     * @param pageable
     * @return com.alibaba.fastjson.JSON
     */
    @Query(value = "Select tt from Taxtree tt where (tt.treename like %?1%) and tt.dataset.id = ?2 and tt.status = 1")
    Page<Taxtree> searchByTreename(String findText, String dsId, Pageable pageable);

    /**
     *<b>根据dataset统计符合条件的Taxtree</b>
     *<p> 根据dataset统计符合条件的Taxtree</p>
     * @author WangTianshan (王天山)
     * @param status 状态
     * @param id Dataset的id
     * @return long
     */
    long countByStatusAndDataset_Id(int status,String id);
    //zxy
    @Query(value = "Select tt From Taxtree tt Where tt.treename = ?1 and tt.treeinfo = ?2")
    Taxtree findOneByTreenameAndInfo(String treename,String treeinfo);
    
    
    @Query(value = "Select tt From Taxtree tt Where tt.dataset.id = ?1 ")
    List<Taxtree> findByDatasetId(String datasetId);
    

    

}
