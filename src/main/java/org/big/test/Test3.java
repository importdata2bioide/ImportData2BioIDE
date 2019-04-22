package org.big.test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.big.entity.Citation;
import org.big.entity.User;
import org.big.service.BirdAddDataImpl;
import org.big.service.ToolService;
import org.big.service.ToolServiceImpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Test3 {

	public static void main(String[] args) {
		String line = "admin.org";
		writeWithStyle(line);

	}

	private static void writeWithStyle(String line) {
		// 起始字符的性质
		if (StringUtils.isEmpty(line)) {
			return;
		}
		// 第一个中文的位置
		ToolService toolService = new ToolServiceImpl();
		int indexOfFirstChinese = toolService.IndexOfFirstBelongToChinese(line);
		// 第一个非中文的位置
		int indexOfFirstNotChinese = toolService.IndexOfFirstNotBelongToChinese(line);
		if(indexOfFirstChinese == -1 && indexOfFirstNotChinese == 0) {
			String notChinese = line;
			System.out.println("finally非中文:"+line);
		}else if(indexOfFirstChinese == 0 && indexOfFirstNotChinese == -1) {
			String chinese = line;
			System.out.println("finally中文:"+line);
		}else if (indexOfFirstChinese > indexOfFirstNotChinese) {// 非中文开头
			String eng = StringUtils.substring(line, indexOfFirstNotChinese, indexOfFirstChinese);
			if(StringUtils.isEmpty(eng)) {
				return;
			}
			System.out.println("非中文：" + eng);
			line = StringUtils.substring(line, indexOfFirstChinese);
			writeWithStyle(line);
		} else {
			String chinese = StringUtils.substring(line, indexOfFirstChinese, indexOfFirstNotChinese);
			if(StringUtils.isEmpty(chinese)) {
				return;
			}
			System.out.println("中文：" + chinese);
			line = StringUtils.substring(line, indexOfFirstNotChinese);
			writeWithStyle(line);
		}
	}

	public static boolean isChinese(String text) {
		String regEx = "[\\u4e00-\\u9fa5]+";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(text);
		if (m.find())
			return true;
		else
			return false;
	}

	private static void sortByEntityOrder() {
		Map<String, String> map = new TreeMap<>();
		map.put("ab", "石家庄");
		map.put("cb", "ts");
		map.put("kb", "he");
		map.put("db", "qhd");
		map.put("bb", "kjdsfk");
		map.put("ac", "石家庄");
		map.put("kc", "he");
		Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<String, String> entry = entries.next();
			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}

	}

	private static void test2() {
		String n = "dsfkjh.& khfd.Pontoppidan。1999";
		n = reppacePointToComma(n);
		System.out.println(n);

	}

	private static String reppacePointToComma(String content) {
		String pattern = "[0-9]{4}";
		Pattern compile = Pattern.compile(pattern);
		Matcher matcher = compile.matcher(content);
		if (matcher.find()) {
			String matcherline = matcher.group();// 提取匹配到的结果
			String butyear = content.substring(0, content.indexOf(matcherline)).trim();
			String lastChar = butyear.substring(butyear.length() - 1);
			System.out.println(lastChar);
			Pattern punctuation = Pattern.compile("\\pP");
			boolean find = punctuation.matcher(lastChar).find();
			if (find) {
				content = butyear.substring(0, butyear.length() - 1) + "," + matcherline;
			}
		}
		return content;
	}

	private static void testSort(int[] numbers) {
		int temp = 0;
		int size = numbers.length;
		for (int i = 0; i < size - 1; i++) {
			for (int j = 0; j < size - 1 - i; j++) {
				if (numbers[j] > numbers[j + 1]) // 交换两数位置
				{
					temp = numbers[j];
					numbers[j] = numbers[j + 1];
					numbers[j + 1] = temp;
				}
			}
		}
	}

	private static void test() {
		System.out.println(".".equals("."));

	}

	private static void containsTest() {
		String s1 = "Mntris, 1832";
		String s2 = "Mntris";
		System.out.println(s1.contains(s2));

	}

	private static void replaceAllTest() {
		String s = "石家庄。bb<br>的伤口缝合；舒服的和，";
		s = s.replaceAll("[。，<br>]", "");
		System.out.println(s);

	}

	private static void testclone() {
		Citation c = new Citation();
		c.setId("100");
		Citation copy = (Citation) c.clone();
		System.out.println(c.getId());
		System.out.println(copy.getId());
		c.setId("500");
		System.out.println(copy.getId());
		System.out.println(c.getId());
	}

	private static void testLinklist() {
		List<User> list = new LinkedList<>();
		for (int i = 0; i < 20; i++) {
			User u = new User();
			u.setId(i + "");
			list.add(u);
		}
		Iterator<User> iter = list.iterator();
		while (iter.hasNext()) {
			User user = iter.next();
			int id = Integer.parseInt(user.getId());
			if (id % 3 == 0) {
				iter.remove();
			}
		}

		for (User user : list) {
			System.out.println(user.getId());
		}

	}

	private static void completePPA() {
		BirdAddDataImpl b = new BirdAddDataImpl();
		String PatternProducingAreaReg = "(.*?)(\\(|\\（)模式产地(.*?)(\\)|\\）)";
		Pattern patternOfPPA = Pattern.compile(PatternProducingAreaReg);
		String citationstr = "Alaudala cheleensis,Swinhoe, 1871,PZS Pt2 p.390（模式产地：中国大连）";
		if (patternOfPPA.matcher(citationstr).matches()) {
			System.out.println("----" + citationstr);
			char[] flagsPPA = { '（', '（' };
			citationstr = citationstr.substring(0, b.getNotYearflagIndex(flagsPPA, citationstr));
			System.out.println("--------" + citationstr);
		}

	}

	/**
	 * 
	 * @Description 字符串转换成JsonArray
	 * @author ZXY
	 */
	private static void testJsonArray() {
		String line = "[{\"refE\": \" 0\", \"refS\": \" 0\", \"refId\": \"FB9F62AA-6BD7-4E6C-B9F0-3D5A6D925BF0\", \"refType\": \"0\"}, {\"refE\": \" 0\", \"refS\": \" 0\", \"refId\": \"43F930D6-45F4-4888-A865-39CC665C5CA1\", \"refType\": \"0\"}, {\"refE\": \" 0\", \"refS\": \" 0\", \"refId\": \"B3CC4E6E-2558-409B-B0BB-844A60F74654\", \"refType\": \"0\"}]";
		JSONArray jsonArray = JSONArray.parseArray(line);
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
			System.out.println(jsonObject.get("refId")); // 得到 每个对象中的属性值
		}

	}

}
