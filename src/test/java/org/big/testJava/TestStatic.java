package org.big.testJava;

public class TestStatic {
	public static String inputtimeStr = "2018-11-05 12:00:00";
	
	public static void main(String[] args) {
		System.out.println(inputtimeStr);
		inputtimeStr = "123";
		System.out.println(inputtimeStr);

	}

}
