package org.big.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	
	public static void main(String[] args) {
		String line = "Muraena Bleeker, 1852.";
		String EngMixLatin = "([a-z]|[\u00A0-\u00FF]|[\u0100-\u017F]|[\u0180-\u024F])+";
		String regEx = "^[A-Z]{1}[a-z]{1,}\\s{1,}"+EngMixLatin+"\\s{1,}[A-Z]{1}(.*?)";
		Pattern pattern = Pattern.compile(regEx);
		// 忽略大小写的写法
		// Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(line);
		// 字符串是否与正则表达式相匹配
		boolean rs = matcher.matches();
		boolean match = false;
		if (rs) {
			System.out.println("匹配：" + line);
			match = true;
		}
		if (!match) {
			System.out.println("没有匹配：" + line);
		}
	}

}
