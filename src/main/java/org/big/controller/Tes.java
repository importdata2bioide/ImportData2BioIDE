package org.big.controller;

import org.apache.commons.lang3.StringUtils;
import org.big.entityVO.RankEnum;
import org.big.service.PlantAsyncServiceImpl;

public class Tes {

	public static void main(String[] args) {
		PlantAsyncServiceImpl x = new PlantAsyncServiceImpl();
		RankEnum rank = x.judgeRankIsWhatByPath("\\合瓣花类\\中国植物志-第63卷（488）\\萝藦科\\14 鹅绒藤属\\17a 大理白前 （原变种）.xlsx");
		System.out.println(rank.getName());
		System.out.println(StringUtils.split("Calanthe arcuata Rolfe", " ").length);

	}

}
