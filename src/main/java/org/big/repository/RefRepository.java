package org.big.repository;

import java.util.List;

import org.big.entity.Ref;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *<p><b>Ref的DAO类接口</b></p>
 *<p> Ref的DAO类接口，与User有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/06/13 10:59</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface RefRepository extends BaseRepository<Ref, String> {
	/**
     *<b>带分页排序的条件查询</b>
     *<p> 带分页排序的条件查询</p>
     * @author BINZI
     * @param findText 条件关键词，这里是模糊匹配
     * @param pageable 分页排序方案实体
     * @return org.springframework.data.domain.Page<org.big.entity.Taxaset>
     */
    @Query(value = "Select r from Ref r "
    		+ "where ("
    		+ "r.author like %?1% or "
    		+ "r.title like %?1% or "
    		+ "r.ptype like %?1% or "
    		+ "r.journal like %?1% or "
    		+ "r.press like %?1% or "
    		+ "r.pyear like %?1% or "
    		+ "r.version like %?1%) and "
    		+ "r.status = 1 and r.inputer = ?2")
	Page<Ref> searchInfo(String searchText, Pageable pageable, String inputer);

    /**
     *<b>根据RefId查找一个Reference实体</b>
     *<p> 据id查找一个实体</p>
     * @author BINZI
     * @param Id 实体的id
     * @return org.big.entity.Ref
     */
	@Query(value = "Select r From Ref r Where r.id = ?1")
	Ref findOneById(String id);
	
    /**
     *<b>根据id删除一个实体</b>
     *<p> 据id删除一个实体</p>
     * @author BINZI
     * @param ID 实体的id
     * @return void
     */
	@Transactional
	@Modifying
	@Query(value = "Delete From Ref r Where r.id = ?1")
	void deleteOneById(String id);

	/**
	 *<b>Ref的select列表</b>
	 *<p> Ref的select检索列表</p>
	 * @author BINZI
	 * @param findText
	 * @param dsId
	 * @param pageable
	 * @return com.alibaba.fastjson.JSON
	 */
	@Query(value = "Select r from Ref r where (r.title like %?1%) and r.status = 1")
	Page<Ref> searchByTitle(String findText, Pageable pageable);

	/**
	 *<b>Ref的select列表</b>
	 *<p> Ref的select检索列表</p>
	 * @author WangTianshan(王天山)
	 * @param findText
	 * @param pageable
	 * @return com.alibaba.fastjson.JSON
	 */
	@Query(value = "Select r from Ref r where (r.refstr like %?1%) and r.status = 1 and r.inputer = ?2")
	Page<Ref> searchByRefstr(String findText, Pageable pageable, String inputer);

	/**
     *<b>根据用户id查找Ref集合</b>
     *<p> 根据用户id查找Ref集合</p>
     * @author BINZI
	 * @param id
	 * @return
	 */
	@Query(value = "Select r From Ref r Where r.inputer = ?1")
	List<Ref> findAllByUserId(String id);
	//--zxy 
//	@Query(value = "Select r From Ref r Where r.remark = ?1")
//	Ref  findByRemark(String remark );
	
	@Query(value = "Select r from Ref r where (r.pyear like %?1%) and (r.title like %?2%) and r.inputer = ?3")
	List<Ref> searchByParams(String year, String title,String userId);
	
	@Query(value = "Select r from Ref r where (r.pyear like %?1%) and (r.author like %?2%)  and r.inputer = ?3 and r.remark = ?4")
	List<Ref> searchByYearAndAuthorAndInpuAndRem(String year, String author ,String inputer,String remark);
	
	
	Ref findByRefstrAndInputer(String refstr,String inputer);
	

}
