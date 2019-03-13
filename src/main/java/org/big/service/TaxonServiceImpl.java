package org.big.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.ValidationException;

import org.apache.commons.lang.StringUtils;
import org.big.common.CommUtils;
import org.big.common.RankRange;
import org.big.entity.Rank;
import org.big.entity.Taxon;
import org.big.entity.TaxonHasTaxtree;
import org.big.entityVO.PartTaxonVO;
import org.big.repository.RankRepository;
import org.big.repository.TaxonHasTaxtreeRepository;
import org.big.repository.TaxonRepository;
import org.big.sp2000.entity.Family;
import org.big.sp2000.entity.ScientificName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebInputException;

@Service
public class TaxonServiceImpl implements TaxonService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private TaxonRepository taxonRepository;
	@Autowired
	private TaxonHasTaxtreeRepository taxonHasTaxtreeRepository;
	@Autowired
	private RankRepository rankRepository;

	@Override
	public void saveOne(Taxon taxon) {
		taxonRepository.save(taxon);

	}

	@Override
	public Taxon findOneById(String id) {
		return taxonRepository.findOneById(id);
	}

	@Override
	public List<Taxon> findTaxonByTaxasetId(String taxasetId) {
		return taxonRepository.findByTaxaset(taxasetId);
	}

	private List<PartTaxonVO> findFamilyByTaxaset(String taxasetId) {
		List<Object[]> familylist = taxonRepository.findByTaxasetAndRank(taxasetId, "Family");
		return turnObject2TaxonToFamiles(familylist);
	}

	private Map<String, PartTaxonVO> findHigherThanFamilyByTaxaset(String taxasetId) {
		List<Object[]> higherThanfamilylist = taxonRepository.findByTaxasetAndRankIn(taxasetId,
				RankRange.higherThanfamilyRankNames());
		return turnToPartTaxonVOMap(higherThanfamilylist);
	}

	public Map<String, PartTaxonVO> turnToPartTaxonVOMap(List<Object[]> higherThanfamilylist) {
		Map<String, PartTaxonVO> map = new HashMap<>();
		for (Object[] obj : higherThanfamilylist) {
			
			PartTaxonVO entity = turnPartTaxonVO(obj);;
			map.put(entity.getId(), entity);
		}
		return map;
	}

	private PartTaxonVO turnPartTaxonVO(Object[] obj) {
		 return new PartTaxonVO(String.valueOf(obj[0]), String.valueOf(obj[1]), String.valueOf(obj[2]),
				Integer.parseInt(obj[3].toString()), String.valueOf(obj[4]),String.valueOf(obj[5]),String.valueOf(obj[6]));
	}

	private List<PartTaxonVO> turnObject2TaxonToFamiles(List<Object[]> objs) {
		List<PartTaxonVO> list = null;
		if (objs != null && objs.size() > 0) {
			list = new ArrayList<>();
			for (Object[] obj : objs) {
				list.add(turnPartTaxonVO(obj));
			}
		}
		return list;
	}

	@Override
	public List<Family> getFamilyDataByTaxaset(String taxasetId, String taxtreeId) {
		// 1、查询分类单元集下rank =科的taxon
		List<PartTaxonVO> familylist = findFamilyByTaxaset(taxasetId);
		// 2、查询分类单元集下rank高于科（不包含科）的所有taxon
		Map<String, PartTaxonVO> higherThanfamilyMap = findHigherThanFamilyByTaxaset(taxasetId);
		// 3、根据分类树查询对应关系
		List<TaxonHasTaxtree> relationlist = taxonHasTaxtreeRepository.findByTaxtreeId(taxtreeId);
		Map<String, String> relationMap = convertToMap(relationlist);
		// 4、重建结构
		List<Family> list = new ArrayList<>(familylist.size() + 5);

		for (PartTaxonVO part : familylist) {
			Family entity = new Family();
			String id = part.getId();
			entity.setRecordId(id);
			entity.setFamily(part.getScientificname());
			entity.setFamilyC(part.getChname());
			rebuild(id, relationMap, higherThanfamilyMap, entity, taxtreeId);
			list.add(entity);

		}
		return list;
	}

	private Map<String, String> convertToMap(List<TaxonHasTaxtree> relationlist) {
		Map<String, String> map = new HashMap<>();
		for (TaxonHasTaxtree taxonHasTaxtree : relationlist) {
			String taxonId = taxonHasTaxtree.getTaxonId();
			String pid = taxonHasTaxtree.getPid();
			map.put(taxonId, pid);
		}
		return map;
	}

	private void rebuild(String id, Map<String, String> relationMap, Map<String, PartTaxonVO> higherThanfamilyMap,
			Family entity, String taxtreeId) {
		String pid = relationMap.get(id);
		// 判断是否已经查询到根节点
		if (taxtreeId.equals(pid) || StringUtils.isEmpty(pid)) {
			return;
		}
		PartTaxonVO parentTaxon = higherThanfamilyMap.get(pid);
		if (parentTaxon == null) {
			return;
		}
		int rankId = parentTaxon.getRankId();
		String scientificname = parentTaxon.getScientificname();
		String chname = parentTaxon.getChname();
		switch (rankId) {
		case 1:// 界
			entity.setKingdom(scientificname);
			entity.setKingdomC(chname);
			break;
		case 2:// 门
			entity.setPhylum(scientificname);
			entity.setPhylumC(chname);
			break;
		case 3:// 纲
			entity.setClass_(scientificname);
			entity.setClassC(chname);
			break;
		case 4:// 目
			entity.setOrder(scientificname);
			entity.setOrderC(chname);
			break;
		case 40:
			entity.setSuperfamily(scientificname);
			entity.setSuperfamilyC(chname);
			break;
		default:
			throw new ValidationException("未定义的rank");
		}
		rebuild(parentTaxon.getId(), relationMap, higherThanfamilyMap, entity, taxtreeId);
	}

	@Override
	public List<ScientificName> getScientificNamesByTaxaset(String taxasetId, String taxtreeId) {
		// 某分类单元集下rank<=family的所有taxon
		Map<String, PartTaxonVO> lowerThanfamilyMap = turnToPartTaxonVOMap(
				taxonRepository.findByTaxasetAndRankIn(taxasetId, RankRange.lowerThanfamilyInculdeRankNames()));
		// 根据分类树查询出的上下级关系
		Map<String, String> relationMap = convertToMap(taxonHasTaxtreeRepository.findByTaxtreeId(taxtreeId));
		// insert的数据
		// 分类单元集下的所有species
		List<PartTaxonVO> specieslist = turnObject2TaxonToFamiles(
				taxonRepository.findByTaxasetAndRank(taxasetId, "Species"));
		for (PartTaxonVO speciesTaxon : specieslist) {
			ScientificName scientificName = new ScientificName();
			String id = speciesTaxon.getId();
			scientificName.setNameCode(id);
			getHigherUntilGenusInfo(scientificName, id, lowerThanfamilyMap, relationMap, taxtreeId);
			scientificName.setSpecies(speciesTaxon.getScientificname());
			scientificName.setSpeciesC(speciesTaxon.getChname());
			scientificName.setAcceptedNameCode(id);
			scientificName.setScrutinyDate("2019-03-13");
			scientificName.setSp2000StatusId(1);
			scientificName.setAuthor(speciesTaxon.getAuthorstr());
			scientificName.setFamilyId(getFaimlyId(id,relationMap,lowerThanfamilyMap));
			scientificName.setIsAcceptedName(1);
			scientificName.setCanonicalName(speciesTaxon.getScientificname());//种阶元是属+空格+种加词拉丁名
			String originalText = CommUtils.strToJSONObject(speciesTaxon.getRemark()).get("originalText").toString();
			scientificName.setComments(CommUtils.cutByStrAfter(originalText, speciesTaxon.getChname()).trim());//一般是名称全称，即canonical_name+作者信息
		}
		// 某分类单元集下的所有subspecies
		List<PartTaxonVO> subspecieslist = turnObject2TaxonToFamiles(
				taxonRepository.findByTaxasetAndRank(taxasetId, "subspecies"));
		
		return null;
	}

	private String getFaimlyId(String id, Map<String, String> relationMap, Map<String, PartTaxonVO> lowerThanfamilyMap) {
		String pid = relationMap.get(id);
		PartTaxonVO parentTaxon = lowerThanfamilyMap.get(pid);
		if(parentTaxon.getRankId()==5) {
			return parentTaxon.getId();
		}else {
			getFaimlyId(parentTaxon.getId(),relationMap,lowerThanfamilyMap);
		}
		return null;
	}

	/**
	 * 
	 * @Description 填充学名和中文名，直到属（包含属信息）
	 * @param scientificName
	 * @param id
	 * @param lowerThanfamilyMap
	 * @param relationMap
	 * @param taxtreeId
	 * @author ZXY
	 */
	private void getHigherUntilGenusInfo(ScientificName scientificName, String id,
			Map<String, PartTaxonVO> lowerThanfamilyMap, Map<String, String> relationMap, String taxtreeId) {
		String pid = relationMap.get(id);
		// 判断是否已经查询到根节点
		if (taxtreeId.equals(pid) || StringUtils.isEmpty(pid)) {
			return;
		}
		// 有可能是脏数据
		PartTaxonVO parentTaxon = lowerThanfamilyMap.get(pid);
		if (parentTaxon == null) {
			return;
		}
		int rankId = parentTaxon.getRankId();
		Rank rank = rankRepository.findOneById(String.valueOf(rankId));
		if (RankRange.higherThanGenusRankNames().contains(rank.getEnname())) {
			return;
		}
		String scientificname = parentTaxon.getScientificname();
		String chname = parentTaxon.getChname();
		switch (rankId) {
		case 6:// 属
			scientificName.setGenus(scientificname);
			scientificName.setGenusC(chname);
			break;
		case 7:// 种
			scientificName.setSpecies(scientificname);
			scientificName.setSpeciesC(chname);
			break;
		default:
			break;
		}
		getHigherUntilGenusInfo(scientificName, pid, lowerThanfamilyMap, relationMap, taxtreeId);

	}

}
