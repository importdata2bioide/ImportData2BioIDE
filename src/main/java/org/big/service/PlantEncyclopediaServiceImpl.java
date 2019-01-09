package org.big.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.validation.ValidationException;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.big.common.CommUtils;
import org.big.common.EntityInit;
import org.big.common.ExcelUtil;
import org.big.entity.Citation;
import org.big.entity.Commonname;
import org.big.entity.Datasource;
import org.big.entity.Descriptiontype;
import org.big.entity.Rank;
import org.big.entity.Ref;
import org.big.entity.Taxon;
import org.big.entityVO.BaseParamsForm;
import org.big.entityVO.LanguageEnum;
import org.big.entityVO.NametypeEnum;
import org.big.entityVO.PlantEncyclopediaExcelVO;
import org.big.entityVO.RankEnum;
import org.big.entityVO.RefTypeEnum;
import org.big.repository.TaxasetRepository;
import org.big.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;

@Service
public class PlantEncyclopediaServiceImpl implements PlantEncyclopediaService {
	private final static Logger logger = LoggerFactory.getLogger(PlantEncyclopediaServiceImpl.class);
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TaxasetRepository taxasetRepository;
	@Autowired
	private DatasourceService datasourceService;
	@Autowired
	private ExpertService expertService;
	@Autowired
	private TaxonService taxonService;
	@Autowired
	private ToolService toolService;
	@Autowired
	private RefService refService;
	@Autowired
	private CitationService citationService;
	@Autowired
	private CommonnameService commonnameService;
	@Autowired
	private DescriptiontypeService descriptiontypeService;
	@Autowired
	private DescriptionService descriptionService;

	private final String JsonExpertName = "expert";// 专家信息
	private final String ClassificationConcept = "ClassificationConcept";// 分类概念依据
	private final String relativeExcelPath = "path";
	private final String refRemark = "金效华植物百科";

	@Override
	public String insertPlantEncyclopedia(BaseParamsForm baseParamsForm) throws Exception {
		// validate 必填验证
		validate(baseParamsForm);
		// get 获取所有文件
		List<String> allFiles = CommUtils.getAllFiles(baseParamsForm.getFilePath(), null);
		// group 拆分为多个数组
		List<List<String>> groupFiles = CommUtils.groupList(allFiles, 1000);
		logger.info("groupFiles.size():" + groupFiles.size() + ",allFiles.size()" + allFiles.size());
		Map<String, String> map = new HashedMap<>();
		for (List<String> partFiles : groupFiles) {
			CommUtils.executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						readAnExcel(baseParamsForm, partFiles, map);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});
		}

		while (true) {
			if (CommUtils.executor.getActiveCount() == 0) {
				logger.info("read  [植物百科]  excel，finish...");
				break;
			}
		}

		return "OK";

	}

	/**
	 * 
	 * title: PlantEncyclopediaServiceImpl.java
	 * 
	 * @param baseParamsForm
	 * @param partFiles
	 * @param map
	 * @throws Exception
	 * @author ZXY
	 */
	public void readAnExcel(BaseParamsForm baseParamsForm, List<String> partFiles, Map<String, String> map)
			throws Exception {
		String[] notReadSheetNames = { "分布数据", "物种名录（sp2000）" };
		List<String> notReadSheetNamesAsList = Arrays.asList(notReadSheetNames);
		for (String path : partFiles) {
			// read an excel(xls or xlsx,Multiple sheet), convert to entities
			Map<String, List<PlantEncyclopediaExcelVO>> excelMap = null;
			try {
				if (path.contains("~$") || path.contains("desktop.ini")) {
					// 临时文件，目的是为了防止文档信息丢失
					// 意外断电，也会造成那些文档不自动消失，也会像正常文件一样始终保存在电脑中
					// 此类文件不处理
				} else if (path.endsWith(".xlsx")) {
					XSSFWorkbook xlsxWorkBook = ExcelUtil.getXlsxWorkBook(path);
					excelMap = handleXSSFWorkbook(xlsxWorkBook, path, baseParamsForm, map, notReadSheetNamesAsList);
				} else if (path.endsWith(".xls")) {
					HSSFWorkbook xlsWorkBook = ExcelUtil.getXlsWorkBook(path);
					excelMap = handleXSSFWorkbook(xlsWorkBook, path, baseParamsForm, map, notReadSheetNamesAsList);
				} else {
					logger.info("error 00001 无法识别的文件" + path);
				}
			} catch (Exception e) {
				logger.info("error 00002 无法识别的文件" + path);
			}
			// handle an excel（include multiple sheets）
			if (excelMap != null) {
				insertExcel(excelMap, path, baseParamsForm);
			}
			break;// test run
		}

	}

	/**
	 * 
	 * title: PlantEncyclopediaServiceImpl.java
	 * 
	 * @param excelMap
	 * @author ZXY
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	private void insertExcel(Map<String, List<PlantEncyclopediaExcelVO>> excelMap, String path, BaseParamsForm params)
			throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		Iterator<Entry<String, List<PlantEncyclopediaExcelVO>>> entries = excelMap.entrySet().iterator();
		Taxon taxon = new Taxon();
		EntityInit.initTaxon(taxon, params);// 初始化taxon属性值
		while (entries.hasNext()) {
			Entry<String, List<PlantEncyclopediaExcelVO>> entry = entries.next();
			String sheetName = entry.getKey();
			List<PlantEncyclopediaExcelVO> sheetValues = entry.getValue();
			if (sheetName.contains("百科")) {// 百科sheet
				for (PlantEncyclopediaExcelVO row : sheetValues) {
					boolean entityAttrNull = toolService.EntityAttrNull(row);// 判断是否所有属性值均为空
					//如果为空行，跳过
					if (entityAttrNull) {
						continue;
					}
					toolService.reflectChangeValue(row, "*", "");// 修改值的等于*的属性值为空
					//以colA为标题录入数据
					if (StringUtils.isNoneEmpty(row.getColA())) {
						insertByColA(row, taxon, path, params);
					}else if (StringUtils.isNoneEmpty(row.getColB())) {//以colB为标题录入数据
						insertByColB(row, taxon, path, params);
					}
				}
				if (params.isInsert()) {
					//update,读取整个excel完成后，更新实体信息
					taxonService.saveOne(taxon);
				}
			} else if (sheetName.contains("性状") || "一般被子植物".equals(sheetName)) {

			} else {
				logger.info("error , 未知sheet,sheetName=" + sheetName + ",path=" + path);
			}
		}
		// excel文件所有内存处理完毕后，更新一次taxon
		if (params.isInsert()) {
			// save or update
			taxonService.saveOne(taxon);
		}

	}

	private void insertByColB(PlantEncyclopediaExcelVO row, Taxon taxon, String path, BaseParamsForm params) {
		//B列是描述类型，D列是描述原文
		String colB = row.getColB().trim();
		String colD = row.getColD();
		if(StringUtils.isEmpty(colB)|| StringUtils.isEmpty(colD)) {
			return;
		}
		Descriptiontype descriptiontype = descriptiontypeService.findOneByName(colB);
		if(descriptiontype == null) {
			logger.info("数据库 [描述类型] 表中没有："+colB);
			return ;
		}
		if (params.isInsert()) {
			// save
			descriptionService.insertDescription(descriptiontype,colD,taxon,params);
		}
		

	}

	private void insertByColA(PlantEncyclopediaExcelVO row, Taxon taxon, String path, BaseParamsForm params) {
		String colA = row.getColA();
		String colD = row.getColD();
		// colA
		if ("物种学名".equals(colA)) {
			handleSciNameRow(row, taxon, path, params);
		} else if ("物种中文名".equals(colA)) {
			if (CommUtils.isStrNotEmpty(colD)) {
				taxon.setChname(colD.trim());
			}
		} else if ("分类概念依据".equals(colA)) {
			if (CommUtils.isStrNotEmpty(colD)) {
				taxon.setRemark(turnJsonRemark(ClassificationConcept, colD, taxon.getRemark()));
			}
			if (params.isInsert()) {
				// save or update
				taxonService.saveOne(taxon);
			}
		} else if ("引证信息".equals(colA)) {
			if (CommUtils.isStrNotEmpty(colD)) {
				Citation citation = new Citation();
				EntityInit.initCitation(citation, params);
				citation.setSciname(taxon.getScientificname());
				citation.setAuthorship(taxon.getAuthorstr());
				citation.setTaxon(taxon);
				citation.setNametype(NametypeEnum.acceptedName.getIndex());
				citation.setCitationstr(colD);
				citation.setRefjson(turnTaxonRefToCitation(taxon.getRefjson()));
				citation.setExpert(params.getmExpert());
				citation.setSourcesid(taxon.getSourcesid());
				if (params.isInsert()) {
					citationService.save(citation);
				}
			}

		} else if ("俗名信息".equals(colA)) {
			if (CommUtils.isStrNotEmpty(colD)) {
				colD = toolService.replaceAllChar(colD, ",，", "、");
				String[] commNames = colD.split("、");
				for (String oneName : commNames) {
					Commonname commonname = new Commonname();
					commonname.setCommonname(oneName);
					EntityInit.initCommonname(commonname, params);
					commonname.setTaxon(taxon);
					commonname.setExpert(params.getmExpert());
					commonname.setLanguage(String.valueOf(LanguageEnum.chinese.getIndex()));
					commonname.setRefjson(taxon.getRefjson());
					commonname.setSourcesid(taxon.getSourcesid());
					if (params.isInsert()) {
						commonnameService.saveOne(commonname);
					}
				}
			}
		} else if ("文献信息".equals(colA) && StringUtils.isBlank(taxon.getSourcesid())) {
			// 参考文献和数据源
			handleRefAndDataSource(row, taxon, path, params);
		} else if ("专家信息".equals(colA) && StringUtils.isNotBlank(colD)) {
			// 参考文献和数据源
			taxon.setRemark(turnJsonRemark(JsonExpertName, colD, taxon.getRemark()));
		} else {
			if (CommUtils.isStrNotEmpty(colA)) {
				System.out.println("colA: "+colA);
			}
		}

	}

	private void handleRefAndDataSource(PlantEncyclopediaExcelVO row, Taxon taxon, String path, BaseParamsForm params) {
		String col = null;
		String colE = row.getColE();
		String colD = row.getColD();
		if (CommUtils.isStrNotEmpty(colE)) {
			col = colE;
		} else if (CommUtils.isStrNotEmpty(colD)) {
			col = colD;
		}
		// colE 数据源/参考文献
		if (CommUtils.isStrNotEmpty(col)) {
			// 数据源
			Datasource datasource = datasourceService.insertDSIfNotExist(col, params.getmLoginUser(),
					params.getmLoginUser());
			taxon.setSourcesid(datasource.getId());

			// 参考文献
			Ref ref = refService.insertRefIfNotExist(col, params.getmLoginUser(), refRemark);
			taxon.setRefjson(turnRefToJson(ref));
		}

	}

	private String turnTaxonRefToCitation(String refjson) {
		if (CommUtils.isStrEmpty(refjson)) {
			return null;
		}
		JSONArray array = JSONArray.parseArray(refjson);
		for (int i = 0; i < array.size(); i++) {
			JSONObject jsonObject = array.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
			jsonObject.put("refType", String.valueOf(RefTypeEnum.other.getIndex()));
		}
		return array.toString();
	}

	private void handleSciNameRow(PlantEncyclopediaExcelVO row, Taxon taxon, String path, BaseParamsForm params) {
		String colD = row.getColD().trim();// 学名和命名人
		String colE = row.getColE();// 来源
		String colF = row.getColF();// 审核专家，可能为空
		colD = colD.replace(" ", " ");
		// 分类等级
		Rank rank = new Rank();
		String excelName = StringUtils.substring(path, path.lastIndexOf("\\") + 1);
		if (excelName.contains("变种")) {
			taxon.setRankid(String.valueOf(RankEnum.var.getIndex()));
		} else {
			taxon.setRankid(String.valueOf(RankEnum.species.getIndex()));
		}
		rank.setId(taxon.getRankid());
		taxon.setRank(rank);
		// colD 按照空格拆分
		if (CommUtils.isStartWithEnglish(colD)) {
			int spaceCount = toolService.countTargetStr(colD, " ");
			if (spaceCount >= 2) {
				String sciName = colD.substring(0, colD.indexOf(" ", colD.indexOf(" ") + 1));
				taxon.setScientificname(sciName.trim());// 学名
				taxon.setAuthorstr(CommUtils.cutByStrAfter(colD, sciName).trim());// 命名人
				taxon.setEpithet(CommUtils.cutByStrAfter(sciName, " "));// 种加词
			} else if (spaceCount == 1) {
				taxon.setScientificname(colD);
				taxon.setEpithet(CommUtils.cutByStrAfter(colD, " "));// 种加词
			} else {
				logger.info("error , 无法根据空格截取sciname,colD = " + colD + ",path=" + path);
			}
		} else {
			logger.info("error , E00001, D列不是物种学名..." + path);
		}
		// colE 数据源/参考文献
		if (CommUtils.isStrNotEmpty(colE)) {
			handleRefAndDataSource(row, taxon, path, params);
		}
		// 审核专家
		if (CommUtils.isStrNotEmpty(colF)) {
			taxon.setRemark(turnJsonRemark(JsonExpertName, colF, taxon.getRemark()));
			// 此审核专家放入remark中
		}
		// 统一使用前台传过来的审核专家
		taxon.setExpert(params.getmExpert());
		// 路径
		taxon.setRemark(
				turnJsonRemark(relativeExcelPath, CommUtils.cutByStrAfter(path, "汇交专项-植物专题"), taxon.getRemark()));
	}

	private String turnJsonRemark(String name, String value, String oldRemark) {
		JSONObject jsonObject = null;
		if (CommUtils.isStrEmpty(oldRemark)) {
			jsonObject = new JSONObject();
		} else {
			jsonObject = CommUtils.strToJSONObject(oldRemark);
		}
		jsonObject.put(name, value);
		return String.valueOf(jsonObject);
	}

	private String turnRefToJson(Ref ref) {
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("refE", " 0");
		jsonObject.put("refS", " 0");
		jsonObject.put("refId", ref.getId());
		jsonArray.add(jsonObject);
		return jsonArray.toJSONString();
	}

	/**
	 * xlsx title: PlantEncyclopediaServiceImpl.java Overloading
	 * 
	 * @param xlsWorkBook
	 * @param path
	 * @param baseParamsForm
	 * @author ZXY
	 * @throws Exception
	 * @throws FileNotFoundException
	 */
	private Map<String, List<PlantEncyclopediaExcelVO>> handleXSSFWorkbook(HSSFWorkbook xlsWorkBook, String path,
			BaseParamsForm baseParamsForm, Map<String, String> map, List<String> notReadSheetNames)
			throws FileNotFoundException, Exception {
		Map<String, List<PlantEncyclopediaExcelVO>> excelMap = new HashMap<>();
		// 循环工作表
		for (int numSheet = 0; numSheet < xlsWorkBook.getNumberOfSheets(); numSheet++) {
			HSSFSheet sheet = xlsWorkBook.getSheetAt(numSheet);
			if (sheet == null) {
				continue;
			}
			String currentSheetName = sheet.getSheetName().trim();
			handleSheet(currentSheetName, notReadSheetNames, path, numSheet, map, excelMap);
		}
		return excelMap;

	}

	/**
	 * xls title: PlantEncyclopediaServiceImpl.java Overloading
	 * 
	 * @param xlsxWorkBook
	 * @param path
	 * @param baseParamsForm
	 * @author ZXY
	 * @throws Exception
	 * @throws FileNotFoundException
	 */
	private Map<String, List<PlantEncyclopediaExcelVO>> handleXSSFWorkbook(XSSFWorkbook xlsxWorkBook, String path,
			BaseParamsForm baseParamsForm, Map<String, String> map, List<String> notReadSheetNames)
			throws FileNotFoundException, Exception {
		Map<String, List<PlantEncyclopediaExcelVO>> excelMap = new HashMap<>();
		// 循环工作表
		for (int numSheet = 0; numSheet < xlsxWorkBook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = xlsxWorkBook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}
			String currentSheetName = xssfSheet.getSheetName().trim();
			handleSheet(currentSheetName, notReadSheetNames, path, numSheet, map, excelMap);

		}
		return excelMap;

	}

	/**
	 * 
	 * title: PlantEncyclopediaServiceImpl.java 处理一个sheet
	 * 
	 * @param currentSheetName
	 * @param notReadSheetNames
	 * @param path
	 * @param numSheet
	 * @param map
	 * @throws FileNotFoundException
	 * @throws Exception
	 * @author ZXY
	 */
	private void handleSheet(String currentSheetName, List<String> notReadSheetNames, String path, int numSheet,
			Map<String, String> map, Map<String, List<PlantEncyclopediaExcelVO>> excelMap)
			throws FileNotFoundException, Exception {
		ImportParams params = new ImportParams();
		// notReadSheetNames中的数据跳过，不读取
		if (notReadSheetNames.contains(currentSheetName)) {
			return;
		}
//		synchronized (map) {
//			map.put(currentSheetName, path);
//		}
		// 表头行数,默认1
		params.setHeadRows(0);
		// 表格标题行数,默认0
		params.setTitleRows(0);
		// 第几个sheet页
		params.setStartSheetIndex(numSheet);
		ExcelImportResult<PlantEncyclopediaExcelVO> result = ExcelImportUtil.importExcelMore(new FileInputStream(path),
				PlantEncyclopediaExcelVO.class, params);
		List<PlantEncyclopediaExcelVO> list = result.getList();
		excelMap.put(currentSheetName, list);
	}

	/**
	 * 
	 * title: PlantEncyclopediaServiceImpl.java
	 * 
	 * @param baseParamsForm
	 * @author ZXY
	 */
	private void validate(BaseParamsForm baseParamsForm) {
		JSONObject jsonObject = new JSONObject();
		if (CommUtils.isStrEmpty(baseParamsForm.getmLoginUser())) {
			jsonObject.put("录入人id", "不能为空");
		} else {
			if (userRepository.findOneById(baseParamsForm.getmLoginUser()) == null) {
				jsonObject.put("录入人[id = " + baseParamsForm.getmLoginUser() + "]", "不存在");
			}
		}
		// 审核人
		if (CommUtils.isStrEmpty(baseParamsForm.getmExpert())) {
			jsonObject.put("审核人id", "不能为空");
		} else {
			if (expertService.findOneById(baseParamsForm.getmExpert()) == null) {
				jsonObject.put("审核人[id = " + baseParamsForm.getmExpert() + "]", "不存在");
			}
		}

		if (CommUtils.isStrEmpty(baseParamsForm.getmInputtimeStr())) {
			baseParamsForm.setmInputtimeStr(CommUtils.getCurrentDate());
		}

		if (CommUtils.isStrEmpty(baseParamsForm.getmTaxasetId())) {
			jsonObject.put("分类单元集id", "不能为空");
		} else {
			if (taxasetRepository.findOneById(baseParamsForm.getmTaxasetId()) == null) {
				jsonObject.put("分类单元集" + "[id = " + baseParamsForm.getmTaxasetId() + "]", "不存在");
			}
		}

		if (CommUtils.isStrEmpty(baseParamsForm.getFilePath())) {
			jsonObject.put("文件路径", "不能为空");
		} else if (!new File(baseParamsForm.getFilePath()).exists()) {
			jsonObject.put(baseParamsForm.getFilePath(), "目标文件不存在");
		}

		if (jsonObject.size() > 0) {
			throw new ValidationException(jsonObject.toJSONString());
		}
	}

}
