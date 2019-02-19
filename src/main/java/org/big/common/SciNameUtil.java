package org.big.common;

import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;

public class SciNameUtil {
	
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
	 * @Description 最简单的形式，如 Xanthomonas populi (ex Ride) Ride et Ride
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
