package org.big.service;

import java.sql.Timestamp;
import java.util.List;

import org.big.entity.Dataset;
import org.big.entity.Team;
import org.big.repository.DatasetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DatasetServiceImpl implements DatasetService {
	@Autowired
	private DatasetRepository datasetRepository;


	@Override
	public void saveOne(Dataset thisDataset) {
		thisDataset.setSynchdate(new Timestamp(System.currentTimeMillis()));
		if (thisDataset.getDsabstract().equals("Default")) {
			thisDataset.setDsabstract("default");
		}
		this.datasetRepository.save(thisDataset);
	}



	@Override
	public Boolean logicRemove(String Id) {
		Dataset thisDataset = this.datasetRepository.findOneById(Id);
		if (!thisDataset.getMark().equals("Default")) {
			if (1 == thisDataset.getStatus()) {
				thisDataset.setStatus(0);
			} else {
				thisDataset.setStatus(1);
			}
			this.datasetRepository.save(thisDataset);
			return true;
		}
		return false;
	}


	@Override
	public Dataset findbyID(String ID) {
		return this.datasetRepository.getOne(ID);
	}

	@Override
	public Dataset findOneByDsabstractAndTeam(String dsabstraction, Team thisTeam) {
		return this.datasetRepository.findOneByDsabstractAndTeam(dsabstraction, thisTeam);
	}

	@Override
	public Dataset findDefaultByUser() {
		return null;
	}

	@Override
	public List<Dataset> findAllDatasetsByTeamId(String teamId) {
		return this.datasetRepository.findAllDatasetsByTeamId(teamId);
	}


	@Override
	public void updateOneById(Dataset thisDataset) {
		thisDataset.setSynchdate(new Timestamp(System.currentTimeMillis()));
		this.datasetRepository.save(thisDataset);
	}

	@Override
	public long countDatasetByTeam_IdAndStatus(String teamId,int status) {
		return this.datasetRepository.countDatasetByTeam_IdAndStatus(teamId,status);
	}
}