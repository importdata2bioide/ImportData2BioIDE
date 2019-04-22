package org.big.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.big.common.CommUtils;
import org.big.common.FilesUtils;
import org.big.common.UUIDUtils;
import org.big.constant.ConfigConsts;
import org.big.constant.DataConsts;
import org.big.constant.MapConsts;
import org.big.entity.Citation;
import org.big.entity.Commonname;
import org.big.entity.Distributiondata;
import org.big.entity.Geoobject;
import org.big.entity.Rank;
import org.big.entity.Ref;
import org.big.entity.Taxon;
import org.big.entity.TaxonHasTaxtree;
import org.big.entity.Taxtree;
import org.big.entityVO.ExcelUntilB;
import org.big.entityVO.ExcelUntilD;
import org.big.entityVO.ExcelUntilF;
import org.big.entityVO.ExcelUntilK;
import org.big.entityVO.ExcelUntilP;
import org.big.entityVO.NametypeEnum;
import org.big.entityVO.ProvinceVO;
import org.big.entityVO.PtypeEnum;
import org.big.entityVO.RankEnum;
import org.big.entityVO.RefTypeEnum;
import org.big.repository.CitationRepository;
import org.big.repository.CommonnameRepository;
import org.big.repository.DistributiondataRepository;
import org.big.repository.GeoobjectRepository;
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
	@Autowired
	private ForcesDBService forcesDBService;
	@Autowired
	private TaxtreeService taxtreeService;
	@Autowired
	private CommonnameRepository commonnameRepository;
	@Autowired
	private DistributiondataRepository distributiondataRepository;
	@Autowired
	private GeoobjectRepository geoobjectRepository;

	private String userId_wangtianshan = "95c24cdc24794909bd140664e2ee9c3b";
	private String datasetId_2019bird = "7da1c0ac-18c6-4710-addd-c9d49e8a2532";
	private String sourcesId_2019bird = "428700b4-449e-4284-a082-b2fe347682ff";
	// 分类单元集：鸟纲-郑-雷-sp2000
	private String taxasetIdBirdZL = "5962f6d5caf8430aba8d85520a0ad1d1";
	boolean insertOrUpdateDB = false;// 数据库变动
	private int citationIndentationLeft = 200;
	private int distributionIndentationLeft = 200;
	private int refIndentationLeft = 200;
	private int fontSize = 10;// 磅
	private String fontFamilyChinese = "宋体";
	private String fontFamily = "Calibri";
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
				List<Citation> addlist = getFromOldBioIDE(taxon, null, taxasetIdBirdZL);
				insertCitationlist.addAll(addlist);
				continue;
			} else if (StringUtils.isEmpty(citationstr)) {
				iter.remove();
//				System.out.println(ConfigConsts.HANDLE_ERROR + "2019鸟类名录引证原文为空,id=" + id);
				// do nothing
				continue;
			} else {
				resetCitationFromCitationstr(citation);
//				System.out.println("----");
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
	 * @Description 根据引证原文解析参考文献
	 * @param citation
	 * @author ZXY
	 */
	private void resetCitationFromCitationstr(Citation citation) {
		String citationstr = citation.getCitationstr();
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
//			toolService.printEntity(ref);
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
//		toolService.printEntity(citation);

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
	private List<Citation> getFromOldBioIDE(Taxon taxon, NametypeEnum pointNameType, String taxasetIdBirdZL)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		List<Citation> insertCitationlist = new LinkedList<>();
		List<Citation> findlist = null;
		if (pointNameType == null) {
			findlist = citationRepository.findByScientificnameAndtsId(taxon.getScientificname(), taxasetIdBirdZL);
		} else if (pointNameType == NametypeEnum.acceptedName) {
			findlist = citationRepository.findByScientificnameAndtsIdAndNameType(taxon.getScientificname(),
					taxasetIdBirdZL, NametypeEnum.acceptedName.getIndex());
		} else if (pointNameType == NametypeEnum.synonym) {
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
			int nametype = clone.getNametype();
			resetCitation(citationstr, clone, array, sciname, nametype);
//			logger.info("DYYZ 0002 打印引证" + taxon.getScientificname());
//			toolService.printEntity(clone);
			insertCitationlist.add(clone);
		}
		return insertCitationlist;
	}

	private void resetCitation(String citationstr, Citation clone, JSONArray array, String sciname, int nametype) {
		String refFromCitationstr = "";// 理论上从引证原文中解析出来的参考文献
		// 设置引证原文 citationstr 和 refFromCitationstr
		if (StringUtils.isEmpty(citationstr)) {
//			logger.info("引证原文为空：" + taxon.getScientificname());
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
//			toolService.printEntity(citation);
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
			if (nametype == NametypeEnum.synonym.getIndex()) {
				refTypeEnum = RefTypeEnum.synonym;
			} else {
				refTypeEnum = RefTypeEnum.other;
			}
			String addRefJson = refService.addRefJson(null, newRefId, refTypeEnum, refS, refE);
			clone.setRefjson(addRefJson);
		}

	}

	private void turnOldToBirdCitation(Citation citation) {
		// TODO Auto-generated method stub

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
		String origncitationstr = citationstr;
		citationstr = citationstr.replaceAll("[..．..]", ".");// 替换特殊字符
		citationstr = citationstr.replace("..", ".");
		citationstr = citationstr.replaceAll("[。]", "");// 替换特殊字符
		citationstr = citationstr.replace("<br>", "");// 替换特殊字符
		citationstr = citationstr.replaceAll("[—-]", "-");
		citationstr = citationstr.replaceAll("[：：]", ":");
		citationstr = citationstr.trim();
		if (citationstr.endsWith(".")) {
			citationstr = citationstr.substring(0, citationstr.length() - 1).trim();
		}
		String PatternProducingAreaReg = "(.*?)(\\(|\\（)(.*?)(\\)|\\）)\\s{0,}";
		Pattern patternOfPPA = Pattern.compile(PatternProducingAreaReg);
//		logger.info("匹配结果："+patternOfPPA.matcher(citationstr).matches());
		if (patternOfPPA.matcher(citationstr).matches()) {
			char[] flagsPPA = { '(', '（' };
			citationstr = citationstr.substring(0, getNotYearflagIndex(flagsPPA, citationstr)).trim();
		}
		if (citationstr.endsWith(".")) {
			citationstr = citationstr.substring(0, citationstr.length() - 1).trim();
		}
		if (citationstr.startsWith(",")) {
			citationstr = citationstr.substring(1).trim();
		}
//		logger.info("替换特殊字符后："+cita tionstr);
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
				case "plate":
					refS = citationstr.substring(citationstr.lastIndexOf("图版"));
					refE = refS;
					break;
				default:
					throw new ValidationException("未定义的key=" + key);
				}
				break;
			}
		}
		if (!rs) {
//			logger.info("------原始："+origncitationstr);
			logger.info("getPageFromCitationStr 正则表达式没有解析出页码citationstr=" + citationstr);
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
		// 图版 数字
		regExPagelist.put("plate", "(.*?)图版\\s{0,}[0-9]+(.*?)");
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
			// 查询某个taxon的接受名引证
			List<String> acceptCitationlist = citationRepository.findIdByTaxonIdAndNametype(taxonid,
					NametypeEnum.acceptedName.getIndex());
			//// 查询某个taxon的异名引证
			List<String> synonymCitationlist = citationRepository.findIdByTaxonIdAndNametype(taxonid,
					NametypeEnum.synonym.getIndex());
			// 第1步：补充接受名引证
			if (acceptCitationlist.size() == 0) {
				i++;
				List<Citation> findlist = citationRepository.findByScientificnameAndtsIdAndNameType(scientificname,
						taxasetIdBirdZL, NametypeEnum.acceptedName.getIndex());
				// 补充引证
				if (findlist.size() > 0) {
					// 1、数据来源于采集系统 数据集=鸟纲-郑-雷-sp2000
					System.out.println("需要补充接受名引证：" + scientificname + "	" + objects[2].toString());
					List<Citation> fromOldBioIDE = getFromOldBioIDE(taxon, NametypeEnum.acceptedName, taxasetIdBirdZL);
					insertlist.addAll(fromOldBioIDE);
				} else if (findlist.size() == 0) {
					// 2、旧系统数据库 鸟纲-郑-雷-sp2000
					List<Citation> acceptCitationlistFromOldDB = forcesDBService.getAcceptCitationByParams(taxon,
							DataConsts.forcesDB_Tree_Id_Bird, DataConsts.Datasource_Id_Old_bird, userId_wangtianshan);
					if (acceptCitationlistFromOldDB != null && acceptCitationlistFromOldDB.size() > 0) {
						insertlist.addAll(acceptCitationlistFromOldDB);
					} else {
						// 3、旧系统数据库 动物志
						List<Citation> acceptlistFromOldDBDWZ = forcesDBService.getAcceptCitationByParams(taxon,
								DataConsts.forcesDB_Tree_Id_DWZ, DataConsts.Datasource_Id_Old_DWZ, userId_wangtianshan);
						if (acceptlistFromOldDBDWZ != null && acceptlistFromOldDBDWZ.size() > 0) {
							insertlist.addAll(acceptlistFromOldDBDWZ);
						}
					}
				}

			}
			// 第2步：补充异名引证
			if (synonymCitationlist.size() == 0) {
				i++;
				// 是否能从旧采集系统查询到
				List<Citation> findlist = citationRepository.findByScientificnameAndtsIdAndNameType(scientificname,
						taxasetIdBirdZL, NametypeEnum.synonym.getIndex());
				// 补充引证
				if (findlist.size() > 0) {
//					System.out.println("需要补充异名引证：" + scientificname + "	" + objects[2].toString());
					List<Citation> fromOldBioIDE = getFromOldBioIDE(taxon, NametypeEnum.synonym, taxasetIdBirdZL);
					insertlist.addAll(fromOldBioIDE);
				}
			}
		}

		if (insertOrUpdateDB) {
			batchSubmitService.saveAll(insertlist);
		}
		logger.info("缺失引证个数：" + i);
	}

	@Override
	public List<ExcelUntilF> exportCitationExcelOfDataSet(NametypeEnum nametype, String datasetId) {
		List<Object[]> objlist = citationRepository.findByDsAndNameType(datasetId, nametype.getIndex());
//		List<Object[]> objlist = citationRepository.findByDsAndCitationIsNull(datasetId);
		List<ExcelUntilF> excellist = turnToExcelUntilF(objlist);
		return excellist;
	}

	private List<ExcelUntilF> turnToExcelUntilF(List<Object[]> objlist) {
		List<ExcelUntilF> list = new LinkedList<>();
		for (Object[] objs : objlist) {
			ExcelUntilF row = new ExcelUntilF();
			String tsname = objs[0].toString();// 分类单元集名称
			String scientificname = objs[1].toString();// taxon学名
			String chname = objs[7] == null ? null : objs[7].toString();// taxon中文名
			String id = objs[2].toString();// 引证主键
			String sciname = objs[3].toString();// 引证名称
			String authorship = objs[4] == null ? null : objs[4].toString();// 作者
			int nametype = Integer.parseInt(objs[5].toString());// 作者
			String citationstr = objs[6] == null ? null : objs[6].toString();// 完整引证

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
		List<String> deleteCitationIdlist = new LinkedList<>();
		// 查询数据集下的所有种阶元taxon
		List<Object[]> list = taxonRepository.findTaxonBydsAndRank(datasetId_2019bird,
				String.valueOf(RankEnum.species.getIndex()));
		for (Object[] objects : list) {
			String taxonid = objects[0].toString();
			String scientificname = objects[1].toString();
			Taxon taxon = new Taxon();
			taxon.setId(taxonid);
			taxon.setScientificname(scientificname);
			// 查询某个taxon的接受名引证
			List<Object[]> acceptCitationlist = citationRepository.findByTaxonIdAndNametype(taxonid,
					NametypeEnum.acceptedName.getIndex());
			// 查询某个taxon的异名引证
			List<Object[]> synonymCitationlist = citationRepository.findByTaxonIdAndNametype(taxonid,
					NametypeEnum.synonym.getIndex());
			// 判断异名引证中是否有和接受名引证 名称和命名人一致的record
			// id,sciname,authorship,nametype,citationstr,refjson
			for (Object[] acceptCitation : acceptCitationlist) {
				String acceptSciname = acceptCitation[1].toString();
				String acceptAuthorship = acceptCitation[2] == null ? null : acceptCitation[2].toString();
				for (Object[] synonymCitation : synonymCitationlist) {
					String synonymSciname = synonymCitation[1].toString();
					if (!acceptSciname.trim().equals(synonymSciname.trim())) {
						continue;
					}
					String synonymAuthorship = synonymCitation[2] == null ? null : acceptCitation[2].toString();
					if (StringUtils.isNotEmpty(acceptAuthorship) && StringUtils.isNotEmpty(synonymAuthorship)) {
						if (!acceptAuthorship.contains(synonymAuthorship)) {
							continue;
						}
					}
					// 需要删除的异名引证
//					System.out.println("---需要删除的异名引证");
//					System.out.println("acceptSciname="+acceptSciname);
//					System.out.println("synonymSciname="+synonymSciname);
//					System.out.println("acceptAuthorship="+acceptAuthorship);
//					System.out.println("synonymAuthorship="+synonymAuthorship);
					String synonymId = synonymCitation[0].toString();
					deleteCitationIdlist.add(synonymId);
//					logger.info("需要删除的异名引证 打印：" + acceptSciname);
				}
			}

		}

		logger.info("需要删除的异名引证个数：" + deleteCitationIdlist.size());
		if (insertOrUpdateDB && deleteCitationIdlist.size() > 0) {
			int deleteByIdsResult = citationRepository.deleteByIds(deleteCitationIdlist);
			logger.info("删除异名引证个数为：" + deleteByIdsResult);
		}

	}

	@Override
	public void perfectCitationStr(String datasetId) throws Exception {
		List<Citation> updateCitationlist = new LinkedList<>();
		// 查询异名引证 的 完整引证字段 为空的数据
		List<Object[]> list = citationRepository.findByDsAndNameTypeAndCitationstrNull(datasetId,
				NametypeEnum.synonym.getIndex());
		logger.info("查询异名引证 的 完整引证字段 为空的数据 条数：" + list.size());
		int i = 0;
		int size = list.size();
		for (Object[] objs : list) {
			i++;
			if (i % 100 == 0) {
				logger.info("进度报告：" + i * 100 / size + "%（i=" + i + ",size=" + size + "）");
			}
			String sciname = objs[3].toString();// 引证名称
			String scientificname = objs[1].toString();// taxon学名
			Citation citation = new Citation();
			String id = objs[2].toString();// 引证主键
			String refjson = objs[8] == null ? null : objs[8].toString();// citation参考文献
			citation.setId(id);
			citation.setRefjson(refjson);
			citation.setRemark("信息来源于旧采集系统 动物志");
			citation.setSciname(sciname);
			Taxon taxon = new Taxon();
			taxon.setScientificname(scientificname);
			citation.setTaxon(taxon);
			// 从旧采集系统（动物志获取）
//			boolean update = getCitationFromDataSet(scientificname,sciname,DataConsts.Dataset_Id_DWZ,citation);
//			logger.info("__"+scientificname+"__"+sciname);
			boolean update = forcesDBService.getCitationFromForcesDB(scientificname, sciname,
					DataConsts.forcesDB_Tree_Id_DWZ, citation, "BEDBB69A-139D-45A3-8CD9-CC7D55BF6E7E");
			if (!update) {
				continue;
			}
			resetCitationFromCitationstr(citation);
			updateCitationlist.add(citation);
//			String tsname = objs[0].toString();//分类单元集名称
//			String chname = objs[7] == null?null:objs[7].toString();//taxon中文名
//			String authorship = objs[4] == null?null:objs[4].toString();//作者
//			int nametype = Integer.parseInt(objs[5].toString());//名称类型
//			String citationstr = objs[6] == null?null:objs[6].toString();//完整引证

		}
		// 更新数据库
		logger.info("需要根据动物志更新的引证数量：" + updateCitationlist.size());
		if (insertOrUpdateDB) {
			// 更新引证：authorship，citationstr，refjson,remark
			batchInsertService.batchUpdateCitationFourById(updateCitationlist);
		}
	}

	private boolean getCitationFromDataSet(String scientificname, String sciname, String datasetId, Citation citation) {
		Citation findcitation = citationRepository.findByTaxonAndScinameAndNameType(scientificname,
				NametypeEnum.synonym.getIndex(), sciname, datasetId);
		if (findcitation != null) {
			String citationstr = findcitation.getCitationstr();
			String authorship = findcitation.getAuthorship();
			String refjson = findcitation.getRefjson();
			JSONArray array = refService.strToJsonArray(refjson);
			citation.setAuthorship(authorship);
			if (StringUtils.isNotEmpty(citationstr)) {
				resetCitation(citationstr, citation, array, sciname, NametypeEnum.synonym.getIndex());
				return true;
			}
		}
		return false;
	}

	@Override
	public void perfectAuthorFromExcel() {
		String excelPath = "E:\\003采集系统\\0012鸟类名录\\2019Bird_synonymCitation20190415.xls";
		List<ExcelUntilB> list = toolService.readExcel(excelPath, ExcelUntilB.class);
		List<Citation> updatelist = new LinkedList<>();
		int i = 0;
		for (ExcelUntilB row : list) {
			String sciname = row.getColA().trim();
			String scinameAndAuthor = row.getColB();
			String authorFromExcel = scinameAndAuthor.replace(sciname, "").trim();
			// 从数据库查询引证
			List<Object[]> citatoionlist = citationRepository.findByDatasetAndSciName(DataConsts.Dataset_Id_Bird2019,
					sciname);
			// 转换成实体类
			List<Citation> clist = findByDatasetAndSciNameTurnToCitation(citatoionlist);
			// 结果遍历
			Iterator<Citation> iterator = clist.iterator();
			if (iterator.hasNext()) {
				Citation citation = iterator.next();
				if (citation.getNametype() == NametypeEnum.acceptedName.getIndex()) {
					iterator.remove();
					continue;
				}
				citation.setAuthorship(authorFromExcel);
				// 添加到更新数组中
				updatelist.add(citation);

			}

		}
		logger.info("根据 id 更新citation 的 authorship，个数：" + updatelist.size());
		// 更新citation:根据主键更新命名信息
		if (insertOrUpdateDB) {
			batchInsertService.batchUpdateCitationAuthorship(updatelist);
		}
	}

	private List<Citation> findByDatasetAndSciNameTurnToCitation(List<Object[]> citatoionlist) {
		List<Citation> list = new LinkedList<>();
		// c.id,c.sciname,c.authorship,c.nametype,c.taxon_id,c.citationstr
		for (Object[] objs : citatoionlist) {
			Citation e = new Citation();
			e.setId(objs[0].toString());
			e.setSciname(objs[1].toString());
			e.setAuthorship(objs[2] == null ? null : objs[2].toString());
			e.setNametype(Integer.parseInt(objs[3].toString()));
			e.setTaxon(new Taxon(objs[4].toString()));
			e.setCitationstr(objs[5] == null ? null : objs[5].toString());
			list.add(e);
		}
		return list;
	}

	@Override
	public void perfectAuthorByCitationStr() {
		// 查询所有引证信息
		List<Citation> list = citationRepository.findByDs(DataConsts.Dataset_Id_Bird2019);
		for (Citation citation : list) {
			String authorship = citation.getAuthorship();
			String citationstr = citation.getCitationstr();
			String sciname = citation.getSciname();
			boolean parseFromCitaionStr = false;
			if (StringUtils.isEmpty(authorship)) {// 判断命名信息是否为空
				// 命名信息为空,从引证中解析命名信息
				parseFromCitaionStr = true;
			} else {
				// 命名信息不为空,判断authorship中是否包含年份
				if (!containsFourNum(authorship)) {
					// 命名信息中不包含年份，从引证中解析命名信息
					parseFromCitaionStr = true;
				}
			}
			if (parseFromCitaionStr && StringUtils.isNotEmpty(citationstr)) {
				Map<String, String> map = toolService.parseSciName(citationstr);
				String newAuthorship = map.get(MapConsts.TAXON_AUTHOR);
				String rankName = map.get(MapConsts.TAXON_RANK_NAME);
				if (rankName.equals(RankEnum.unknown.getName())) {
					int yearStart = toolService.getYearStart(citationstr);
					String beforeYearInclude = citationstr.substring(0, yearStart + 4);
					newAuthorship = beforeYearInclude.replace(sciname, "");
//					System.out.println("截取年份之前的内容："+beforeYearInclude);
//					System.out.println("命名信息："+newAuthorship);
				}
				if (StringUtils.isNotEmpty(newAuthorship)) {
					if (newAuthorship.startsWith(",") || newAuthorship.startsWith("，")
							|| newAuthorship.startsWith(".")) {
						newAuthorship = newAuthorship.substring(1);
					}
					newAuthorship = newAuthorship.replaceAll(sciname, "");
					// 新命名信息包含旧命名信息，但是不以旧命名信息开头
					if (StringUtils.isNotEmpty(authorship) && newAuthorship.contains(authorship)
							&& !newAuthorship.startsWith(authorship)) {
						newAuthorship = newAuthorship.substring(newAuthorship.indexOf(authorship));
					}
					citation.setAuthorship(newAuthorship);
				}
//				System.out.println("---原命名信息：" + authorship);
//				System.out.println("sciname = " + sciname);
//				System.out.println("引证原文：" + citationstr);
//				System.out.println("从引证原文中解析出来的命名信息" + newAuthorship);
			}

		}
		//
		if (insertOrUpdateDB) {
			// 根据 id 更新citation 的 authorship
			batchInsertService.batchUpdateCitationAuthorship(list);
		}
	}

	private boolean containsFourNum(String line) {
		Pattern pattern = Pattern.compile("(.*?)[0-9]{4}(.*?)");
		// 忽略大小写的写法
		// Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(line);
		// 字符串是否与正则表达式相匹配
		boolean rs = matcher.matches();
		return rs;
	}

	@Override
	public void printDontHasAcceptCitationTaxon() {

		List<Object[]> list = taxonRepository.findTaxonBydsAndRank(datasetId_2019bird,
				String.valueOf(RankEnum.species.getIndex()));
		for (Object[] objects : list) {
			String taxonid = objects[0].toString();
			String scientificname = objects[1].toString();
			String tsname = objects[2].toString();
			String chname = objects[3].toString();
			Taxon taxon = new Taxon();
			taxon.setId(taxonid);
			taxon.setScientificname(scientificname);
			// 查询某个taxon的接受名引证
			List<String> acceptCitationlist = citationRepository.findIdByTaxonIdAndNametype(taxonid,
					NametypeEnum.acceptedName.getIndex());

			// 第1步：补充接受名引证
			if (acceptCitationlist.size() == 0) {
				logger.info(tsname + "	" + scientificname + "	" + chname);
			}
		}

	}

	@Override
	public void countCitationByDs(String datasetId) {
		int acceptcount = citationRepository.countCitationByDsAndNametype(datasetId,
				NametypeEnum.acceptedName.getIndex());
		int syncount = citationRepository.countCitationByDsAndNametype(datasetId, NametypeEnum.synonym.getIndex());
		logger.info("--@@统计结果：接受名引证数量：" + acceptcount);
		logger.info("--@@统计结果：异名引证数量：" + syncount);
	}

	/**
	 * 
	 * @Description 属补充中文名，从2018版名录鸟类部分补充
	 * @author ZXY
	 */
	private void perfactGenusCHname() {
		List<Object[]> genuslist = taxonRepository.findTaxonBydsAndRank(DataConsts.Dataset_Id_Bird2019,
				String.valueOf(RankEnum.genus.getIndex()));
		logger.info("2019鸟类名录属个数：" + genuslist.size());
		List<Taxon> updatechanmelist = new LinkedList<>();
		int cantfindGenusCount = 0;
		for (Object[] objs : genuslist) {
			String chname = objs[3] == null ? null : objs[3].toString();
			String scientificname = objs[1].toString().trim();
			String tsname = objs[2].toString().trim();
			if (StringUtils.isNotEmpty(chname)) {
//				logger.info("已有属中文名		"+tsname+"	"+scientificname);
				continue;
			}
			String id = objs[0].toString();
			// 从旧采集系统 鸟纲 郑雷 补充 属中文名
			Taxon taxon = taxonRepository.findByTaxasetAndScientificname(DataConsts.Taxaset_Id_Bird2018,
					scientificname);
			if (taxon == null || StringUtils.isEmpty(taxon.getChname()) || taxon.getChname().equals(" ")) {
				cantfindGenusCount++;
//				logger.info(cantfindGenusCount+":"+(taxon == null)+"（bioide数据库）从旧采集系统 鸟纲 郑雷 查询到属中文名为空，tsname="+tsname+"	scientificname="+scientificname);
//				logger.info(tsname+"	"+scientificname);
				continue;
			}
			Taxon updateTaxon = new Taxon();
			updateTaxon.setId(id);
			updateTaxon.setChname(taxon.getChname());
			// 更新属中文名
			updatechanmelist.add(updateTaxon);

		}
		// 数据库更新中文名
		if (insertOrUpdateDB && updatechanmelist.size() > 0) {
			batchInsertService.updateTaxonChname(updatechanmelist);
		}
	}

	private void writeSpeciesOrSubp(Taxon taxon, XWPFDocument doc, int citationStyle,
			Map<String, ProvinceVO> provinceEntityMap, Map<String, Integer> provinceMap) {
		// 1、接受名 命名信息 中文名
		XWPFParagraph nameParagraph = doc.createParagraph();// 创建段落
		nameParagraph.setAlignment(ParagraphAlignment.LEFT);// 居中对齐
		writeTaxon(nameParagraph, taxon);
		// 2、引证信息
		// 接受名引证
		List<Citation> citationListByTaxonId = citationRepository.findAllByTaxonIdAndNametype(taxon.getId(),
				NametypeEnum.acceptedName.getIndex());
		writeCitation(citationStyle, taxon, doc, citationListByTaxonId);
		// 异名引证
		List<Citation> citationSynByTaxonId = citationRepository.findAllByTaxonIdAndNametype(taxon.getId(),
				NametypeEnum.synonym.getIndex());
		if (citationSynByTaxonId.size() > 0) {
			XWPFParagraph para = doc.createParagraph();// 创建段落
			para.setAlignment(ParagraphAlignment.LEFT);// 居中对齐
			para.setIndentationLeft(citationIndentationLeft);
			writeWithStyle(para, "异名：", false, false);
			writeCitation(citationStyle, taxon, doc, citationSynByTaxonId);
		}
		// 3、别名
		writeCommName(taxon, doc);
		// 4、分布
		writeDistribution(taxon, doc, provinceEntityMap, provinceMap);
		// 5、文献
		writeRef(taxon, doc);

	}

	private void writeRef(Taxon taxon, XWPFDocument doc) {
		XWPFParagraph paragraph = doc.createParagraph();// 创建段落
		paragraph.setAlignment(ParagraphAlignment.LEFT);// 居中对齐
		paragraph.setIndentationLeft(refIndentationLeft);
		writeWithStyle(paragraph, "文献：", false, false);
		String refStr = "Zheng Zuoxin et al., 1979; 郑作新等, 1979; Zheng Zuoxin, 1989; 郑作新, 1989; ZHENG Guangmei, WANG Qishan, 1998; 郑光美, 王岐山, 1998; 约翰.马敬能, 卡伦.菲利普斯, 何芬奇, 2000; 郑光美, 2011";
		writeWithStyle(paragraph, refStr, false, false);
	}

	private void writeDistribution(Taxon taxon, XWPFDocument doc, Map<String, ProvinceVO> provinceEntityMap,
			Map<String, Integer> provinceMap) {
		List<Distributiondata> list = distributiondataRepository.findDistributiondataListByTaxonId(taxon.getId());
		if (list.size() == 0) {
			return;
		}
		StringBuffer line = new StringBuffer();
		List<String> geoIdlist = new ArrayList<>();
		Map<Integer, String> sortMap = new TreeMap<>();
		// 从bioide查询分布地
		for (Distributiondata distributiondata : list) {
			String geojson = distributiondata.getGeojson();
			Object geoIds = CommUtils.strToJSONObject(geojson).get("geoIds");
			String geoIdsStr = geoIds == null ? null : geoIds.toString();
			if (StringUtils.isNotEmpty(geoIdsStr)) {
				String[] geoIdSplit = geoIdsStr.split("&");
				geoIdlist.addAll(Arrays.asList(geoIdSplit));
			}
		}
		List<Geoobject> geoobjectlist = geoobjectRepository.findAllById(geoIdlist);
		// 排序
		for (Geoobject geoobject : geoobjectlist) {
			String cngeoname = geoobject.getCngeoname();
			cngeoname = handleSpecial(cngeoname);
			if (StringUtils.isNotEmpty(cngeoname)) {
				Integer order = provinceMap.get(cngeoname);
				if (order == null || order == 0) {
					logger.info("taxon = " + taxon.getScientificname());
					logger.info("-没有找到：cngeoname=" + cngeoname);
				} else {
					sortMap.put(order, cngeoname);
				}
			}
		}
		// 创建段落
		if (sortMap.size() > 0) {
			XWPFParagraph paragraph = doc.createParagraph();// 创建段落
			paragraph.setAlignment(ParagraphAlignment.LEFT);// 居中对齐
			paragraph.setIndentationLeft(distributionIndentationLeft);
			writeWithStyle(paragraph, "分布：", false, false);
			// 遍历结果，重组段落内容
			Iterator<Map.Entry<Integer, String>> entries = sortMap.entrySet().iterator();
			while (entries.hasNext()) {
				Map.Entry<Integer, String> entry = entries.next();
				String name = entry.getValue();
				// 获得英文缩写
				ProvinceVO provinceVO = provinceEntityMap.get(name);
				String codeChar = "";
				if (provinceVO == null) {
					logger.info("-错误：找不到的provinceVO实体类name = " + name);
				} else {
					codeChar = provinceVO.getCodeChar();
					codeChar = "(" + codeChar + ")";
				}
				line.append(name + codeChar + "，");
			}
			if (line.length() > 0) {
				String strline = line.toString();
				strline = strline.substring(0, strline.length() - 1).toString(); // 写到word中
				writeWithStyle(paragraph, strline, false, false);
			}
		}

	}

	private String handleSpecial(String cngeoname) {
		cngeoname = cngeoname.replace("市辖区", "");
		if (cngeoname.equals("吉林市")) {
			cngeoname = "吉林省";
		} else if (cngeoname.equals("中国")) {
			cngeoname = null;
		} else if (cngeoname.equals("东沙群岛")) {
			cngeoname = null;
		}
		return cngeoname;
	}

	private void writeCommName(Taxon taxon, XWPFDocument doc) {
		List<Commonname> commonnameList = commonnameRepository.findCommonnameListByTaxonId(taxon.getId());
		if (commonnameList.size() > 0) {
			XWPFParagraph paragraph = doc.createParagraph();// 创建段落
			paragraph.setAlignment(ParagraphAlignment.LEFT);// 居中对齐
			paragraph.setIndentationLeft(citationIndentationLeft);
			StringBuffer sb = new StringBuffer();
			for (Commonname commonname : commonnameList) {
				sb.append(commonname.getCommonname() + "，");
			}
			String nameline = sb.toString();
			nameline = nameline.substring(0, nameline.length() - 1);
			writeWithStyle(paragraph, "别名：", false, false);
			writeWithStyle(paragraph, nameline, false, false);
		}
	}

	private void writeTaxon(XWPFParagraph paragraph, Taxon taxon) {
		String scientificname = taxon.getScientificname();
		String chname = taxon.getChname();
		String authorstr = taxon.getAuthorstr();
		String butSciname = "";
		if (StringUtils.isNotEmpty(authorstr)) {
			butSciname = butSciname + " " + authorstr;
		}
		if (StringUtils.isNotEmpty(chname)) {
			butSciname = butSciname + " " + chname;
		}
		writeWithStyle(paragraph, scientificname, true, true);
		if (StringUtils.isNotEmpty(butSciname)) {
			writeWithStyle(paragraph, butSciname, true, false);
		}

	}

	private void writeGenus(Taxon taxon, XWPFDocument doc, int citationStyle) {
		// 1、学名 命名信息 中文名
		XWPFParagraph paragraph = doc.createParagraph();// 创建段落
		paragraph.setAlignment(ParagraphAlignment.CENTER);// 居中对齐
		writeTaxon(paragraph, taxon);
		// 2、引证信息
		// 接受名引证
		List<Citation> citationListByTaxonId = citationRepository.findAllByTaxonIdAndNametype(taxon.getId(),
				NametypeEnum.acceptedName.getIndex());
		writeCitation(citationStyle, taxon, doc, citationListByTaxonId);
		// 异名引证
		List<Citation> citationSynByTaxonId = citationRepository.findAllByTaxonIdAndNametype(taxon.getId(),
				NametypeEnum.synonym.getIndex());
		if (citationSynByTaxonId.size() > 0) {
			XWPFParagraph para = doc.createParagraph();// 创建段落
			para.setAlignment(ParagraphAlignment.LEFT);// 居中对齐
			para.setIndentationLeft(citationIndentationLeft);
			writeWithStyle(para, "异名：", false, false);
			writeCitation(citationStyle, taxon, doc, citationSynByTaxonId);
		}

	}

	private void writeCitation(int citationStyle, Taxon taxon, XWPFDocument doc, List<Citation> citationListByTaxonId) {
		for (Citation citation : citationListByTaxonId) {
			String authorship = citation.getAuthorship();// 命名信息
			int nametype = citation.getNametype();
			String sciname = citation.getSciname();// 学名
			String citationstr = citation.getCitationstr();// 引证原文
			if (StringUtils.isNotEmpty(citationstr)) {
				citationstr = citationstr.replace(sciname, "");// 去掉接受名的引证原文
			}
			if (StringUtils.isNotEmpty(authorship)) {
				authorship = authorship.replaceAll("[()]", "");
				authorship = reppacePointToComma(authorship);
			}
			JSONArray refJsonArray = refService.strToJsonArray(citation.getRefjson());
			String pages = "";// 页码
			String refS = "";// 起始页
			String refE = "";// 终止页
			for (int i = 0; i < refJsonArray.size(); i++) {
				JSONObject jsonObject = refJsonArray.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
				refS = jsonObject.get("refS").toString(); // 得到 每个对象中的属性值
				refE = jsonObject.get("refE").toString();
				if (StringUtils.isEmpty(refS) && StringUtils.isEmpty(refE)) {// 起始页码均为空
					continue;
				} else if (refS.equals(refE) && !refS.equals("0")) {// 页码相同
					pages = refS;
				} else {
					try {
						int parseIntS = Integer.parseInt(refS);
						int parseIntE = Integer.parseInt(refE);
						if (parseIntS < parseIntE) {
							pages = refS + "-" + refE;// 页码是一个范围
						}
					} catch (NumberFormatException e) {
						pages = refE + "-" + refS;// 页码是一个范围（数据库中的数据页码写反了）
					}

				}
				break;
			}

			XWPFParagraph paragraph = doc.createParagraph();// 创建段落
			paragraph.setAlignment(ParagraphAlignment.LEFT);// 居中对齐
			paragraph.setIndentationLeft(citationIndentationLeft);
			// citationStyle 1|引证全文/2|引证只包含命名信息和页码
			switch (citationStyle) {
			case 1:
				if (nametype == NametypeEnum.acceptedName.getIndex()) {
					// 接受名引证无学名
				} else if (nametype == NametypeEnum.synonym.getIndex()) {
					// 异名引证有学名
					writeWithStyle(paragraph, sciname, false, true);
				} else {
					throw new ValidationException("未定义的名称类型：" + nametype);
				}
				// 引证原文
				if (StringUtils.isNotEmpty(citationstr)) {
					writeWithStyle(paragraph, citationstr, false, false);
				}
				break;
			case 2:
				if (nametype == NametypeEnum.acceptedName.getIndex()) {
					// 接受名引证无学名
				} else if (nametype == NametypeEnum.synonym.getIndex()) {
					// 异名引证有学名
					writeWithStyle(paragraph, sciname, false, true);
				} else {
					throw new ValidationException("未定义的名称类型：" + nametype);
				}
				// 命名信息
				if (StringUtils.isNotEmpty(authorship)) {
					writeWithStyle(paragraph, authorship, false, false);
				}
				// 页码
				if (StringUtils.isNotEmpty(pages) && !pages.trim().equals("0")) {
					writeWithStyle(paragraph, ", " + pages, false, false);
				}
				break;
			default:
				throw new ValidationException("未定义的citationStyle=" + citationStyle);
			}
		}

	}

	private void writeFamily(Taxon taxon, XWPFDocument doc, int citationStyle) {
		String scientificname = taxon.getScientificname();
		String chname = taxon.getChname();
		String text = scientificname + " " + chname;
		XWPFParagraph paragraph = doc.createParagraph();// 创建段落
		paragraph.setAlignment(ParagraphAlignment.CENTER);// 居中对齐
		writeWithStyle(paragraph, text, true, false);
	}

	@Override
	public void exportToWord() throws Exception {
		logger.info("定义参数");
		String outputfolder = "E:\\003采集系统\\0012鸟类名录\\输出文件20190418\\";
		String fileName = "2019鸟类名录.docx";
		int citationStyle = 1;// 1|引证全文/2|引证只包含命名信息和页码
		// logger.info("第0步：属补充中文名，从2018版名录鸟类部分补充+根据接受名引证命名信息更新taxon命名信息");
		// perfactGenusCHname();
		// updateTaxonAuthor();
		if (citationStyle == 1) {
			fileName = "2019鸟类名录(引证全文).docx";
		}
		logger.info("第1步：从forces查询分布地");
		List<ProvinceVO> provincelist = forcesDBService.findProvince();
		Map<String, Integer> provinceMap = turnToMap(provincelist);
		Map<String, ProvinceVO> provinceEntityMap = turnToEntityMap(provincelist);
		logger.info("第2步：查询数据集下的所有分类树");
		List<Taxtree> treelist = taxtreeService.findTaxtreeByDataset(DataConsts.Dataset_Id_Bird2019);
		logger.info("《中国鸟类分类与分布名录第三版》下共有分类树个数:" + treelist.size());
		logger.info("第3步：遍历每一颗分类树,并输出到word文件中");
		XWPFDocument doc = null;
		OutputStream os = null;
		try {
			// 创建一个word文件
			doc = new XWPFDocument();
			// 写内容
			for (Taxtree taxtree : treelist) {
				logger.info("--" + taxtree.getTreename());
				writeToWord(taxtree, doc, citationStyle, provinceEntityMap, provinceMap);

				// break;
			}
			// 保存到磁盘上
			os = new FileOutputStream(outputfolder + fileName);
			doc.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (doc != null) {
				doc.close();
			}
			if (os != null) {
				os.close();
			}
			System.out.println("执行结束");
		}

	}

	private Map<String, ProvinceVO> turnToEntityMap(List<ProvinceVO> provincelist) {
		Map<String, ProvinceVO> map = new HashMap<>();
		for (ProvinceVO provinceVO : provincelist) {
			map.put(provinceVO.getCname(), provinceVO);
		}
		return map;
	}

	private Map<String, Integer> turnToMap(List<ProvinceVO> provincelist) {
		Map<String, Integer> map = new HashMap<>();
		for (ProvinceVO provinceVO : provincelist) {
			map.put(provinceVO.getCname(), provinceVO.getOrderr());
		}
		return map;
	}

	/**
	 * 
	 * @Description
	 * @param paragraph
	 * @param line
	 * @param bold      是否加粗
	 * @param italic    是否倾斜（只针对非中文）
	 * @author ZXY
	 */
	private void writeWithStyle(XWPFParagraph paragraph, String line, boolean bold, boolean italic) {
		// 起始字符的性质
		if (StringUtils.isEmpty(line)) {
			return;
		}
		// 第一个中文的位置
		ToolService toolService = new ToolServiceImpl();
		int indexOfFirstChinese = toolService.IndexOfFirstBelongToChinese(line);
		// 第一个非中文的位置
		int indexOfFirstNotChinese = toolService.IndexOfFirstNotBelongToChinese(line);
		if (indexOfFirstChinese == -1 && indexOfFirstNotChinese == 0) {
//			String notChinese = line;
//			System.out.println("finally非中文:" + line);
			XWPFRun run = paragraph.createRun();
			run.setFontFamily(fontFamily);// 字体
			run.setBold(bold);// 加粗
			run.setFontSize(fontSize);// 字号
			run.setText(line);// 标题内容
			run.setItalic(italic);// 斜体（字体倾斜）
		} else if (indexOfFirstChinese == 0 && indexOfFirstNotChinese == -1) {
//			String chinese = line;
//			System.out.println("finally中文:" + line);
			XWPFRun run = paragraph.createRun();
			run.setFontFamily(fontFamilyChinese);// 字体
			run.setBold(bold);// 加粗
			run.setFontSize(fontSize);// 字号
			run.setText(line);// 标题内容
		} else if (indexOfFirstChinese > indexOfFirstNotChinese) {// 非中文开头
			String eng = StringUtils.substring(line, indexOfFirstNotChinese, indexOfFirstChinese);
			if (StringUtils.isEmpty(eng)) {
				return;
			}
//			System.out.println("非中文：" + eng);
			XWPFRun run = paragraph.createRun();
			run.setFontFamily(fontFamily);// 字体
			run.setBold(bold);// 加粗
			run.setFontSize(fontSize);// 字号
			run.setText(eng);// 标题内容
			run.setItalic(italic);// 斜体（字体倾斜）
			line = StringUtils.substring(line, indexOfFirstChinese);
			writeWithStyle(paragraph, line, bold, italic);
		} else {
			String chinese = StringUtils.substring(line, indexOfFirstChinese, indexOfFirstNotChinese);
			if (StringUtils.isEmpty(chinese)) {
				return;
			}
//			System.out.println("中文：" + chinese);
			XWPFRun run = paragraph.createRun();
			run.setFontFamily(fontFamilyChinese);// 字体
			run.setBold(bold);// 加粗
			run.setFontSize(fontSize);// 字号
			run.setText(chinese);// 标题内容
			line = StringUtils.substring(line, indexOfFirstNotChinese);
			writeWithStyle(paragraph, line, bold, italic);
		}

//		for (int i = 0; i < line.length(); i++) {
//			String charAt = String.valueOf(line.charAt(i));
//			if (isChinese(charAt)) {// 中文：宋体
//				XWPFRun run = paragraph.createRun();
//				run.setFontFamily(fontFamilyChinese);// 字体
//				run.setBold(bold);// 加粗
//				run.setFontSize(fontSize);// 字号
//				run.setText(charAt);// 标题内容
//			} else {// 其他：Calibri
//				XWPFRun run = paragraph.createRun();
//				run.setFontFamily(fontFamily);// 字体
//				run.setBold(bold);// 加粗
//				run.setFontSize(fontSize);// 字号
//				run.setText(charAt);// 标题内容
//				run.setItalic(italic);// 斜体（字体倾斜）
//			}
//
//		}

	}

	public boolean isChinese(String text) {
		String regEx = "[\\u4e00-\\u9fa5]+";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(text);
		if (m.find())
			return true;
		else
			return false;
	}

	/**
	 * 
	 * @Description 向word文件中写内容
	 * @param taxtreeId
	 * @param doc
	 * @param citationStyle
	 * @author ZXY
	 */
	private void writeToWord(Taxtree taxtree, XWPFDocument doc, int citationStyle,
			Map<String, ProvinceVO> provinceEntityMap, Map<String, Integer> provinceMap) {
		String taxtreeId = taxtree.getId();
		// 查询整棵树的所有孩子节点
		List<TaxonHasTaxtree> childrenNode = taxtreeService.findAllChildrenByTxtree(taxtreeId);

		logger.info(taxtree.getTreename() + "  的孩子节点有：" + childrenNode.size());
		for (TaxonHasTaxtree child : childrenNode) {
			Taxon taxon = taxonRepository.findOneById(child.getTaxonId());
			String rankId = taxon.getRank().getId();
			switch (rankId) {
			case "5":// 科
				writeFamily(taxon, doc, citationStyle);
				break;
			case "6":// 属
				writeGenus(taxon, doc, citationStyle);
				break;
			case "7":// 种
				writeSpeciesOrSubp(taxon, doc, citationStyle, provinceEntityMap, provinceMap);
				break;
			case "42":// 亚种
				writeSpeciesOrSubp(taxon, doc, citationStyle, provinceEntityMap, provinceMap);
				break;
			default:
				throw new ValidationException("未定义的rankId = " + rankId);
			}
//			System.out.println(taxon.getScientificname() + "	" + taxon.getChname());
		}

	}

	/**
	 * 
	 * @Description 根据接受名引证命名信息更新taxon命名信息
	 * @author ZXY
	 */
	private void updateTaxonAuthor() {
		List<Object[]> list = taxonRepository.findTaxonBydsAndRank(datasetId_2019bird,
				String.valueOf(RankEnum.species.getIndex()));
		int i = 0;
		List<Taxon> updatelist = new LinkedList<>();
		for (Object[] objects : list) {
			String taxonid = objects[0].toString();

			// 查询某个taxon的接受名引证
			List<Citation> acceptCitationlist = citationRepository.findAllByTaxonIdAndNametype(taxonid,
					NametypeEnum.acceptedName.getIndex());
			// 第1步：补充接受名引证
			if (acceptCitationlist.size() > 0) {
				Citation citation = acceptCitationlist.get(0);
				String authorship = citation.getAuthorship();
				if (StringUtils.isNotEmpty(authorship)) {
					Taxon taxon = new Taxon();
					taxon.setId(taxonid);
					taxon.setAuthorstr(authorship);
					updatelist.add(taxon);
					try {

						toolService.printEntity(taxon);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}
		logger.info("更新命名信息数量：" + updatelist.size());
		batchInsertService.batchUpdateTaxonAuthorstrById(updatelist);

	}

	/**
	 * 
	 * @Description 年份前面的第一个标点符号转换成英文逗号 ,
	 * @param content
	 * @return
	 * @author ZXY
	 */
	private String reppacePointToComma(String content) {
		String pattern = "[0-9]{4}";
		Pattern compile = Pattern.compile(pattern);
		Matcher matcher = compile.matcher(content);
		if (matcher.find()) {
			String matcherline = matcher.group();// 提取匹配到的结果
			String butyear = content.substring(0, content.indexOf(matcherline)).trim();
			String lastChar = butyear.substring(butyear.length() - 1);
			Pattern punctuation = Pattern.compile("\\pP");
			boolean find = punctuation.matcher(lastChar).find();
			if (find) {
				content = butyear.substring(0, butyear.length() - 1) + ", " + matcherline;
			}
		}
		return content;
	}

	@Override
	public void addCommonName() {
		List<Commonname> insertCommname = new LinkedList<>();
		// 从bioide2019鸟类名录中查询所有taxon
		List<Taxon> taxonbyDataset = taxonRepository.findByDataset(DataConsts.Dataset_Id_Bird2019);
		for (Taxon taxon : taxonbyDataset) {
			// 从bioide2018鸟类名录中查询某taxon的俗名
			Taxon taxon2018 = taxonRepository.findByTaxasetAndScientificname(DataConsts.Taxaset_Id_Bird2018,
					taxon.getScientificname());
			if (taxon2018 != null && StringUtils.isNotEmpty(taxon2018.getId())) {
				// 将2019俗名放到一个String数组中， 将2019俗名放到一个Map中
				List<Commonname> commname2019 = commonnameRepository.findCommonnameListByTaxonId(taxon.getId());
				List<String> nameArray2019 = new ArrayList<>();

				for (int i = 0; i < commname2019.size(); i++) {
					nameArray2019.add(commname2019.get(i).getCommonname());
				}

				List<Commonname> commname2018 = commonnameRepository.findCommonnameListByTaxonId(taxon2018.getId());
				// 遍历2018俗名，如果2019俗名中没有,则new一个commname实体insert到数据库
				for (Commonname record2018 : commname2018) {
					String commname = record2018.getCommonname().trim();
					if (!nameArray2019.contains(commname)) {
						// new一个commname实体insert到数据库
						Commonname clone = (Commonname) record2018.clone();
						clone.setId(UUIDUtils.getUUID32());
						clone.setTaxon(taxon);
						insertCommname.add(clone);

					}
				}
			}
		}
		logger.info("需要补充的commname个数：" + insertCommname.size());

		// 保存数据库
		try {
			batchSubmitService.saveAll(insertCommname);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void perfectAcceptCitationStr() {
		String path = "E:\\003采集系统\\0012鸟类名录\\补充接受名引证20190422.xlsx";
		List<ExcelUntilF> list = toolService.readExcel(path, ExcelUntilF.class);
		for (ExcelUntilF row : list) {
			String sciname = row.getColD();//接受名
			String author = row.getColE();//命名信息
			String citationstr = row.getColF();//完整引证
			Citation citation = citationRepository.findByDsAndNameTypeAndSciname(DataConsts.Dataset_Id_Bird2019,NametypeEnum.acceptedName.getIndex(),sciname);
			citation.setAuthorship(author);
			citation.setCitationstr(citationstr);
			resetCitationFromCitationstr(citation);
			String id = citation.getTaxon().getId();
			Taxon taxon = taxonRepository.findOneById(id);
			taxon.setAuthorstr(author);//更新taxon的命名信息
			if (insertOrUpdateDB) {
				citationRepository.save(citation);
				taxonRepository.save(taxon);
			}
		}
		//
		
	}
}
