package org.big.service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.big.common.CommUtils;
import org.big.common.EntityInit;
import org.big.entity.Rank;
import org.big.entity.Taxon;
import org.big.entityVO.BaseParamsForm;
import org.big.entityVO.RankEnum;
import org.hibernate.mapping.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class FourServiceImpl implements FourService{
	@Autowired
	private BatchInsertService batchInsertService;

	@Override
	public void handleTxt(BaseParamsForm baseParamsForm) throws Exception {
		//1、读取txt文件
		String filePath = baseParamsForm.getFilePath();
		String upperPath = StringUtils.substring(filePath, 0, filePath.lastIndexOf(".txt"));
		String catalogPath = upperPath +"目录.txt";
		System.out.println("目录路径："+catalogPath);
		List<String> list = CommUtils.readTxt(filePath, "utf-8");
		String allContent = MergeStrings(list);
		System.out.println("读取数据行数："+list.size());
		List<String> cataloglist = CommUtils.readTxt(catalogPath, "utf-8");
		for (String catalog : cataloglist) {
			catalog = catalog.substring(0, catalog.indexOf("	")).trim();
			if(allContent.contains(catalog)) {
				
			}else {
				System.out.println("找不到");
			}
		}
		//2、解析目录，录入taxon
//		parseCatalogAndInsert(true,catalogPath,baseParamsForm);
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
		System.out.println(catalogPath+",读取数据行数："+list.size());
		
		
		for (String line : list) {
			//a、跳过空行
			if(StringUtils.isEmpty(line)) {
				continue;
			}
			Taxon taxon = new Taxon();
			//b、解析line
			String chinesName = CommUtils.cutChinese(line);
			taxon.setChname(chinesName);
			String pageNum = CommUtils.cutNumber(line);
			line = StringUtils.remove(line, pageNum);//去除最后的页码
			line = CommUtils.removeChinese(line);//删除中文
			taxon.setScientificname(line.trim());
			taxon.setRankid(String.valueOf(getRank(line, chinesName).getIndex()));
			Rank rank = new Rank();
			rank.setId(taxon.getRankid());
			taxon.setRank(rank);
			
			EntityInit.initTaxon(taxon, baseParamsForm);
			taxonList.add(taxon);
		}
		//保存到数据库
		batchInsertService.batchInsertTaxon(taxonList, baseParamsForm.getmInputtimeStr());
		
	}
	
	public RankEnum  getRank(String sciName,String chinesName){
		if(sciName.contains("var.")) {
			return RankEnum.var;
		}else if(chinesName.endsWith("门")) {
			return RankEnum.Phylum;
		}else if(chinesName.endsWith("纲")) {
			return RankEnum.Class;
		}else {
			return RankEnum.species;
		}
	}

	
	

}
