package org.big.common;

import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;

public class SciNameUtil {
	/**
	 * 
	 * @Description 亚种的学名和命名解析
	 * @param str 学名和命名信息的组合串（不能包含其他内容）
	 * @return 返回学名和命名信息
	 * @author ZXY
	 */
	public static Map<String,String> getSubsp(String str){
		Map<String,String> map = null;
		try {
			String cutByStrBefore = CommUtils.cutByStrBeforeInclude(str, "subsp.").trim();
			String cutByStrAfter = CommUtils.cutByStrAfter(str, "subsp.").trim();
			String substring = cutByStrAfter.substring(0,cutByStrAfter.indexOf(" "));
			String sciName = cutByStrBefore.trim()+" " + substring.trim();
			String author = CommUtils.cutByStrAfter(str, substring).trim();
			map = new HashedMap<>();
			map.put("sciName", sciName);
			map.put("author", author);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 
	 * @Description  
	 * @param str pv 学名和命名信息的组合串（不能包含其他内容）
	 * @return
	 * @author ZXY
	 */
	public static Map<String,String> getPV(String str){
		Map<String,String> map = null;
		try {
			String cutByStrBefore = CommUtils.cutByStrBeforeInclude(str, "pv.").trim();
			String cutByStrAfter = CommUtils.cutByStrAfter(str, "pv.").trim();
			String substring = cutByStrAfter.substring(0,cutByStrAfter.indexOf(" "));
			String sciName = cutByStrBefore.trim()+" " + substring.trim();
			String author = CommUtils.cutByStrAfter(str, substring).trim();
			map = new HashedMap<>();
			map.put("sciName", sciName);
			map.put("author", author);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 
	 * @Description 解析rank = species 学名最简单的形式（学名+命名信息），如 Xanthomonas populi (ex Ride) Ride et Ride
	 * @param str
	 * @return
	 * @author ZXY
	 */
	public static Map<String,String> getSpecies(String str){
		Map<String,String> map = null;
		try {
			String sciName = str.substring(0, str.indexOf(" ",str.indexOf(" ")+1 )).trim();
			if(sciName.contains("(")||sciName.contains("（")||sciName.contains(")")||sciName.contains("）")) {
				return null;
			}
			String author = CommUtils.cutByStrAfter(str, sciName).trim();
			map = new HashedMap<>();
			map.put("sciName", sciName);
			map.put("author", author);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
	

}
