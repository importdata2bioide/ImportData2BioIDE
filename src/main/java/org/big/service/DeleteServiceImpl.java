package org.big.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.big.common.CommUtils;
import org.big.entity.Dataset;
import org.big.entity.Distributiondata;
import org.big.entity.Taxaset;
import org.big.entity.TaxonHasTaxtree;
import org.big.entity.Taxtree;
import org.big.entity.Team;
import org.big.repository.CitationRepository;
import org.big.repository.CommonnameRepository;
import org.big.repository.DatasetRepository;
import org.big.repository.DescriptionRepository;
import org.big.repository.DistributiondataRepository;
import org.big.repository.MultimediaRepository;
import org.big.repository.SpecimendataRepository;
import org.big.repository.TaxasetRepository;
import org.big.repository.TaxonHasTaxtreeRepository;
import org.big.repository.TaxonRepository;
import org.big.repository.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Service
public class DeleteServiceImpl implements DeleteService {
	final static Logger logger = LoggerFactory.getLogger(DeleteServiceImpl.class);

	@Autowired
	TaxonRepository taxonRepository;
	@Autowired
	CommonnameRepository commonnameRepository;
	@Autowired
	DescriptionRepository descriptionRepository;
	@Autowired
	DistributiondataRepository distributiondataRepository;
	@Autowired
	MultimediaRepository multimediaRepository;
	@Autowired
	TaxonHasTaxtreeRepository taxonHasTaxtreeRepository;
	@Autowired
	TaxasetRepository taxasetRepository;
	@Autowired
	DatasetRepository datasetRepository;
	@Autowired
	TeamRepository teamRepository;
	@Autowired
	CitationRepository citationRepository;
	@Autowired
	KeyitemService keyitemService;
	@Autowired
	SpecimendataRepository specimendataRepository;
	@Autowired
	TaxtreeService taxtreeService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.big.service.DeleteService#findNodeAndAllChildren(javax.servlet.http.
	 * HttpServletRequest)
	 */
	@Override
	@RequestMapping(value = "/findNodeAndAllChildren")
	@ResponseBody
	public String findNodeAndAllChildren(HttpServletRequest request) {

		String nodeId = request.getParameter("nodeId");
		String taxtreeId = request.getParameter("taxtreeId");

		List<Distributiondata> distributionList = new ArrayList<>();
		List<TaxonHasTaxtree> children = taxtreeService.findNodeAndAllChildren(nodeId, taxtreeId);
		int i = 0;

		for (TaxonHasTaxtree c : children) {
			// 删除一条树，删除一条taxon
//			taxtreeService.removeOneNode(c.getTaxonId(), c.getTaxtreeId());
//			taxonRepository.deleteById(c.getTaxonId());
			List<Distributiondata> distribution = distributiondataRepository
					.findDistributiondataListByTaxonId(c.getTaxonId());
			if (distribution.size() != 0) {
				distributionList.addAll(distribution);
			}
			i++;
			logger.info(i + "\t||\t" + c.getTaxonId() + "\t||\t" + c.getTaxtreeId() + "\t||\t" + c.getPid());
		}
		logger.info("子节点数量：" + children.size());
		logger.info("分布数量：" + distributionList.size());
		// 删除分布,分布已经删除完成
//		distributiondataRepository.deleteInBatch(distributionList);
		return "hello";

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.big.service.DeleteService#deleteByTaxaSet(javax.servlet.http.
	 * HttpServletRequest)
	 */
	@Override
	@RequestMapping(value = "/deleteDataByTaxaSet")
	@ResponseBody
	public String deleteByTaxaSet(HttpServletRequest request) {
		String teamId = request.getParameter("team").trim();
		Team team = teamRepository.findOneById(teamId);
		String dsId = request.getParameter("dataset").trim();
		Dataset dataset = datasetRepository.findOneById(dsId);
		String tsId = request.getParameter("taxaset").trim();
		Taxaset taxaset = taxasetRepository.findOneById(tsId);
		logger.info("正在执行删除操作：请稍后........................");
		logger.info("团队： " + team.getName());
		logger.info("数据集： " + dataset.getDsname());
		logger.info("分类单元集： " + taxaset.getTsname());
		deleteOperById(teamId, dsId, tsId);
		logger.info("执行删除操作已经完成...");

		return "删除完成....";
	}

	private void deleteOperById(String teamId, String dsId, String tsId) {
		// 1.删除多媒体
		logger.info("正在删除多媒体......");
		multimediaRepository.delMultimediaByTaxaSetId(tsId);
		// 2.删除俗名
		logger.info("正在删除俗名......");
		commonnameRepository.delCommonnameByTaxaSetId(tsId);
		// 3.删除分布
		logger.info("正在删除分布......");
		distributiondataRepository.delDistributiondataByTaxaSetId(tsId);
		// 4.删除描述
		logger.info("正在删除描述......");
		descriptionRepository.delDescriptionByTaxaSetId(tsId);
		// 5.删除引证
		logger.info("正在删除引证......");
		citationRepository.deleteCitationByTaxaSetId(tsId);
		// 6.删除分类树
		logger.info("正在删除分类树......");
		taxonHasTaxtreeRepository.deleteTaxonHasTaxtreesByTaxaSetId(tsId);
		// 7.删除检索表
		logger.info("正在删除检索表......");
		keyitemService.deleteRelaKeyItemByTaxaSetId(tsId);
		// 8.删除标本
		logger.info("正在删除标本......");
		specimendataRepository.deleteSpecimendataByTaxaSetId(tsId);
		// 删除taxon
		logger.info("正在删除taxon......");
		taxonRepository.deleteTaxonByTaxaSetId(tsId);

	}

	/**
	 * 
	 * title: DeleteServiceImpl.java
	 * 
	 * @param ts       分类单元集名称
	 * @param ds       数据集名称
	 * @param teamName 团队名称
	 * @author ZXY
	 */
	public void deleteOper(String ts, String ds, String teamName) {
		// 1.删除多媒体
		logger.info("正在删除多媒体......");
		multimediaRepository.delMultimedia(ts, ds, teamName);
		// 2.删除俗名
		logger.info("正在删除俗名......");
		commonnameRepository.delCommonname(ts, ds, teamName);
		// 3.删除分布
		logger.info("正在删除分布......");
		distributiondataRepository.delDistributiondata(ts, ds, teamName);
		// 4.删除描述
		logger.info("正在删除描述......");
		descriptionRepository.delDescription(ts, ds, teamName);
		// 5.删除引证
		logger.info("正在删除引证......");
		citationRepository.deleteCitation(ts, ds, teamName);
		// 6.删除分类树
		logger.info("正在删除分类树......");
		taxonHasTaxtreeRepository.deleteTaxonHasTaxtrees(ts, ds, teamName);
		// 7.删除检索表
		logger.info("正在删除检索表......");
		keyitemService.deleteRelaKeyItem(ts, ds, teamName);
		// 8.删除标本
		logger.info("正在删除标本......");
		specimendataRepository.deleteSpecimendata(ts, ds, teamName);
		// 删除taxon
		logger.info("正在删除taxon......");
		taxonRepository.deleteTaxon(ts, ds, teamName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.big.service.DeleteService#deleteDescription(javax.servlet.http.
	 * HttpServletRequest)
	 */
	@Override
	@RequestMapping(value = "/deleteDescription")
	@ResponseBody
	public String deleteDescription(HttpServletRequest request) {
		String teamName = request.getParameter("teamName");
		Team team = teamRepository.findOneByName(teamName);
		String ds = request.getParameter("datesetName");
		Dataset dataset = datasetRepository.findOneByDsnameAndTeam(ds, team.getId());
		String ts = request.getParameter("taxasetName");
		Taxaset taxaset = taxasetRepository.findOneByTsnameAndDataset(ts, dataset.getId());
		logger.info("正在执行删除操作：请稍后......");
		logger.info("团队： " + team.getName());
		logger.info("数据集： " + dataset.getDsname());
		logger.info("分类单元集： " + taxaset.getTsname());
		logger.info("正在删除描述......");
		descriptionRepository.delDescription(ts, ds, teamName);
		return "删除描述完成....";

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.big.service.DeleteService#deleteTreeByTaxaSet(javax.servlet.http.
	 * HttpServletRequest)
	 */
	@Override
	@RequestMapping(value = "/deleteTreeByTaxaSet")
	@ResponseBody
	public String deleteTreeByTaxaSet(HttpServletRequest request) {
		String teamName = request.getParameter("teamName");
		Team team = teamRepository.findOneByName(teamName);
		String ds = request.getParameter("datesetName");
		Dataset dataset = datasetRepository.findOneByDsnameAndTeam(ds, team.getId());
		String ts = request.getParameter("taxasetName");
		Taxaset taxaset = taxasetRepository.findOneByTsnameAndDataset(ts, dataset.getId());
		logger.info("正在执行删除操作：请稍后......");
		logger.info("团队： " + team.getName());
		logger.info("数据集： " + dataset.getDsname());
		logger.info("分类单元集： " + taxaset.getTsname());
		// 删除分类树
		logger.info("正在删除分类树...");
		taxonHasTaxtreeRepository.deleteTaxonHasTaxtrees(ts, ds, teamName);

		logger.info("执行删除操作已经完成");
		return "删除完成";
	}

	@Override
	public String deleteByTeam(HttpServletRequest request) {
		logger.info("正在执行删除操作：请稍后......");
		Team team = null;
		try {
			team = teamRepository.findOneByName(request.getParameter("teamName"));
		} catch (Exception e) {
			return "根据团队名称无法唯一确定一个团队";
		}
		if (team == null) {
			return "查询不到该团队";
		}
		// 查询该团队下的 status = 1 的数据集
		List<Dataset> datasetsByTeam = datasetRepository.findAllDatasetsByTeamAndStatus(team.getId(), 1);
		logger.info("查询到" + datasetsByTeam.size() + "个数据集");
		for (Dataset dataset : datasetsByTeam) {
			List<Taxaset> taxasetsByDatase = taxasetRepository.findTaxasetsByDatasetId(dataset.getId());
			logger.info("在数据集" + dataset.getDsname() + "下查询到" + taxasetsByDatase.size() + "个分类单元集，逐一执行删除操作");
			for (Taxaset taxaset : taxasetsByDatase) {
				deleteOper(taxaset.getTsname(), dataset.getDsname(), team.getName());
			}
			List<Taxtree> taxtreeByDataset = taxtreeService.findTaxtreeByDataset(dataset.getId());
			logger.info("在数据集" + dataset.getDsname() + "下查询到" + taxtreeByDataset.size() + "个分类树，逐一执行删除操作");
			for (Taxtree taxtree : taxtreeByDataset) {
				taxtreeService.deleteByTaxtreeId(taxtree.getId());
			}
		}
		return "删除完成";
	}

	@Override
	public String deleteByTaxtreeId(String mTaxtreeId) {
		if (CommUtils.isStrEmpty(mTaxtreeId)) {
			return "分类树ID 不能为空";
		}
		Taxtree taxtree = taxtreeService.findOneById(mTaxtreeId);
		if (taxtree == null || CommUtils.isStrEmpty(taxtree.getTreename())) {
			return "查询不到此树！";
		}
		int count = taxtreeService.deleteByTaxtreeId(mTaxtreeId);
		return "删除成功！ 共" + count + "条。";
	}

	@Override
	public List<Team> findAllTeams() {
		return teamRepository.findAll();
	}

	@Override
	public List<Dataset> findAllDataSetByTeam(String teamId) {
		return datasetRepository.findAllDatasetsByTeamId(teamId);
	}

	@Override
	public List<Taxaset> findAllTaxaSetByDS(String dataset) {
		return taxasetRepository.findTaxasetsByDatasetId(dataset);
	}

}
