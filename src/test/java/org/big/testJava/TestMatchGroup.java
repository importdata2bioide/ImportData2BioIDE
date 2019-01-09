package org.big.testJava;

import java.util.List;

import org.big.common.CommUtils;

public class TestMatchGroup {

	public static void main(String[] args) {
		String str = "An, 1996. Chin. J. Vector Bio. Control 7(6): 473; Chen et An, 2003. The Blackflies of China: 275;";
		//匹配多个数组多个空格
		String citationRegx = "(:\\s{0,}\\d+\\s{0,}(\\.)?)|(\\.\\s{0,}\\d+\\s{0,}\\.\\s{0,}Type locality)";
//		 String regx2 = "(\\s{0,}\\d+\\s{0,}(-|–|‒|−)\\s{0,}\\d+(,|，)?\\s{0,})+";
		 //保留
//		 String regx = "(\\d+\\s{0,}pp)|(pp\\s{0,}\\d+)|(\\d+\\s{0,}(-|–|‒|−)\\s{0,}\\d+)|(:\\s{0,}\\d+\\.)|(\\.\\s{0,}\\d+\\s{0,}\\.$)";
//		List<String> matchGroup = CommUtils.getMatchGroup(str, regx);
//		for (String s : matchGroup) {
//			System.out.println(s);
//		}
//		List<String> matchGroup2 = CommUtils.getMatchGroup(str, regx2);
//		for (String s : matchGroup2) {
//			System.out.println(s);
//		}
		 
		 List<String> matchGroup2 = CommUtils.getMatchGroup(str, citationRegx);
			for (String s : matchGroup2) {
				System.out.println(s);
			}
		System.out.println("执行完了");

	}

}
