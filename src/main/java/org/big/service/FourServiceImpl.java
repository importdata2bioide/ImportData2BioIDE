package org.big.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
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

	@Override
	public void handleTxt(BaseParamsForm baseParamsForm) throws Exception {
		// 1、读取txt文件
		String filePath = baseParamsForm.getFilePath();
		String upperPath = StringUtils.substring(filePath, 0, filePath.lastIndexOf(".txt"));
		String catalogPath = upperPath + "目录.txt";
		System.out.println("目录路径：" + catalogPath);
//		parseCatalogAndInsert(true,catalogPath,baseParamsForm);
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
			if(line.contains("门")||line.contains("纲")) {
				continue;
			}
			k++;
			line = line.substring(0, line.indexOf("	")).trim();// 去除页码
			if (allContent.contains(line)) {

			} else if (allContent.contains(StringUtils.substring(line, 0, line.indexOf("菌")))) {
				line = StringUtils.substring(line, 0, line.indexOf("菌"));
			} else {
				System.out.println("找不到、" + line);
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
		//可以处理的list
		for (String line : reList) {
			line = line.replace(":", "：");
			line = line.replace("分布地区", "地理分布");
			line = line.replace("寄主名称", "寄主");
			int indexOfFLDW = line.indexOf("分类地位：");
			int indexOfDLFB = line.indexOf("地理分布：");
			int indexOfJZ = line.indexOf("寄主：");
			int indexOfIMG = line.indexOf("<img");
			if( indexOfFLDW== -1) {
				System.out.println("分类地位__"+line);
			}
			if( indexOfDLFB== -1) {
				System.out.println("地理分布__"+line);
			}
			if( indexOfJZ== -1) {
				System.out.println("寄主__"+line);
			}
			if( indexOfIMG== -1) {
				System.out.println("图片__"+line);
			}
//			System.out.println("--------------------");
			String name = StringUtils.substring(line, 0, indexOfFLDW);
			String fldw = StringUtils.substring(line, indexOfFLDW, indexOfDLFB);//分类地位
			String dlfb = StringUtils.substring(line, indexOfDLFB, indexOfJZ);//地理分布
			String jz = StringUtils.substring(line, indexOfJZ, indexOfIMG);//寄主
			String img = StringUtils.substring(line, indexOfIMG);//图片
//			System.out.println(name);
//			System.out.println(fldw);
//			System.out.println(dlfb);
//			System.out.println(jz);
//			System.out.println(img);
			Taxon taxon = taxonRepository.findByTaxasetAndRemark(baseParamsForm.getmTaxasetId(), name);
			
			if(taxon == null) {
				taxon = taxonRepository.findByTaxasetAndChname(baseParamsForm.getmTaxasetId(), CommUtils.cutChinese(name));
				if(taxon == null) {
					System.out.println("找不到taxon，name = "+name);
				}
			}
			//拆分图片
			String[] images = img.split("src");
			for (String image : images) {
				if(!image.contains("\'")) {
					continue;
				}
				System.out.println(image);
				image = image.substring(image.indexOf("\'")+1, image.lastIndexOf("\'"));
				System.out.println("-----"+image);
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
