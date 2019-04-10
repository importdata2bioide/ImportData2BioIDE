package org.big.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.validation.ValidationException;

import org.apache.commons.lang.StringUtils;
import org.big.common.CommUtils;
import org.big.common.EntityInit;
import org.big.common.LineAttreEnum;
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
	//种阶元正则表达式
	private volatile Map<String, String> speciesRegExlist = new HashMap<>();
	//一个或多个中文
	String multChinese = "[\\u4e00-\\u9fa5]+";
	//小写英文和拉丁文混合
	String EngMixLatin = "([a-z]|[\u00A0-\u00FF]|[\u0100-\u017F]|[\u0180-\u024F])+";
	//属
	String genusWord = "[A-Z]{1}"+EngMixLatin;
	//序号
	String numRgex = "(\\([0-9]*\\)|\\（[0-9]*\\）)";

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
		line = handCitationLine(line);// 最后加.
		Citation citation = new Citation();
		

		// 学名
//		String sciName = "";
//		int rankid = preTaxon.getRankid();
//		if (rankid == RankEnum.genus.getIndex()) {
//			sciName = toolService.getSciNameFromCitation(line, 1);
//		} else if (rankid == RankEnum.Subgenus.getIndex()) {
//			sciName = toolService.getSciNameFromCitation(line, 1);
//		} else if (rankid == RankEnum.species.getIndex()) {
//			//
//			System.out.println(line);
//			sciName = toolService.getSciNameFromCitation(line, 2);
//		} else if (rankid == RankEnum.subsp.getIndex()) {
//			sciName = toolService.getSciNameFromCitation(line, 3);
//			if (sciName.contains("：")) {
//				sciName = sciName.substring(0, sciName.indexOf("："));
//			} else {
//				int lastindexOfSpace = sciName.lastIndexOf(" ") + 1;
//				String lastWord = sciName.substring(lastindexOfSpace);
//				char firstCharOfLastWord = lastWord.charAt(0);
//				if ('A' <= firstCharOfLastWord && firstCharOfLastWord <= 'Z') {
//					sciName = sciName.substring(0, lastindexOfSpace).trim();
//				}
//			}
//		} else {
//			throw new ValidationException("未定义的rankid = " + rankid);
//		}
//		citation.setSciname(sciName);
//		// nameType
//		if (preTaxon.getScientificname().equals(sciName)) {
//			citation.setNametype(NametypeEnum.acceptedName.getIndex());// 接受名引证
//		} else {
//			citation.setNametype(NametypeEnum.synonym.getIndex());// 异名引证
//		}
//		// 命名人和命名年代
//		String year = toolService.getYear(line);
//		if (StringUtils.isNotEmpty(year)) {
//			String author = line.substring(line.indexOf(sciName) + sciName.length(),
//					line.indexOf(year) + year.length());
//			if (author.startsWith(":") || author.startsWith("：")) {
//				author = author.substring(1);
//			}
//			citation.setAuthorship(author);
//		}
//		// taxon
//		citation.setTaxon(preTaxon);
//		// 引证原文
//		citation.setCitationstr(line);
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
		//替换中文括号为英文括号
//		line = line.replace("（", "(");
//		line = line.replace("）", ")");
		return line;
	}

	@Override
	public List<Commonname> parseCommonName(String line, Taxon preTaxon, BaseParamsForm baseParamsForm) {
		List<Commonname> list = new ArrayList<>();
		line = line.replace("别名（common name）：", "");
		line = line.replace("曾用名或俗名：", "");
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

	public LineAttreEnum isWhat(String line, LineAttreEnum preAttr, String sourceLine) {
		if (line.startsWith("文献")) {
			return LineAttreEnum.ref;
		}
		if (line.startsWith("别名")||line.startsWith("曾用名")) {
			return LineAttreEnum.commonName;
		}
		if (line.startsWith("分布")) {
			return LineAttreEnum.Distribute;
		}
		if (line.startsWith("保护等级") || line.startsWith("保护类型")) {
			return LineAttreEnum.protectLevel;
		}
		if (line.equals("名   录")) {
			return LineAttreEnum.minglu;
		}
		String chinese = CommUtils.cutChinese(line);
		if (chinese.endsWith("纲")) {
			return LineAttreEnum.Class;
		}
		if (chinese.endsWith("目")) {
			return LineAttreEnum.order;
		}
		if (chinese.contains("亚属")) {
			return LineAttreEnum.subgenus;
		}
		if (chinese.endsWith("属")) {
			return LineAttreEnum.genus;
		}
		if (line.contains("亚科")) {
			return LineAttreEnum.subfamily;
		}
		if (chinese.endsWith("科")) {
			return LineAttreEnum.family;
		}
		// 头两个字母是英文：引证
		if (isEnglish(getChartASC(line, 2))) {
			return LineAttreEnum.ciation;
		}
		// 根据sourceLine区分种和亚种
		if (isSubSpecies(sourceLine)) {
			return LineAttreEnum.subsp;
		}
		//截取学名
		int index = CommUtils.indexOfFirstLetter(line);// 第一个英文字母的位置
		String sciNameAndAuthor = line.substring(index);
		//是否符合亚种的命名规则
		boolean suspecies = isSubspeciesNameRule(sciNameAndAuthor);
		if(suspecies) {
			logger.info("isWhat判定 可能是亚种："+line);
			return LineAttreEnum.subsp;
		}
		//剩下的是种阶元
		return LineAttreEnum.species;

	}


	/**
	 * 
	 * @Description 判断是否为英文
	 * @param text
	 * @return
	 * @author ZXY
	 */
	public boolean isEnglish(String text) {
		return text.matches("^[a-zA-Z]*");
	}

	boolean isSubSpecies(String sourceLine) {
		String rgex = "\"(.*?)\"";
		String engWithSpaceRgex = "^[A-Za-z][A-Za-z\\s]*[A-Za-z]$";
		List<String> subUtil = CommUtils.getSubUtil(sourceLine, rgex);
		Pattern sciNamePattern = Pattern.compile(engWithSpaceRgex);// 匹配的模式
		String sciName = "";
		for (String str : subUtil) {
			str = str.replace("\"", "").trim();
			Matcher m = sciNamePattern.matcher(str);
			if (m.find()) {
				sciName = str;
			}
		}
		if (StringUtils.isNotEmpty(sciName)) {
			// 计算空格的个数
			int occur = CommUtils.getOccur(sciName, " ");
			if (occur == 2) {
				// 亚种
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @Description 截取字符串的前几位
	 * @param text
	 * @param num
	 * @return
	 * @author ZXY
	 */
	public String getChartASC(String text, int num) {
		String result = "";
		if (StringUtils.isNotEmpty(text)) {
			result = text.substring(0, num);
		}
		return result;
	}
	


	@Override
	public boolean validateSpecies(String line) {
		Set<Entry<String, String>> entrySet = speciesRegExlist.entrySet();
		boolean match = false;
		for (Entry<String, String> entry : entrySet) {
			String key = entry.getKey();
			String regEx = entry.getValue();
			Pattern pattern = Pattern.compile(regEx);
			// 忽略大小写的写法
			// Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(line);
			// 字符串是否与正则表达式相匹配
			boolean rs = matcher.matches();
			if (rs) {
				
				match = true;
				break;
			}
		}
		if (!match) {
			logger.info("不符合设定的种阶元："+line);
		}
		return false;
	}
	
	
	@PostConstruct
	public void initSpeciesRegExlist() {
		speciesRegExlist.clear();
		//下盔鲨(黑鳍翅鲨)Hypogaleus hyugaensis (Miyosi, 1939)
		speciesRegExlist.put("species-0", "^\\s{0,}"+multChinese+"\\s{0,}\\(\\s{0,}"+multChinese+"\\s{0,}\\)\\s{0,}"+genusWord+"\\s{1,}"+EngMixLatin+"\\s{0,}\\((.*?)\\)\\s{0,}");
		//灰星鲨Mustelus griseus Pietschmann, 1908
		speciesRegExlist.put("species-1", "^\\s{0,}"+multChinese+"\\s{0,}"+genusWord+"\\s{1,}"+EngMixLatin+"\\s{0,}[A-Z]{1}(.*?)");
//		//大口尖齿鲨Chaenogaleus macrostoma (Bleeker, 1852)
		speciesRegExlist.put("species-2", "^\\s{0,}"+multChinese+"\\s{0,}"+genusWord+"\\s{1,}"+EngMixLatin+"\\s{0,}\\((.*?)\\)\\s{0,}");
		//(1097)长须纹胸Glyptothorax longinema Li
		speciesRegExlist.put("species-3", "^\\s{0,}"+numRgex+multChinese+"\\s{0,}"+genusWord+"\\s{1,}"+EngMixLatin+"\\s{0,}[A-Z]{1}(.*?)");
		//（1）蒲氏黏盲鳗Eptatretus burgeri (Girard, 1855)
		speciesRegExlist.put("species-4", "^\\s{0,}"+numRgex+multChinese+"\\s{0,}"+genusWord+"\\s{1,}"+EngMixLatin+"\\s{0,}\\([A-Z]{1}(.*?)\\)");
	
	}
	
	public boolean isSubspeciesNameRule(String sciNameAndAuthor) {
		String regEx = "^\\s{0,}"+genusWord+"\\s{1,}"+EngMixLatin+"\\s{1,}"+EngMixLatin+"(.*?)";
		Pattern pattern = Pattern.compile(regEx );
		// 忽略大小写的写法
		// Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(sciNameAndAuthor);
		// 字符串是否与正则表达式相匹配
		boolean rs = matcher.matches();
		return rs;
	}
	
	
	public static void main(String[] args) {
		String line = "小齿拟皱唇鲨Pseudotriakis microdon de Brito Capello, 1868";
		ParseLineFishWord parseLineFishWord = new ParseLineFishWord();
		boolean subspeciesNameRule = parseLineFishWord.isSubspeciesNameRule(line);
		System.out.println(subspeciesNameRule);
	}


}
