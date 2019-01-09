package org.big.test;

import java.util.ArrayList;
import java.util.List;

public class TestParams {
	
	public static void main(String[] args) {
		
		String s = "123";
		System.out.println(s.length());
		addStr(s);
		System.out.println(s.length());
		List<String> list = new ArrayList<>();
//		TestParams t = new TestParams();
//		t.addList(list);
		addList(list);
		System.out.println(list.size());
		
	}
	public static void  addList(List<String> list) {
		for (int i = 0;i<100;i++) {
			list.add("i"+i);
		}
	}
	
	public static void  addStr(String str) {
		str = str  +"13122222123";
	}

}
