package org.big.repository;

import org.big.entity.Expert;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface ExpertRepository extends BaseRepository<Expert, Integer> {

    /**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author BINZI
     * @param searchText 条件关键词，这里是模糊匹配
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.License>
     */
    @Query(value = "Select e from Expert e where (e.cnName like %?1% or e.enName like %?1% or e.cnAddress like %?1% or e.enAddress like %?1% or e.expEmail like %?1%)")
	Page<Expert> findExpertList(String searchText, Pageable pageable);

	/**
     *<b>根据Expert的属性查询Expert列表</b>
     *<p> 根据Expert的属性查询Expert列表</p>
     * @param findText
	 * @param pageable
     * @return
     */
    @Query(value = "Select e from Expert e where (e.cnName like %?1% or e.expEmail like %?1%)")
	Page<Expert> searchByInfo(String findText, Pageable pageable);
    
    /**
     *<b>根据id属性查询Expert对象</b>
     *<p> 根据id属性查询Expert对象</p>
     * @param id
     * @return
     */
	Expert findById(String id);
	
	/**
	 * 
	 * title: ExpertRepository.java
	 * @param cnName
	 * @param inputer
	 * @return
	 * @author ZXY
	 */
	Expert findOneByCnNameAndInputer(String cnName,String inputer);

}
