package org.big.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.big.service.BirdAddData;
import org.big.service.BirdAddDataImpl;

public class Test2 {
	
	private static final String s = "123";
	
	private static final String s2 = "444"+s;
	public static void main(String[] args) {
		
		BirdAddData birdAddData = new BirdAddDataImpl();
		birdAddData.initRegExPagelist();
		List<String> list = new ArrayList<>();
//		list.add("Temminck, 1836,Pl.Col. Livr,99 pl.588");
//		list.add("Hemprich & Ehrenberg, 1833，Symb.Phys.Aves(1828) sig.bb,note2");
//		list.add("Blyth, 1845,J.Asiat.Soc.Bengal 13 ,943");
//		list.add("Blyth, 1843，J.Asiat.Soc.Bengal 12，938");
//		list.add("Rasmussen, Round, Dickinson & Rozendaal, 2000， Auk 117，280-286");
//		list.add("Alström et al., 2009，Ibis 152:145-168");
//		list.add("Wardlaw Ramsay, 1876，PZS Pt3 ，677 pl.63");
//		list.add("Moore, F, 1858，Cat.BirdsMus.East-Ind.Co.[Horsfield & Moore] 2，537");
//		list.add("Bambusicola sonorivox, Gould 1863,Proceedings the Zoological Society of London: 283‐286");
//		list.add("Brachypteryx leucophrys,Temminck, 1828.Pl.Col. livr.74 [=livr.75] pl.448 fig.1");
//		list.add("Anas clypeata Linnaeus,1758, Syst. Nat. ed. 10, 1:124");
//		list.add("Parus dichrous,Blyth, 1845,J.Asiat.Soc.Bengal 13 ,943");
		list.add("Anas clypeata Linnaeus,1758, Syst. Nat. ed. 10, 1:124");
//		list.add("");
//		list.add("");
//		list.add("");
//		list.add("");
		
		for (String line : list) {
			System.out.println("--------------------------");
			Map<String, String> map = birdAddData.getPageFromCitationStr(line);
			System.out.println(line);
			System.out.println(map.get("refS"));
			System.out.println(map.get("refE"));
		}
	}
	
	

}
