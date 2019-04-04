package org.big.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ValidationException;

import org.big.common.CommUtils;
import org.big.constant.MapConsts;
import org.big.service.ToolService;
import org.big.service.ToolServiceImpl;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.internal.util.StringHelper;

public class C {

	static Map<String, String> regExlist = new HashMap<>();

	public static void main(String[] args) {
		ToolService toolService = new ToolServiceImpl();
		boolean goon = true;
		if (goon) {
			String line = "Pseudogastromyzon(Subgenus of Hemimyzon)Nichols，1925，1.";
			int pointUpperCaseIndex = toolService.indexOfPointUpperCaseWithoutBrackets(line, 3);
			String substring = line.substring(0, pointUpperCaseIndex);
			System.out.println(substring);
			return;
		}
		initregExlist();
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
		list.add("Glyptosternon maculatum 张春霖等，1964，278；伍汉霖、邵广昭、赖春福、庄棣华、林沛立，2012，111.");
		list.add("Anabarilius liui luquanensis 刘振华和何纪昌，1983，102.");
		list.add("Gobiobotia(Gobiobotia)filifer(Garman，1912)");
		list.add("Cyprinus(Cyprinus)pellegrini barbatus Chen et Huang(陈湘粦和黄宏金，见伍献文等)，1982，423.");
		list.add("Cyprinus carpio var. hungaricus：Tchang，1930a，62.");
		list.add(
				"Tor(Foliter)brevifilis hainanensis伍献文等，1982，329；金鑫波(见中国水产科学院珠江水产研究所)，1986，126；陈湘粦等，1991，166；单乡红等(见乐佩琦等)，2000，158.");
		list.add("Barbodes 伍献文等，1982，236.");
		list.add("Tor(Tor)tor sinensis Wu(伍献文，见伍献文等)，1982，325；褚新洛和崔桂华(见褚新洛和陈银瑞)，1989，142.");
		list.add("Garra imberba imberba (non Garman)：Chen，in Yang et al.，2010(部分，怒江).");
		list.add("Schizothorax(Racoma)esocina 中国科学院动物研究所等，1979，34；武云飞和吴翠珍，1992，347；伍汉霖、邵广昭、赖春福、庄棣华、林沛立，2012，61.");
		list.add("Schizothorax o'connori Lloyd，1908");
		list.add("Glyptosternum 武云飞和吴翠珍，1992，539.");
		list.add("Euchiloglanis feae feae(non Vinciguerra)：Chu，1979.");
		list.add("Vanmanenia polylepis:Pan，Liu et Zheng(潘炯华、刘成汉和郑文彪)1983，107.");
		list.add("Pseudogastromyzon(Pseudogastromyzon)lianjiangen-sis：Tang et Chen，2000，6；乐佩琦等，2000，486.");
		toolService.initregExlist();
		for (String line : list) {
			toolService.parseSciName(line);
		}

	}

	public static Map<String, String> parseSciName(String line, ToolService toolService) {
		Map<String, String> map = new HashMap<>();
		String sciname = "";
		String author = "";// 命名人和年代
		boolean match = false;
		Set<Entry<String, String>> entrySet = regExlist.entrySet();
		for (Entry<String, String> entry : entrySet) {
			String key = entry.getKey();
			String regEx = entry.getValue();
			Pattern pattern = Pattern.compile(regEx);
			// 忽略大小写的写法
			// Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(line);
			// 字符串是否与正则表达式相匹配
			boolean rs = matcher.matches();
			if (rs) {
				if (key.equals("genus-0")) {
					sciname = line.substring(0, toolService.getPointUpperCaseIndex(line, 2));
				} else if (key.equals("species-0")) {
					sciname = line.substring(0, toolService.getPointUpperCaseIndex(line, 2));
				} else if (key.equals("species-1")) {
					sciname = line.substring(0, line.indexOf("("));
				} else if (key.equals("species-2")) {
					sciname = line.substring(0, line.indexOf("："));
				} else if (key.equals("species-3")) {
					sciname = line.substring(0, line.indexOf("："));
				} else if (key.equals("species-4")) {
					sciname = line.substring(0, toolService.getPointUpperCaseIndex(line, 3));
				} else if (key.equals("species-5")) {
					sciname = line.substring(0, line.indexOf("（"));
				} else if (key.equals("species-6")) {
					sciname = line.substring(0, toolService.getPointUpperCaseIndex(line, 2));
				} else if (key.equals("species-7")) {
					sciname = line.substring(0, toolService.IndexOfFirstChinese(line));
				} else if (key.equals("species-8")) {
					sciname = line.substring(0, toolService.getPointUpperCaseIndex(line, 3) - 1);
					System.out.println(sciname);
					System.out.println(line);
//					sciname = line.substring(0, toolService.IndexOfFirstChinese(line));
				} else if (key.equals("subspecies-0")) {
					sciname = line.substring(0, toolService.getPointUpperCaseIndex(line, 2));
				} else if (key.equals("subspecies-1")) {
					sciname = line.substring(0, line.indexOf("："));
				} else if (key.equals("subspecies-2")) {
					sciname = line.substring(0, line.indexOf("："));
				} else if (key.equals("subspecies-3")) {
					sciname = line.substring(0, line.indexOf("("));
				} else if (key.equals("var-0")) {
					sciname = line.substring(0, line.indexOf("("));
				} else if (key.equals("var-1")) {
					sciname = line.substring(0, toolService.getPointUpperCaseIndex(line, 2));
				} else if (key.equals("subspecies-4")) {
					sciname = line.substring(0, toolService.IndexOfFirstChinese(line));
				} else {
					throw new ValidationException("未定义的key:" + key);
				}
				match = true;
				break;
			}
		}
		if (!match) {
			System.out.println("没有匹配：" + line);
		}
		map.put(MapConsts.TAXON_SCI_NAME, sciname);
		map.put(MapConsts.TAXON_AUTHOR, author);
		return map;
	}

	public static void initregExlist() {

		// 小写字母或拉丁文
		String EngMixLatin = "([a-z]|[\u00A0-\u00FF]|[\u0100-\u017F]|[\u0180-\u024F])+";
		String multChinese = "[\\u4e00-\\u9fa5]+";
		if (regExlist.size() == 0) {
			// 4、genus 属 空格 命名信息首字母大写
			regExlist.put("genus-0", "^[A-Z]{1}" + EngMixLatin + "\\s{1,}[A-Z]{1}(.*?)");
			// 0、species 属 空格 种加词 空格 命名首字母大写
			regExlist.put("species-0", "^[A-Z]{1}" + EngMixLatin + "\\s{1,}" + EngMixLatin + "\\s{1,}[A-Z]{1,}(.*?)");
			// 1、species 属 空格 种加词 空格可忽略 (命名信息首字母大写)
			regExlist.put("species-1", "^[A-Z]{1}" + EngMixLatin + "\\s{1,}" + EngMixLatin + "\\s{0,}\\({1}(.*?)");
			// 2、species 属 空格 种加词 空格可忽略：
			regExlist.put("species-2", "^[A-Z]{1}" + EngMixLatin + "\\s{1,}" + EngMixLatin + "\\s{0,}：{1}(.*?)");
			// 3、species 属 空格可忽略(亚属)空格可忽略 种加词 空格可忽略：
			regExlist.put("species-3", "^[A-Z]{1}" + EngMixLatin + "\\s{0,}\\([A-Z]{1}" + EngMixLatin + "\\)\\s{0,}"
					+ EngMixLatin + "\\s{0,}：{1}(.*?)");
			// 9、species 属 空格可忽略(亚属)空格可忽略 种加词 空格 命名信息首字母大写
			regExlist.put("species-4", "^[A-Z]{1}" + EngMixLatin + "\\s{0,}\\([A-Z]{1}" + EngMixLatin + "\\)\\s{0,}"
					+ EngMixLatin + "\\s{1,}[A-Z]{1,}(.*?)");
			// 10、species 属 空格 种加词 空格可忽略 （命名信息首字母大写）
			regExlist.put("species-5", "^[A-Z]{1}" + EngMixLatin + "\\s{1,}" + EngMixLatin + "\\s{0,}\\（(.*?)\\）(.*?)");
			// 13 species 属 空格 种加词拉丁文 空格 命名信息首字母大写
			regExlist.put("species-6", "^[A-Z]{1}" + EngMixLatin + "\\s{1,}" + EngMixLatin + "\\s{1,}[A-Z]{1}(.*?)");
			// 14 species 属 空格 种加词 空格可忽略 中文命名信息
			regExlist.put("species-7",
					"^[A-Z]{1}" + EngMixLatin + "\\s{1,}" + EngMixLatin + "\\s{0,}" + multChinese + "(.*?)");
			// speciese 属(亚属)种加词(命名信息首字母大写
			regExlist.put("species-8", "^[A-Z]{1}" + EngMixLatin + "\\s{0,}\\([A-Z]{1}" + EngMixLatin + "\\)\\s{0,}"
					+ EngMixLatin + "\\s{0,}\\([A-Z]{1}(.*?)");

			// 5、subspecies 属 空格 种加词 空格 亚种加词 空格 命名信息首字母大写

			regExlist.put("subspecies-0", "^[A-Z]{1}" + EngMixLatin + "\\s{1,}" + EngMixLatin + "\\s{1,}" + EngMixLatin
					+ "\\s{1,}[A-Z]{1}(.*?)");
			// 6、subspecies 属 空格 种加词 空格 亚种加词 空格可忽略：
			regExlist.put("subspecies-1",
					"^[A-Z]{1}" + EngMixLatin + "\\s{1,}" + EngMixLatin + "\\s{1,}" + EngMixLatin + "\\s{0,}：(.*?)");
			// 7、subspecies 属(亚属)种加词 亚种加词：
			regExlist.put("subspecies-2", "^[A-Z]{1}" + EngMixLatin + "\\s{0,}\\([A-Z]{1}" + EngMixLatin + "\\)\\s{0,}"
					+ EngMixLatin + "\\s{1,}" + EngMixLatin + "：(.*?)");
			// 8、subspecies 属 空格 种加词 空格 亚种加词 空格可忽略(命名信息首字母大写)
			regExlist.put("subspecies-3", "^[A-Z]{1}" + EngMixLatin + "\\s{1,}" + EngMixLatin + "\\s{1,}" + EngMixLatin
					+ "\\s{0,}\\([A-Z]{1}(.*?)");
			// 15 subspecies 属 空格 种加词 空格 亚种加词 空格可忽略 中文命名信息
			regExlist.put("subspecies-4", "^[A-Z]{1}" + EngMixLatin + "\\s{1,}" + EngMixLatin + "\\s{1,}" + EngMixLatin
					+ "\\s{0,}" + multChinese + "(.*?)");

			// 11、var 属 空格 种加词 空格可忽略 var. 空格 亚种加词 空格可忽略(命名信息首字母大写
			regExlist.put("var-0", "^[A-Z]{1}" + EngMixLatin + "\\s{1,}" + EngMixLatin + "\\s{0,}var.\\s{1,}"
					+ EngMixLatin + "\\s{0,}\\((.*?)");
			// 12 var 属 空格 种加词 空格可忽略 var. 空格 亚种加词 空格 命名信息首字母大写
			regExlist.put("var-1", "^[A-Z]{1}" + EngMixLatin + "\\s{1,}" + EngMixLatin + "\\s{0,}var.\\s{1,}"
					+ EngMixLatin + "\\s{1,}[A-Z]{1,}(.*?)");

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
