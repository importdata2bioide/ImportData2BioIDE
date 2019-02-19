package org.big.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.big.common.CommUtils;
import org.big.common.FilesUtils;
import org.big.common.SciNameUtil;
import org.big.common.UUIDUtils;
import org.big.entity.Commonname;
import org.big.entity.Description;
import org.big.entity.Geoobject;
import org.big.entity.Rank;
import org.big.entity.Taxon;
import org.big.entityVO.ExcelWithColNumVO;
import org.big.entityVO.LanguageEnum;
import org.big.entityVO.NationalListOfProtectedAnimalsVO;
import org.big.repository.CommonnameRepository;
import org.big.repository.DescriptionRepository;
import org.big.repository.GeoobjectRepository;
import org.big.repository.TaxonRepository;
import org.big.service.BatchInsertService;
import org.big.service.ToolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;

@Controller
@RequestMapping(value = "guest")
public class TestController {

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
//	@Autowired
//	private TeamRepository teamRepository;
//	@Autowired
//	private CitationRepository citationRepository;
//	@Autowired
//	private KeyitemService keyitemService;
//	@Autowired
//	private SpecimendataRepository specimendataRepository;
//	@Autowired
//	private TaxtreeService taxtreeService;
	@Autowired
	private ToolService toolService;

	@RequestMapping(value = "/testController_test1")
	public void test1(HttpServletResponse response) {
		updateRankByChname(true);
		
		
		updateTaxonBySciName(false);
		insertCompare(false);
		py(false);

	}
	private void updateRankByChname(boolean execute) {
		if(!execute) {
			return;
		}
		List<Taxon> taxonList = taxonRepository.findByTaxaset("68d7c55db3934b588983a5a197147fff");
		for (Taxon taxon : taxonList) {
			String scientificname = taxon.getScientificname();
			String rankid = taxon.getRankid();
			String remark = taxon.getRemark();
			if(remark.contains(",")) {
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
		if(!execute) {
			return;
		}
		List<Taxon> taxonList = taxonRepository.findByTaxaset("44cbb5dad5044b599c3125cf921274ec");
		for (Taxon taxon : taxonList) {
			String rankid = taxon.getRankid();
			String scientificname = taxon.getScientificname();
			String chname = taxon.getChname();
			List<Description> descriptionListByTaxonId = descriptionRepository.findDescriptionListByTaxonId(taxon.getId());

			
			if("7".equals(rankid) && search(scientificname, " ")>=2 && !chname.contains("中国")) {//7	7	种	Species
//				System.out.println("1、scientificname值是："+scientificname);
				String nameBy2Blank = scientificname.substring(0, scientificname.indexOf(" ",scientificname.indexOf(" ")+1 ));//截取第二个空格之前的字符串
				if(nameBy2Blank.contains("(")||nameBy2Blank.contains(")")||nameBy2Blank.contains("（")||nameBy2Blank.contains("）")) {
					//jfjdf (sdfjfh)
					System.out.println("2、A情况"+nameBy2Blank);
				}else {
					taxon.setScientificname(nameBy2Blank.trim());//学名
					taxon.setAuthorstr(scientificname.replace(nameBy2Blank, "").trim());//命名人和年代
					taxon.setEpithet(nameBy2Blank.substring(nameBy2Blank.indexOf(" ")+1));//种加词
					System.out.println(nameBy2Blank);
					//更新数据库
					taxonRepository.save(taxon);
				}
			}
		}
		
	}
	private void insertCompare(boolean execute) {
		logger.info("execute insertCompare");
		if(!execute) {
			return;
		}
		List<Commonname> records = new ArrayList<>(3050);
		for (int i = 0;i<10000;i++) {
			Commonname commonname = new Commonname();
			String uuid32 = UUIDUtils.getUUID32();
			commonname.setId(uuid32);
			commonname.setCommonname("测试"+uuid32);
			commonname.setExpert("123");
			commonname.setInputer("4a83e6bb2f2f4b588b2fd12ef22e507f");
			commonname.setLanguage(String.valueOf(LanguageEnum.chinese.getIndex()));
			commonname.setRemark("测试20190130");
			commonname.setSourcesid("f8c51d42-b8bd-4d29-864a-e3ada935c47d");
			Taxon taxon = new Taxon();
			taxon.setId("76f0f697bcdd48c99448c04721005dcc");
			commonname.setTaxon(taxon );
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
		logger.info(records.size()+"、batchInsertService.batchInsertCommonname(records);：运行时间: " + (System.currentTimeMillis() - startTime2)/60000+"min, "+(System.currentTimeMillis() - startTime2)/1000 + "s ( "+(System.currentTimeMillis() - startTime2)+"ms)");
	}
	
	private void py(boolean execute) {
		if(!execute) {
			return;
		}
		List<Geoobject> list = geoobjectRepository.findByPid("710000");
		logger.info(list.size()+"");
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
		List<ExcelWithColNumVO> list = ExcelImportUtil.importExcel(new File(filePath),
				ExcelWithColNumVO.class, params);
		logger.info("读取耗费时间："+(new Date().getTime() - start));
		logger.info(filePath+"，读取总行数："+list.size());
		logger.info(ReflectionToStringBuilder.toString(list.get(0)));

		for (ExcelWithColNumVO row : list) {
			String colB = row.getColB().trim();//code
			String colC = row.getColC().trim();//name
			List<Geoobject> list1 = geoobjectRepository.findByCngeonameAndAdcode(colC, colB);
			if(list1.size() < 1) {//没找到
				List<Geoobject> list2 = geoobjectRepository.findByCngeoname(colC);
				List<Geoobject> list3 = geoobjectRepository.findByAdcode(colB);
				if(list2.size() == 1 && list3.size() ==0) {//name 查询到了，adcode没有查询到
					row.setColD("name 同，adcode 不同 , 数据库adcode="+list2.get(0).getAdcode());
				}else if(list3.size() == 1 && list2.size() ==0) {//adcode 查询到了，name 没有查询到
					row.setColD("adcode相同，name不同；数据库中name="+list3.get(0).getCngeoname());
				}else if(list3.size() ==0 && list2.size() ==0) {//adcode和name 都没有查询到
					row.setColD("数据库中没有此地；name="+colC);
				}else {
					Geoobject geoobjectByCngeoname = list2.get(0);
					Geoobject geoobjectByAdcode = list3.get(0);
					row.setColD("数据库中 [name ="+geoobjectByCngeoname.getCngeoname()+",adcode="+geoobjectByCngeoname.getAdcode()+"] [name= "+geoobjectByAdcode.getCngeoname()+",adcode="+geoobjectByAdcode.getAdcode()+"]");
					logger.info("error 根据name查询到："+list2.size()+",根据adcode 查询到"+list3.size());
					
				}
			}else if(list1.size() > 1) {
				//找到多个
				logger.info("数据库中有重复数据："+colB+","+colC);
			}else {
				//找到一个，perfect
			}
			
		}//end for 
		//导出excel
		try {
			FilesUtils.exportExcel(list, "行政分布" ,"行政分布", ExcelWithColNumVO.class, URLEncoder.encode("行政分布.xls", "GBK"),
					response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private void testAsync() {
		for(int i = 0;i<10;i++) {
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
		List<ExcelWithColNumVO> list = ExcelImportUtil.importExcel(new File(filePath),
				ExcelWithColNumVO.class, params);
		logger.info("读取耗费时间："+(new Date().getTime() - start));
		logger.info(filePath+"，读取总行数："+list.size());
		logger.info(ReflectionToStringBuilder.toString(list.get(0)));
		//2.循环处理xls文件
		String gangCN = null;
		String gangEN = null;
		String muCN = null;
		String muEN = null;
		String keCN = null;
		String keEN = null;
		List<NationalListOfProtectedAnimalsVO> exportlist = new ArrayList<>();
		for (ExcelWithColNumVO row : list) {
			String colA = row.getColA().trim();
			String colB = row.getColB();
			if(StringUtils.isNotEmpty(colB)) {
				colB = colB.trim();
			}
			if(colA.contains("纲")) {
				muCN = null;
				muEN = null;
				keCN = null;
				keEN = null;
				gangCN = CommUtils.cutChinese(colA).trim();
				gangEN = CommUtils.cutByStrAfter(colA, gangCN).trim();
			}else if(colA.contains("目")) {
				keCN = null;
				keEN = null;
				muCN = colA;
				muEN = row.getColB().trim();
			}else if(colA.contains("科")) {
				keCN = colA;
				keEN = colB;
			}else{
				//种
				NationalListOfProtectedAnimalsVO entity = new NationalListOfProtectedAnimalsVO();
				entity.setColA(gangCN);
				entity.setColB(gangEN);
				entity.setColC(muCN);
				entity.setColD(muEN);
				entity.setColE(keCN);
				entity.setColF(keEN);
				entity.setColG(colA);
				entity.setColH(colB);
				entity.setColI(StringUtils.isNotEmpty(row.getColC())?row.getColC():row.getColD());
				exportlist.add(entity);
				try {
					toolService.printEntity(entity);
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		//3.导出excel
		try {
			FilesUtils.exportExcel(exportlist, "国家保护动物名录" ,"国家保护动物名录", NationalListOfProtectedAnimalsVO.class, URLEncoder.encode("国家保护动物名录.xls", "GBK"),
					response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}
	
	  public int search(String str,String strRes) {//查找字符串里与指定字符串相同的个数
	        int n=0;//计数器
//	      for(int i = 0;i<str.length();i++) {
//	         
//	      }
	        while(str.indexOf(strRes)!=-1) {
	            int i = str.indexOf(strRes);
	            n++;
	            str = str.substring(i+1);
	        }
	        return n;
	    }

}
