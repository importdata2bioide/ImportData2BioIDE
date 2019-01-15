package org.big.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.big.common.CommUtils;
import org.big.common.FilesUtils;
import org.big.common.HttpUtils;
import org.big.entityVO.ExcelWithColNumVO;
import org.big.entityVO.NationalListOfProtectedAnimalsVO;
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

//	@Autowired
//	private TaxonRepository taxonRepository;
//	@Autowired
//	private CommonnameRepository commonnameRepository;
//	@Autowired
//	private DescriptionRepository descriptionRepository;
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
	public void test1(HttpServletResponse res) {
		String url = "http://www.zoology.csdb.cn/WebServices/taxonNameParser";
		String data = "name=Anisodus tanguticus (Maxim.) Pascher var. viridulus";
		String response = HttpUtils.doGet(url,data);
		System.out.println("响应："+response);
//		testAsync();
		
//		NationalListOfProtectedAnimals(res);

//		distributiondata.geojson新换旧
//		String oldChar = "A9B74666A075495893FEF53C1D6268B9";
//		String newChar = "BB77C40FCBB94212BF71C622CE1B74D7";
//		int i = 0;
//		logger.info("开始...：distributiondata.geojson(替换吉林)");
//		List<Distributiondata> distributionlist = distributiondataRepository.findDistrByLikeGeojson(oldChar);
//		int size = distributionlist.size();
//		logger.info("查询完成："+size);
//		for (Distributiondata distributiondata : distributionlist) {
//			String geojson = distributiondata.getGeojson();
//			JSONObject jsonObject = CommUtils.strToJSONObject(geojson);
//			String value = String.valueOf(jsonObject.get("geoIds"));
//			if(CommUtils.isStrNotEmpty(value) && value.contains(oldChar)) {
//				i++;
//				jsonObject.put("geoIds", value.replace(oldChar, newChar));
//				distributiondata.setGeojson(String.valueOf(jsonObject));
//				distributiondataRepository.save(distributiondata);
//				if(i%10 == 0) {
//					logger.info("i = "+i+" ,总计："+size+",保存进度"+i*100/size+"%");
//				}
//			}
//		}
//		logger.info("i = "+i+" ,总计："+size+",保存进度"+i*100/size+"%");
//		logger.info("OK, i = "+i+", 查询总数："+distributionlist.size());
//		

//		taxon 根据Scientificname更新Epithet
//		int i = 0;
//		List<Taxon> taxonlist = null;
//		try {
//			taxonlist = taxonRepository.findByTaxaset("");
//			
//			for (Taxon taxon : taxonlist) {
//				if(taxon.getRank().getId().equals(String.valueOf(RankEnum.species.getIndex()))) {
//					String[] strs = taxon.getScientificname().split(" ");
//					if(strs.length == 2) {
//						i++;
//						taxon.setEpithet(strs[1]);
//						taxonRepository.save(taxon);
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return e.getMessage();
//		}
//		return "OK,更新数量："+i+",总数："+taxonlist.size();
//		return "OK";
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

}
