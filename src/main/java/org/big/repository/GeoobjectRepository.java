package org.big.repository;

import java.util.List;

import org.big.entity.Geoobject;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *<p><b>Geoobject的DAO类接口</b></p>
 *<p> Geoobject的DAO类接口，与User有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/06/11 10:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface GeoobjectRepository extends BaseRepository<Geoobject, String> {

    /**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author BINZI
     * @param searchText 条件关键词，这里是模糊匹配
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.Taxaset>
     */
    @Query(value = "Select go from Geoobject go where ("
    		+ "go.cngeoname like %?1% or "
    		+ "go.engeoname like %?1%) and "
    		+ "go.status = 1")
	Page<Geoobject> searchInfo(String searchText, Pageable pageable, String geogroup_id);
    
    /**
     *<b>根据id查询一个Geoobject实体</b>
     *<p> 根据id查询一个Geoobject实体</p>
     * @author BINZI
     * @param pageable 分页排序方案实体
     * @return org.big.entity.Geoobject
     */
    @Query(value = "Select go from Geoobject go where go.id = ?1 and go.status = 1")
    Geoobject findOneById(String geoobjectId);
    
    @Query(value = "Select go from Geoobject go where go.cngeoname = ?1 ")
    List<Geoobject> findByCngeoname(String cngeoname);
    
    @Query(value = "select *  from geoobject where id not in  (:ids) and citycode in  ('0') ",nativeQuery = true)
    List<Geoobject>  findByIdNotIn(@Param("ids") List<String> ids);
    
    @Query(value = "Select go from Geoobject go where  go.citycode in  ('0') or go.id in ('DEA27AFE9A4A480B9D9F1061DFAE85F9','6240992072A5401AA54EC65986C6A1C6','D8041FB023F246E5BACA74A62C4B3622','C07768E11C894F7BAE7AA4A6AAE9C307')")
    List<Geoobject> findAll();
    
    @Query(value = "Select go from Geoobject go where cngeoname like %?1% and (go.citycode in  ('0') or go.id in ('DEA27AFE9A4A480B9D9F1061DFAE85F9','6240992072A5401AA54EC65986C6A1C6','D8041FB023F246E5BACA74A62C4B3622','C07768E11C894F7BAE7AA4A6AAE9C307','E5359306A80541A1BB12338548F4CC53'))")
    Geoobject findOneByLikeCngeoname(String cngeoname);
    
    @Query(value = "Select go from Geoobject go where cngeoname like %?1%  order by adcode asc")
    List<Geoobject> findByLikeCngeoname(String cngeoname);

}
