package org.big.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.big.common.CommUtils;
import org.big.common.FilesUtils;
import org.big.common.UUIDUtils;
import org.big.entity.Citation;
import org.big.entity.Commonname;
import org.big.entity.Description;
import org.big.entity.Geoobject;
import org.big.entity.Rank;
import org.big.entity.Taxaset;
import org.big.entity.Taxon;
import org.big.entityVO.ExcelUutilH;
import org.big.entityVO.ExcelUntilD;
import org.big.entityVO.FileTypeEnum;
import org.big.entityVO.LanguageEnum;
import org.big.entityVO.LineAttreEnum;
import org.big.entityVO.NationalListOfProtectedAnimalsVO;
import org.big.repository.CitationRepository;
import org.big.repository.CommonnameRepository;
import org.big.repository.DescriptionRepository;
import org.big.repository.GeoobjectRepository;
import org.big.repository.RefRepository;
import org.big.repository.TaxonHasTaxtreeRepository;
import org.big.repository.TaxonRepository;
import org.big.repository.TeamRepository;
import org.big.service.BatchInsertService;
import org.big.service.BatchSubmitService;
import org.big.service.ToolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;

@Controller
@RequestMapping(value = "guest")
public class TestController {
	private static ThreadLocal<Integer> local = new ThreadLocal<>();
	private int count = 100;
	private final static Logger logger = LoggerFactory.getLogger(TestController.class);

	@Autowired
	private GeoobjectRepository geoobjectRepository;
	@Autowired
	private TaxonRepository taxonRepository;
	@Autowired
	private CommonnameRepository commonnameRepository;
	@Autowired
	private BatchInsertService batchInsertService;
	@Autowired
	private DescriptionRepository descriptionRepository;
//	@Autowired
//	private DistributiondataRepository distributiondataRepository;
//	@Autowired
//	private MultimediaRepository multimediaRepository;
//	@Autowired
//	private TaxonHasTaxtreeRepository taxonHasTaxtreeRepository;
//	@Autowired
//	private TaxasetRepository taxasetRepository;
//	@Autowired
//	private DatasetRepository datasetRepository;
	@Autowired
	private TeamRepository teamRepository;
	@Autowired
	private CitationRepository citationRepository;
//	@Autowired
//	private KeyitemService keyitemService;
//	@Autowired
//	private SpecimendataRepository specimendataRepository;
//	@Autowired
//	private TaxtreeService taxtreeService;
	@Autowired
	private ToolService toolService;
//	@Autowired
//	private EntityManager entityManager;
//	
//	@Autowired
//	private JpaQueryMethod jpaQueryMethod;
	@Autowired
	private RefRepository refRepository;
	
	@Autowired
	private BatchSubmitService batchSubmitService;
	@Autowired
	private TaxonHasTaxtreeRepository taxonHasTaxtreeRepository;
	
	@RequestMapping(value = "/testController_test1")
	public void test1(HttpServletResponse response) throws Exception {
		updateBlank();
	}
	
	private void updateBlank() {
		List<Object[]> list = citationRepository.findAllIdAndSciname();
		List<Object[]> updatelist = new ArrayList<>();
		int i = 0;
		for (Object[] objs : list) {
			String id = objs[0].toString();
			String sciname = objs[1].toString();
			if(sciname.startsWith(" ")||sciname.endsWith(" ")) {
				i++;
				objs[1] = sciname.trim();
				updatelist.add(objs);
			}
		}
	}

	private void updateCitationSciName() {
		List<Citation> list = citationRepository.findByScinameEndingWith(",");
		for (Citation citation : list) {
			String id = citation.getId();
			String sciname = citation.getSciname();
			if(sciname.endsWith(",")) {
				String newSciname = sciname.substring(0, sciname.length()-1);
				citation.setSciname(newSciname.trim());
				System.out.println(sciname+"||"+newSciname);
//				citationRepository.save(citation);
			}
		}
	}

	@SuppressWarnings("unused")
	private void updateCitation() {
		List<Citation> list = citationRepository.findByAuthorshipContaining("var.");
		System.out.println(list.size());
		for (Citation citation : list) {
			String id = citation.getId();
			String sciname = citation.getSciname();
			String authorship = citation.getAuthorship();
			String delVar = CommUtils.cutByStrAfter(authorship, "var.").trim();
			String trueAuthor = delVar.substring(delVar.indexOf(" ")+1).trim();
			String partSciName = CommUtils.cutByStrBefore(authorship, trueAuthor);
			String trueSciName = sciname.trim()+" "+partSciName.trim();
			System.out.println(sciname+"||"+authorship+"||"+trueAuthor+"||"+trueSciName);
			citation.setAuthorship(trueAuthor.trim());
			citation.setSciname(trueSciName.trim());
		}
		batchInsertService.batchUpdateNameAndAuthorById(list);
	
	}

	private void buchongBird2019() throws Exception {
		String txtPath = "E:\\003采集系统\\0002导入鸟类\\20190320-1补充.txt";
		String fileCode = "UTF-8";
		//按行读取txt文件
		List<String> readTxt = CommUtils.readTxt(txtPath, fileCode);
		for (String line : readTxt) {
			LineAttreEnum lineAttr = isWhat(line);
		}
		
	}

	private LineAttreEnum isWhat(String line) {
		LineAttreEnum attr = null;
		int i =0;
		System.out.println(line);
		line = line.trim();
		//是否为中文名，（以中文开头，以英文结尾）
		if(isChinese(getChartASC(line,1)) && isEnglish(line.substring(line.length()-1, -1))) {
			attr = LineAttreEnum.chname;
			i++;
		}
		//是否为亚种
		if(isSubSpecies(line)) {
			i++;
			attr = LineAttreEnum.subsp;
		}
		//是否为学名（以英文开头，以英文结尾）
		if(isEnglish(getChartASC(line,1)) && isEnglish(line.substring(line.length()-1, -1))) {
			attr = LineAttreEnum.species;
			i++;
		}
		
		
		return null;
	}
	/**
	 * 
	 * @Description 是否为亚种
	 * @param text
	 * @param lastLineType
	 * @return
	 * @author ZXY
	 */
    public static boolean isSubSpecies(String text){
        if (text.substring(0,1).equals("—")) {//—开头
            return true;
        }
        else{
            return false;
        }
    }
    
    /**
     * 
     * @Description 判断是否为英文
     * @param text
     * @return
     * @author ZXY
     */
    public static boolean isEnglish(String text){
        return text.matches("^[a-zA-Z]*");
    }

    /**
     * 
     * @Description 判断是否为中文
     * @param text
     * @return
     * @author ZXY
     */
    public static boolean isChinese(String text){
        String regEx = "[\\u4e00-\\u9fa5]+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(text);
        if(m.find())
            return true;
        else
            return false;
    }

    /**
     * 
     * @Description 判断是否为数字
     * @param text
     * @return
     * @author ZXY
     */
    public static boolean isNumeric(String text){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(text);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
    
    /**
     * 
     * @Description 截取字符串的前几位
     * @param text
     * @param num
     * @return
     * @author ZXY
     */
    public  String getChartASC(String text,int num){
        String result = "";
        if (StringUtils.isNotEmpty(text)) {
            result = text.substring(0,num);
        }
        return result;
    }

	private void CorrectingIncorrectDataForSubspecies() {
		
//		String taxtreeId = "";
//		String taxonId = "418cfc0356ca4da9bc5d5a1f07eb8cff";
//		Taxon yazhong = taxonRepository.findOneById(taxonId);
//		String remark = yazhong.getRemark();
//		String originalText = CommUtils.strToJSONObject(remark).get("originalText").toString();
//		//录入一个种
//		
//		//构建属和种的父级关系
//		//修改亚种的父级关系
//		
//		
//		TaxonHasTaxtree hasTaxtree = taxonHasTaxtreeRepository.findTaxonHasTaxtreesByTaxonIdAndTaxtreeId(taxon.getId(), taxtreeId);
		
		
	}

	private void insertTest() {
		List<Taxon> list = taxonRepository.findByTaxaset("d9154ee24e89424c853f822363731cf4");
		int i = 0;
		for (Taxon taxon : list) {
			i++;
			if(taxon.getScientificname().contains(" ")||(StringUtils.isNotEmpty(taxon.getEpithet())&&taxon.getEpithet().contains(" "))) {
				taxon.setScientificname(taxon.getScientificname().trim());
				if(StringUtils.isNotEmpty(taxon.getEpithet())) {
					taxon.setEpithet(taxon.getEpithet().trim());
				}
				taxonRepository.save(taxon);
			}
			if(i%100==0) {
				System.out.println(i+"--"+list.size());
			}
		}
		
	}

	private void excelModel() {
		String path = "E:\\003采集系统\\0011菌物百科\\菌物百科2018";
		List<String> files = CommUtils.getAllFiles(path, null);
		Map<FileTypeEnum, String> map = converToEnum(files);
		Map<FileTypeEnum, String> expertMap = new HashMap<>();
		Map<FileTypeEnum, String> dataSourceMap = new HashMap<>();
		Map<FileTypeEnum, String> refMap = new HashMap<>();
		readExpert(expertMap,map.get(FileTypeEnum.Expert));
		String dataSourceExcelPath = map.get(FileTypeEnum.DataSource);
		String refExcelPath = map.get(FileTypeEnum.Ref);
		
		

	}
	/**
	 * 
	 * @Description 读取专家excel
	 * @param expertMap 返回结果 key:序号;value:id
	 * @param string 文件路径
	 * @author ZXY
	 */
	private void readExpert(Map<FileTypeEnum, String> expertMap, String string) {
		
		
	}

	private Map<FileTypeEnum, String> converToEnum(List<String> files) {
		Map<FileTypeEnum, String> map = new HashMap<>();
		for (String file : files) {
			if (file.contains("专家")) {
				map.put(FileTypeEnum.Expert, file);
			} else if (file.contains("数据源")) {
				map.put(FileTypeEnum.DataSource, file);
			} else if (file.contains("参考文献")) {
				map.put(FileTypeEnum.Ref, file);
			} else if (file.contains("分类单元")) {
				map.put(FileTypeEnum.Taxon, file);
			} else if (file.contains("引证")) {
				map.put(FileTypeEnum.Citation, file);
			} else if (file.contains("描述")) {
				map.put(FileTypeEnum.Description, file);
			} else if (file.contains("俗名")) {
				map.put(FileTypeEnum.Commname, file);
			} else if (file.contains("分布")) {
				map.put(FileTypeEnum.Distribution, file);
			} else if (file.contains("特征")) {
				map.put(FileTypeEnum.Features, file);
			} else if (file.contains("保护")) {
				map.put(FileTypeEnum.Protect, file);
			} else if (file.contains("多媒体")) {
				map.put(FileTypeEnum.MultiMedia, file);
			} else {
				System.out.println("I don't know what's this:" + file);
			}
		}
		return map;
	}

	private void testBatchInsert() {
		long startTime = System.currentTimeMillis();
		int size = 1000;
		try {
			List<Taxon> list = new ArrayList<>(size  + 10);
			for (int k = 0; k < size; k++) {
				Taxon taxon = new Taxon();
				taxon.setId(UUIDUtils.getUUID32());
				Rank rank = new Rank();
				rank.setId("8");
				taxon.setRank(rank);
				taxon.setRankid(7);
				Taxaset taxaset = new Taxaset();
				taxaset.setId("05313d7bbeb6417b8733cac3f74a830d");
				taxon.setTaxaset(taxaset);
				taxon.setRemark("20190318测试");
				list.add(taxon);
			}
			batchSubmitService.saveAll(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(size + "  运行时间: " + (System.currentTimeMillis() - startTime) / 60000 + "min, "
				+ (System.currentTimeMillis() - startTime) / 1000 + "s ( " + (System.currentTimeMillis() - startTime)
				+ "ms)");

	}

	private void commname() {

	}

	private void pinyin() {
		List<Geoobject> list = geoobjectRepository.findByGeogroupId("A1A25AA0D98C4441997D891A229F35E8");
		System.out.println(list.size());
		for (Geoobject geoobject : list) {
			String engeoname = geoobject.getEngeoname();
			String cngeoname = geoobject.getCngeoname();
			String remark = geoobject.getRemark();
			JSONObject jsonObject = CommUtils.strToJSONObject(remark);
			String name = (String) jsonObject.get("国家或地区（ISO英文用名）");
			geoobject.setEngeoname(name);
			geoobject.setAdcode("000000");
			geoobject.setPid("0");
			geoobjectRepository.save(geoobject);

		}

//		List<Word> words = WordSegmenter.segWithStopWords("《速度与激情7》的中国内地票房自4月12日上映以来，在短短两周内突破20亿人民币");
//		System.out.println(words);

	}

	private void guojia() {
		List<ExcelUutilH> list = readExcel("E:\\003采集系统\\国家代码与名称.xlsx");
		for (ExcelUutilH row : list) {
			JSONObject jsonObject = new JSONObject();
			String colA = row.getColA();
			String colB = row.getColB();
			String colC = row.getColC();
			String colD = row.getColD();
			String colE = row.getColE();
			String colF = row.getColF();// 中国惯用名
			String colG = row.getColG();
			String colH = row.getColH();
			jsonObject.put("二位字母", colA);
			jsonObject.put("三位字母", colB);
			jsonObject.put("数字", colC);
			jsonObject.put("ISO 3166-2相应代码", colD);
			jsonObject.put("国家或地区（ISO英文用名）", colE);
			jsonObject.put("台湾惯用名", colG);
			jsonObject.put("香港惯用名", colH);
			Geoobject obj = new Geoobject();
			obj.setId(UUIDUtils.getUUID32());
			obj.setCngeoname(colF);
			obj.setGeogroupId("A1A25AA0D98C4441997D891A229F35E8");// 世界国家与地区
			obj.setRemark(String.valueOf(jsonObject));
			obj.setStatus(1);
			obj.setInputtime(new Date());
			obj.setSynchstatus(0);
			obj.setSynchdate(new Date());
			geoobjectRepository.save(obj);
		}
	}

	private List<ExcelUutilH> readExcel(String path) {
		ImportParams params = new ImportParams();
		params.setTitleRows(0);
		params.setHeadRows(1);
		long start = new Date().getTime();
		List<ExcelUutilH> list = ExcelImportUtil.importExcel(new File(path), ExcelUutilH.class, params);
		System.out.println("读取excel所消耗时间：" + (new Date().getTime() - start));
		System.out.println("数量：" + list.size());
		System.out.println(ReflectionToStringBuilder.toString(list.get(0)));
		System.out.println("读取excel完成");
		return list;
	}

	private void up() {
//		
//		
//		
		List<String> sourcesidlist = new ArrayList<>();
		sourcesidlist.add("1123f4fb4d854e9e93b014a833878c55");
		sourcesidlist.add("5246f4fb4d854e9e93b014a833878c55");
		sourcesidlist.add("9964f4fb4d854e9e93b014a833878c55");
		List<Description> list = descriptionRepository.findByDescontentLikeAndSourcesid("危害：", sourcesidlist);
		for (Description description : list) {
//			String descontent = description.getDescontent();
//			String destitle = description.getDestitle();
//			if(descontent.contains("地理分布")) {
//				String fenbu = CommUtils.cutByStrBefore(descontent, "危害：");
//				String weihai = CommUtils.cutByStrAfterInclude(descontent,  "危害：");
//				System.out.println("-----------"+destitle);
//				System.out.println(fenbu);
//				System.out.println(weihai);
//				description.setDescontent(fenbu);
//				//危害
//				Description d = new Description();
//				d.setId(UUIDUtils.getUUID32());
//				d.setDestitle("危害");
//				d.setDescontent(weihai);
//				d.setDestypeid("22b5f3a816cd471793f82a5044be1bfe");
//				Descriptiontype descriptiontype = new Descriptiontype();
//				descriptiontype.setId("22b5f3a816cd471793f82a5044be1bfe");
//				d.setDescriptiontype(descriptiontype );
//				d.setLicenseid("4");
//				d.setLanguage("1");
//				d.setSourcesid(description.getSourcesid());
//				d.setStatus(1);
//				d.setInputer(description.getInputer());
//				d.setInputtime(new Date());
//				d.setSynchstatus(0);
//				d.setSynchdate(new Date());
//				d.setTaxon(description.getTaxon());
//				d.setExpert(description.getExpert());
//				descriptionRepository.save(d);
//				descriptionRepository.save(description);
//				
//			}
		}
	}

	private void updateRankByChname(boolean execute) {
		if (!execute) {
			return;
		}
		List<Taxon> taxonList = taxonRepository.findByTaxaset("68d7c55db3934b588983a5a197147fff");
		for (Taxon taxon : taxonList) {
			String scientificname = taxon.getScientificname();
			String rankid = String.valueOf(taxon.getRankid());
			String remark = taxon.getRemark();
			if (remark.contains(",")) {
				String chinese = CommUtils.cutChinese(remark);
				String eng = CommUtils.cutByStrAfter(remark, chinese).trim();
				taxon.setScientificname(eng);
				taxon.setAuthorstr(null);
				taxonRepository.save(taxon);
			}

//			if("7".equals(rankid)) {
//				Map<String, String> map = SciNameUtil.getSpecies(scientificname);
//				if(map != null) {
//					taxon.setScientificname(map.get("sciName"));
//					taxon.setAuthorstr(map.get("author"));
//					taxonRepository.save(taxon);
//				}
//				
//			}
//			if(scientificname.contains("subsp.")) {
//				taxon.setRankid("42");
//				Rank r = new Rank();
//				r.setId(taxon.getRankid());
//				taxon.setRank(r);
//				taxonRepository.save(taxon);
//			}

		}

	}

	private void updateTaxonBySciName(boolean execute) {
		logger.info("execute updateTaxonBySciName,");
		if (!execute) {
			return;
		}
		List<Taxon> taxonList = taxonRepository.findByTaxaset("44cbb5dad5044b599c3125cf921274ec");
		for (Taxon taxon : taxonList) {
			String rankid = String.valueOf(taxon.getRankid());
			String scientificname = taxon.getScientificname();
			String chname = taxon.getChname();
			List<Description> descriptionListByTaxonId = descriptionRepository
					.findDescriptionListByTaxonId(taxon.getId());

			if ("7".equals(rankid) && search(scientificname, " ") >= 2 && !chname.contains("中国")) {// 7 7 种 Species
//				System.out.println("1、scientificname值是："+scientificname);
				String nameBy2Blank = scientificname.substring(0,
						scientificname.indexOf(" ", scientificname.indexOf(" ") + 1));// 截取第二个空格之前的字符串
				if (nameBy2Blank.contains("(") || nameBy2Blank.contains(")") || nameBy2Blank.contains("（")
						|| nameBy2Blank.contains("）")) {
					// jfjdf (sdfjfh)
					System.out.println("2、A情况" + nameBy2Blank);
				} else {
					taxon.setScientificname(nameBy2Blank.trim());// 学名
					taxon.setAuthorstr(scientificname.replace(nameBy2Blank, "").trim());// 命名人和年代
					taxon.setEpithet(nameBy2Blank.substring(nameBy2Blank.indexOf(" ") + 1));// 种加词
					System.out.println(nameBy2Blank);
					// 更新数据库
					taxonRepository.save(taxon);
				}
			}
		}

	}

	private void insertCompare(boolean execute) {
		logger.info("execute insertCompare");
		if (!execute) {
			return;
		}
		List<Commonname> records = new ArrayList<>(3050);
		for (int i = 0; i < 10000; i++) {
			Commonname commonname = new Commonname();
			String uuid32 = UUIDUtils.getUUID32();
			commonname.setId(uuid32);
			commonname.setCommonname("测试" + uuid32);
			commonname.setExpert("123");
			commonname.setInputer("4a83e6bb2f2f4b588b2fd12ef22e507f");
			commonname.setLanguage(String.valueOf(LanguageEnum.chinese.getIndex()));
			commonname.setRemark("测试20190130");
			commonname.setSourcesid("f8c51d42-b8bd-4d29-864a-e3ada935c47d");
			Taxon taxon = new Taxon();
			taxon.setId("76f0f697bcdd48c99448c04721005dcc");
			commonname.setTaxon(taxon);
			records.add(commonname);
		}
//		long startTime = System.currentTimeMillis();
//		commonnameRepository.saveAll(records);
//		logger.info("commonnameRepository.saveAll(records)：运行时间: " + (System.currentTimeMillis() - startTime)/60000+"min, "+(System.currentTimeMillis() - startTime)/1000 + "s ( "+(System.currentTimeMillis() - startTime)+"ms)");
//		for (Commonname commonname : records) {
//			commonname.setId(UUIDUtils.getUUID32());
//		}
		long startTime2 = System.currentTimeMillis();
		batchInsertService.batchInsertCommonname(records);
		logger.info(records.size() + "、batchInsertService.batchInsertCommonname(records);：运行时间: "
				+ (System.currentTimeMillis() - startTime2) / 60000 + "min, "
				+ (System.currentTimeMillis() - startTime2) / 1000 + "s ( " + (System.currentTimeMillis() - startTime2)
				+ "ms)");
	}

	private void py(boolean execute) {
		if (!execute) {
			return;
		}
		List<Geoobject> list = geoobjectRepository.findByPid("710000");
		logger.info(list.size() + "");
		for (Geoobject geoobject : list) {
			logger.info(geoobject.getCngeoname());
			geoobject.setEngeoname(CommUtils.ToPinyin(geoobject.getCngeoname()));
			logger.info(geoobject.getEngeoname());
			geoobjectRepository.save(geoobject);
		}

	}

	/**
	 * 
	 * @Description 行政区对比
	 * @author ZXY
	 */
	private void compareArea(HttpServletResponse response) {
		String filePath = "E:\\003采集系统\\行政区.xlsx";
		ImportParams params = new ImportParams();
		params.setTitleRows(1);
		params.setHeadRows(0);
		long start = new Date().getTime();
		List<ExcelUntilD> list = ExcelImportUtil.importExcel(new File(filePath), ExcelUntilD.class, params);
		logger.info("读取耗费时间：" + (new Date().getTime() - start));
		logger.info(filePath + "，读取总行数：" + list.size());
		logger.info(ReflectionToStringBuilder.toString(list.get(0)));

		for (ExcelUntilD row : list) {
			String colB = row.getColB().trim();// code
			String colC = row.getColC().trim();// name
			List<Geoobject> list1 = geoobjectRepository.findByCngeonameAndAdcode(colC, colB);
			if (list1.size() < 1) {// 没找到
				List<Geoobject> list2 = geoobjectRepository.findByCngeoname(colC);
				List<Geoobject> list3 = geoobjectRepository.findByAdcode(colB);
				if (list2.size() == 1 && list3.size() == 0) {// name 查询到了，adcode没有查询到
					row.setColD("name 同，adcode 不同 , 数据库adcode=" + list2.get(0).getAdcode());
				} else if (list3.size() == 1 && list2.size() == 0) {// adcode 查询到了，name 没有查询到
					row.setColD("adcode相同，name不同；数据库中name=" + list3.get(0).getCngeoname());
				} else if (list3.size() == 0 && list2.size() == 0) {// adcode和name 都没有查询到
					row.setColD("数据库中没有此地；name=" + colC);
				} else {
					Geoobject geoobjectByCngeoname = list2.get(0);
					Geoobject geoobjectByAdcode = list3.get(0);
					row.setColD("数据库中 [name =" + geoobjectByCngeoname.getCngeoname() + ",adcode="
							+ geoobjectByCngeoname.getAdcode() + "] [name= " + geoobjectByAdcode.getCngeoname()
							+ ",adcode=" + geoobjectByAdcode.getAdcode() + "]");
					logger.info("error 根据name查询到：" + list2.size() + ",根据adcode 查询到" + list3.size());

				}
			} else if (list1.size() > 1) {
				// 找到多个
				logger.info("数据库中有重复数据：" + colB + "," + colC);
			} else {
				// 找到一个，perfect
			}

		} // end for
			// 导出excel
		try {
			FilesUtils.exportExcel(list, "行政分布", "行政分布", ExcelUntilD.class, URLEncoder.encode("行政分布.xls", "GBK"),
					response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private void testAsync() {
		for (int i = 0; i < 10; i++) {
			toolService.asy(i);
		}

	}

	@SuppressWarnings("unused")
	private void NationalListOfProtectedAnimals(HttpServletResponse response) {
		// 1.读取xls文件
		String filePath = "E:\\003采集系统\\0009国家保护动物名录\\国家保护动物名录 - 自己.xlsx";
		System.out.println("test");
		ImportParams params = new ImportParams();
		params.setTitleRows(1);
		params.setHeadRows(0);
		long start = new Date().getTime();
		List<ExcelUntilD> list = ExcelImportUtil.importExcel(new File(filePath), ExcelUntilD.class, params);
		logger.info("读取耗费时间：" + (new Date().getTime() - start));
		logger.info(filePath + "，读取总行数：" + list.size());
		logger.info(ReflectionToStringBuilder.toString(list.get(0)));
		// 2.循环处理xls文件
		String gangCN = null;
		String gangEN = null;
		String muCN = null;
		String muEN = null;
		String keCN = null;
		String keEN = null;
		List<NationalListOfProtectedAnimalsVO> exportlist = new ArrayList<>();
		for (ExcelUntilD row : list) {
			String colA = row.getColA().trim();
			String colB = row.getColB();
			if (StringUtils.isNotEmpty(colB)) {
				colB = colB.trim();
			}
			if (colA.contains("纲")) {
				muCN = null;
				muEN = null;
				keCN = null;
				keEN = null;
				gangCN = CommUtils.cutChinese(colA).trim();
				gangEN = CommUtils.cutByStrAfter(colA, gangCN).trim();
			} else if (colA.contains("目")) {
				keCN = null;
				keEN = null;
				muCN = colA;
				muEN = row.getColB().trim();
			} else if (colA.contains("科")) {
				keCN = colA;
				keEN = colB;
			} else {
				// 种
				NationalListOfProtectedAnimalsVO entity = new NationalListOfProtectedAnimalsVO();
				entity.setColA(gangCN);
				entity.setColB(gangEN);
				entity.setColC(muCN);
				entity.setColD(muEN);
				entity.setColE(keCN);
				entity.setColF(keEN);
				entity.setColG(colA);
				entity.setColH(colB);
				entity.setColI(StringUtils.isNotEmpty(row.getColC()) ? row.getColC() : row.getColD());
				exportlist.add(entity);
				try {
					toolService.printEntity(entity);
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		// 3.导出excel
		try {
			FilesUtils.exportExcel(exportlist, "国家保护动物名录", "国家保护动物名录", NationalListOfProtectedAnimalsVO.class,
					URLEncoder.encode("国家保护动物名录.xls", "GBK"), response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	public int search(String str, String strRes) {// 查找字符串里与指定字符串相同的个数
		int n = 0;// 计数器
//	      for(int i = 0;i<str.length();i++) {
//	         
//	      }
		while (str.indexOf(strRes) != -1) {
			int i = str.indexOf(strRes);
			n++;
			str = str.substring(i + 1);
		}
		return n;
	}

}
