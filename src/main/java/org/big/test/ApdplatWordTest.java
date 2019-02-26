package org.big.test;

import java.util.List;

import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;

/**
 * 
 * @Description org.apdplat是一个Java实现的中文分词组件，提供了多种基于词典的分词算法，并利用ngram模型来消除歧义。
 *              能准确识别英文、数字，以及日期、时间等数量词，能识别人名、地名、组织机构名等未登录词。
 *              同时提供了Lucene、Solr、ElasticSearch插件。
 * @author ZXY
 */
public class ApdplatWordTest {
	
	public static void main(String[] args) {
		 long startTime = System.currentTimeMillis();
		System.out.println("执行时间："+(System.currentTimeMillis() - startTime)/60000+"min, "+(System.currentTimeMillis() - startTime)/1000 + "s ( "+(System.currentTimeMillis() - startTime)+"ms)");
		System.out.println(WordSegmenter.segWithStopWords("地理分布：美洲的美国、加拿大、墨西哥、巴西、智力，大洋洲的澳大利亚、新西兰，欧洲的英国、捷克、斯洛伐克、意大利，亚洲的日本、沙特阿拉伯等"));
		System.out.println("执行时间："+(System.currentTimeMillis() - startTime)/60000+"min, "+(System.currentTimeMillis() - startTime)/1000 + "s ( "+(System.currentTimeMillis() - startTime)+"ms)");
		System.out.println(WordSegmenter.segWithStopWords("某些炎热、干燥的柑桔产区, 包括美国加利福尼亚州, 北非的多数地区, 东地中海盆地和中东。"));
		System.out.println("执行时间："+(System.currentTimeMillis() - startTime)/60000+"min, "+(System.currentTimeMillis() - startTime)/1000 + "s ( "+(System.currentTimeMillis() - startTime)+"ms)");
		System.out.println(WordSegmenter.segWithStopWords("地理分布：原产欧洲，盛产于叙利亚和巴勒斯坦一带，在我国分布于黑龙江、吉林、辽宁、内蒙古、山东、宁夏、青海、新疆、江苏、江西、湖北、云南、西藏、河北、山西、上海、浙江、湖南、福建、北京、陕西、河南、甘肃、安徽、四川和广东等省（市、区）。"));
		System.out.println("执行时间："+(System.currentTimeMillis() - startTime)/60000+"min, "+(System.currentTimeMillis() - startTime)/1000 + "s ( "+(System.currentTimeMillis() - startTime)+"ms)");
		
	}

}
