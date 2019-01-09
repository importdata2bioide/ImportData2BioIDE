package org.big.repository;

import org.big.entity.Molecular;
import org.big.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
/**
 *<p><b>Molecular的DAO类接口</b></p>
 *<p> Molecular的DAO类接口，与Molecular有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/7/12 16:25</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */

@Repository
public interface MolecularRepository extends BaseRepository<Molecular, String> {
	/**
     *<b>根据用户id查询Molecular列表</b>
     *<p> 根据用户id查询Molecular列表，即根据用户id查找其所属的Molecular列表</p>
     * @param searchText
	 * @param pageable
     * @return
     */
	@Query("Select m from Molecular m where (m.title like %?1% or m.synchdate like %?1%) and m.status = 1")
	Page<Molecular> searchInfo(String searchText, Pageable pageable);
	
    /**
     *<b>根据MolecularId查找一个Molecular实体</b>
     *<p> 据MolecularId查找一个Molecular实体</p>
     * @author BINZI
     * @param Id 实体的id
     * @return org.big.entity.Molecular
     */
	@Query(value = "Select m From Molecular m Where m.id = ?1")
	Molecular findOneById(String id);

}
