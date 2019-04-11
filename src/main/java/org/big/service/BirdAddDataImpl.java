package org.big.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.big.common.CommUtils;
import org.big.common.FilesUtils;
import org.big.common.UUIDUtils;
import org.big.constant.ConfigConsts;
import org.big.entity.Citation;
import org.big.entity.Rank;
import org.big.entity.Ref;
import org.big.entity.Taxon;
import org.big.entityVO.ExcelUntilB;
import org.big.entityVO.ExcelUntilD;
import org.big.entityVO.ExcelUntilF;
import org.big.entityVO.ExcelUntilK;
import org.big.entityVO.ExcelUntilP;
import org.big.entityVO.NametypeEnum;
import org.big.entityVO.PtypeEnum;
import org.big.entityVO.RankEnum;
import org.big.entityVO.RefTypeEnum;
import org.big.repository.CitationRepository;
import org.big.repository.RefRepository;
import org.big.repository.TaxonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;

@Service
public class BirdAddDataImpl implements BirdAddData {

	final static Logger logger = LoggerFactory.getLogger(BirdAddDataImpl.class);

	@Autowired
	private RefRepository refRepository;
	@Autowired
	private TaxonRepository taxonRepository;
	@Autowired
	private BatchSubmitService batchSubmitService;
	@Autowired
	private BatchInsertService batchInsertService;
	@Autowired
	private CitationRepository citationRepository;
	@Autowired
	private ToolService toolService;
	@Autowired
	private TaxasetService taxasetService;
	@Autowired
	private RefService refService;

	private String userId_wangtianshan = "95c24cdc24794909bd140664e2ee9c3b";
	private String datasetId_2019bird = "7da1c0ac-18c6-4710-addd-c9d49e8a2532";
	private String sourcesId_2019bird = "428700b4-449e-4284-a082-b2fe347682ff";
//	private String sourcesId_citationRef_2019bird = "b8fcc018-8d0e-4e0a-9ac8-6ea7c1bf4632";// 2019鸟类名录-引证文献
//	private String teamIdOldBioIDE = "04fd60c66610416083f7d988c1f9bbfb";
	// 分类单元集：鸟纲-郑-雷-sp2000
	private String taxasetIdBirdZL = "5962f6d5caf8430aba8d85520a0ad1d1";
	boolean insertOrUpdateDB = false;// 数据库变动

	Map<String, String> sciNameMap = new HashMap<>();

	private volatile Map<String, String> regExPagelist = new LinkedHashMap<>();// 有序

	@Override
	public void importByExcel() throws Exception {
		String folderPath = "E:\\003采集系统\\0012鸟类名录\\";
		String reffilePath = folderPath + "最终-整合-参考文献.xlsx";
//		String UnPasseriformesCitationPath = folderPath + "非雀形目.xlsx";
//		String passeriformesCitationPath = folderPath + "雀形目（整理顺序）.xlsx";
		String integrationPath = folderPath + "最终-整合-0.xlsx";
		String otherCitationPath = folderPath + "其他-引证信息.xlsx";
		boolean save = false;
		// 1、读取参考文献，并保存到map中
		Map<String, String> refMap = readRefs(reffilePath);
		// 2、导入非雀形目引证信息
//		importCitation(refMap, UnPasseriformesCitationPath, save);
		// 3、导入雀形目引证信息
//		importCitation(refMap, passeriformesCitationPath, save);
		// 导入整合引证
		importCitation(refMap, integrationPath, save);
		// 4、导入接受名引证信息
		otherCitationPath(refMap, otherCitationPath, save);
	}

	/**
	 * 
	 * @Description 6、 导入引证信息“其他-引证信息.xlsx”，
	 * @Description 这表都是接受名引证，请用“author”“year”两个字段更新命名信息“authorship”字段，
	 * @Description 如果原来“authorhsip”字段带括号，请为新的authorship添加括号
	 * @Description
	 * @param refMap
	 * @param otherCitationPath
	 * @param save
	 * @author ZXY
	 * @throws Exception
	 */
	private void otherCitationPath(Map<String, String> refMap, String filePath, boolean save) throws Exception {
		List<ExcelUntilP> list = readExcel(filePath, ExcelUntilP.class);
		List<Citation> resultAddlist = new ArrayList<>();
		List<Citation> resultUpdatelist = new ArrayList<>();
		List<Taxon> updateTaxonlist = new ArrayList<>();
		int i = 0;
		for (ExcelUntilP row : list) {
			String acceptName = row.getColF();
			if (StringUtils.isEmpty(acceptName)) {
				continue;
			}
			acceptName = acceptName.trim();
			List<Object[]> objlist = taxonRepository.findByDatasetAndSciName(datasetId_2019bird, acceptName);
			Taxon taxon = null;
			try {
				taxon = turnObjToTaxon(objlist, acceptName);
			} catch (Exception e) {

			}
			if (taxon != null) {
				String author = row.getColK();
				String year = row.getColL();
				String authorAndYear = row.getColH();// excel文件里的命名信息
				String authorship = "";// 存储到数据库的命名信息
				if (StringUtils.isEmpty(author) && StringUtils.isEmpty(year)) {
					continue;
				}
				// 命名人和命名年代
				authorship = row.getColK().trim() + "," + row.getColL().trim();
				if (StringUtils.isNotEmpty(authorAndYear)
						&& (authorAndYear.contains("（") || authorAndYear.contains("("))) {
					authorship = "(" + authorship + ")";
				}
				String taxonId = taxon.getId();
				Citation citation = citationRepository.findByTaxonIdAndSciname(taxonId, acceptName);
				String page = row.getColM();
				JSONObject remark = null;
				if (StringUtils.isNotEmpty(page)) {
					remark = new JSONObject();
					remark.put("page", page.trim());
				}
				if (citation != null) {
					// 前两个excel已经保存接受名引证，更新命名信息
//					logger.info("前两个excel已经保存接受名引证了:" + acceptName);
					// 更新（引证信息）操作,更新的字段有authorship、citationstr、remark
					citation.setAuthorship(authorship);
					citation.setCitationstr(row.getColN());
					citation.setRemark(String.valueOf(remark));
					resultUpdatelist.add(citation);
				} else {
					// 新增（引证信息）操作
					Citation record = new Citation();
					record.setId(UUIDUtils.getUUID32());
					record.setSciname(acceptName);
					record.setAuthorship(authorship);
					record.setNametype(NametypeEnum.acceptedName.getIndex());
					record.setCitationstr(row.getColN());
					record.setSourcesid(sourcesId_2019bird);
					record.setSourcesidId(sourcesId_2019bird);
					record.setInputer(userId_wangtianshan);
					record.setTaxon(taxon);
					record.setRemark(String.valueOf(remark));
					record.setStatus(1);
					resultAddlist.add(record);
				}
				if (StringUtils.isNotEmpty(authorship)) {
					taxon.setAuthorstr(authorship);
					updateTaxonlist.add(taxon);
				}
			} else {
				i++;
//				logger.info(row.getColA()+"	"+row.getColB()+"	"+row.getColC()+"	"+row.getColD()+"	"+row.getColE()+"	"+row.getColF()+"	"+row.getColG()+"	"+row.getColH()+"	"+row.getColI()+"	"+row.getColJ()+"	"+row.getColK()+"	"+row.getColL()+"	"+row.getColM()+"	"+row.getColN()+"	"+row.getColO()+"	"+row.getColP());
			}
		}
//		for (Citation c : resultUpdatelist) {
//			toolService.printEntity(c);
//		}
		logger.info("更新：" + resultUpdatelist.size());
		logger.info("新增：" + resultAddlist.size());
		logger.info("没有找到的：" + i);
		if (save) {
			batchSubmitService.saveAll(resultAddlist);
			batchInsertService.batchUpdateCitationById(resultUpdatelist);
			batchInsertService.batchUpdateTaxonAuthorstrById(updateTaxonlist);
		}

	}

	private boolean existInCitationExcel(String acceptName) {
		if (sciNameMap.get(acceptName) != null) {
			return true;
		}
		return false;
	}

	private Citation turnObjToCitation(List<Object[]> objlist, String sciname) {
		Citation citation = null;
		if (objlist.size() != 1) {
			throw new ValidationException("error turnObjToCitation 从数据库里查询出多条或0条引证数据, sciname=" + sciname);
		} else {
			citation = new Citation();
			Object[] obj = objlist.get(0);
			citation.setId(obj[0].toString());
			citation.setSciname(obj[1].toString());
			citation.setAuthorship(obj[2] == null ? null : obj[2].toString());
			citation.setNametype(Integer.parseInt(obj[3].toString()));
			citation.setTaxon(new Taxon(obj[4].toString()));
		}
		return citation;
	}

	/**
	 * 
	 * @Description 3、 导入引证信息“非雀形目.xlsx”：
	 * @Description 判断名称命名信息是否完整，进行补充
	 * @Description 如果备注写“属名不同，种加词相同  [新组合]”，给接受名的命名信息加上括号
	 * @param refMap
	 * @author ZXY
	 * @throws Exception
	 */
	private void importCitation(Map<String, String> refMap, String filePath, boolean save) throws Exception {
		List<ExcelUntilK> list = readExcel(filePath, ExcelUntilK.class);
		List<Citation> resultlist = new ArrayList<>();
		List<Taxon> taxonlist = new ArrayList<>();
		// 查询taxon
		for (ExcelUntilK row : list) {
			String acceptName = row.getColJ().trim();
			if (StringUtils.isEmpty(acceptName)) {
				continue;
			}
			sciNameMap.put(acceptName, filePath);
			List<Object[]> objlist = taxonRepository.findByDatasetAndSciName(datasetId_2019bird, acceptName);
			Taxon taxon = turnObjToTaxon(objlist, acceptName);
			String sciname = row.getColE().trim();
			String author = row.getColF();// 可能空
			String nametypeStr = row.getColG();// 可能空值
			int nametype = -1;
			String refType = "";
			switch (nametypeStr) {
			case "accepted name":
				nametype = NametypeEnum.acceptedName.getIndex();
				refType = "0";
				break;
			case "synonym":
				nametype = NametypeEnum.synonym.getIndex();
				refType = "1";
				break;
			default:
				throw new ValidationException("未定义的nameType:" + nametypeStr);
			}
			String citationstr = row.getColH();// 可能空值
			String ref = row.getColI();
			JSONArray jsonArray = new JSONArray();
			if (StringUtils.isNotEmpty(ref)) {
				ref = ref.replace(",", ";");
				ref = ref.replace("，", ";");
				String[] refSeqs = ref.split(";");
				for (String seq : refSeqs) {
					seq = seq.trim();
					String refId = refMap.get(seq);
					if (StringUtils.isEmpty(refId)) {
						logger.error(ConfigConsts.HANDLE_ERROR + "未找到的参考文献序号：" + seq);
						continue;
//						throw new ValidationException("未找到的参考文献序号：" + seq);
					}
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("refE", " 0");
					jsonObject.put("refS", " 0");
					jsonObject.put("refId", "" + refId);
					jsonObject.put("refType", refType);
					jsonArray.add(jsonObject);
				}

			}
			String remark = row.getColK();
			if (nametype == NametypeEnum.acceptedName.getIndex() && StringUtils.isNotEmpty(author)
					&& remark.equals("属名不同，种加词相同  [新组合]")) {
				author = "(" + author + ")";
			}
			// 新建一条引证信息
			Citation record = new Citation();
			record.setId(UUIDUtils.getUUID32());
			record.setSciname(sciname);
			record.setAuthorship(author);
			record.setNametype(nametype);
			record.setCitationstr(citationstr);
			if (jsonArray.size() > 0) {
				record.setRefjson(jsonArray.toJSONString());
			}
			record.setSourcesid(sourcesId_2019bird);
			record.setSourcesidId(sourcesId_2019bird);
			record.setInputer(userId_wangtianshan);
			record.setTaxon(taxon);
			record.setRemark(remark);
			record.setStatus(1);
			resultlist.add(record);
			// 更新taxon的authorstr字段
			taxon.setAuthorstr(author);
			if (StringUtils.isNotEmpty(author)) {
				taxonlist.add(taxon);
			}

		}
		if (save) {
			batchSubmitService.saveAll(resultlist);
//			batchInsertService.batchUpdateTaxonAuthorstrById(taxonlist);
		}

	}

	private Taxon turnObjToTaxon(List<Object[]> objlist, String acceptName) {
		Taxon taxon = null;
		if (objlist.size() != 1) {
			throw new ValidationException("error 从数据库里查询出多条或0条数据, scientificname=" + acceptName);
		} else {
			taxon = new Taxon();
			Object[] obj = objlist.get(0);
			taxon.setId(obj[0].toString());
			taxon.setScientificname(obj[1].toString());
			taxon.setAuthorstr(obj[2] == null ? null : obj[2].toString());
			taxon.setEpithet(obj[3] == null ? null : obj[3].toString());
			taxon.setChname(obj[4] == null ? null : obj[4].toString());
			String rankid = obj[5].toString();
			taxon.setRankid(Integer.parseInt(rankid));
			Rank rank = new Rank();
			rank.setId(rankid);
			taxon.setRank(rank);
		}

		return taxon;
	}

	/**
	 * 
	 * @Description
	 * @param filePath
	 * @return
	 * @author ZXY
	 */
	private Map<String, String> readRefs(String filePath) {
		List<ExcelUntilB> list = readExcel(filePath, ExcelUntilB.class);
		logger.info("从excel读取参考文献条数：" + list.size());
		Map<String, String> refMaP = new HashMap<>(list.size() + 10);
		for (ExcelUntilB excelUntilB : list) {
			String refstr = excelUntilB.getColB().trim();
			String seq = excelUntilB.getColA().trim();
			Ref ref = refRepository.findByRefstrAndInputer(refstr, userId_wangtianshan);
			if (ref == null) {

//				logger.info(seq + ",数据库中没有找到完整题录=" + refstr);
				throw new ValidationException(seq + ",数据库中没有找到完整题录=" + refstr);
			} else {
				refMaP.put(seq, ref.getId());
			}
		}
		logger.info("excel文件行数：" + list.size() + ",map大小：" + refMaP.size());
		return refMaP;
	}

	private <T> List<T> readExcel(String path, Class<T> t) {
		ImportParams params = new ImportParams();
		params.setTitleRows(0);
		params.setHeadRows(1);
		long start = new Date().getTime();
		List<T> list = ExcelImportUtil.importExcel(new File(path), t, params);
		logger.info("读取excel所消耗时间：" + (new Date().getTime() - start));
		logger.info(path + ",excel行数：" + list.size());
		logger.info("打印第一行表格内容：" + ReflectionToStringBuilder.toString(list.get(0)));
		logger.info("读取excel完成");
		return list;
	}

	@Override
	public void countCitationByTaxon(HttpServletResponse response) {
		List<String> genusRankIds = new ArrayList<>();
		genusRankIds.add(String.valueOf(RankEnum.genus.getIndex()));
		// 属接受名引证统计
		List<Object[]> genusAcceptNamelist = citationRepository.countByTaxonAndNameType(datasetId_2019bird,
				genusRankIds, String.valueOf(NametypeEnum.acceptedName.getIndex()));
		// 属异名引证统计
		List<Object[]> genusSynamelist = citationRepository.countByTaxonAndNameType(datasetId_2019bird, genusRankIds,
				String.valueOf(NametypeEnum.synonym.getIndex()));
		List<String> speciesRankIds = new ArrayList<>();
		speciesRankIds.add(String.valueOf(RankEnum.species.getIndex()));
		// species接受名引证统计
		List<Object[]> speciesAcceptNamelist = citationRepository.countByTaxonAndNameType(datasetId_2019bird,
				speciesRankIds, String.valueOf(NametypeEnum.acceptedName.getIndex()));
		// species异名引证统计
		List<Object[]> speciesSynamelist = citationRepository.countByTaxonAndNameType(datasetId_2019bird,
				speciesRankIds, String.valueOf(NametypeEnum.synonym.getIndex()));
		List<ExcelUntilD> resultlist = new ArrayList<>(genusAcceptNamelist.size() + speciesAcceptNamelist.size() + 10);
//		mergeList(genusAcceptNamelist,genusSynamelist,resultlist);
		mergeList(speciesAcceptNamelist, speciesSynamelist, resultlist);
		// 导出文件
		try {
			FilesUtils.exportExcel(resultlist, "引证统计", "引证统计", ExcelUntilD.class,
					URLEncoder.encode("CitationCount.xls", "UTF-8"), response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void mergeList(List<Object[]> genusAcceptNamelist, List<Object[]> genusSynamelist,
			List<ExcelUntilD> resultlist) {
		// 转换成map
		Map<String, Object[]> map = new HashMap<>(genusSynamelist.size() + 10);
		for (Object[] obj : genusSynamelist) {
			map.put(obj[0].toString(), obj);
		}
		// a.id,a.scientificname,b.tsname,e.sl
		for (Object[] obj : genusAcceptNamelist) {
			String taxonId = obj[0].toString();
			String acceptName = obj[1].toString();
			String orderCHName = obj[2].toString();
			Object synameCountObj = map.get(taxonId)[3];
			int synameCount = synameCountObj == null ? 0 : Integer.parseInt(synameCountObj.toString());
			int acceptNameCount = obj[3] == null ? 0 : Integer.parseInt(obj[3].toString());
			ExcelUntilD row = new ExcelUntilD();
			row.setColA(orderCHName);
			row.setColB(acceptName);
			row.setColC(String.valueOf(acceptNameCount));
			row.setColD(String.valueOf(synameCount));
			resultlist.add(row);
		}

	}

	@Override
	public void updateCitationStrBySciName(HttpServletResponse response) throws Exception {
		List<String> speciesRankIds = new ArrayList<>();
		speciesRankIds.add(String.valueOf(RankEnum.species.getIndex()));
		List<Citation> insertCitationlist = new LinkedList<>();
		// 根据数据集查询引证
		List<Object[]> objlist = citationRepository.findByTaxasetId(datasetId_2019bird, speciesRankIds);
		// Object数组转换List
		List<Citation> list = turnObjToCitation(objlist);
		Iterator<Citation> iter = list.iterator();
		int i = 0;
		int size = list.size();
		while (iter.hasNext()) {
			i++;
			Citation citation = iter.next();
			String id = citation.getId();
			String citationstr = citation.getCitationstr();
			if (StringUtils.isEmpty(id)) {
				iter.remove();
				Taxon taxon = citation.getTaxon();
				// 从旧采集系统引入
				List<Citation> addlist = getFromOldBioIDE(taxon,null);
				insertCitationlist.addAll(addlist);
				continue;
			} else if (StringUtils.isEmpty(citationstr)) {
				iter.remove();
				System.out.println(ConfigConsts.HANDLE_ERROR + "2019鸟类名录引证原文为空,id=" + id);
				// do nothing
				continue;
			} else {
//				System.out.println("----");
				// 在控制台打印实体数据
//				toolService.printEntity(citation);
				String sciname = citation.getSciname();
				citationstr = citation.getCitationstr().trim();
				String refjson = citation.getRefjson();
				String remark = citation.getRemark();
				// 第一步：根据引证原文或备注解析参考文献页码
				String refS = "";
				String refE = "";
				try {
					Object pageObj = CommUtils.strToJSONObject(remark).get("page");
					String rangPage = pageObj == null ? null : pageObj.toString();
					String splitflag = "";
					if (rangPage.contains("-")) {
						splitflag = "-";
					} else if (rangPage.contains("‐")) {
						splitflag = "‐";
					} else if (rangPage.contains("–")) {
						splitflag = "–";
					}
					if (StringUtils.isNotEmpty(splitflag)) {
						String[] split = rangPage.split(splitflag);
						refS = split[0];
						refE = split[1];
					} else {
						refS = rangPage;
						refE = rangPage;
					}
				} catch (Exception e) {
					Map<String, String> pageMap = getPageFromCitationStr(citationstr);
					refS = pageMap.get("refS");
					refE = pageMap.get("refE");
				}
				// 第二步：引证原文（citationstr）需要学名（sciname）开头
				if (!citationstr.startsWith(sciname)) {
					citationstr = sciname + " " + citationstr;
					citation.setCitationstr(citationstr);
				}
				// 第三步：根据引证原文解析参考文献
				Ref ref = null;
				String refstr = citationstr.substring(sciname.length()).trim();
				List<Ref> reflist = refRepository.findByRefstr(refstr);
				if (reflist == null || reflist.size() == 0) {
					// 新建一条参考文献
					ref = new Ref();
					ref.setId(UUIDUtils.getUUID32());
					refService.parseLineChineseOrEng(ref, refstr);
					ref.setStatus(1);
					ref.setPtype(String.valueOf(PtypeEnum.other.getIndex()));
					ref.setInputer(userId_wangtianshan);
//					toolService.printEntity(ref);
					if (insertOrUpdateDB) {
						refService.saveOne(ref);
					}
				} else {
					ref = reflist.get(0);
				}
				RefTypeEnum refTypeEnum = null;
				int nametype = citation.getNametype();
				if (nametype == NametypeEnum.synonym.getIndex()) {
					refTypeEnum = RefTypeEnum.synonym;
				} else {
					refTypeEnum = RefTypeEnum.other;
				}
				String addRefJson = refService.addRefJson(refjson, ref.getId(), refTypeEnum, refS, refE);
				citation.setRefjson(addRefJson);
				// 在控制台打印实体数据
//				toolService.printEntity(citation);

			}
			if (i % 100 == 0) {
				logger.info("进度打印：" + i * 100 / size + "%");
			}
		}
		// 更新到数据库
		if (insertOrUpdateDB) {
			// 更新
			batchInsertService.batchUpdateCitation(list);
			// 插入新记录
			batchSubmitService.saveAll(insertCitationlist);
		}

	}

	/**
	 * 
	 * @Description 根据taxon的接受名从旧采集系统查询引证，并转换成新Citation
	 * @param taxon
	 * @return
	 * @author ZXY
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	private List<Citation> getFromOldBioIDE(Taxon taxon,NametypeEnum pointNameType) throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		List<Citation> insertCitationlist = new LinkedList<>();
		List<Citation> findlist = null;
		if(pointNameType == null) {
			findlist = citationRepository.findByScientificnameAndtsId(taxon.getScientificname(),
					taxasetIdBirdZL);
		}else if(pointNameType == NametypeEnum.acceptedName) {
			findlist = citationRepository.findByScientificnameAndtsIdAndNameType(taxon.getScientificname(),
					taxasetIdBirdZL, NametypeEnum.acceptedName.getIndex());
		}else if(pointNameType == NametypeEnum.synonym) {
			findlist = citationRepository.findByScientificnameAndtsIdAndNameType(taxon.getScientificname(),
					taxasetIdBirdZL, NametypeEnum.synonym.getIndex());
		}
		for (Citation citation : findlist) {
			citation.setCitationstr(citation.getCitationstr().replace("<br>", ""));
			Citation clone = (Citation) citation.clone();
			clone.setId(UUIDUtils.getUUID32());
			clone.setInputer(userId_wangtianshan);
			clone.setTaxon(taxon);
			String refjson = clone.getRefjson();// 参考文献
			JSONArray array = refService.strToJsonArray(refjson);
			String citationstr = clone.getCitationstr();// 引证原文
			String sciname = clone.getSciname();// 学名
			String refFromCitationstr = "";// 理论上从引证原文中解析出来的参考文献
			// 设置引证原文 citationstr 和 refFromCitationstr
			if (StringUtils.isEmpty(citationstr)) {
//				logger.info("引证原文为空：" + taxon.getScientificname());
				// 引证原文为空，参考文献转换成引证原文
				if (array.size() == 1) {
					clone.setRemark("参考文献-->引证原文");
					Ref ref = refRepository.findOneById(array.getJSONObject(0).get("refId").toString());
					String refstr = ref.getRefstr();
					if (StringUtils.isNotEmpty(refstr)) {
						if (!refstr.contains(sciname)) {
							refFromCitationstr = refstr;
							citationstr = sciname + " " + refstr;
						} else {
							refFromCitationstr = citationstr.replace(sciname, "").trim();
							citationstr = refstr;
						}
						clone.setCitationstr(citationstr);
					}
				} else {
					// 参考文献使用以前的
					clone.setRemark("参考文献与旧系统一致");
				}
//				toolService.printEntity(citation);
			} else {// 引证原文不为空，根据原文查询引证参考文献
				clone.setRemark("引证原文-->参考文献");
				if (citationstr.contains(sciname)) {
					refFromCitationstr = citationstr.replace(sciname, "").trim();
				} else {
					refFromCitationstr = citationstr;
					citationstr = sciname + " " + citationstr;
					clone.setCitationstr(citationstr);
				}
			}
			// 更新参考文献
			if (StringUtils.isNotEmpty(refFromCitationstr)) {
				String newRefId = existInJSONArray(array, refFromCitationstr);
				if (StringUtils.isEmpty(newRefId)) {
					// 新建一条参考文献
					Ref ref = new Ref();
					ref.setId(UUIDUtils.getUUID32());
					refService.parseLineChineseOrEng(ref, refFromCitationstr);
					ref.setStatus(1);
					ref.setPtype(String.valueOf(PtypeEnum.other.getIndex()));
					ref.setInputer(userId_wangtianshan);
					if (insertOrUpdateDB) {
						refService.saveOne(ref);
					}
					newRefId = ref.getId();
				}
				Map<String, String> pageMap = getPageFromCitationStr(refFromCitationstr);
				String refS = pageMap.get("refS");
				String refE = pageMap.get("refE");
				RefTypeEnum refTypeEnum = null;
				int nametype = citation.getNametype();
				if (nametype == NametypeEnum.synonym.getIndex()) {
					refTypeEnum = RefTypeEnum.synonym;
				} else {
					refTypeEnum = RefTypeEnum.other;
				}
				String addRefJson = refService.addRefJson(null, newRefId, refTypeEnum, refS, refE);
				clone.setRefjson(addRefJson);
			}
//			logger.info("DYYZ 0002 打印引证" + taxon.getScientificname());
//			toolService.printEntity(clone);
			insertCitationlist.add(clone);
		}
		return insertCitationlist;
	}

	/**
	 * 
	 * @Description 参考文献原文是否存在于json串中，如果存在，返回refId
	 * @param array
	 * @param refFromCitationstr
	 * @return
	 * @author ZXY
	 */
	private String existInJSONArray(JSONArray jsonArray, String refFromCitationstr) {
		List<String> refIds = new ArrayList<>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
			String existRefId = jsonObject.get("refId").toString(); // 得到 每个对象中的属性值
			refIds.add(existRefId);
		}
		List<Ref> list = refService.findByIds(refIds);
		for (Ref ref : list) {
			if (refFromCitationstr.contains(ref.getRefstr())) {
				return ref.getId();
			}
		}
		return null;
	}

	private List<Citation> turnObjToCitation(List<Object[]> objlist) {
		List<Citation> list = new LinkedList<>();
		for (Object[] objs : objlist) {
			Citation c = new Citation();
			// a.scientificname,c.id,c.sciname,c.authorship,
			// c.nametype,c.citationstr,c.refjson,a.id as taxon_id,c.remark
			String scientificname = objs[0].toString();// 肯定不为空
			String taxonId = objs[7].toString();// 肯定不为空
			c.setTaxon(new Taxon(0, taxonId, scientificname));
			String id = objs[1] == null ? null : objs[1].toString();// 主键
			if (StringUtils.isNotEmpty(id)) {
				c.setId(id);
				c.setSciname(objs[2] == null ? null : objs[2].toString());
				c.setAuthorship(objs[3] == null ? null : objs[3].toString());
				c.setNametype(Integer.parseInt(objs[4].toString()));
				c.setCitationstr(objs[5] == null ? null : objs[5].toString());
				c.setRefjson(objs[6] == null ? null : objs[6].toString());
				c.setRemark(objs[8] == null ? null : objs[8].toString());
			}
			list.add(c);
		}
		return list;
	}

	public Map<String, String> getPageFromCitationStr(String citationstr) {
		citationstr = citationstr.replace("．", ".");// 替换特殊字符
		String PatternProducingAreaReg = "(.*?)(\\(|\\（)(.*?)(\\)|\\）)\\s{0,}";
		Pattern patternOfPPA = Pattern.compile(PatternProducingAreaReg);
		if (patternOfPPA.matcher(citationstr).matches()) {
			char[] flagsPPA = { '(', '（' };
			citationstr = citationstr.substring(0, getNotYearflagIndex(flagsPPA, citationstr)).trim();
		}
		if (citationstr.endsWith(".")) {
			citationstr = citationstr.substring(0, citationstr.length() - 1).trim();
		}
		Map<String, String> resultMap = new HashMap<>();
		Set<Entry<String, String>> entrySet = regExPagelist.entrySet();
		String refS = "0";
		String refE = "0";
		boolean rs = false;
		for (Entry<String, String> entry : entrySet) {
			String key = entry.getKey();
			String regEx = entry.getValue();
			Pattern pattern = Pattern.compile(regEx);
			// 忽略大小写的写法
			// Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(citationstr);
			// 字符串是否与正则表达式相匹配
			rs = matcher.matches();
			if (rs) {
				switch (key) {
				case "page1":
					refS = citationstr.substring(citationstr.lastIndexOf("p."));
					refE = refS;
					break;
				case "page1-1":
					refS = citationstr.substring(citationstr.lastIndexOf("pl."));
					refE = refS;
					break;
				case "page2":
					refS = citationstr.substring(citationstr.lastIndexOf("p."));
					refE = refS;
					break;
				case "page3":
					char[] flags3 = { ',', '，' };
					refS = citationstr.substring(1 + getNotYearflagIndex(flags3, citationstr));
					refE = refS;
					break;
				case "page4":

					break;
				case "page5":
					int lastIndexOfpl = citationstr.lastIndexOf("pl.");
					refS = citationstr.substring(lastIndexOfpl);
					refE = citationstr.substring(lastIndexOfpl);
					break;
				case "page6":
					int lastIndexOf6 = citationstr.lastIndexOf(".");
					refS = citationstr.substring(lastIndexOf6);
					refE = refS;
					break;
				case "page7":
					int lastIndexOf = citationstr.lastIndexOf("note");
					refS = citationstr.substring(lastIndexOf);
					refE = citationstr.substring(lastIndexOf);
					break;
				case "page8":// 纯数字
					char[] cutflags = { '.', ',', '，', '：', ':', ':' };
					refS = citationstr.substring(1 + getNotYearflagIndex(cutflags, citationstr));
					refE = refS;
					break;
				case "page9"://
					char[] flags = { ',', '，', ':', '：', '.' };
					String rangPage = "";
					rangPage = citationstr.substring(1 + getNotYearflagIndex(flags, citationstr));
					String splitflag = "";
					if (rangPage.contains("-")) {
						splitflag = "-";
					} else if (rangPage.contains("‐")) {
						splitflag = "‐";
					} else if (rangPage.contains("–")) {
						splitflag = "–";
					}
					String[] split = rangPage.split(splitflag);
					refS = split[0];
					refE = split[1];
					break;
				case "centerPL"://
//					System.out.println("centerPL="+citationstr);
					String containPage = citationstr.substring(citationstr.indexOf("pl."));
					char[] jumpChars = { 'p', 'l', '.', ' ', 'f', 'i', 'g' };
					for (int index = 0; index < containPage.length(); index++) {
						char charAt = containPage.charAt(index);

						if (!ArrayUtils.contains(jumpChars, charAt) && !CommUtils.isNumeric(String.valueOf(charAt))) {
							refS = containPage.substring(0, index);
							refE = refS;
							break;
						}
					}
					break;
				default:
					throw new ValidationException("未定义的key=" + key);
				}
				break;
			}
		}
		if (!rs) {
			logger.info("正则表达式没有解析出页码citationstr=" + citationstr);
		}
		resultMap.put("refS", refS);
		resultMap.put("refE", refE);
		return resultMap;
	}

	// 项目启动后执行
	@PostConstruct
	public void initRegExPagelist() {
		logger.info("初始化引证解析页码正则表达式，注意前后顺序");
		// p.480 pl.23 fig.3
		regExPagelist.put("page1", "(.*?)p\\.{1}[0-9]+\\s{0,}pl\\.{1}[0-9]+\\s{0,}fig\\.[0-9]+\\s{0,}");
		// pl.448 fig.1
		regExPagelist.put("page1-1", "(.*?)pl\\.[0-9]+\\s{0,}fig\\.[0-9]+\\s{0,}");
		// p.91 pl.52.
		regExPagelist.put("page2", "(.*?)p\\.{1}[0-9]+\\s{0,}pl\\.{1}[0-9]+\\s{0,}$");
		// 数字 pl.数字
		regExPagelist.put("page3", "(.*?)(,|，)\\s{0,}[0-9]+\\s{0,}pl\\.\\s{0,}[0-9]+$");
		// pl.数字
		regExPagelist.put("page5", "(.*?)pl\\.\\s{0,}[0-9]+$");
		// note数字
		regExPagelist.put("page7", "(.*?)note\\s{0,}[0-9]+$");
		// 逗号 数字-数字
		regExPagelist.put("page9", "(.*?)(,|，|:|：|\\.)\\s{0,}[0-9]+(-|‐|–)[0-9]+$");
		// 逗号 数字
		regExPagelist.put("page8", "(.*?)(，|:|：|,|\\.)\\s{0,}[0-9]+\\s{0,}$");
		// p.数字
		regExPagelist.put("page6", "(.*?)p.\\s{0,}[0-9]+(.*?)");
		// pl.数字（包含在字符串中间）
		regExPagelist.put("centerPL", "(.*?)pl\\.\\s{0,}[0-9]+(.*?)");
	}

	public int getNotYearflagIndex(char[] flags, String citationstr) {
		int length = citationstr.length() - 1;
		for (int index = length; index >= 0; index--) {
			char charAt = citationstr.charAt(index);
			if (ArrayUtils.contains(flags, charAt)) {
				return index;
			}
		}
		return -1;
	}

	@Override
	public void validateCitation() throws Exception {
		List<Object[]> list = taxonRepository.findTaxonBydsAndRank(datasetId_2019bird,
				String.valueOf(RankEnum.species.getIndex()));
		int i = 0;
		List<Citation> insertlist = new LinkedList<>();
		for (Object[] objects : list) {
			String taxonid = objects[0].toString();
			String scientificname = objects[1].toString();
			Taxon taxon = new Taxon();
			taxon.setId(taxonid);
			taxon.setScientificname(scientificname);
			//查询某个taxon的接受名引证
			List<String> acceptCitationlist = citationRepository.findIdByTaxonIdAndNametype(taxonid,
					NametypeEnum.acceptedName.getIndex());
			////查询某个taxon的异名引证
			List<String> synonymCitationlist = citationRepository.findIdByTaxonIdAndNametype(taxonid,
					NametypeEnum.synonym.getIndex());
			if (acceptCitationlist.size() == 0) {
				i++;
				List<Citation> findlist = citationRepository.findByScientificnameAndtsIdAndNameType(scientificname,
						taxasetIdBirdZL, NametypeEnum.acceptedName.getIndex());
				//补充引证
				if (findlist.size() > 0) {
					System.out.println("需要补充异名引证：" + scientificname + "	" + objects[2].toString());
					List<Citation> fromOldBioIDE = getFromOldBioIDE(taxon, NametypeEnum.acceptedName);
					insertlist.addAll(fromOldBioIDE);
				}
			}
			if (synonymCitationlist.size() == 0) {
				i++;
				// 是否能从旧采集系统查询到
				List<Citation> findlist = citationRepository.findByScientificnameAndtsIdAndNameType(scientificname,
						taxasetIdBirdZL, NametypeEnum.synonym.getIndex());
				//补充引证
				if (findlist.size() > 0) {
					System.out.println("需要补充异名引证：" + scientificname + "	" + objects[2].toString());
					List<Citation> fromOldBioIDE = getFromOldBioIDE(taxon, NametypeEnum.synonym);
					insertlist.addAll(fromOldBioIDE);
				}
			}
		}
		
		if(insertOrUpdateDB) {
			batchSubmitService.saveAll(insertlist);
		}
		logger.info("缺失引证个数：" + i);
	}

	@Override
	public List<ExcelUntilF> exportCitationExcelOfDataSet(NametypeEnum nametype, String datasetId) {
		List<Object[]> objlist = citationRepository.findByDsAndNameType(datasetId,nametype.getIndex());
		List<ExcelUntilF>  excellist = turnToExcelUntilF(objlist);
		return excellist;
	}

	private List<ExcelUntilF> turnToExcelUntilF(List<Object[]> objlist) {
		List<ExcelUntilF> list = new LinkedList<>();
		for (Object[] objs : objlist) {
			ExcelUntilF row = new ExcelUntilF();
			String tsname = objs[0].toString();//分类单元集名称
			String scientificname = objs[1].toString();//taxon学名
			String chname = objs[7] == null?null:objs[7].toString();//taxon中文名
			String id = objs[2].toString();//引证主键
			String sciname = objs[3].toString();//引证名称
			String authorship = objs[4] == null?null:objs[4].toString();//作者
			int nametype = Integer.parseInt(objs[5].toString());//作者
			String citationstr = objs[6] == null?null:objs[6].toString();//完整引证
			
			row.setColA(tsname);
			row.setColB(scientificname);
			row.setColC(chname);
			row.setColD(sciname);
			row.setColE(authorship);
			row.setColF(citationstr);
			list.add(row);
		}
		return list;
	}

	@Override
	public void deleteCitationOfSameSciname(String datasetId) {
		// TODO Auto-generated method stub
		
	}

}
