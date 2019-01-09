package org.big.controller;



import org.big.repository.DistributiondataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


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
	@Autowired
	private DistributiondataRepository distributiondataRepository;
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

	@ResponseBody
	@RequestMapping(value = "/testController_test1")
	public String test1() {
		logger.info("test1");
		distributiondataRepository.findAll();
		distributiondataRepository.findOneById("12");
		
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
		return "OK";
	}

}
