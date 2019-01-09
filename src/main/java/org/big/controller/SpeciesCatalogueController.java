package org.big.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.big.common.CommUtils;
import org.big.entity.Rank;
import org.big.entity.Taxon;
import org.big.entityVO.BaseParamsForm;
import org.big.entityVO.LineStatus;
import org.big.entityVO.SpeciesCatalogueEnum;
import org.big.service.SpeciesCatalogueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

/**
 * yd 杨定 名录导入数据库
 * 
 * @author BIGIOZ
 *
 */

@Controller
public class SpeciesCatalogueController {
	private final static Logger logger = LoggerFactory.getLogger(SpeciesCatalogueController.class);
	@Autowired
	private SpeciesCatalogueService speciesCatalogueService;
	
//	public static void main(String[] args) {
//		List<String> txt = CommUtils.readTxt("E:\\采集系统\\杨定-名录-导入\\双翅目长角亚目名录.txt", "Unicode");
//		SpeciesCatalogueController s = new SpeciesCatalogueController();
//		Map<String, List<String>> map = s.spliteBySperFamily(txt);
//        //第二种 
//        Set<Map.Entry<String, List<String>>> entryseSet=map.entrySet(); 
//        for (Map.Entry<String,  List<String>> entry:entryseSet) {
//            System.out.println("第二种："+entry.getKey()+":"+entry.getValue().size());
//        }
//        System.out.println("执行完了");
//		
//	}
	
	/**
	 * 读取并解析txt文件，根据params参数判断是否保存到数据库
	 * title: SpeciesCatalogueController.java
	 * @param params
	 * @return
	 * @throws Exception
	 * @author ZXY
	 */
	@ResponseBody
	@RequestMapping(value = "/guest/SpeciesCatalogueController_importTxt", method = RequestMethod.POST)
	public String ImportAll(BaseParamsForm params) throws Exception {
		//线程池核心线程数:5;线程池最大数:10;空闲线程存活时间:2;时间单位:MINUTES;线程池所使用的缓冲队列
		ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 2, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(5));
		try {
			// 1.read 读取txt文件
			List<String> txt = CommUtils.readTxt(params.getTxtUrl(), params.getCode());
			//拆分为多个数组
			Map<String,List<String>> map = spliteBySuperFamily(txt);
			// 2.handleLine时，是否保存到数据库 true/false
			System.out.println("是否保存到数据库( true/false): "+params.isInsert());
			//3.judge 判断该行内容的属性,遇到错误在控制台输出信息
			Set<Map.Entry<String, List<String>>> entryseSet=map.entrySet(); 
			for (Map.Entry<String,  List<String>> entry:entryseSet) {
				executor.execute(new Runnable() {
					@Override
					public void run() {
						System.out.println("线程池中线程数目："+executor.getPoolSize()+"，队列中等待执行的任务数目："+ executor.getQueue().size()+"，已执行完的任务数目："+executor.getCompletedTaskCount());
						try {
							handleLine(entry.getValue(), params);
						} catch (Exception e) {
							System.out.println("error IA001 ："+e.getMessage());
							e.printStackTrace();
						}
					}
				});
			}
			//关闭线程池,不能再提交新任务，等待执行的任务不受影响  
			executor.shutdown();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return e.getMessage();
		}
		
		while (true) {  
            if (executor.isTerminated()) {  
            	logger.info("/guest/SpeciesCatalogueController_importTxt 执行完了");
                break;  
            }  
        }  
		return "finish";

	}
	/**
	 * splite 根据总科拆分为多个数组
	 * title: SpeciesCatalogueController.java
	 * @param txt
	 * @return
	 * @author ZXY
	 */
	private Map<String, List<String>> spliteBySuperFamily(List<String> txt) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		List<String> list = null;
		for (String line : txt) {
			SpeciesCatalogueEnum isWhat = judgeIsWhat(line, 0);
			if(isWhat == SpeciesCatalogueEnum.superfamily) {
				list = new ArrayList<>();
				map.put(line, list);
			}
			list.add(line);
		} 
		return map;
	}
	/**
	 * 根据分类单元集创建分类树，遇到错误不保存并在控制台输出错误信息。
	 * title: SpeciesCatalogueController.java
	 * @param params
	 * @return
	 * @throws Exception
	 * @author ZXY
	 */
	@ResponseBody
	@RequestMapping(value = "/guest/SpeciesCatalogueController_insertTree", method = RequestMethod.POST)
	public String insertTree(BaseParamsForm params) throws Exception {
		try {
			speciesCatalogueService.insertTreeByDataSet(params);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return e.getMessage();
		}
		return "finish";
	}

	/**
	 * 
	 * @param txt
	 * @throws Exception
	 */
	private void handleLine(List<String> txt, BaseParamsForm params) throws Exception {
		// temporary 构建临时状态
		LineStatus thisLineStatus = new LineStatus();
		// 存储入数据库
		SpeciesCatalogueEnum preTaxon = null;
		for (String line : txt) {
			SpeciesCatalogueEnum isWhat = judgeIsWhat(line, 0);
			switch (isWhat) {
			case superfamily://总科
				preTaxon = SpeciesCatalogueEnum.superfamily;
				thisLineStatus = new LineStatus();
				String chname = CommUtils.cutByStrBeforeInclude(line, "总科");
				String sciname = CommUtils.cutByStrAfter(line, "总科");
				Taxon t = new Taxon();
				t.setScientificname(sciname);
				t.setChname(chname);
				t.setRank(new Rank("40"));
				JSONObject js = new JSONObject();
				js.put("parentId", "");
				js.put("parentSciName", "");
				js.put("originalText", line);
				t.setRemark(String.valueOf(js));
				Taxon taxon = speciesCatalogueService.savefamilyTaxon(t, params);
				thisLineStatus.setSuperfamily(taxon);
				// 清空下级
				thisLineStatus.setFamily(null);
				thisLineStatus.setSubfamily(null);
				thisLineStatus.setTribe(null);
				thisLineStatus.setSubtribe(null);
				thisLineStatus.setGenus(null);
				thisLineStatus.setSubgenus(null);
				thisLineStatus.setSpecies(null);
				thisLineStatus.setSubspecies(null);
//				logger.info("superfamily----" + line + " chname = " + chname + " sciname = " + sciname);
				break;
			case family://科
				preTaxon = SpeciesCatalogueEnum.family;
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("parentId", thisLineStatus.getSuperfamily().getId());
				jsonObject.put("parentSciName", thisLineStatus.getSuperfamily().getScientificname());
				jsonObject.put("originalText", line);
				String familychname = CommUtils.cutByStrBeforeInclude(line, "科");
				String familysciname = CommUtils.cutByStrAfter(line, "科");
				Taxon familyTaxon = new Taxon();
				familyTaxon.setScientificname(familysciname);
				familyTaxon.setChname(familychname);
				familyTaxon.setRemark(String.valueOf(jsonObject));
				familyTaxon.setRank(new Rank("5"));
				Taxon family_taxon = speciesCatalogueService.savefamilyTaxon(familyTaxon, params);
				thisLineStatus.setFamily(family_taxon);
				// 清空下级
				thisLineStatus.setSubfamily(null);
				thisLineStatus.setTribe(null);
				thisLineStatus.setSubtribe(null);
				thisLineStatus.setGenus(null);
				thisLineStatus.setSubgenus(null);
				thisLineStatus.setSpecies(null);
				thisLineStatus.setSubspecies(null);
//				logger.info(
//						"family----" + line + "       chname = " + familychname + "       sciname = " + familysciname);
				break;
			case subfamily://亚科
				preTaxon = SpeciesCatalogueEnum.subfamily;
				JSONObject subfamily_jsonObject = new JSONObject();
				subfamily_jsonObject.put("parentId", thisLineStatus.getFamily().getId());
				subfamily_jsonObject.put("parentSciName", thisLineStatus.getFamily().getScientificname());
				subfamily_jsonObject.put("originalText", line);
				String subfamilychname = CommUtils.cutByStrBeforeInclude(line, "亚科").trim();
				String scinameAndAuthor = CommUtils.cutByStrAfter(line, "亚科").trim();
				String author = "";
				String subfamily_sciname = "";
				if (scinameAndAuthor.contains(",")) {
					subfamily_sciname = scinameAndAuthor.substring(0, scinameAndAuthor.indexOf(" "));
					author = scinameAndAuthor.substring(scinameAndAuthor.indexOf(" "));
				} else {
					subfamily_sciname = scinameAndAuthor;
				}
				Taxon subfamilyTaxon = new Taxon();
				subfamilyTaxon.setRank(new Rank("11"));
				subfamilyTaxon.setScientificname(subfamily_sciname);
				subfamilyTaxon.setChname(subfamilychname);
				subfamilyTaxon.setRemark(String.valueOf(subfamily_jsonObject));
				subfamilyTaxon.setAuthorstr(author);
				Taxon subfamily_taxon = speciesCatalogueService.savefamilyTaxon(subfamilyTaxon, params);
				thisLineStatus.setSubfamily(subfamily_taxon);
				// 清空下级
				thisLineStatus.setTribe(null);
				thisLineStatus.setSubtribe(null);
				thisLineStatus.setGenus(null);
				thisLineStatus.setSubgenus(null);
				thisLineStatus.setSpecies(null);
				thisLineStatus.setSubspecies(null);
//				logger.info(
//						"subfamily----" + line + "||" + subfamilychname + "||" + subfamily_sciname + "||" + author);
				break;

			case tribe:
				preTaxon = SpeciesCatalogueEnum.tribe;
				JSONObject tribe_jsonObject = new JSONObject();
				String tribe_parentId = null;
				String tribe_parentSciName = "";
				if (thisLineStatus.getSubfamily() != null
						&& CommUtils.isStrNotEmpty(thisLineStatus.getSubfamily().getId())) {// 亚科
					tribe_parentId = thisLineStatus.getSubfamily().getId();
					tribe_parentSciName = thisLineStatus.getSubfamily().getScientificname();
				} else if (thisLineStatus.getFamily() != null
						&& CommUtils.isStrNotEmpty(thisLineStatus.getFamily().getId())) {// 科
					tribe_parentId = thisLineStatus.getFamily().getId();
					tribe_parentSciName = thisLineStatus.getFamily().getScientificname();
				}
				tribe_jsonObject.put("parentSciName", tribe_parentSciName);
				tribe_jsonObject.put("parentId", tribe_parentId);
				tribe_jsonObject.put("originalText", line);
				String tribe_chname = CommUtils.cutByStrBeforeInclude(line, "族").trim();
				String tribe_sciname = CommUtils.cutByStrAfter(line, "族").trim().trim();
				Taxon tribeTaxon = new Taxon();
				tribeTaxon.setRank(new Rank("32"));
				tribeTaxon.setScientificname(tribe_sciname);
				tribeTaxon.setChname(tribe_chname);
				tribeTaxon.setRemark(String.valueOf(tribe_jsonObject));
				Taxon tribe_taxon = speciesCatalogueService.savefamilyTaxon(tribeTaxon, params);
				thisLineStatus.setTribe(tribe_taxon);
				// 清空下级
				thisLineStatus.setSubtribe(null);
				thisLineStatus.setGenus(null);
				thisLineStatus.setSubgenus(null);
				thisLineStatus.setSpecies(null);
				thisLineStatus.setSubspecies(null);
//				logger.info("族----" + line);
				break;
			case subtribe:
				preTaxon = SpeciesCatalogueEnum.subtribe;
				JSONObject subtribe_jsonObject = new JSONObject();
				subtribe_jsonObject.put("originalText", line);
				String subtribe_parentId = null;
				String subtribe_parentSciName = null;
				if (thisLineStatus.getTribe() != null && CommUtils.isStrNotEmpty(thisLineStatus.getTribe().getId())) {// 亚科
					subtribe_parentId = thisLineStatus.getTribe().getId();
					subtribe_parentSciName = thisLineStatus.getTribe().getScientificname();
				}
				subtribe_jsonObject.put("parentId", subtribe_parentId);
				subtribe_jsonObject.put("parentSciName", subtribe_parentSciName);
				String subtribe_chname = CommUtils.cutByStrBeforeInclude(line, "亚族").trim();
				String subtribe_sciname = CommUtils.cutByStrAfter(line, "亚族").trim().trim();
				Taxon subtribeTaxon = new Taxon();
				subtribeTaxon.setScientificname(subtribe_sciname);
				subtribeTaxon.setChname(subtribe_chname);
				subtribeTaxon.setRemark(String.valueOf(subtribe_jsonObject));
				subtribeTaxon.setRank(new Rank("43"));
				thisLineStatus.setSubtribe(speciesCatalogueService.savefamilyTaxon(subtribeTaxon, params));
				// 清空下级
				thisLineStatus.setGenus(null);
				thisLineStatus.setSubgenus(null);
				thisLineStatus.setSpecies(null);
				thisLineStatus.setSubspecies(null);
				break;
			case genus:
				preTaxon = SpeciesCatalogueEnum.genus;
				JSONObject genus_jsonObject = new JSONObject();
				genus_jsonObject.put("originalText", line);
				String parentId = "";
				String parentSciName = "";
				if (thisLineStatus.getSubtribe() != null
						&& CommUtils.isStrNotEmpty(thisLineStatus.getSubtribe().getId())) {// 亚族
					parentId = thisLineStatus.getSubtribe().getId();
					parentSciName = thisLineStatus.getSubtribe().getScientificname();
				} else if (thisLineStatus.getTribe() != null
						&& CommUtils.isStrNotEmpty(thisLineStatus.getTribe().getId())) {// 族
					parentId = thisLineStatus.getTribe().getId();
					parentSciName = thisLineStatus.getTribe().getScientificname();
				} else if (thisLineStatus.getSubfamily() != null
						&& CommUtils.isStrNotEmpty(thisLineStatus.getSubfamily().getId())) {// 亚科
					parentId = thisLineStatus.getSubfamily().getId();
					parentSciName = thisLineStatus.getSubfamily().getScientificname();
				} else if (thisLineStatus.getFamily() != null
						&& CommUtils.isStrNotEmpty(thisLineStatus.getFamily().getId())) {// 科
					parentId = thisLineStatus.getFamily().getId();
					parentSciName = thisLineStatus.getFamily().getScientificname();
				}
				genus_jsonObject.put("parentSciName", parentSciName);
				genus_jsonObject.put("parentId", parentId);
				String SeqAndgenuschname = CommUtils.cutByStrBeforeInclude(line, "属").trim();// 序号+中文
				String genuschname = SeqAndgenuschname.substring(SeqAndgenuschname.indexOf(".") + 1).trim();// 去除序号
				String genusscinameAndAuthor = CommUtils.cutByStrAfter(line, "属").trim();// 拉丁名+命名信息
				String genusauthor = "";
				String genussciname = "";
				if (genusscinameAndAuthor.indexOf(" ") != -1) {// 第一个空格的位置
					genussciname = genusscinameAndAuthor.substring(0, genusscinameAndAuthor.indexOf(" "));// 第一个空格之前的内容
					genusauthor = genusscinameAndAuthor.substring(genusscinameAndAuthor.indexOf(" "));// 第一个空格后的内容
				} else {
					genussciname = genusscinameAndAuthor;// 没有命名信息
				}
				Taxon genusTaxon = new Taxon();
				genusTaxon.setScientificname(genussciname);
				genusTaxon.setChname(genuschname);
				genusTaxon.setRemark(String.valueOf(genus_jsonObject));
				genusTaxon.setAuthorstr(genusauthor);
				genusTaxon.setRank(new Rank("6"));
				thisLineStatus.setGenus(speciesCatalogueService.savefamilyTaxon(genusTaxon, params));
				// 清空下级
				thisLineStatus.setSubgenus(null);
				thisLineStatus.setSpecies(null);
				thisLineStatus.setSubspecies(null);
//				logger.info("genus----" + line+"||"+genuschname+"||"+genussciname+"||"+genusauthor);
				break;
			case subgenus:
				preTaxon = SpeciesCatalogueEnum.subgenus;
				JSONObject subgenus_jsonObject = new JSONObject();
				subgenus_jsonObject.put("parentSciName", thisLineStatus.getGenus().getScientificname());
				subgenus_jsonObject.put("parentId", thisLineStatus.getGenus().getId());
				subgenus_jsonObject.put("originalText", line);
				line = CommUtils.cutByStrAfter(line, ")").trim();
				String subgenus_chname = CommUtils.cutByStrBeforeInclude(line, "亚属").trim();
				String subgenus_scinameAndAuthor = CommUtils.cutByStrAfter(line, "亚属").trim().trim();
				String subgenus_sciname = "";
				String subgenus_author = "";
				// 学名和命名信息
				if (subgenus_scinameAndAuthor.indexOf(")") != -1) {// 包含“)”
					subgenus_sciname = CommUtils.cutByStrBeforeInclude(subgenus_scinameAndAuthor, ")").trim();
					subgenus_author = CommUtils.cutByStrAfter(subgenus_scinameAndAuthor, ")").trim();
				} else if (subgenus_scinameAndAuthor.indexOf(" ") != -1) {// 包含空格
					subgenus_sciname = CommUtils.cutByStrBeforeInclude(subgenus_scinameAndAuthor, " ").trim();
					subgenus_author = CommUtils.cutByStrAfter(subgenus_scinameAndAuthor, " ").trim();
				} else {
					subgenus_sciname = subgenus_scinameAndAuthor;// 没有命名信息
				}

				Taxon subgenusTaxon = new Taxon();
				subgenusTaxon.setScientificname(subgenus_sciname);
				subgenusTaxon.setChname(subgenus_chname);
				subgenusTaxon.setRemark(String.valueOf(subgenus_jsonObject));
				subgenusTaxon.setAuthorstr(subgenus_author);
				subgenusTaxon.setRank(new Rank("12"));
				thisLineStatus.setSubgenus(speciesCatalogueService.savefamilyTaxon(subgenusTaxon, params));
				// 清空下级
				thisLineStatus.setSpecies(null);
				thisLineStatus.setSubspecies(null);
//				logger.info("subgenus----" + line+"||"+subgenus_chname+"||"+subgenus_sciname+"||"+subgenus_author);
				break;
			case species:
				preTaxon = SpeciesCatalogueEnum.species;
				JSONObject species_jsonObject = new JSONObject();
				species_jsonObject.put("originalText", line);
				line = CommUtils.cutByStrAfter(line, ")").trim();// 去除序号
				String species_chname = CommUtils.cutChinese(line);// 中文名
				line = line.substring(species_chname.length(), line.length()).trim();// line 剔除中文名
				String species_sciname = "";
				String species_author = "";
				if (line.endsWith(")") && line.contains("(")) { // 以)结尾，且包含)-->()中间为命名信息，其余为拉丁名
					int lastIndexOf = line.lastIndexOf("(");// 最后一个 ( 的位置
					species_sciname = line.substring(0, lastIndexOf);
					species_author = line.substring(lastIndexOf);

				} else {
					species_sciname = line.substring(0, line.indexOf(" ", line.indexOf(" ") + 1));
					if (species_sciname.endsWith(")")) {
						species_sciname = line
								.substring(0, line.indexOf(" ", line.indexOf(" ", line.indexOf(" ") + 1) + 1)).trim();

					}
					species_author = line.substring(species_sciname.length(), line.length()).trim();// 命名人和命名年代
				}

				species_author = species_author.replace("(", "");
				species_author = species_author.replace(")", "");
				String species_parentId = "";
				String species_parentSciName = null;

				if (thisLineStatus.getSubgenus() != null
						&& CommUtils.isStrNotEmpty(thisLineStatus.getSubgenus().getId())) {
					species_parentId = thisLineStatus.getSubgenus().getId();
					species_parentSciName = thisLineStatus.getSubgenus().getScientificname();
				} else if (thisLineStatus.getGenus() != null
						&& CommUtils.isStrNotEmpty(thisLineStatus.getGenus().getId())) {
					species_parentId = thisLineStatus.getGenus().getId();
					species_parentSciName = thisLineStatus.getGenus().getScientificname();
				}

				species_jsonObject.put("parentId", species_parentId);
				species_jsonObject.put("parentSciName", species_parentSciName);

				Taxon speciesTaxon = new Taxon();
				speciesTaxon.setScientificname(species_sciname);
				speciesTaxon.setChname(species_chname);
				speciesTaxon.setRemark(String.valueOf(species_jsonObject));
				speciesTaxon.setAuthorstr(species_author);
				speciesTaxon.setRank(new Rank("7"));
				thisLineStatus.setSpecies(speciesCatalogueService.savefamilyTaxon(speciesTaxon, params));
				// 清空下级
				thisLineStatus.setSubspecies(null);
//				logger.info("种----" + line+"||"+species_chname+"||"+species_sciname+"||"+species_author);
				break;
			case subspecies:
//				logger.info("subspecies"+line);
				preTaxon = SpeciesCatalogueEnum.subspecies;
				JSONObject subspecies_jsonObject = new JSONObject();
				subspecies_jsonObject.put("originalText", line);
				try {
					subspecies_jsonObject.put("parentId", thisLineStatus.getSpecies().getId());
					subspecies_jsonObject.put("parentSciName", thisLineStatus.getSpecies().getScientificname());
				} catch (Exception e1) {
					logger.info("error 没有找到上一级种 species，line = " + line);
				}
				line = CommUtils.cutByStrAfter(line, ")").trim();
				String subspecies_chname = CommUtils.cutChinese(line);// 中文名
				line = line.substring(subspecies_chname.length(), line.length()).trim();// line 剔除中文名
				String subspecies_author = "";
				String subspecies_sciname = "";
				try {
					subspecies_author = line.substring(line.lastIndexOf("("));
					subspecies_sciname = line.substring(0, line.lastIndexOf("(")).trim();
				} catch (Exception e) {
					logger.info("line=" + line + "||" + "命名信息出错||" + e.getMessage());
				}

				Taxon subspeciesTaxon = new Taxon();
				subspeciesTaxon.setScientificname(subspecies_sciname);
				subspeciesTaxon.setChname(subspecies_chname);
				subspeciesTaxon.setRemark(String.valueOf(subspecies_jsonObject));
				subspeciesTaxon.setAuthorstr(subspecies_author);
				subspeciesTaxon.setRank(new Rank("42"));
				thisLineStatus.setSubspecies(speciesCatalogueService.savefamilyTaxon(subspeciesTaxon, params));
//				logger.info(subspecies_chname+"||"+subspecies_author+"||"+subspecies_sciname);
				break;
			case citation:
				speciesCatalogueService.handleCitation(thisLineStatus, preTaxon, line, params);
				break;
			case distributiondata:
				speciesCatalogueService.handleDistribution(thisLineStatus, preTaxon, line, params);
				break;
			default:
				logger.info("(unknown line):" + line);
			}
		}
	}

	private SpeciesCatalogueEnum judgeIsWhat(String line, int rowNum) {
		SpeciesCatalogueEnum result = SpeciesCatalogueEnum.unknown;
		int i = 0;
		String errorMessage = "";
		// superfamily 总科
		if (line.contains("总科")) {
			i = i + 1;
			result = SpeciesCatalogueEnum.superfamily;
			errorMessage = errorMessage + "||" + SpeciesCatalogueEnum.superfamily.getName();
		}
		// family 科
		if (line.contains("科") && !line.contains("亚科") && !line.contains("总科") && !line.contains("(")
				&& !line.startsWith("分布") && !CommUtils.isStartWithNum(line)) {
			i = i + 1;
			result = SpeciesCatalogueEnum.family;
			errorMessage = errorMessage + "||" + SpeciesCatalogueEnum.family.getName();
		}
		// 亚科
		if (line.contains("亚科") && !line.contains("总")) {
			i = i + 1;
			result = SpeciesCatalogueEnum.subfamily;
			errorMessage = errorMessage + "||" + SpeciesCatalogueEnum.subfamily.getName();
		}
		// 族
		if (line.contains("族") && !line.contains("亚族") && !line.startsWith("(")) {
			i = i + 1;
			result = SpeciesCatalogueEnum.tribe;
			errorMessage = errorMessage + "||" + SpeciesCatalogueEnum.tribe.getName();
		}
		// 亚族
		if (line.contains("亚族")) {
			i = i + 1;
			result = SpeciesCatalogueEnum.subtribe;
			errorMessage = errorMessage + "||" + SpeciesCatalogueEnum.subtribe.getName();
		}
		// genus 属(有属字，并且数字开头)
		if (line.contains("属") && CommUtils.isStartWithNum(line) && !line.contains("亚属")) {
			i = i + 1;
			result = SpeciesCatalogueEnum.genus;
			errorMessage = errorMessage + "||" + SpeciesCatalogueEnum.genus.getName();
		}
		// 亚属
		if (line.contains("亚属")) {
			i = i + 1;
			result = SpeciesCatalogueEnum.subgenus;
			errorMessage = errorMessage + "||" + SpeciesCatalogueEnum.subgenus.getName();
		}
		// species 种 (符合以"(数字)"开头的格式)
		if (CommUtils.isStartWithSeq(line) && !line.contains("亚种")) {
			i = i + 1;
			result = SpeciesCatalogueEnum.species;
			errorMessage = errorMessage + "||" + SpeciesCatalogueEnum.species.getName();
		}
		// 亚种
		if (line.contains("亚种")) {
			i = i + 1;
			result = SpeciesCatalogueEnum.subspecies;
			errorMessage = errorMessage + "||" + SpeciesCatalogueEnum.subspecies.getName();
		}
		// 引证 citation
		if (CommUtils.isStartWithEnglish(line) && line.endsWith(".")) {
			i = i + 1;
			result = SpeciesCatalogueEnum.citation;
			errorMessage = errorMessage + "||" + SpeciesCatalogueEnum.citation.getName();
			if (line.length() < 37) {
				result = SpeciesCatalogueEnum.unknown;
				logger.info(rowNum + " || citation 长度不够 = " + line + "||" + line.length());
			}
		}
		// 分布 distributiondata
		if (line.startsWith("分布")) {
			i = i + 1;
			result = SpeciesCatalogueEnum.distributiondata;
			errorMessage = errorMessage + "||" + SpeciesCatalogueEnum.distributiondata.getName();
		}

		if (i > 1) {
			result = SpeciesCatalogueEnum.unknown;
			logger.info("(error 000M Mutil definded)errorMessage:" + errorMessage + "\t||\tline = " + line);
		}
		// 引证
		if (result == SpeciesCatalogueEnum.unknown) {
			if (CommUtils.isStartWithEnglish(line)) {
				result = SpeciesCatalogueEnum.citation;
			} else {
				logger.info("unknown line = " + line);
			}
		}

		return result;

	}

}
