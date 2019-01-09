package org.big.repository;

import java.util.List;

import org.big.entity.Dataset;
import org.big.entity.Team;
import org.big.repository.base.BaseRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *<p><b>Dataset的DAO类接口</b></p>
 *<p> Dataset的DAO类接口，与Dataset有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/04/09 16:25</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface DatasetRepository extends BaseRepository<Dataset, String> {
	/**
     *<b>查询指定团队下的所有Dataset</b>
     *<p> 查询指定团队下的所有Dataset</p>
     * @author BINZI
     * @param teamId
     * @return 
     */
	@Query(value = "select d from Dataset d where d.team.id = ?1 and d.status = 1")
	List<Dataset> findAllDatasetsByTeamId(String teamId);
	
	/**
	 * 查询指定团队下的所有Dataset
	 * title: DatasetRepository.java
	 * @param teamId
	 * @param status
	 * @return
	 * @author ZXY
	 */
	@Query(value = "select d from Dataset d where d.team.id = ?1 and d.status = ?2")
	List<Dataset> findAllDatasetsByTeamAndStatus(String teamId,int status);
	
	/**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author BINZI 
     * @param findText 条件关键词，这里是模糊匹配
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.Dataset>
     */
	// 根据dsname & dsabstract & createdDate 模糊 | 排序 | 条件 | 分页查询
   @Query(value = "select d from Dataset d where (d.dsname like %?1% or d.dsabstract like %?1% or d.createdDate like %?1%)")
   Page<Dataset> searchInfo(String findText, Pageable pageable);

    /**
     *<b>带分页排序的条件查询（当前用户組下数据集）</b>
     *<p> 带分页排序的条件查询（当前用户）</p>
     * @author BINZI 
     * @param findText 条件关键词，这里是模糊匹配
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.Dataset>
     */
    @Query(value = "select d from Dataset d where (d.dsname like %?1% or d.dsabstract like %?1% or d.createdDate like %?1%) and d.team.id = ?2")
    Page<Dataset> searchMyTeamDataInfo(String findText, Pageable pageable, String id);

    /**
     *<b>根据id查询一个符合条件的Dataset</b>
     *<p> 根据id查询一个符合条件的Dataset</p>
     * @author BINZI 
     * @param id id
     * @return org.springframework.data.domain.Page<org.big.entity.Dataset>
     */
    @Cacheable(value="findDatasetOneById")
    @Query(value = "select d from Dataset d where d.id = ?1")
    Dataset findOneById(String id);

    /**
     *<b>根据Team的name查询一个符合条件的Team</b>
     *<p> 根据Team的name查询一个符合条件的Team</p>
     * @author BINZI
     * @param dsabstract 备注
     * @param thisTeam 当前用户
     * @return org.springframework.data.domain.Page<org.big.entity.Dataset>
     */
    Dataset findOneByDsabstractAndTeam(String dsabstract,Team thisTeam);

    /**
     *<b>根据TeamId统计有几个dataset</b>
     *<p> 根据TeamId统计有几个dataset</p>
     * @author WangTianshan (王天山)
     * @param teamId
     * @param status
     * @return long
     */
    long countDatasetByTeam_IdAndStatus(String teamId,int status);
    
    //zxy
    @Query(value = "select d from Dataset d where d.dsname = ?1 and d.team.id = ?2")
    Dataset findOneByDsnameAndTeam(String dsname,String thisTeam);
}
