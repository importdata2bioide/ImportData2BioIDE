package org.big.repository;

import org.big.entity.Specimendata;
import org.big.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
@Repository
public interface SpecimendataRepository extends BaseRepository<Specimendata, String> {
	
	@Transactional
	@Modifying
	@Query(value = "delete from specimendata where taxon_id in (select id  from taxon where taxaset_id = (select id from taxaset where tsname = ?1  and dataset_id = (select id  from dataset where dsname = ?2 and team_id = (select id from team where name = ?3 ))))", nativeQuery = true)
	void deleteSpecimendata(String tsname, String dsname, String teamName);

	@Transactional
	@Modifying
	@Query(value = "delete from specimendata where taxon_id in (select id  from taxon where taxaset_id = ?1)", nativeQuery = true)
	void deleteSpecimendataByTaxaSetId(String tsId);

}
