package org.big.repository;


import org.big.entity.Traitontology;
import org.big.repository.base.BaseRepository;

import org.springframework.stereotype.Repository;
/**
 *<p><b>Traitontology的DAO类接口</b></p>
 *<p> Traitontology的DAO类接口，与Traitontology有关的持久化操作方法</p>
 * @author BINZI
 *<p>Created date: 2018/06/22 10:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Repository
public interface TraitontologyRepository extends BaseRepository<Traitontology, Integer> {
	

	
}
