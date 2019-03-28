package org.big.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ValidationException;

import org.apache.commons.lang.StringUtils;
import org.big.common.CommUtils;
import org.big.common.EntityInit;
import org.big.constant.ConfigConsts;
import org.big.constant.MapConsts;
import org.big.entity.Citation;
import org.big.entity.Commonname;
import org.big.entity.Description;
import org.big.entity.Descriptiontype;
import org.big.entity.Distributiondata;
import org.big.entity.Ref;
import org.big.entity.Taxon;
import org.big.entityVO.BaseParamsForm;
import org.big.entityVO.LanguageEnum;
import org.big.entityVO.LineStatus;
import org.big.entityVO.NametypeEnum;
import org.big.entityVO.RankEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service("parseLineFishWord")
public class ParseLineFishWord implements ParseLine {
	final static Logger logger = LoggerFactory.getLogger(ParseLineFishWord.class);

	@Autowired
	private ToolService toolService;
	@Autowired
	private DescriptiontypeService descriptiontypeService;
	@Autowired
	private DistributiondataService distributiondataService;

	@Override
	public Taxon parseClass(String line, BaseParamsForm params, LineStatus thisLineStatus) {
		Taxon parent = thisLineStatus.getParentOfClass();
		Taxon taxon = subWithPointChar(RankEnum.Class.getIndex(), line, params, "纲", parent);
		String chname = taxon.getChname();
		if (chname.contains(".")) {
			taxon.setChname(chname.substring(chname.indexOf(".") + 1).trim());
		}
		if (chname.contains("．")) {
			taxon.setChname(chname.substring(chname.indexOf("．") + 1).trim());
		}
		return taxon;
	}

	@Override
	public Taxon parseOrder(String line, BaseParamsForm params, LineStatus thisLineStatus) {
		Taxon parent = thisLineStatus.getParentOfOrder();
		Taxon taxon = subWithPointChar(RankEnum.Order.getIndex(), line, params, "目", parent);
		String chname = taxon.getChname();
		if (chname.contains("、")) {
			taxon.setChname(chname.substring(chname.indexOf("、") + 1).trim());
		}
		return taxon;
	}

	@Override
	public Taxon parseFamily(String line, BaseParamsForm params, LineStatus thisLineStatus) {
		Taxon parent = thisLineStatus.getParentOfFamily();
		Taxon taxon = subWithPointChar(RankEnum.family.getIndex(), line, params, "科", parent);
		String chname = taxon.getChname();
		if (chname.startsWith("（")) {
			taxon.setChname(chname.substring(chname.indexOf("）") + 1).trim());
		}
		if (chname.startsWith("(")) {
			taxon.setChname(chname.substring(chname.indexOf(")") + 1).trim());
		}
		return taxon;
	}

	@Override
	public Taxon parseSubfamily(String line, BaseParamsForm params, LineStatus thisLineStatus) {
		Taxon parent = thisLineStatus.getParentOfSubFamily();
		Taxon taxon = subWithPointChar(RankEnum.Subfamily.getIndex(), line, params, "亚科", parent);
		String chname = taxon.getChname();
		if (chname.startsWith("(")) {
			taxon.setChname(chname.substring(chname.indexOf(")") + 1).trim());
		}
		if (chname.contains("）") && !chname.contains("（")) {
			taxon.setChname(chname.substring(chname.indexOf("）") + 1).trim());
		}
		return taxon;
	}

	Taxon subWithPointChar(int rankId, String line, BaseParamsForm params, String pointChar, Taxon parent) {
		Taxon record = new Taxon();
		int index = line.indexOf(pointChar) + pointChar.length();
		String chname = line.substring(0, index);// 中文名称
		String sciname = line.substring(index);
		record.setChname(chname);
		record.setScientificname(sciname);
		record.setRankid(rankId);
		JSONObject jsonRemark = new JSONObject();
		jsonRemark.put(CommUtils.TAXON_REMARK_FROM_FILE, line);
		if (parent != null) {
			jsonRemark.put(CommUtils.TAXON_REMARK_PARENT_ID, parent.getId());
			jsonRemark.put(CommUtils.TAXON_REMARK_PARENT_NAME, parent.getScientificname());
		}
		record.setRemark(String.valueOf(jsonRemark));
		EntityInit.initTaxon(record, params);
		return record;
	}

	Taxon createTaxonWithKnownName(int rankId, String line, BaseParamsForm params, Map<String, String> map,
			Taxon parent) {
		Taxon taxon = new Taxon();
		taxon.setChname(map.get(MapConsts.TAXON_CHNAME));
		taxon.setScientificname(map.get(MapConsts.TAXON_SCI_NAME));
		taxon.setAuthorstr(map.get(MapConsts.TAXON_AUTHOR));
		taxon.setRankid(rankId);
		// 备注
		JSONObject jsonRemark = new JSONObject();
		jsonRemark.put(CommUtils.TAXON_REMARK_FROM_FILE, line);
		if (parent != null) {
			jsonRemark.put(CommUtils.TAXON_REMARK_PARENT_ID, parent.getId());
			jsonRemark.put(CommUtils.TAXON_REMARK_PARENT_NAME, parent.getScientificname());
		}
		taxon.setRemark(String.valueOf(jsonRemark));
		EntityInit.initTaxon(taxon, params);
		return taxon;
	}

	@Override
	public Taxon parseGenus(String line, BaseParamsForm params, LineStatus thisLineStatus,
			Map<String, String> genusMap) {
		Taxon parent = thisLineStatus.getParentOfGenus();
		Taxon taxon = createTaxonWithKnownName(RankEnum.genus.getIndex(), line, params, genusMap, parent);
		// 再次处理中文名
		if (taxon.getChname().contains(".")) {
			String chname = taxon.getChname();
			taxon.setChname(chname.substring(chname.indexOf(".") + 1).trim());
		}
		return taxon;
	}

	@Override
	public Taxon parseSubgenus(String line, BaseParamsForm baseParamsForm, LineStatus thisLineStatus,
			Map<String, String> subgenusMap) {
		Taxon parent = thisLineStatus.getParentOfSubgenus();
		Taxon taxon = createTaxonWithKnownName(RankEnum.Subgenus.getIndex(), line, baseParamsForm, subgenusMap, parent);
		// 再次处理中文名
		if (taxon.getChname().contains(".")) {
			String chname = taxon.getChname();
			taxon.setChname(chname.substring(chname.indexOf(".") + 1).trim());
		}
		return taxon;
	}

	@Override
	public Taxon parseSpecies(String line, BaseParamsForm baseParamsForm, LineStatus thisLineStatus,
			Map<String, String> speciesMap) {
		Taxon parent = thisLineStatus.getParentOfSpecies();
		Taxon taxon = createTaxonWithKnownName(RankEnum.species.getIndex(), line, baseParamsForm, speciesMap, parent);
		// 再次处理中文名
		String chname = taxon.getChname();
		if (chname.startsWith("（")) {
			taxon.setChname(chname.substring(chname.indexOf("）") + 1));
		}
		int upperCaseCount = toolService.getUpperCaseCount(taxon.getScientificname());
		if (upperCaseCount > 1) {
			int caseIndex = toolService.getSecondUpperCaseIndex(line);
			String word = line.substring(caseIndex - 1, caseIndex);// 第二个大写字母的前一位
			if (("（").equals(word) || ("(").equals(word)) {
				// 包含亚属
			} else {
				logger.error(ConfigConsts.HANDLE_ERROR + "种(species)学名可能不正确：" + taxon.getScientificname());
			}
		}
		return taxon;
	}

	@Override
	public Taxon parseSubspecies(String line, BaseParamsForm baseParamsForm, LineStatus thisLineStatus) {
		Taxon parent = thisLineStatus.getParentOfSubspecies();
		Map<String, String> subspMap = new HashMap<>();
		int indexOfFirstLetter = CommUtils.indexOfFirstLetter(line);
		String chname = line.substring(0, indexOfFirstLetter);
		String sciNameAndAuthor = line.substring(indexOfFirstLetter);
		// 去除中文名中的序号
		if (chname.startsWith("(")) {
			chname = chname.substring(chname.indexOf(")") + 1);
		}
		String sciName = toolService.getSciNameFromCitation(sciNameAndAuthor, 3);
		String author = CommUtils.cutByStrAfter(sciNameAndAuthor, sciName);
		subspMap.put(MapConsts.TAXON_CHNAME, chname);
		subspMap.put(MapConsts.TAXON_SCI_NAME, sciName);
		subspMap.put(MapConsts.TAXON_AUTHOR, author);
		// 可能只有亚种，没有种
		if (!sciName.contains(parent.getScientificname())) {
			parent = thisLineStatus.getParentOfSpecies();
		}
		Taxon taxon = createTaxonWithKnownName(RankEnum.subsp.getIndex(), line, baseParamsForm, subspMap, parent);
		int upperCaseCount = toolService.getUpperCaseCount(taxon.getScientificname());
		if (upperCaseCount > 1) {
			logger.error(ConfigConsts.HANDLE_ERROR + "亚种学名可能不正确：" + taxon.getScientificname());
		}
//		System.out.println(taxon.getScientificname()+"___"+upperCaseCount);
		return taxon;
	}

	@Override
	public Citation parseCitation(String line, Taxon preTaxon, BaseParamsForm baseParamsForm) {
		line = handCitationLine(line);
		Citation citation = new Citation();
		// 学名
		String sciName = "";
		int rankid = preTaxon.getRankid();
		if (sciName.contains("：")) {
			sciName = sciName.substring(0, sciName.indexOf("："));
		} else if (sciName.contains(":")) {
			sciName = sciName.substring(0, sciName.indexOf(":"));
		} else if (sciName.contains("：")) {
			sciName = sciName.substring(0, sciName.indexOf(":"));
		} else if (rankid == RankEnum.genus.getIndex()) {
			sciName = toolService.getSciNameFromCitation(line, 1);
		} else if (rankid == RankEnum.Subgenus.getIndex()) {
			sciName = toolService.getSciNameFromCitation(line, 1);
		} else if (rankid == RankEnum.species.getIndex()) {
			sciName = toolService.getSciNameFromCitation(line, 2);
		} else if (rankid == RankEnum.subsp.getIndex()) {
			sciName = toolService.getSciNameFromCitation(line, 3);
			if (sciName.contains("：")) {
				sciName = sciName.substring(0, sciName.indexOf("："));
			} else {
				int lastindexOfSpace = sciName.lastIndexOf(" ") + 1;
				String lastWord = sciName.substring(lastindexOfSpace);
				char firstCharOfLastWord = lastWord.charAt(0);
				if ('A' <= firstCharOfLastWord && firstCharOfLastWord <= 'Z') {
					sciName = sciName.substring(0, lastindexOfSpace).trim();
				}
			}
		} else {
			throw new ValidationException("未定义的rankid = " + rankid);
		}
		citation.setSciname(sciName);
		// nameType
		if (preTaxon.getScientificname().equals(sciName)) {
			citation.setNametype(NametypeEnum.acceptedName.getIndex());// 接受名引证
		} else {
			citation.setNametype(NametypeEnum.synonym.getIndex());// 异名引证
		}
		// 命名人和命名年代
		String year = toolService.getYear(line);
		if (StringUtils.isNotEmpty(year)) {
			String author = line.substring(line.indexOf(sciName) + sciName.length(),
					line.indexOf(year) + year.length());
			if (author.startsWith(":") || author.startsWith("：")) {
				author = author.substring(1);
			}
			citation.setAuthorship(author);
		}
		// taxon
		citation.setTaxon(preTaxon);
		// 引证原文
		citation.setCitationstr(line);
		EntityInit.initCitation(citation, baseParamsForm);
		return citation;
	}

	@Override
	public String handCitationLine(String line) {
		String theLastCharacter = line.substring(line.length() - 1);
		if (theLastCharacter.equals("。") || theLastCharacter.equals(".")) {
			line = line.substring(0, line.length() - 1);// 删除最后一个句号
		}
		line = line.trim() + ".";
		return line;
	}

	@Override
	public List<Commonname> parseCommonName(String line, Taxon preTaxon, BaseParamsForm baseParamsForm) {
		List<Commonname> list = new ArrayList<>();
		line = line.replace("别名（common name）：", "");
		line = line.replace("。", "");
		String[] names = line.split("、");
		for (String name : names) {
			Commonname record = new Commonname();
			record.setCommonname(name);
			record.setLanguage(String.valueOf(LanguageEnum.chinese.getIndex()));
			record.setTaxon(preTaxon);
			EntityInit.initCommonname(record, baseParamsForm);
			list.add(record);
		}
		return list;
	}

	@Override
	public Description parseDesc(String line, Taxon preTaxon, BaseParamsForm baseParamsForm, String descType) {
		Descriptiontype descriptiontype = descriptiontypeService.findOneByName(descType);
		Description record = new Description();
		record.setDestitle(descType);
		record.setDescontent(line);
		record.setDestypeid(descriptiontype.getId());
		record.setDescriptiontype(descriptiontype);
		record.setLanguage(String.valueOf(LanguageEnum.chinese.getIndex()));
		record.setTaxon(preTaxon);
		EntityInit.initDescription(record, baseParamsForm);
		return record;
	}

	@Override
	public Distributiondata parseDistribution(String line, Taxon preTaxon, BaseParamsForm baseParamsForm,
			Description desc) {
		Distributiondata record = new Distributiondata();
		record.setDiscontent(line);
		line = line.replace("分布（distribution）：", "");
		String disJsonByLine = distributiondataService.getDisJsonByLine(line);
		if (StringUtils.isEmpty(disJsonByLine)) {
			return null;
		}
		record.setTaxon(preTaxon);
		record.setGeojson(disJsonByLine);
		record.setTaxonid(preTaxon.getId());
		record.setDescid(desc.getId());
		EntityInit.initDistributiondata(record, baseParamsForm);
		return record;
	}

	@Override
	public List<Ref> parseRefs(String line, Taxon preTaxon, BaseParamsForm baseParamsForm) {
//		List<Ref> reflist = new ArrayList<>();
//		line = line.replace("文献(reference)：", "");
//		String[] refs = line.split(";");
//		for (String str : refs) {
//			System.out.println(str);
//		}
		return null;
	}

}
