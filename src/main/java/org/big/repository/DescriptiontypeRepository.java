package org.big.repository;

import java.util.List;

import org.big.entity.Descriptiontype;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *<p><b>Descriptiontype的DAO类接口</b></p>
 *<p> Descriptiontype的DAO类接口，与User有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/06/13 10:59</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface DescriptiontypeRepository extends BaseRepository<Descriptiontype, String> {
	 /**
     *<b>Descriptiontype的select列表</b>
     *<p> Descriptiontype的select检索列表</p>
     * @author BINZI
     * @param findText
     * @param pageable
     * @return com.alibaba.fastjson.JSON
     */
	@Query(value = "Select dt from Descriptiontype dt where (dt.name like %?1%) order by dt.id asc")
	Page<Descriptiontype> searchByDescTypeList(String findText, Pageable pageable);
	/**
     *<b>根据id查找一个实体</b>
     *<p> 根据id查找一个实体</p>
     * @author BINZI
     * @param Id 实体的id
     * @return org.big.entity.Descriptiontype
     */
	@Query(value = "Select dt From Descriptiontype dt Where dt.id = ?1")
	Descriptiontype findOneById(Integer id);
	
	@Query(value = "Select dt From Descriptiontype dt Where dt.pid = ?1")
	List<Descriptiontype> findByPid(String id);
	//zxy
	@Query(value = "Select dt From Descriptiontype dt Where dt.name = ?1")
	List<Descriptiontype> findByName(String name);
	
	@Query(value = "Select dt From Descriptiontype dt Where dt.name = ?1")
	Descriptiontype findOneByName(String name);
	
	
	

}
