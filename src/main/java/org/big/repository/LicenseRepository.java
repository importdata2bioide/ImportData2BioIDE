package org.big.repository;

import org.big.entity.License;
import org.big.repository.base.BaseRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *<p><b>License的DAO类接口</b></p>
 *<p> License的DAO类接口，与User有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/06/13 10:59</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface LicenseRepository extends BaseRepository<License, String> {
	/**
     *<b>根据用户enname查询License列表</b>
     *<p> 根据用户enname查询License列表，即根据用户id查找其所属的Occurrence列表</p>
     * @param findText
	 * @param pageable
     * @return
     */
	@Query(value = "Select L from License L where (L.title like %?1% or L.text like %?1%) and L.status = 1")
	Page<License> searchByEnname(String findText, Pageable pageable);
	
	/**
     *<b>根据Id查找一个License实体</b>
     *<p> 据Id查找一个License实体</p>
     * @author BINZI
     * @param Id 实体的id
     * @return org.big.entity.License
     */
	@Cacheable(value="oneLicense")
	@Query(value = "Select L from License L where L.id = ?1")
	License findOneById(String licenseId);

    /**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author BINZI
     * @param searchText 条件关键词，这里是模糊匹配
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.License>
     */
    @Query(value = "Select L from License L where (L.title like %?1% or L.text like %?1%)")
	Page<License> findLicenseList(String searchText, Pageable pageable);
}
