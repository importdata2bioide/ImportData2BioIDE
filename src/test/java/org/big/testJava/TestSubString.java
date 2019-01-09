package org.big.testJava;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

public class TestSubString {
	
	public static void main(String[] args) {
		String s = "123456789.jpg";
		String filePath = "c:"+File.separator+"sd"+File.separator+"123.jpg";
		filePath .substring(filePath.lastIndexOf("\\")+1, filePath.lastIndexOf("."));
		System.out.println(s.substring(filePath.lastIndexOf(".")+1));
		System.out.println(s.length());
		s.substring(0, 9);
		System.out.println("----------------");
		String cellContent = "Androsace paxiana R. Knuth";
		
//		cellContent = cellContent.replaceAll("\\s{1,}", " ");
		cellContent = cellContent.replace(" ", " ");
		System.out.println(cellContent);
		int indexOfSci = StringUtils.ordinalIndexOf(cellContent.trim()," ",2);
		System.out.println(indexOfSci);
		String sciName = cellContent.substring(0, indexOfSci).trim();
		System.out.println(sciName);
	}

}
