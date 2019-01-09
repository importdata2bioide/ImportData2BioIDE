package org.big.service;

import java.util.ArrayList;

import java.util.List;



import org.big.common.CommUtils;
import org.big.common.UUIDUtils;
import org.big.entity.Citation;
import org.big.entity.Distributiondata;
import org.big.entity.Geoobject;
import org.big.entity.Ref;
import org.big.entity.Taxaset;
import org.big.entity.Taxon;
import org.big.entity.TaxonHasTaxtree;
import org.big.entityVO.LineStatus;
import org.big.entityVO.NametypeEnum;
import org.big.entityVO.BaseParamsForm;
import org.big.entityVO.SpeciesCatalogueEnum;
import org.big.repository.CitationRepository;
import org.big.repository.DistributiondataRepository;
import org.big.repository.GeoobjectRepository;
import org.big.repository.RefRepository;
import org.big.repository.TaxonHasTaxtreeRepository;
import org.big.repository.TaxonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


@Service
public class SpeciesCatalogueServiceImpl implements SpeciesCatalogueService {

	private final static Logger logger = LoggerFactory.getLogger(SpeciesCatalogueServiceImpl.class);
	@Autowired
	private GeoobjectRepository geoobjectRepository;
	@Autowired
	private RefRepository refRepository;
	@Autowired
	private TaxonRepository taxonRepository;
	@Autowired
	private CitationRepository citationRepository;
	@Autowired
	private DistributiondataRepository distributiondataRepository;
	@Autowired
	private BatchInsertService batchInsertService;
	@Autowired
	TaxonHasTaxtreeRepository taxonHasTaxtreeRepository;

	

//	List<Taxon> taxonList = new ArrayList<>();
//	List<Distributiondata> distributionList = new ArrayList<>();
//	List<Citation> citationList = new ArrayList<>();

	private int sleepTime = 60000;

	@SuppressWarnings("static-access")
	@Override
	public Taxon savefamilyTaxon(Taxon t, BaseParamsForm params) throws Exception {
		// validate 数据验证
		String scientificname = t.getScientificname().trim();
		if (CommUtils.isContainChinese(scientificname)) {
			logger.info("(error 0001 接受名种包含中文)" + t.getScientificname() + "||" + t.getChname());
		}
		if ((scientificname.contains("(") && !scientificname.contains(")"))
				|| !scientificname.contains("(") && scientificname.contains(")")) {
			logger.info("(error 0002 接受名只包含一半括号)" + t.getScientificname());
		}
		// 种/亚种加词
		if ("7".equals(t.getRank().getId())) {// species 种
			try {
				int lastIndexOf = scientificname.lastIndexOf(" ");
				String epithet = scientificname.substring(lastIndexOf);
				// 通过空格没有截取到种加词，并且scientificname为Lynchia (Ardmoeca) ardeae 的格式
				if (CommUtils.isStrEmpty(epithet) && scientificname.contains("(") && !scientificname.endsWith(")")) {
					epithet = scientificname.substring(scientificname.lastIndexOf(")"));
				}
				t.setEpithet(epithet);
			} catch (Exception e) {
				logger.info("(error 0003 种加词不合格)" + scientificname + "||" + t.getChname());
			}
		} else if ("42".equals(t.getRank().getId())) {// subspecies 亚种
			try {
				int lastIndexOf = scientificname.lastIndexOf(" ");
				String epithet = scientificname.substring(lastIndexOf);
				t.setEpithet(epithet);
			} catch (Exception e) {
				logger.info("(error 0004亚种加词不合格)" + scientificname + "||" + t.getChname());
			}
		}
		// 剔除命名信息的括号
		String authorstr = t.getAuthorstr();
		if (CommUtils.isStrNotEmpty(authorstr)) {
			authorstr = authorstr.replace("(", "");
			authorstr = authorstr.replace(")", "");
			t.setAuthorstr(authorstr.trim());
		}
		// 通用的
		t.setId(UUIDUtils.getUUID32());
		t.setInputer(params.getmLoginUser());
		t.setSourcesid(params.getmSourcesid());
		t.setStatus(params.getStatus());
		t.setInputtime(CommUtils.getTimestamp(params.getmInputtimeStr()));
		t.setSynchstatus(params.getSynchstatus());
		t.setSynchdate(CommUtils.getTimestamp(params.getmInputtimeStr()));
		Taxaset taxaset = new Taxaset();
		taxaset.setId(params.getmTaxasetId());
		t.setTaxaset(taxaset);
		t.setRankid(t.getRank().getId());
		t.setTaxonCondition(params.getTaxonCondition());
		// 保存到数据库
		if (params.isInsert()) {
			try {
				taxonRepository.save(t);
			} catch (Exception e) {
				logger.info("insert taxon error:" + e.getMessage());
				Thread.currentThread().sleep(sleepTime);
				taxonRepository.save(t);
			}
		} else {
			// 放到集合中
//			taxonList.add(t);
		}
		return t;
	}

	@SuppressWarnings("static-access")
	@Override
	public Distributiondata handleDistribution(LineStatus thisLineStatus, SpeciesCatalogueEnum preTaxon, String line,
			BaseParamsForm params) throws Exception {
		Taxon taxon = getTaxon(thisLineStatus, preTaxon);
		Distributiondata t = new Distributiondata();
		t.setId(UUIDUtils.getUUID32());
		t.setTaxon(taxon);
		t.setGeojson(this.getGeojson(line));
		t.setTaxonid(taxon.getId());
		t.setDiscontent(line);
		// 通用的
		t.setInputer(params.getmLoginUser());
		t.setSourcesid(params.getmSourcesid());
		t.setStatus(params.getStatus());
		t.setInputtime(CommUtils.getTimestamp(params.getmInputtimeStr()));
		t.setSynchstatus(params.getSynchstatus());
		t.setSynchdate(CommUtils.getTimestamp(params.getmInputtimeStr()));

		// 测试保存
		if (params.isInsert()) {
			try {
				distributiondataRepository.save(t);
			} catch (Exception e) {
				logger.info("insert distributiondata error:" + e.getMessage());
				Thread.currentThread().sleep(sleepTime);
				distributiondataRepository.save(t);
			}
		} else {
//			distributionList.add(t);
		}
		return t;
	}

	@SuppressWarnings("static-access")
	@Override
	public Citation handleCitation(LineStatus thisLineStatus, SpeciesCatalogueEnum preTaxon, String line,
			BaseParamsForm params) throws Exception {
		Taxon taxon = getTaxon(thisLineStatus, preTaxon);
		Citation t = new Citation();
		t.setCitationstr(line.trim());
		String year = null;
		try {
			year = CommUtils.getCitationYear(line);
		} catch (Exception e1) {
			System.out.println("EDIT0001 LINE ERROR(引证,无法截取到4位年份)error message:" + e1.getMessage() + "|| line:" + line);
			return null;
		}
		// 判断名称状态(类型)
		if (line.contains(taxon.getAuthorstr()) && line.contains(taxon.getScientificname())) {// acceptedName
			t.setNametype(NametypeEnum.acceptedName.getIndex());
			t.setSciname(taxon.getScientificname());
			t.setAuthorship(taxon.getAuthorstr());
		} else if (CommUtils.isStrNotEmpty(year) && isSciNameInLine(taxon, line, year)) {// acceptedName
			t.setNametype(NametypeEnum.acceptedName.getIndex());
			t.setSciname(taxon.getScientificname());
			t.setAuthorship(taxon.getAuthorstr());
		} else if (line.startsWith("Citation list after")) {// 特殊的

		} else if (CommUtils.isStrNotEmpty(year)) {// synonym
			String SciNameAndAuthorship = CommUtils.cutByStrBeforeInclude(line, year);
			t.setNametype(NametypeEnum.synonym.getIndex());
			distinctSciNameAndAuthorship(SciNameAndAuthorship, taxon, t);
		} else {// synonym
			logger.info("error 此行无法正确解析，直接存入Citation数据库：" + line);
			t.setNametype(NametypeEnum.synonym.getIndex());

		}
		// 去掉命名信息前后空格
		if (CommUtils.isStrNotEmpty(t.getAuthorship())) {
			t.setAuthorship(t.getAuthorship().trim());
		}
		// 参考文献
		if (CommUtils.isStrNotEmpty(year)) {
			String citationRefJson = getCitationRefJson(t, line, year, params);
			if (CommUtils.isStrNotEmpty(citationRefJson)) {
				t.setRefjson(citationRefJson);
			}
		}

		t.setShortrefs(null);
		t.setTaxon(taxon);
		t.setId(UUIDUtils.getUUID32());
		t.setCitationstr(line);
		// 通用的
		t.setInputer(params.getmLoginUser());
		t.setSourcesid(params.getmSourcesid());
		t.setStatus(params.getStatus());
		t.setInputtime(CommUtils.getTimestamp(params.getmInputtimeStr()));
		t.setSynchstatus(params.getSynchstatus());
		t.setSynchdate(CommUtils.getTimestamp(params.getmInputtimeStr()));
		// 测试保存
		if (params.isInsert()) {
			try {
				citationRepository.save(t);
			} catch (Exception e) {
				logger.info("insert citation error:" + e.getMessage());
				Thread.currentThread().sleep(sleepTime);
				citationRepository.save(t);
			}
		} else {
			// 放到数组中
//			citationList.add(t);
		}
		return t;

	}

	/**
	 *  query 查询引证匹配的参考文献 title: SpeciesCatalogueServiceImpl.java
	 * 
	 * @param t
	 * @param   line;引证原文
	 * @param   year;年代（从引证原文中拆分出）
	 * @param   inputer（录入人）
	 * @return
	 * @author ZXY
	 */
	private String getCitationRefJson(Citation t, String line, String year, BaseParamsForm params) {
		String citationstr = t.getCitationstr();// 引证原文
		com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
		
		String citationRegx = "(:\\s{0,}\\d+\\s{0,}(\\.|;))|(\\.\\s{0,}\\d+\\s{0,}\\.\\s{0,}Type)|(;)";// (:页码.)//获取引证页码,根据页码个数判断由几个参考文献组成
		List<String> citationMatchGroup = CommUtils.getMatchGroup(citationstr, citationRegx);

		if (citationMatchGroup.size() > 1) {// 引证原文中有多个参考文献信息
			List<String> list = new ArrayList<>();
			//将引证原文拆分为多个
			for (String pageMsg : citationMatchGroup) {
				String pageInfo = CommUtils.cutByStrBeforeInclude(citationstr, pageMsg).trim();
				if(pageInfo.startsWith(";")) {
					pageInfo = pageInfo.substring(1,pageInfo.length());
				}
				list.add(pageInfo);
				citationstr = CommUtils.cutByStrAfter(line, pageMsg);
			}
			//查询每个引证原文可能对应的参考文献
			for (String partCitationStr : list) {
				String citationYear = null;
				try {
					citationYear = CommUtils.getCitationYear(partCitationStr).trim();
				} catch (Exception e) {
					logger.info("IY0001没有截取到命名年代："+partCitationStr);
					continue;
				}
				String citationAuthor = CommUtils.cutByStrBefore(partCitationStr, citationYear).trim();
				if (citationAuthor.endsWith(",")) {
					citationAuthor = citationAuthor.substring(0, citationAuthor.length() - 1);
				}
				List<Ref> reflist = refRepository.searchByYearAndAuthorAndInpuAndRem(citationYear, citationAuthor,
						params.getmLoginUser(), params.getRemark());
				for (Ref ref : reflist) {
					boolean matchResult = matchWithCitation(ref, t, jsonArray, partCitationStr);
					if (!matchResult) {
						// 无法确认，需要人工校验
						com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
						jsonObject.put("refE", " 0");
						jsonObject.put("refS", " 0");
						jsonObject.put("refId", "" + ref.getId());
						jsonObject.put("refType", "0");
						jsonArray.add(jsonObject);
					}else {
						jsonArray.clear();
						com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
						jsonObject.put("refE", " 0");
						jsonObject.put("refS", " 0");
						jsonObject.put("refId", "" + ref.getId());
						jsonObject.put("refType", "0");
						jsonArray.add(jsonObject);
						break;
					}
				}
			}
		} else if (citationMatchGroup.size() <= 1) { // 引证原文中只有一个参考文献信息
			String authorship = t.getAuthorship();// 命名信息
			if (CommUtils.isStrEmpty(authorship)) {// 命名信息为空，直接返回null
				return null;
			}
			String author = CommUtils.cutByStrBefore(authorship, year).trim();// 命名人
			if (author.endsWith(",") || author.endsWith("，")) {
				author = author.substring(0, author.length() - 1);
			}
			// 根据作者和年代查询
			List<Ref> reflist = refRepository.searchByYearAndAuthorAndInpuAndRem(year, author, params.getmLoginUser(),
					params.getRemark());
			for (Ref ref : reflist) {
				// match 获得匹配结果，
				boolean matchResult = matchWithCitation(ref, t, jsonArray, null);
				if (matchResult) {
					// 无法确认，需要人工校验
					com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
					jsonObject.put("refE", " 0");
					jsonObject.put("refS", " 0");
					jsonObject.put("refId", "" + ref.getId());
					jsonObject.put("refType", "0");
					jsonArray.add(jsonObject);
				}
			}

		}
		
		
		if(jsonArray.size() == 0) {
			return null;
		}
//		logger.info("jsonArray-----"+jsonArray.toString());
		return jsonArray.toString();
	}

	/**
	 * true/false 引证和参考文献匹配 title: SpeciesCatalogueServiceImpl.java
	 * 
	 * @param ref
	 * @param t
	 * @return
	 * @author ZXY
	 */
	private boolean matchWithCitation(Ref ref, Citation t, JSONArray jsonArray, String partCitationstr) {
		String citationstr = t.getCitationstr();// 引证原文
		String refstr = ref.getRefstr();// 参考文献完整引证
		// 第一步 ：获得参考文献的起始页码
		String refPage = "";
		String citationPage = "";
		// 匹配（页码 pp）（pp 页码）（页码-页码）（页码–页码）（：页码.）（. 页码 .[结束]）
		String regx = "(\\d+\\s{0,}pp)|(pp\\s{0,}\\d+)|(\\s{0,}\\d+\\s{0,}(-|–|‒|−)\\s{0,}\\d+(,|，)?\\s{0,})+|(:\\s{0,}\\d+\\.)|(\\.\\s{0,}\\d+\\s{0,}\\.$)";
		List<String> matchGroup = CommUtils.getMatchGroup(refstr, regx);
		if (matchGroup.size() == 1) {
			refPage = matchGroup.get(0);
			refPage = refPage.replaceAll("[. :pp]", "");
			refPage = refPage.replaceAll("[–‒−]", "-");
		} else if (matchGroup.size() > 1) {
//			logger.info("error M0001正则表达式解析出多个【参考文献】页码：【参考文献】" + refstr + "【id】" + ref.getId());
		} else {
//			logger.info("error M0002正则表达式没有解析出【参考文献】页码：【参考文献】" + refstr);
		}
		// 第二步：获取引证的页码 (:页码.)
		if(CommUtils.isStrNotEmpty(partCitationstr)) {
			citationstr = partCitationstr;
		}
		String citationRegx = "(:\\s{0,}\\d+\\s{0,}(\\.)?)|(\\.\\s{0,}\\d+\\s{0,}\\.\\s{0,}Type locality)";
		List<String> citationMatchGroup = CommUtils.getMatchGroup(citationstr, citationRegx);
		if (citationMatchGroup.size() == 1) {
			citationPage = citationMatchGroup.get(0).replaceAll(":", "");
			citationPage = citationPage.replaceAll(".", "");
		} else {
			logger.info("warn M0003正则表达式解析出"+citationMatchGroup.size()+"个【引证】页码：【引证原文】" + citationstr);
			
		}
		// 第三步：两个页码对比
		if(CommUtils.isStrNotEmpty(refPage) && CommUtils.isStrNotEmpty(citationPage) ) {
			int citationPageInt = Integer.parseInt(citationPage);
			if(refPage.contains(",")) {
				String[] split = refPage.split(",");
				for (String yearRange : split) {
					int end = Integer.parseInt(CommUtils.cutByStrAfter(yearRange, "-"));
					int begin = Integer.parseInt(CommUtils.cutByStrBefore(yearRange, "-"));
					if(citationPageInt >= end || citationPageInt<=begin) {
						return false;
					}
				}
			}else if(CommUtils.isNumeric(refstr)){
				if(Integer.parseInt(refstr) != citationPageInt) {
					return false;
				}
				
			}else if (!refPage.contains(",")&&refPage.contains("-")) {
				int end = Integer.parseInt(CommUtils.cutByStrAfter(refPage, "-"));
				int begin = Integer.parseInt(CommUtils.cutByStrBefore(refPage, "-"));
				if(citationPageInt >= end && citationPageInt<=begin) {
					return false;
				}
			}else {
				logger.info("无法识别的两个页码"+refPage+"||"+citationPage);
			}
		}

		return true;
	}

	private void distinctSciNameAndAuthorship(String sciNameAndAuthorship, Taxon taxon, Citation t) {
		int rankId = Integer.parseInt(taxon.getRank().getId());
		switch (rankId) {
		case 7:// 种
			if (sciNameAndAuthorship.contains(":")) {// 根据冒号“：”分割异名和命名人
				t.setSciname(CommUtils.cutByStrBefore(sciNameAndAuthorship, ":"));
				t.setAuthorship(CommUtils.cutByStrAfter(sciNameAndAuthorship, ":"));
			} else if (sciNameAndAuthorship.contains("(") && sciNameAndAuthorship.contains(")")) {
				String[] split = sciNameAndAuthorship.split(" ");
				if (split.length > 3) {
					t.setSciname(split[0].trim() + " " + split[1].trim() + " " + split[2].trim());
					t.setAuthorship(CommUtils.cutByStrAfter(sciNameAndAuthorship, split[2]).trim());
				} else {
					logger.info("error D0001 此种的引证无法解析，请在异名和命名人中间加英文冒号：" + taxon.getScientificname() + "||"
							+ taxon.getChname() + "||" + sciNameAndAuthorship);
				}

			} else {
				String[] split = sciNameAndAuthorship.split(" ");
				if (split.length > 2) {
					t.setSciname(split[0].trim() + " " + split[1].trim());
					t.setAuthorship(CommUtils.cutByStrAfterInclude(sciNameAndAuthorship, split[2]).trim());
				} else {
					logger.info("error D0002 此种的引证无法解析，请在异名和命名人中间加英文冒号：" + taxon.getScientificname() + "||"
							+ taxon.getChname() + "||" + sciNameAndAuthorship);
				}
			}
			break;
		case 42:// 亚种
			if (sciNameAndAuthorship.contains(":")) {// 根据：分割异名和命名人
				t.setSciname(CommUtils.cutByStrBefore(sciNameAndAuthorship, ":"));
				t.setAuthorship(CommUtils.cutByStrAfter(sciNameAndAuthorship, ":"));
			} else {
				logger.info("error D0003 此亚种的引证无法解析，请在异名和命名人中间加英文冒号:" + sciNameAndAuthorship);
			}
			break;
		default:
			if (sciNameAndAuthorship.contains("(") && sciNameAndAuthorship.contains(")")) {
				// 适用与 Savtshenkia (as subgenus of Tipula): Alexander et Alexander, 1973. Cat.
				// Dipt. Orient. Reg. 1......
				sciNameAndAuthorship = sciNameAndAuthorship.replace("：", "");
				sciNameAndAuthorship = sciNameAndAuthorship.replace(":", "");
				t.setSciname(CommUtils.cutByStrBeforeInclude(sciNameAndAuthorship, ")"));
				t.setAuthorship(CommUtils.cutByStrAfter(sciNameAndAuthorship, ")"));
			} else if (sciNameAndAuthorship.contains(":")) {
				t.setSciname(CommUtils.cutByStrBefore(sciNameAndAuthorship, ":"));
				t.setAuthorship(CommUtils.cutByStrAfter(sciNameAndAuthorship, ":"));
			} else {
				t.setSciname(CommUtils.cutByStrBefore(sciNameAndAuthorship, " "));
				t.setAuthorship(CommUtils.cutByStrAfter(sciNameAndAuthorship, " "));
			}
			break;
		}

	}

	public String getGeojson(String line) {
		String value = "";
		line = CommUtils.handleDistributionLine(line);
		JSONObject jsonObject = new JSONObject();
		String[] geos = line.split("、");
		for (String geo : geos) {
			if (CommUtils.isStrEmpty(geo)) {
				continue;
			} else if ((geo.contains("台湾") || geo.contains("台北")) && !geo.contains("除")) {// 香港作特殊处理，以免丢失
				geo = "台湾";
			}
			List<Geoobject> geoobjectList = geoobjectRepository.findByLikeCngeoname(geo.trim());
			if (geoobjectList.size() > 0) {
				Geoobject geoobject = geoobjectList.get(0);
				if (geoobject != null && CommUtils.isStrNotEmpty(geoobject.getId())) {
					// 拼接字符串
					value = value + geoobject.getId() + "&";
				}
			}

		}
		jsonObject.put("geoIds", value);
		if (CommUtils.isStrEmpty(value)) {
//			logger.info("02没有中国分布地，line："+line);
			jsonObject.put("geoIds", "A1A25AA0D98C4441997D891A229F35E4");
		}
		if (value.endsWith("&")) {
			value = value.substring(0, value.length() - 1);
			jsonObject.put("geoIds", value);
		}
		return String.valueOf(jsonObject);
	}

	private Taxon getTaxon(LineStatus thisLineStatus, SpeciesCatalogueEnum preTaxon) {
		Taxon taxon = null;
		switch (preTaxon) {
		case superfamily:
			taxon = thisLineStatus.getSuperfamily();
			break;
		case family:
			taxon = thisLineStatus.getFamily();
			break;
		case subfamily:
			taxon = thisLineStatus.getSubfamily();
			break;
		case tribe:
			taxon = thisLineStatus.getTribe();
			break;
		case subtribe:
			taxon = thisLineStatus.getSubtribe();
			break;
		case genus:
			taxon = thisLineStatus.getGenus();
			break;
		case subgenus:
			taxon = thisLineStatus.getSubgenus();
			break;
		case species:
			taxon = thisLineStatus.getSpecies();
			break;
		case subspecies:
			taxon = thisLineStatus.getSubspecies();
			break;
		default:
			break;
		}
		return taxon;
	}

//	public List<Taxon> getTaxonList() {
//		return taxonList;
//	}
//	public void setTaxonList(List<Taxon> taxonList) {
//		this.taxonList = taxonList;
//	}
//	public List<Distributiondata> getDistributionList() {
//		return distributionList;
//	}
//	public void setDistributionList(List<Distributiondata> distributionList) {
//		this.distributionList = distributionList;
//	}
//	public List<Citation> getCitationList() {
//		return citationList;
//	}
//	public void setCitationList(List<Citation> citationList) {
//		this.citationList = citationList;
//	}

	private boolean isSciNameInLine(Taxon taxon, String line, String year) {
		String sciName = taxon.getScientificname();
		String authorstr = taxon.getAuthorstr();
		String lineSciNameAndAuthor = CommUtils.cutByStrBeforeInclude(line, year).trim();
		if (sciName.contains("(") && !lineSciNameAndAuthor.contains("(")) {// 是否因为旧名干扰
			String newSciName = CommUtils.cutByStrBefore(sciName, "(") + CommUtils.cutByStrAfter(sciName, ")");// 去掉Taxon.getScientificname里的旧名
			if (CommUtils.isStrNotEmpty(newSciName)) {
				String trimLine = lineSciNameAndAuthor.replace(" ", "");
				newSciName = newSciName.replace(" ", "");
				authorstr = authorstr.replace(" ", "");
				if (trimLine.contains(newSciName) && trimLine.contains(authorstr)) {
					return true;
				} else {
					return false;
				}
			}
		} else {// 是否因为空格或标点符号干扰//if(sciName.contains("(") &&
				// lineSciNameAndAuthor.contains("("))
			String trimSciName = CommUtils.replaceAllPunctuation(sciName);
			String trimAuthorstr = CommUtils.replaceAllPunctuation(authorstr);
			String trimLine = CommUtils.replaceAllPunctuation(lineSciNameAndAuthor);
			if (trimLine.contains(trimSciName) && trimLine.contains(trimAuthorstr)) {
				return true;
			} else {
				return false;
			}
		}
		return false;

	}

	@Transactional
	@Override
	public void insertTreeByDataSet(BaseParamsForm params) {
		boolean isInsert = true;
		List<TaxonHasTaxtree> records = new ArrayList<>();
		List<Taxon> taxaset = taxonRepository.findByTaxaset(params.getmTaxasetId());
		logger.info("解析Taxon to tree，数量：" + taxaset.size());
		for (Taxon taxon : taxaset) {
			TaxonHasTaxtree t = new TaxonHasTaxtree();
			t.setTaxtreeId(params.getmTaxtreeId());
			t.setTaxonId(taxon.getId());

			com.alibaba.fastjson.JSONObject jsonObject = CommUtils.strToJSONObject(taxon.getRemark());
			if (taxon.getRank().getId().equals("40")) {
				t.setPid(params.getmTaxtreeId());
			} else if (jsonObject == null) {
				isInsert = false;
				logger.info("error A001 remark 为空，找不到parent " + taxon.getId() + "\t" + taxon.getRemark());
			} else {
				String parentId = String.valueOf(jsonObject.get("parentId"));
				if (CommUtils.isStrEmpty(parentId)) {
					isInsert = false;
					logger.info("error A002 parentId 为空，找不到parentId " + taxon.getId() + "\t" + taxon.getRemark());
				} else {
					t.setPid(parentId);
				}
			}
			records.add(t);
		}

		if (isInsert) {
			logger.info("保存 分类树，数量：" + records.size());
//			taxonHasTaxtreeRepository.saveAll(records);
			batchInsertService.batchInsertTaxonHasTaxtree(records);
			logger.info("保存 分类树完成，数量：" + records.size());
		}

	}

//	@Override
	public void saveSpeciesCatalogue() throws Exception {
		JdbcBatchItemWriter<Taxon> writer = new JdbcBatchItemWriter<>();
		writer.setSql("");
//		batchInsertService.batchInsertTaxon(taxonList, "2018-11-26 01:01:01");
//		batchInsertService.batchInsertCitation(citationList, "2018-11-26 01:01:01");
//		batchInsertService.batchInsertDistributiondata(distributionList, "2018-11-26 01:01:01");
//		taxonRepository.saveAll(speciesCatalogueService.getTaxonList());
//		citationRepository.saveAll(speciesCatalogueService.getCitationList());
//		distributiondataRepository.saveAll(speciesCatalogueService.getDistributionList());

	}

}
