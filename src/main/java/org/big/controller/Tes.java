package org.big.controller;


import org.apache.commons.lang3.StringUtils;
import org.big.common.CommUtils;
import org.big.entityVO.RankEnum;
import org.big.service.PlantAsyncServiceImpl;

public class Tes {

	public static void main(String[] args) {

		String path = "E:\\003采集系统\\0007-5-2-1-金效华\\汇交专项-植物专题\\单子叶植物\\中国植物志第13卷第1册棕榈科（122）\\槟榔亚科\\鱼尾葵族\\鱼尾葵属\\9．透明水玉簪.xlsx";
		String[] split = StringUtils.split(path, "\\");
		String name = CommUtils.cutChinese(split[split.length - 1]);
		System.out.println("excel文件名称：" + name);
		PlantAsyncServiceImpl x = new PlantAsyncServiceImpl();
		RankEnum rank = x.judgeRankIsWhatByPath("\\合瓣花类\\中国植物志-第63卷（488）\\萝藦科\\14 鹅绒藤属\\17a 大理白前 （原变种）.xlsx");
		System.out.println(rank.getName());
		System.out.println(StringUtils.split("Calanthe arcuata Rolfe", " ").length);

	}



}
