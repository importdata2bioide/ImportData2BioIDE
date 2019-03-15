package org.big.common;

import java.util.ArrayList;
import java.util.List;

public class RankRange {
	/**
	 * 
	 * @Description 比family高的阶元（不包含family）
	 * @return
	 * @author ZXY
	 */
	public static List<String> higherThanfamilyRankNames(){
		List<String> list = new ArrayList<String>();
		list.add("Kingdom");//界
		list.add("Phylum");//门
		list.add("Class");//纲
		list.add("Order");//目
		list.add("superfamily");//超科
		return list;
	}
	/**
	 * 
	 * @Description rank <= family
	 * @return
	 * @author ZXY
	 */
	public static List<String> lowerThanfamilyInculdeRankNames() {
		List<String> list = new ArrayList<String>();
		list.add("Family");//科
		list.add("Subfamily");//亚科
		list.add("Tribe");//族
		list.add("subtribe");//亚族
		list.add("Genus");//属
		list.add("Subgenus");//亚属
		list.add("Species");//种
		list.add("subspecies");//亚种
		return list;
	}
	
	/**
	 * 
	 * @Description  rank > genus(不包含genus)
	 * @return
	 * @author ZXY
	 */
	public static List<String> higherThanGenusRankNames() {
		List<String> list = new ArrayList<String>();
		list.add("Kingdom");//界
		list.add("Phylum");//门
		list.add("Class");//纲
		list.add("Order");//目
		list.add("superfamily");//超科
		list.add("Family");//科
		return list;
	}
	

}
