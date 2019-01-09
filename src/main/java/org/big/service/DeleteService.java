package org.big.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.big.entity.Dataset;
import org.big.entity.Taxaset;
import org.big.entity.Team;

public interface DeleteService {

	String findNodeAndAllChildren(HttpServletRequest request);
	
	String deleteByTaxaSet(HttpServletRequest request);

	String deleteDescription(HttpServletRequest request);

	String deleteTreeByTaxaSet(HttpServletRequest request);

	String deleteByTeam(HttpServletRequest request);

	String deleteByTaxtreeId(String mTaxtreeId);

	List<Team> findAllTeams();

	List<Dataset> findAllDataSetByTeam(String teamId);

	List<Taxaset> findAllTaxaSetByDS(String dataset);

	

}