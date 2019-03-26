package org.big.test;

import java.util.List;

import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.PartOfSpeech;
import org.apdplat.word.segmentation.Word;
import org.apdplat.word.tagging.PartOfSpeechTagging;

/**
 * 
 * @Description org.apdplat是一个Java实现的中文分词组件，提供了多种基于词典的分词算法，并利用ngram模型来消除歧义。
 *              能准确识别英文、数字，以及日期、时间等数量词，能识别人名、地名、组织机构名等未登录词。
 *              同时提供了Lucene、Solr、ElasticSearch插件。
 * @author ZXY
 */
public class ApdplatWordTest {
	
	public static void main(String[] args) {
		String descontent = "I. 盲鳗纲Myxini";
		List<Word> words = WordSegmenter.segWithStopWords(descontent );//分词，不移除停用词
		PartOfSpeechTagging.process(words);// 词性标注
		for (Word word : words) {
			PartOfSpeech speech = word.getPartOfSpeech();
			String des = speech.getDes();// 词性
			String text = word.getText();
			System.out.println(des+"__"+text);
		}
	}

}
