package org.big.controller;

import org.big.entityVO.RankEnum;
import org.big.service.PlantAsyncServiceImpl;

public class Tes {

	public static void main(String[] args) {
		PlantAsyncServiceImpl x = new PlantAsyncServiceImpl();
		RankEnum rank = x.judgeRankIsWhatByPath("\\合瓣花类\\中国植物志第68卷玄参科（505）\\53. 马先蒿属\\系54 轮叶系\\124b.岩居马先蒿岩居亚种岩居变型.xlsx");
		System.out.println(rank.getName());

	}

}
