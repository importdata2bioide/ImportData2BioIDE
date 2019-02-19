package org.big.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import org.apache.commons.lang.StringUtils;
import org.big.common.CommUtils;
import org.big.common.EntityInit;
import org.big.entity.Rank;
import org.big.entity.Taxon;
import org.big.entityVO.BaseParamsForm;
import org.big.entityVO.RankEnum;
import org.big.repository.TaxonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FourServiceImpl implements FourService {
	@Autowired
	private BatchInsertService batchInsertService;
	@Autowired
	private TaxonRepository taxonRepository;

	@Autowired
	private DescriptionService descriptionService;
	@Autowired
	private DescriptiontypeService descriptiontypeService;
	@Autowired
	private MultimediaService multimediaService;

	@Override
	public void handleTxt(BaseParamsForm baseParamsForm) throws Exception {
		
		// 1、读取txt文件
		String filePath = baseParamsForm.getFilePath();
		String upperPath = StringUtils.substring(filePath, 0, filePath.lastIndexOf(".txt"));
		String catalogPath = upperPath + "目录.txt";
		System.out.println("目录路径：" + catalogPath);
		if(false) {
			parseCatalogAndInsert(true,catalogPath,baseParamsForm);
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

			} else if (allContent.contains(StringUtils.substring(line, 0, line.indexOf("菌")))) {
				line = StringUtils.substring(line, 0, line.indexOf("菌"));
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
			if(org.apache.commons.lang3.StringUtils.isEmpty(line)) {
				continue;
			}
			int indexOfMu = line.indexOf("目");
			int indexOfKe = line.indexOf("科");
			if((indexOfKe>=1)&&(indexOfKe<=4) || (indexOfMu>=1)&&(indexOfMu<=2)) {
				//粉蚧科Pseudococcidae \蚧科Coccidae 等跳过
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
			//save desc 保存分类地位、地理分布、寄主到描述表
			if(baseParamsForm.isInsert()) {
				if(StringUtils.isNotEmpty(fldw))
					descriptionService.insertDescription(descriptiontypeService.insertOrFind("分类学"), fldw, taxon, baseParamsForm);
				if(StringUtils.isNotEmpty(dlfb))
					descriptionService.insertDescription(descriptiontypeService.insertOrFind("分布信息"), dlfb, taxon, baseParamsForm);
				if(StringUtils.isNotEmpty(jz))
					descriptionService.insertDescription(descriptiontypeService.insertOrFind("寄主"), jz, taxon, baseParamsForm);
				if(StringUtils.isNotEmpty(wh))
					descriptionService.insertDescription(descriptiontypeService.insertOrFind("危害"), wh, taxon, baseParamsForm);
			}
			// 拆分图片
			String[] images = img.split("src");
			
			for (String image : images) {
				
				if (!image.contains("\'") || image.contains("未知")) {
					continue;
				}
				int firstIndex = image.indexOf("'")+1;
				int lastIndexOf = image.lastIndexOf("'");
				image = image.substring(firstIndex, lastIndexOf);
				image = image.replace("：", ":");
				File file=new File(image); 
				if(!file.exists()) {
					System.out.println("error 找不到这张图片："+image);
					continue;
				}

				//save image
				if(baseParamsForm.isInsert()) {
					multimediaService.saveMultimedia(taxon, baseParamsForm, image);
				}
				
			}

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

}
