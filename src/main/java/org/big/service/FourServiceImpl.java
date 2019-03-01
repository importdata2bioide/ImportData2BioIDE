package org.big.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.dictionary.DictionaryFactory;
import org.apdplat.word.segmentation.PartOfSpeech;
import org.apdplat.word.segmentation.Word;
import org.apdplat.word.tagging.PartOfSpeechTagging;
import org.big.common.CommUtils;
import org.big.common.EntityInit;
import org.big.entity.Description;
import org.big.entity.Distributiondata;
import org.big.entity.Geoobject;
import org.big.entity.Rank;
import org.big.entity.Taxon;
import org.big.entityVO.BaseParamsForm;
import org.big.entityVO.RankEnum;
import org.big.repository.GeoobjectRepository;
import org.big.repository.TaxonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service
public class FourServiceImpl implements FourService {

	private final static Logger logger = LoggerFactory.getLogger(FourServiceImpl.class);

	@Autowired
	private BatchInsertService batchInsertService;
	@Autowired
	private TaxonRepository taxonRepository;
	@Autowired
	private DistributiondataService distributiondataService;
	@Autowired
	private DescriptionService descriptionService;
	@Autowired
	private DescriptiontypeService descriptiontypeService;
	@Autowired
	private MultimediaService multimediaService;
	@Autowired
	private GeoobjectRepository geoobjectRepository;

	@Override
	public void handleTxt(BaseParamsForm baseParamsForm) throws Exception {
		// insert
		insertDesc(baseParamsForm);
		if (true) {
			return;
		}

		// 1、读取txt文件
		String filePath = baseParamsForm.getFilePath();
		String upperPath = StringUtils.substring(filePath, 0, filePath.lastIndexOf(".txt"));
		String catalogPath = upperPath + "目录.txt";
		System.out.println("目录路径：" + catalogPath);
		if (false) {
			parseCatalogAndInsert(true, catalogPath, baseParamsForm);
			return;
		}

		List<String> list = CommUtils.readTxt(filePath, "utf-8");
		String allContent = MergeStrings(list);
		System.out.println("读取数据行数：" + list.size());
		int startIndex = 0;
		int endIndex = 0;
//		List<Taxon> taxonList = taxonRepository.findTaxonByTaxasetId(baseParamsForm.getmTaxasetId());
		List<String> cataloglist = CommUtils.readTxt(catalogPath, "utf-8");
		List<String> reList = new ArrayList<>();
		int k = 0;
		for (String line : cataloglist) {
			if (line.contains("门") || line.contains("纲")) {
				continue;
			}
			k++;
			line = line.substring(0, line.indexOf("	")).trim();// 去除页码
			if (allContent.contains(line)) {

			} else {
				System.out.println("AZ001 ERROR 找不到、" + line);
				continue;
			}
			endIndex = allContent.indexOf(line);
//			System.out.println(line);
//			System.out.println(startIndex + "、" + endIndex);
			if (k != 0) {
				String desc = allContent.substring(startIndex, endIndex);
				reList.add(desc);
			}
			startIndex = allContent.indexOf(line);
		}
		// 可以处理的list
		for (String line : reList) {
			if (org.apache.commons.lang3.StringUtils.isEmpty(line)) {
				continue;
			}
			int indexOfMu = line.indexOf("目");
			int indexOfKe = line.indexOf("科");
			if ((indexOfKe >= 1) && (indexOfKe <= 4) || (indexOfMu >= 1) && (indexOfMu <= 2)) {
				// 粉蚧科Pseudococcidae \蚧科Coccidae 等跳过
				continue;
			}

			line = line.replace(":", "：");
			line = line.replace("分布地区", "地理分布");
			line = line.replace("寄主名称", "寄主");
			int indexOfFLDW = line.indexOf("分类地位：");
			int indexOfDLFB = line.indexOf("地理分布：");
			int indexOfJZ = line.indexOf("寄主：");
			int indexOfWH = line.indexOf("危害：");
			int indexOfIMG = line.indexOf("<img");
			if (indexOfFLDW == -1) {
				System.out.println("找不到分类地位__原文：" + line);
			}
			if (indexOfDLFB == -1) {
				System.out.println("找不到地理分布__原文：" + line);
			}
			if (indexOfJZ == -1) {
				System.out.println("找不到寄主__原文：" + line);
			}
			if (indexOfIMG == -1) {
				System.out.println("找不到图片__原文：" + line);
			}
//			System.out.println("--------------------");
			String name = StringUtils.substring(line, 0, indexOfFLDW);
			String fldw = StringUtils.substring(line, indexOfFLDW, indexOfDLFB);// 分类地位
			String dlfb = StringUtils.substring(line, indexOfDLFB, indexOfJZ);// 地理分布
			String jz = StringUtils.substring(line, indexOfJZ, indexOfIMG);// 寄主
			String wh = StringUtils.substring(line, indexOfWH, indexOfIMG);// 寄主
			String img = StringUtils.substring(line, indexOfIMG);// 图片
//			System.out.println(name);
//			System.out.println(fldw);
//			System.out.println(dlfb);
//			System.out.println(jz);
//			System.out.println(img);
			Taxon taxon = taxonRepository.findByTaxasetAndRemark(baseParamsForm.getmTaxasetId(), name);

			if (taxon == null) {
				taxon = taxonRepository.findByTaxasetAndChname(baseParamsForm.getmTaxasetId(),
						CommUtils.cutChinese(name));
				if (taxon == null) {
					System.out.println("找不到taxon，name = " + name);
					continue;
				}
			}
			// save desc 保存分类地位、地理分布、寄主到描述表
			if (baseParamsForm.isInsert()) {
				if (StringUtils.isNotEmpty(fldw))
					descriptionService.insertDescription(descriptiontypeService.insertOrFind("分类学"), fldw, taxon,
							baseParamsForm);
				if (StringUtils.isNotEmpty(dlfb))
					descriptionService.insertDescription(descriptiontypeService.insertOrFind("分布信息"), dlfb, taxon,
							baseParamsForm);
				if (StringUtils.isNotEmpty(jz))
					descriptionService.insertDescription(descriptiontypeService.insertOrFind("寄主"), jz, taxon,
							baseParamsForm);
				if (StringUtils.isNotEmpty(wh))
					descriptionService.insertDescription(descriptiontypeService.insertOrFind("危害"), wh, taxon,
							baseParamsForm);
			}
			// 拆分图片
			String[] images = img.split("src");

			for (String image : images) {

				if (!image.contains("\'") || image.contains("未知")) {
					continue;
				}
				int firstIndex = image.indexOf("'") + 1;
				int lastIndexOf = image.lastIndexOf("'");
				image = image.substring(firstIndex, lastIndexOf);
				image = image.replace("：", ":");
				File file = new File(image);
				if (!file.exists()) {
					System.out.println("error 找不到这张图片：" + image);
					continue;
				}

				// save image
				if (baseParamsForm.isInsert()) {
					multimediaService.saveMultimedia(taxon, baseParamsForm, image);
				}

			}

		}

	}

	private void insertDesc(BaseParamsForm baseParamsForm) {
		try {
			List<String> list = CommUtils.readTxt("E:\\003采集系统\\0010四册版\\原核生物及病毒类\\描述补充.txt", "utf-8");
			String fldw = null;
			String dlfb = null;
			String jz = null;
			Taxon taxon = null;
			String taxasetId = baseParamsForm.getmTaxasetId();
			for (String line : list) {
				if ("QQ".equals(line)) {
					line = line.trim();
					if (baseParamsForm.isInsert()) {
						if (StringUtils.isNotEmpty(fldw))
							descriptionService.insertDescription(descriptiontypeService.insertOrFind("分类学"), fldw,
									taxon, baseParamsForm);
						if (StringUtils.isNotEmpty(dlfb))
							descriptionService.insertDescription(descriptiontypeService.insertOrFind("分布信息"), dlfb,
									taxon, baseParamsForm);
						if (StringUtils.isNotEmpty(jz))
							descriptionService.insertDescription(descriptiontypeService.insertOrFind("寄主"), jz, taxon,
									baseParamsForm);
					}
					fldw = null;
					dlfb = null;
					jz = null;
					taxon = null;
				} else if (line.startsWith("分类地位")) {
					fldw = line;
				} else if (line.startsWith("地理分布")) {
					dlfb = line;
				} else if (line.startsWith("寄主")) {
					jz = line;
				} else {
					String chinese = CommUtils.cutChinese(line);
					List<Taxon> taxonList = taxonRepository.findByRemarkLikeAndTaxaset(chinese, taxasetId);
					if (taxonList.size() == 1) {
						taxon = taxonList.get(0);
					} else {
						if (line.equals("桃X病植原体	Peach X-disease phytoplasma")) {
							taxon = taxonRepository.findOneById("4deadf6cbd2b48939f384a4a600fc28b");
						} else if (line
								.equals("香蕉细菌性枯萎病菌（2号小种）Ralstonia solanacearum ( Smith ) Yabuuchi et al.（race 2）")) {
							taxon = taxonRepository.findOneById("e10ba2500a3a474486f1107215fdb440");
						} else {
							System.out.println("找不到:" + line);
						}
					}
				}

			}

			// save desc 保存分类地位、地理分布、寄主到描述表

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String MergeStrings(List<String> list) {
		StringBuffer sb = new StringBuffer();
		for (String line : list) {
			sb.append(line);
		}
		return sb.toString();
	}

	/**
	 * 
	 * @Description 将目录录入taxon
	 * @param b
	 * @param catalogPath
	 * @param baseParamsForm
	 * @author ZXY
	 * @throws Exception
	 */
	private void parseCatalogAndInsert(boolean b, String catalogPath, BaseParamsForm baseParamsForm) throws Exception {
		List<Taxon> taxonList = new ArrayList<>();
		List<String> list = CommUtils.readTxt(catalogPath, "utf-8");
		System.out.println(catalogPath + ",读取数据行数：" + list.size());

		for (String line : list) {
			// a、跳过空行
			if (StringUtils.isEmpty(line)) {
				continue;
			}
			line = line.substring(0, line.indexOf("	")).trim();// 去除页码
			Taxon taxon = new Taxon();
			taxon.setRemark(line);
			// b、解析line
			String chinesName = CommUtils.cutChinese(line);
			taxon.setChname(chinesName);
			line = CommUtils.removeChinese(line);// 删除中文
			taxon.setScientificname(line.trim());
			taxon.setRankid(String.valueOf(getRank(line, chinesName).getIndex()));
			Rank rank = new Rank();
			rank.setId(taxon.getRankid());
			taxon.setRank(rank);

			EntityInit.initTaxon(taxon, baseParamsForm);
			taxonList.add(taxon);
		}
		// 保存到数据库
		batchInsertService.batchInsertTaxon(taxonList, baseParamsForm.getmInputtimeStr());

	}

	public RankEnum getRank(String sciName, String chinesName) {
		if (sciName.contains("var.")) {
			return RankEnum.var;
		} else if (chinesName.endsWith("门")) {
			return RankEnum.Phylum;
		} else if (chinesName.endsWith("纲")) {
			return RankEnum.Class;
		} else {
			return RankEnum.species;
		}
	}

	String[] uselessArray = { "的", "北方", "等地", "等", "和", "以及", "记载", "也", "欧洲", "均有", "在", "主要" };

	@Override
	public void turnDescToDistribution(String teamId, boolean save) {
		
		long startTime = System.currentTimeMillis();
		// 查询地理分布描述信息
		String descTypeId = "201";// 描述类型201代表：分布信息
		List<Description> list = descriptionService.findByTeamAndDescType(teamId, descTypeId);
		Map<String, String> map = new HashMap<>();// 查询不到的分布地
		logger.info("在描述表查询分布描述（数量）：" + list.size());
		for (Description description : list) {
			Map<String, String> geojsonValue = new HashMap<>();// 使用map,过滤重复数据
			String descontent = description.getDescontent();
			List<Word> words = WordSegmenter.segWithStopWords(descontent);//分词，不移除停用词
			PartOfSpeechTagging.process(words);// 词性标注
			for (Word word : words) {
				PartOfSpeech speech = word.getPartOfSpeech();
				String des = speech.getDes();// 词性
				String text = word.getText();
				if (des.equals("地名") || des.equals("未知")) {
					// 查询
					Geoobject obj = geoobjectRepository.findOneByCngeoname(text);// 精确查询
					if (obj == null) {
						obj = getSpecialProvinces(text);// 特殊的
						if (obj == null) {
							obj = findWithAddSheng(text);// 添加省字查询
						}
						if (obj == null) {
							obj = fuzzyQuery(text);// 模糊查询
						}
					}
					// 处理查询结果，为空则放入map,不为空则append
					if (obj != null) {
						geojsonValue.put(obj.getId(), obj.getId());
					} else {
						map.put(text, des + ",原文：" + descontent);
					}
				}
			} // end for
				// 如果全部能查询到，则save ;一个描述对应保存一个分布
			String keyString = CommUtils.getKeyString(geojsonValue);
			if (StringUtils.isEmpty(keyString)) {
				System.out.println("数据库没有查询到分布地,原文：" + descontent);
			} 
			if (save && StringUtils.isNotEmpty(keyString)) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("geoIds", keyString);
				Distributiondata record = new Distributiondata();
				record.setTaxonid(description.getTaxon().getId());
				record.setTaxon(description.getTaxon());
				record.setSourcesid(description.getSourcesid());
				record.setInputer(description.getInputer());
				record.setGeojson(String.valueOf(jsonObject));
				record.setDescid(description.getId());
				distributiondataService.saveOne(record);
			}
		} // end for

		// 遍历map
		int k = 0;
		for (Entry<String, String> entry : map.entrySet()) {
			k++;
			logger.info(" Key = " + entry.getKey() + "    Value = " + entry.getValue());
		}

		logger.info("执行时间：" + (System.currentTimeMillis() - startTime) / 60000 + "min, "
				+ (System.currentTimeMillis() - startTime) / 1000 + "s ( " + (System.currentTimeMillis() - startTime)
				+ "ms)");

	}

	/**
	 * 
	 * @Description 模糊查询
	 * @param text
	 * @return
	 * @author ZXY
	 */
	private Geoobject fuzzyQuery(String text) {
		Geoobject object = null;
		List<Geoobject> list = geoobjectRepository.findByLikeCngeoname(text);
		if (list.size() > 0) {
			object = list.get(0);
		}
		if (object == null) {
			List<Geoobject> list2 = geoobjectRepository.findByLikeRemark(text);
			if (list2.size() > 0) {
				object = list2.get(0);
			}
		}
		return object;
	}

	private Geoobject findWithAddSheng(String text) {
		text = text + "省";
		return geoobjectRepository.findOneByCngeoname(text);
	}

	/**
	 * 
	 * @Description
	 * @param text
	 * @return
	 * @author ZXY
	 */
	private Geoobject getSpecialProvinces(String text) {
		String queryText = null;
		if (text.contains("宁夏")) {
			queryText = "宁夏回族自治区";
		} else if (text.contains("香港")) {
			queryText = "香港特别行政区";
		} else if (text.contains("澳门")) {
			queryText = "澳门特别行政区";
		} else if (text.contains("台湾")) {
			queryText = "台湾省";
		} else if (text.contains("西藏")) {
			queryText = "西藏自治区";
		} else if (text.contains("内蒙古")) {
			queryText = "内蒙古自治区";
		} else if (text.contains("新疆")) {
			queryText = "新疆维吾尔自治区";
		} else if (text.contains("广西")) {
			queryText = "广西壮族自治区";
		} else if (text.contains("北京")) {
			queryText = "北京市";
		} else if (text.contains("重庆")) {
			queryText = "重庆市";
		} else if (text.contains("上海")) {
			queryText = "上海市";
		} else if (text.contains("天津")) {
			queryText = "天津市";
		}
		//仅此次使用
		else if (text.contains("刚果")) {
			queryText = "刚果（金）";
		}else if (text.contains("俄国")) {
			queryText = "俄罗斯";
		}else if (text.contains("捷克")) {
			queryText = "捷克";
		}else if (text.contains("汶莱")) {
			queryText = "文莱";
		}else if (text.contains("加沙")) {
			queryText = "巴勒斯坦";
		}

		if (StringUtils.isNotEmpty(queryText)) {
			return geoobjectRepository.findOneByCngeoname(queryText);
		}
		return null;
	}

}
