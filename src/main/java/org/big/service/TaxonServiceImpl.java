package org.big.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ValidationException;

import org.apache.commons.lang.StringUtils;
import org.big.common.CommUtils;
import org.big.common.RankRange;
import org.big.entity.Citation;
import org.big.entity.Rank;
import org.big.entity.Taxon;
import org.big.entity.TaxonHasTaxtree;
import org.big.entityVO.NametypeEnum;
import org.big.entityVO.PartTaxonVO;
import org.big.entityVO.RankEnum;
import org.big.repository.CitationRepository;
import org.big.repository.RankRepository;
import org.big.repository.TaxonHasTaxtreeRepository;
import org.big.repository.TaxonRepository;
import org.big.sp2000.entity.Family;
import org.big.sp2000.entity.ScientificName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

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
	@Autowired
	private CitationRepository citationRepository;

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

			PartTaxonVO entity = turnPartTaxonVO(obj);
			;
			map.put(entity.getId(), entity);
		}
		return map;
	}

	private PartTaxonVO turnPartTaxonVO(Object[] obj) {
		return new PartTaxonVO(String.valueOf(obj[0]), String.valueOf(obj[1]), String.valueOf(obj[2]),
				Integer.parseInt(obj[3].toString()), String.valueOf(obj[4]), String.valueOf(obj[5]),
				String.valueOf(obj[6]));
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
		// 1、分类单元集下的所有species
		List<PartTaxonVO> specieslist = turnObject2TaxonToFamiles(
				taxonRepository.findByTaxasetAndRank(taxasetId, "Species"));
		List<ScientificName> scientificNamelist = new ArrayList<>();
		for (PartTaxonVO speciesTaxon : specieslist) {
			ScientificName scientificName = new ScientificName();
			String id = speciesTaxon.getId();
			scientificName.setNameCode(id);
			getHigherUntilGenusInfo(scientificName, id, lowerThanfamilyMap, relationMap, taxtreeId);
			scientificName.setSpecies(speciesTaxon.getEpithet().trim());
			scientificName.setSpeciesC(speciesTaxon.getChname());
			scientificName.setAcceptedNameCode(id);
			scientificName.setScrutinyDate(CommUtils.getCurrentDate("yyyy-MM-dd"));
			scientificName.setSp2000StatusId(1);
			scientificName.setAuthor(speciesTaxon.getAuthorstr());
			scientificName.setFamilyId(getPointRankId(id, relationMap, lowerThanfamilyMap, RankEnum.family.getIndex()));
			scientificName.setIsAcceptedName(1);
			scientificName.setCanonicalName(scientificName.getGenus() + " " + scientificName.getSpecies());// 种阶元是属+空格+种加词拉丁名
			String comments = speciesTaxon.getScientificname().trim()+" "+speciesTaxon.getAuthorstr();
			scientificName.setComments(comments.trim());// 一般是名称全称，即canonical_name+作者信息
			scientificNamelist.add(scientificName);
		}
		// 2、某分类单元集下的所有subspecies
		List<PartTaxonVO> subspecieslist = turnObject2TaxonToFamiles(
				taxonRepository.findByTaxasetAndRank(taxasetId, "subspecies"));
		for (PartTaxonVO subspeciesTaxon : subspecieslist) {
			ScientificName scientificName = new ScientificName();
			String id = subspeciesTaxon.getId();
			scientificName.setNameCode(id);
			getHigherUntilGenusInfo(scientificName, id, lowerThanfamilyMap, relationMap, taxtreeId);
			scientificName.setInfraspecies(subspeciesTaxon.getEpithet().trim());
			scientificName.setInfraspeciesC(subspeciesTaxon.getChname());
			scientificName.setAcceptedNameCode(
					getPointRankId(id, relationMap, lowerThanfamilyMap, RankEnum.species.getIndex()));
			scientificName.setScrutinyDate(CommUtils.getCurrentDate("yyyy-MM-dd"));
			scientificName.setSp2000StatusId(1);
			scientificName.setAuthor(subspeciesTaxon.getAuthorstr());
			scientificName.setFamilyId(getPointRankId(id, relationMap, lowerThanfamilyMap, RankEnum.family.getIndex()));
			scientificName.setIsAcceptedName(1);// 如果是接受名，则为1，否则为0
			scientificName.setCanonicalName(scientificName.getGenus() + " " + scientificName.getSpecies() + " "
					+ scientificName.getInfraspecies());// 种阶元是属+空格+种加词拉丁名
			if (StringUtils.isNotEmpty(scientificName.getSpecies())
					&& !subspeciesTaxon.getScientificname().contains(scientificName.getSpecies())) {
				throw new ValidationException("亚种和种的父级关系不对：亚种id=" + subspeciesTaxon.getId() + "，亚种中文名="
						+ subspeciesTaxon.getChname() + ",亚种学名=" + subspeciesTaxon.getScientificname());
			}
			String comments = subspeciesTaxon.getScientificname().trim()+" "+subspeciesTaxon.getAuthorstr();
			scientificName.setComments(comments.trim());// 一般是名称全称，即canonical_name+作者信息
			scientificName.setInfraspeciesMarker("subsp.");
			scientificNamelist.add(scientificName);
		}
		// 3、species和subspecies的所有异名
		List<String> rankNameIn = new ArrayList<>();
		rankNameIn.add(String.valueOf(RankEnum.species.getIndex()));
		rankNameIn.add(String.valueOf(RankEnum.subsp.getIndex()));
		List<Citation> ctationlist = convertToCitation(citationRepository.findByNametypeAndTaxaSetAndRankIn(taxasetId,
				NametypeEnum.acceptedName.getIndex(), rankNameIn));
		for (Citation citation : ctationlist) {
			ScientificName scientificName = new ScientificName();
			scientificName.setNameCode(citation.getId());// 主键
			String sciname = citation.getSciname().trim();
			if (sciname.contains("(")) {// 去掉亚属
				sciname = CommUtils.cutByStrBefore(sciname, "(").trim() + " "
						+ CommUtils.cutByStrAfter(sciname, ")").trim();
			}
			getHigherUntilGenusInfo(scientificName, citation.getTaxon().getId(), lowerThanfamilyMap, relationMap,
					taxtreeId);
			int rankid = citation.getTaxon().getRankid();
			switch (rankid) {
			case 7:// species的异名
				String[] spli = sciname.split(" ");
				if (spli.length >= 2) {
					scientificName.setSpecies(spli[1].trim());

				}
				if (spli.length >= 3) {
					scientificName.setInfraspecies(spli[spli.length-1].trim());
				}
//				scientificName.setSpecies(sciname.substring(sciname.lastIndexOf(" ")).trim());
				break;
			case 42:// subspecies的异名
				String[] splits = sciname.split(" ");
				if (splits.length >= 2) {
					scientificName.setSpecies(splits[1].trim());

				}
				if (splits.length >= 3) {
					scientificName.setInfraspecies(splits[splits.length-1].trim());
				}
				break;
			default:
				throw new ValidationException("CITATION 0000C 未定义的rank:" + rankid);
			}
			scientificName.setSpeciesC(null);
			scientificName.setAuthor(citation.getAuthorship());
			scientificName.setAcceptedNameCode(citation.getTaxon().getId());// 指向taxon
			scientificName.setScrutinyDate(CommUtils.getCurrentDate("yyyy-MM-dd"));
			scientificName.setSp2000StatusId(citation.getNametype());
			scientificName.setFamilyId(getPointRankId(citation.getTaxon().getId(), relationMap, lowerThanfamilyMap,
					RankEnum.family.getIndex()));
			scientificName.setIsAcceptedName(0);// 如果是接受名，则为1，否则为0
			scientificName.setCanonicalName(sciname);
			scientificName.setComments(citation.getCitationstr());
			scientificName.setInfraspeciesMarker("var.");
			scientificNamelist.add(scientificName);
		}
		return scientificNamelist;
	}

	private void fileSpeciesAndInfraspecies(ScientificName scientificName, String sciname) {
		// TODO Auto-generated method stub
		
	}

	private List<Citation> convertToCitation(List<Object[]> findByNametypeAndTaxaSet) {
		List<Citation> list = new ArrayList<>();
		for (Object[] obj : findByNametypeAndTaxaSet) {
			if (obj[2] == null) {
				continue;
			}
			Citation c = new Citation();
			c.setId(obj[0].toString());
			c.setTaxon(new Taxon(obj[1].toString(), obj[6].toString()));
			c.setSciname(obj[2].toString());
			c.setAuthorship(obj[3].toString());
			c.setNametype(Integer.parseInt(obj[4].toString()));
			c.setCitationstr(obj[5].toString());
			list.add(c);
		}
		return list;
	}

	private String getPointRankId(String id, Map<String, String> relationMap,
			Map<String, PartTaxonVO> lowerThanfamilyMap, int rankId) {
		String pointTaxonId = "";
		String pid = "";
		try {
			pid = relationMap.get(id);
			PartTaxonVO parentTaxon = lowerThanfamilyMap.get(pid);
			if (parentTaxon.getRankId() == rankId) {
				pointTaxonId = parentTaxon.getId();
				return pointTaxonId;
			} else {
				pointTaxonId = getPointRankId(parentTaxon.getId(), relationMap, lowerThanfamilyMap, rankId);
				return pointTaxonId;
			}
		} catch (Exception e) {
			throw new ValidationException("获取指定rank失败，id=" + id + ",pid=" + pid);
		}
	}

	/**
	 * 
	 * @Description 填充学名和中文名，直到属（包含属信息）
	 * @param scientificName
	 * @param id                 taxonId,此taxon的学名和中文名不会放到scientificName中
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
		String epither = parentTaxon.getEpithet();
		String scientificname = parentTaxon.getScientificname();
		String chname = parentTaxon.getChname();
		switch (rankId) {
		case 6:// 属
			scientificName.setGenus(scientificname);
			scientificName.setGenusC(chname);
			break;
		case 7:// 种
			scientificName.setSpecies(epither);
			scientificName.setSpeciesC(chname);
			break;
		default:
			break;
		}
		getHigherUntilGenusInfo(scientificName, pid, lowerThanfamilyMap, relationMap, taxtreeId);

	}

}
