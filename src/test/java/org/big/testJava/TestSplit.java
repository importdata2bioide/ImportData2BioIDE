package org.big.testJava;

public class TestSplit {
	
	public static void main(String[] args) {
		String line = "qwqw&";
		System.out.println(line.substring(0, line.length()-1));
		String[] geos = line .split("、");
		System.out.println(geos.length);
	}

}
