package org.big.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.big.common.CommUtils;

public class Tes {

	public static void main(String[] args) {
		System.out.println(CommUtils.cutChinese("69窄头橐吾（含一种）"));
		Pattern pattern = Pattern.compile("\\d{1,}\\w{1,}.");
		Matcher matcher = pattern.matcher("b.粉背薯蓣");
		System.out.println(matcher.find());

	}

}
