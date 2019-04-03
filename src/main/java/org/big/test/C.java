package org.big.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.big.common.CommUtils;
import org.big.service.ToolService;
import org.big.service.ToolServiceImpl;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.internal.util.StringHelper;

public class C {

	public static void main(String[] args) {
		ToolService toolService = new ToolServiceImpl();
		// 要验证的字符串
		List<String> list = new ArrayList<>();
		list.add("Zacco Jordan et Evermann，1902，322.");
		list.add("Bdellostoma burgeri Girard, 1855: 1.");
		list.add("Triplophysa daqiaoensis Ding(丁瑞华)，1993，247；刘明玉等，2000，161；He et al.");
		list.add("Heptatretus burgeri (Girard, 1855).");
		list.add("Lampetra reissneri ：任慕莲，1981，3；朱元鼎和孟庆文等，2001，25.");
		list.add("Triplophysa(Qinghaichthys)chondrostoma：成庆泰和郑葆珊，1987，193.");
		list.add("Rasbora cephalotaenia steineri Nichols et Pope，1927，364；杨干荣和黄宏金(见伍献文等)，1964，36；朱松泉");
		list.add("Rasbora lateristriata allos：林书颜，1931，67；杨干荣和黄宏金(见伍献文等)，1964，37.");
		list.add("Cyprinus(Cyprinus)carpio carpio：陈湘粦和黄宏金(见伍献文等)，1982，411.");
		list.add("Fugu vermiculare porphyreum (Temminck & schlegel, 1850).");
		list.add("Xenodon(Balistes)niger Rüppell, 1836: 1-148.");
		list.add("Etmopterus spinax（Linnaeus，1758）");
		list.add("Chiloscyllium indicum var. plagiosum (Anonymous [Bennett], 1830): 686-694.");
		list.add("Synaphobranchus pinnatus var. brevidorsalis Lloyd, 1909.");
		list.add("Muraena isingleenoïdes Bleeker, 1852.");
		
		String EngMixLatin = "([a-z]|[\u00A0-\u00FF]|[\u0100-\u017F]|[\u0180-\u024F])+";
		List<String> regExlist = new ArrayList<>();
		//0、species 属 空格 种加词 空格 命名首字母大写
		regExlist.add("^[A-Z]{1}[a-z]{1,}\\s{1,}[a-z]{1,}\\s{1,}[A-Z]{1,}(.*?)");
		//1、species 属 空格 种加词 空格可忽略 (命名信息首字母大写)
		regExlist.add("^[A-Z]{1}[a-z]{1,}\\s{1,}[a-z]{1,}\\s{0,}\\({1}(.*?)");
		//2、species 属 空格 种加词 空格可忽略：
		regExlist.add("^[A-Z]{1}[a-z]{1,}\\s{1,}[a-z]{1,}\\s{0,}：{1}(.*?)");
		//3、species 属 空格可忽略(亚属)空格可忽略  种加词 空格可忽略：
		regExlist.add("^[A-Z]{1}[a-z]{1,}\\s{0,}\\([A-Z]{1}[a-z]{1,}\\)\\s{0,}[a-z]{1,}\\s{0,}：{1}(.*?)");
		//4、genus 属 空格 命名信息首字母大写
		regExlist.add("^[A-Z]{1}[a-z]{1,}\\s{1,}[A-Z]{1}[a-z]{1,}\\s{1,}(.*?)");
		//5、subspecies 属 空格 种加词 空格 亚种加词 空格 命名信息首字母大写
		regExlist.add("^[A-Z]{1}[a-z]{1,}\\s{1,}[a-z]{1,}\\s{1,}[a-z]{1,}\\s{1,}[A-Z]{1}(.*?)");
		//6、subspecies 属 空格 种加词 空格 亚种加词 空格可忽略：
		regExlist.add("^[A-Z]{1}[a-z]{1,}\\s{1,}[a-z]{1,}\\s{1,}[a-z]{1,}\\s{0,}：(.*?)");
		//7、subspecies 属(亚属)种加词 亚种加词：
		regExlist.add("^[A-Z]{1}[a-z]{1,}\\s{0,}\\([A-Z]{1}[a-z]{1,}\\)\\s{0,}[a-z]{1,}\\s{1,}[a-z]{1,}：(.*?)");
		//8、subspecies 属 空格 种加词 空格 亚种加词 空格可忽略(命名信息首字母大写)
		regExlist.add("^[A-Z]{1}[a-z]{1,}\\s{1,}[a-z]{1,}\\s{1,}[a-z]{1,}\\s{0,}\\([A-Z]{1}(.*?)");
		//9、属 空格可忽略(亚属)空格可忽略 种加词 空格 命名信息首字母大写
		regExlist.add("^[A-Z]{1}[a-z]{1,}\\s{0,}\\([A-Z]{1}[a-z]{1,}\\)\\s{0,}[a-z]{1,}\\s{1,}[A-Z]{1,}(.*?)");
		//10、species 属 空格 种加词 空格可忽略 （命名信息首字母大写）
		regExlist.add("^[A-Z]{1}[a-z]{1,}\\s{1,}[a-z]{1,}\\s{0,}\\（(.*?)\\）(.*?)");
		//Chiloscyllium indicum var. plagiosum (Anonymous [Bennett], 1830): 686-694.
		//11、var 属名 空格 种加词 空格可忽略 var. 空格 亚种加词 空格可忽略(命名信息首字母大写
		regExlist.add("^[A-Z]{1}[a-z]{1,}\\s{1,}[a-z]{1,}\\s{0,}var.\\s{1,}[a-z]{1,}\\s{0,}\\((.*?)");
		//Synaphobranchus pinnatus var. brevidorsalis Lloyd, 1909.
		//12 var 属名 空格 种加词 空格可忽略 var. 空格 亚种加词 空格 命名信息首字母大写
		regExlist.add("^[A-Z]{1}[a-z]{1,}\\s{1,}[a-z]{1,}\\s{0,}var.\\s{1,}[a-z]{1,}\\s{1,}[A-Z]{1,}(.*?)");
		//Muraena isingleenoïdes Bleeker, 1852.
		//13.species 属名 空格 种加词拉丁文 空格 命名信息首字母大写
		regExlist.add("^[A-Z]{1}[a-z]{1,}\\s{1,}"+EngMixLatin+"\\s{1,}[A-Z]{1}(.*?)");
		
		
		for (String line : list) {
			String sciname = "";
			boolean match = false;
			int indexOfRegExlist = -1;
			for (String regEx : regExlist) {
				indexOfRegExlist++;
				Pattern pattern = Pattern.compile(regEx);
				// 忽略大小写的写法
				// Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
				Matcher matcher = pattern.matcher(line);
				// 字符串是否与正则表达式相匹配
				boolean rs = matcher.matches();
				if (rs) {
//					System.out.println("匹配：" + line);
					if (indexOfRegExlist == 0) {
						sciname = line.substring(0, toolService.getPointUpperCaseIndex(line, 2));
//						System.out.println("匹配0：" + line);
//						System.out.println(line+"__"+sciname);
					}else if (indexOfRegExlist == 1) {
						sciname = line.substring(0,line.indexOf("("));
//						System.out.println("匹配1：" + line);
//						System.out.println(line+"__"+sciname);
					}else if (indexOfRegExlist == 2) {
						sciname = line.substring(0,line.indexOf("："));
//						System.out.println("匹配2：" + line);
//						System.out.println(line+"__"+sciname);
					}else if (indexOfRegExlist == 3) {
						sciname = line.substring(0,line.indexOf("："));
//						System.out.println("匹配3：" + line);
//						System.out.println(line+"__"+sciname);
					}else if (indexOfRegExlist == 4) {
						sciname = line.substring(0,toolService.getPointUpperCaseIndex(line, 2));
//						System.out.println("匹配4：" + line);
//						System.out.println(line+"__"+sciname);
					}else if (indexOfRegExlist == 5) {
						sciname = line.substring(0,toolService.getPointUpperCaseIndex(line, 2));
//						System.out.println("匹配5：" + line);
//						System.out.println(line+"__"+sciname);
					}else if (indexOfRegExlist == 6) {
						sciname = line.substring(0,line.indexOf("："));
//						System.out.println("匹配6：" + line);
//						System.out.println(line+"__"+sciname);
					}else if (indexOfRegExlist == 7) {
						sciname = line.substring(0,line.indexOf("："));
//						System.out.println("匹配7：" + line);
//						System.out.println(line+"__"+sciname);
					}else if (indexOfRegExlist == 8) {//亚种
						
						sciname = line.substring(0,line.indexOf("("));
//						System.out.println("匹配8：" + line);
//						System.out.println(line+"__"+sciname);
					}else if (indexOfRegExlist == 9) {//属 (亚属) 种加词 命名信息
						sciname = line.substring(0,toolService.getPointUpperCaseIndex(line, 3));
//						System.out.println("匹配9：" + line);
//						System.out.println(line+"__"+sciname);
					}else if (indexOfRegExlist == 10) {
						sciname = line.substring(0,line.indexOf("（"));
					}else if (indexOfRegExlist == 11) {
						sciname = line.substring(0,line.indexOf("("));
					}else if (indexOfRegExlist == 12) {
						sciname = line.substring(0,toolService.getPointUpperCaseIndex(line, 2));
					}else if (indexOfRegExlist == 13) {
						sciname = line.substring(0,toolService.getPointUpperCaseIndex(line, 2));
						System.out.println("匹配13：" + line);
						System.out.println(sciname+"__"+line);
						
					}
					match = true;
					break;
				}
			}
			if (!match) {
				System.out.println("没有匹配：" + line);
			}

		}

	}

	public int getYearStart(String line) {
		int start = -1;
		for (int i = 0; i < line.length() - 4; i++) {
			String tmp = line.substring(i, i + 4);
			if (CommUtils.isNumeric(tmp)) {
				start = i;
				break;
			}
		}
		return start;

	}

	public String apply(String name) {
		if (name == null) {
			return null;
		}
		// 大写字母前加下划线
		StringBuilder builder = new StringBuilder(name.replace('.', '_'));
		for (int i = 1; i < builder.length() - 1; i++) {
			if (isUnderscoreRequired(builder.charAt(i - 1), builder.charAt(i), builder.charAt(i + 1))) {
				builder.insert(i++, '_');
			}
		}
		// 大写变小写
		name = builder.toString().toLowerCase(Locale.ROOT);
		return name;
	}

	protected boolean isCaseInsensitive(JdbcEnvironment jdbcEnvironment) {
		return true;
	}

	private boolean isUnderscoreRequired(char before, char current, char after) {
		return Character.isLowerCase(before) && Character.isUpperCase(current) && Character.isLowerCase(after);
	}

	public static Identifier toIdentifier(String text) {
		if (StringHelper.isEmpty(text)) {
			return null;
		}
		final String trimmedText = text.trim();
		if (isQuoted(trimmedText)) {
			final String bareName = trimmedText.substring(1, trimmedText.length() - 1);
			return new Identifier(bareName, true);
		} else {
			return new Identifier(trimmedText, false);
		}
	}

	public static boolean isQuoted(String name) {
		return (name.startsWith("`") && name.endsWith("`")) || (name.startsWith("[") && name.endsWith("]"))
				|| (name.startsWith("\"") && name.endsWith("\""));
	}

}
