package org.big.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.validation.ValidationException;

import org.apache.commons.lang.StringUtils;
import org.big.common.CommUtils;
import org.big.config.HttpConfig;
import org.big.constant.ConfigConsts;
import org.big.constant.MapConsts;
import org.big.entityVO.RankEnum;
import org.big.entityVO.SpeciesCatalogueEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class ToolServiceImpl implements ToolService {
	private final static Logger logger = LoggerFactory.getLogger(ToolServiceImpl.class);

	private volatile Map<String, String> regExlist = new HashMap<>();

	@Autowired
	private RestTemplate restTemplate;

	@SuppressWarnings("static-access")
	@Async
	public void asy(int i) {

		logger.info("线程" + Thread.currentThread().getName() + " 执行异步任务：" + i);
		try {
			Thread.currentThread().sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("线程" + Thread.currentThread().getName() + " 执行异步任务结束：" + i);
	}

	public String replaceAllChar(String line, String expression, String newChar) {
		line = line.replaceAll(expression, newChar).trim();
		return line;

	}

	public int getUpperCaseCount(String str) {
		int count = 0;
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			if (Character.isUpperCase(ch)) {
				count++;
			}
		}
		return count;
	}

	@Override
	public int getSecondUpperCaseIndex(String line) {
		int index = -1;
		int count = 0;
		for (int i = 0; i < line.length(); i++) {
			char charAt = line.charAt(i);
			index++;
			if (charAt >= 'A' && charAt <= 'Z') {
				count++;
				if (count == 2) {
					break;
				}
			}
		}
		return index;
	}

	@Override
	public int countTargetStr(String line, String target) {// 查找字符串里与指定字符串相同的个数
		int n = 0;// 计数器
		while (line.indexOf(target) != -1) {
			int i = line.indexOf(target);
			n++;
			line = line.substring(i + 1);
		}
		return n;
	}

	/**
	 * @Description通过反射机制，更改属性值 title: ToolServiceImpl.java
	 * @param model
	 * @param oldChar
	 * @param newChar
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 * @author ZXY
	 */
	public void reflectChangeValue(Object model, String oldChar, String newChar)
			throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		// 获取实体类的所有属性，返回Field数组
		Field[] field = model.getClass().getDeclaredFields();
		// 获取属性的名字
		String[] modelName = new String[field.length];
		String[] modelType = new String[field.length];
		for (int i = 0; i < field.length; i++) {
			// 获取属性的名字
			String name = field[i].getName();
			modelName[i] = name;
			// 获取属性类型
			String type = field[i].getGenericType().toString();
			modelType[i] = type;
			// 关键.可访问私有变量
			field[i].setAccessible(true);
			// 将属性的首字母大写
			name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
			if (type.equals("class java.lang.String")) {
				// 如果type是类类型，则前面包含"class "，后面跟类名
				Method m = model.getClass().getMethod("get" + name);
				// 调用getter方法获取属性值
				String value = (String) m.invoke(model);
				if (value != null && value.equals(oldChar)) {
					// 赋值
					field[i].set(model, field[i].getType().getConstructor(field[i].getType()).newInstance(newChar));
				}

			}
			if (type.equals("class java.lang.Integer")) {
				Method m = model.getClass().getMethod("get" + name);
				Integer value = (Integer) m.invoke(model);
				if (value != null) {
					// do something
				}
			}
			if (type.equals("class java.lang.Short")) {
				Method m = model.getClass().getMethod("get" + name);
				Short value = (Short) m.invoke(model);
				if (value != null) {
					// do something
				}
			}
			if (type.equals("class java.lang.Double")) {
				Method m = model.getClass().getMethod("get" + name);
				Double value = (Double) m.invoke(model);
				if (value != null) {
					// do something
				}
			}
			if (type.equals("class java.lang.Boolean")) {
				Method m = model.getClass().getMethod("get" + name);
				Boolean value = (Boolean) m.invoke(model);
				if (value != null) {
					// do something
				}
			}
			if (type.equals("class java.util.Date")) {
				Method m = model.getClass().getMethod("get" + name);
				Date value = (Date) m.invoke(model);
				if (value != null) {
					// do something
				}
			}
		}
	}

	/**
	 * @Description打印实体中的所有属性值 title: ToolServiceImpl.java
	 * @param model
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @author ZXY
	 */
	public void printEntity(Object model) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		StringBuffer sb = new StringBuffer();
		// 获取实体类的所有属性，返回Field数组
		Field[] field = model.getClass().getDeclaredFields();
		// 获取属性的名字
		String[] modelName = new String[field.length];
		String[] modelType = new String[field.length];
		for (int i = 0; i < field.length; i++) {
			// 获取属性的名字
			String name = field[i].getName();
			modelName[i] = name;
			// 获取属性类型
			String type = field[i].getGenericType().toString();
			modelType[i] = type;
			// 关键.可访问私有变量
			field[i].setAccessible(true);
			// 将属性的首字母大写
			String orignName = name;
			name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
			if (name.equals("SerialVersionUID")) {
				continue;
			}
			Method m = null;
			try {
				m = model.getClass().getMethod("get" + name);
			} catch (NoSuchMethodException e) {
				try {
					m = model.getClass().getMethod("get" + orignName);
				} catch (NoSuchMethodException e1) {
					e1.printStackTrace();
				} catch (SecurityException e1) {
					e1.printStackTrace();
				}
			}
			// 调用getter方法获取属性值
			Object value = (Object) m.invoke(model);
			if (CommUtils.isStrNotEmpty(String.valueOf(value))) {
				sb.append("[" + name + "=" + value + "] ");
			} else {
				sb.append("[" + name + "=" + null + "] ");
			}

		}

		logger.info(sb.toString());
	}

	/**
	 * @Description实体中的属性是否都为空(包含多个空格也算空)，都为空返回true,否则返回false
	 * @Description无法处理boolean类型 title: CommUtils.java
	 * @param model
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @author ZXY
	 */
	public boolean EntityAttrNull(Object model) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		boolean entityAttrNull = true;
		// 获取实体类的所有属性，返回Field数组
		Field[] field = model.getClass().getDeclaredFields();
		// 获取属性的名字
		String[] modelName = new String[field.length];
		String[] modelType = new String[field.length];
		for (int i = 0; i < field.length; i++) {
			// 获取属性的名字
			String name = field[i].getName();
			modelName[i] = name;
			// 获取属性类型
			String type = field[i].getGenericType().toString();
			modelType[i] = type;
			// 关键.可访问私有变量
			field[i].setAccessible(true);
			// 将属性的首字母大写
			name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
			if (name.equals("SerialVersionUID")) {
				continue;
			}
			Method m = model.getClass().getMethod("get" + name);
			// 调用getter方法获取属性值
			Object value = (Object) m.invoke(model);
			if (CommUtils.isStrNotEmpty(String.valueOf(value))) {
				entityAttrNull = false;
			}

		}
		return entityAttrNull;
	}

	public SpeciesCatalogueEnum judgeIsWhat(String line, int rowNum) {
		SpeciesCatalogueEnum result = SpeciesCatalogueEnum.unknown;
		int i = 0;
		String errorMessage = "";
		// superfamily 总科
		if (line.contains("总科")) {
			i = i + 1;
			result = SpeciesCatalogueEnum.superfamily;
			errorMessage = errorMessage + "||" + SpeciesCatalogueEnum.superfamily.getName();
		}
		// family 科
		if (line.contains("科") && !line.contains("亚科") && !line.contains("总科") && !line.contains("(")
				&& !line.startsWith("分布") && !CommUtils.isStartWithNum(line)) {
			i = i + 1;
			result = SpeciesCatalogueEnum.family;
			errorMessage = errorMessage + "||" + SpeciesCatalogueEnum.family.getName();
		}
		// 亚科
		if (line.contains("亚科") && !line.contains("总")) {
			i = i + 1;
			result = SpeciesCatalogueEnum.subfamily;
			errorMessage = errorMessage + "||" + SpeciesCatalogueEnum.subfamily.getName();
		}
		// 族
		if (line.contains("族") && !line.contains("亚族") && !line.startsWith("(")) {
			i = i + 1;
			result = SpeciesCatalogueEnum.tribe;
			errorMessage = errorMessage + "||" + SpeciesCatalogueEnum.tribe.getName();
		}
		// 亚族
		if (line.contains("亚族")) {
			i = i + 1;
			result = SpeciesCatalogueEnum.subtribe;
			errorMessage = errorMessage + "||" + SpeciesCatalogueEnum.subtribe.getName();
		}
		// genus 属(有属字，并且数字开头)
		if (line.contains("属") && CommUtils.isStartWithNum(line) && !line.contains("亚属")) {
			i = i + 1;
			result = SpeciesCatalogueEnum.genus;
			errorMessage = errorMessage + "||" + SpeciesCatalogueEnum.genus.getName();
		}
		// 亚属
		if (line.contains("亚属")) {
			i = i + 1;
			result = SpeciesCatalogueEnum.subgenus;
			errorMessage = errorMessage + "||" + SpeciesCatalogueEnum.subgenus.getName();
		}
		// species 种 (符合以"(数字)"开头的格式)
		if (CommUtils.isStartWithSeq(line) && !line.contains("亚种")) {
			i = i + 1;
			result = SpeciesCatalogueEnum.species;
			errorMessage = errorMessage + "||" + SpeciesCatalogueEnum.species.getName();
		}
		// 亚种
		if (line.contains("亚种")) {
			i = i + 1;
			result = SpeciesCatalogueEnum.subspecies;
			errorMessage = errorMessage + "||" + SpeciesCatalogueEnum.subspecies.getName();
		}
		// 引证 citation
		if (CommUtils.isStartWithEnglish(line) && line.endsWith(".")) {
			i = i + 1;
			result = SpeciesCatalogueEnum.citation;
			errorMessage = errorMessage + "||" + SpeciesCatalogueEnum.citation.getName();
			if (line.length() < 37) {
				result = SpeciesCatalogueEnum.unknown;
				logger.info(rowNum + " || citation 长度不够 = " + line + "||" + line.length());
			}
		}
		// 分布 distributiondata
		if (line.startsWith("分布")) {
			i = i + 1;
			result = SpeciesCatalogueEnum.distributiondata;
			errorMessage = errorMessage + "||" + SpeciesCatalogueEnum.distributiondata.getName();
		}

		if (i > 1) {
			result = SpeciesCatalogueEnum.unknown;
			logger.info("(error 000M Mutil definded)errorMessage:" + errorMessage + "\t||\tline = " + line);
		}
		// 引证
		if (result == SpeciesCatalogueEnum.unknown) {
			if (CommUtils.isStartWithEnglish(line)) {
				result = SpeciesCatalogueEnum.citation;
			} else {
				logger.info("unknown line = " + line);
			}
		}

		return result;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Annotation getClassAnnotation(Class<?> cla, Class annotationClass) {
		Annotation annotation = cla.getAnnotation(annotationClass);
		return annotation;
	}

	@Override
	public String getSciNameFromCitation(String line, int count) {
		int index = 0;
		int flagCount = 0;
		for (int i = 0; i < line.length(); i++) {
			String str = String.valueOf(line.charAt(i));
			index++;
			if (str.equals(" ") || str.equals("：") || str.equals(":") || str.equals("(") || str.equals("（")
					|| str.equals("{") || str.equals(",") || str.equals("，")) {
				flagCount++;
				if (count == flagCount) {
					return line.substring(0, index - 1);
				}
			}
		}
		return line;
	}

	@Override
	public String getYear(String line) {
		int spos = getYearStart(line);
		if (spos == -1) {
			return null;
		}
		String year = "";
		try {
			year = line.substring(spos, spos + 4).trim();
			String title = line.substring(spos + 4).trim();
			if (title.startsWith("-")) {
				year = line.substring(spos, spos + 9).trim();
				title = line.substring(spos + 10).trim();
			} else {
				title = line.substring(spos + 5).trim();
			}

			if (title.startsWith(".")) {
				title = line.substring(spos + 6).trim();
				year = line.substring(spos, spos + 6).trim();
			}
			year = year.replace(".", "");
		} catch (Exception e) {

		}
		return year;
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

	public List<String> readDoc(String path) throws SQLException {
		// 替换特殊字符，斜杠不能出现在restful api路径中
		path = path.replace("\\", "=");
		// 读取文件
		String colchinaUrl = HttpConfig.getInstance().get("COLCHINA");
		String url = colchinaUrl + "getDocFileContent/" + path;

		ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
		String jsonStr = results.getBody();
		JSONObject obj = new JSONObject().parseObject(jsonStr);// 将json字符串转换为json对象
		if (Integer.parseInt(obj.get("errcode").toString()) != 200) {
			throw new ValidationException(ConfigConsts.READ_FILE_ERROR + ",读取word文件出错：" + obj.get("errmsg").toString());
		}
		List<String> readByLine = JSONArray.parseArray(obj.get("p2pdata").toString(), String.class);
		return readByLine;
	}

	@Override
	public int getPointUpperCaseIndex(String line, int point) {
		int index = -1;
		int count = 0;
		for (int i = 0; i < line.length(); i++) {
			char charAt = line.charAt(i);
			index++;
			if (charAt >= 'A' && charAt <= 'Z') {
				count++;
				if (count == point) {
					break;
				}
			}
		}
		return index;
	}

	@Override
	public Map<String, String> parseSciName(String line) {
		Map<String, String> map = new HashMap<>();
		String sciname = "";
		boolean match = false;
		RankEnum rank = RankEnum.unknown;
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
					rank = RankEnum.genus;
					sciname = line.substring(0, getPointUpperCaseIndex(line, 2));
				} else if (key.equals("genus-1")) {
					rank = RankEnum.genus;
					sciname = line.substring(0, IndexOfFirstChinese(line));
				} else if (key.equals("species-0")) {
					rank = RankEnum.species;
					sciname = line.substring(0, getPointUpperCaseIndex(line, 2));
				} else if (key.equals("species-1")) {
					rank = RankEnum.species;
					sciname = line.substring(0, line.indexOf("("));
				} else if (key.equals("species-2")) {
					rank = RankEnum.species;
					sciname = line.substring(0, line.indexOf("："));
				} else if (key.equals("species-3")) {
					rank = RankEnum.species;
					sciname = line.substring(0, line.indexOf("："));
				} else if (key.equals("species-4")) {
					rank = RankEnum.species;
					sciname = line.substring(0, getPointUpperCaseIndex(line, 3));
				} else if (key.equals("species-5")) {
					rank = RankEnum.species;
					sciname = line.substring(0, line.indexOf("（"));
				} else if (key.equals("species-6")) {
					rank = RankEnum.species;
					sciname = line.substring(0, getPointUpperCaseIndex(line, 2));
				} else if (key.equals("species-7")) {
					rank = RankEnum.species;
					sciname = line.substring(0, IndexOfFirstChinese(line));
				} else if (key.equals("species-8")) {
					rank = RankEnum.species;
					sciname = line.substring(0, getPointUpperCaseIndex(line, 3) - 1);
				} else if (key.equals("species-9")) {
					rank = RankEnum.species;
					sciname = line.substring(0, IndexOfFirstChinese(line));
				} else if (key.equals("species-10")) {
					rank = RankEnum.species;
					sciname = line.substring(0, line.indexOf(":"));
				} else if (key.equals("species-11")) {
					rank = RankEnum.species;
					sciname = line.substring(0, line.indexOf("("));
				} else if (key.equals("subspecies-0")) {
					rank = RankEnum.subsp;
					sciname = line.substring(0, getPointUpperCaseIndex(line, 2));
				} else if (key.equals("subspecies-1")) {
					rank = RankEnum.subsp;
					sciname = line.substring(0, line.indexOf("："));
				} else if (key.equals("subspecies-2")) {
					sciname = line.substring(0, line.indexOf("："));
				} else if (key.equals("subspecies-3")) {
					rank = RankEnum.subsp;
					sciname = line.substring(0, line.indexOf("("));
				} else if (key.equals("subspecies-4")) {
					rank = RankEnum.subsp;
					sciname = line.substring(0, IndexOfFirstChinese(line));
				} else if (key.equals("subspecies-5")) {
					rank = RankEnum.subsp;
					sciname = line.substring(0, getPointUpperCaseIndex(line, 3));
				} else if (key.equals("subspecies-6")) {
					rank = RankEnum.subsp;
					sciname = line.substring(0, IndexOfFirstChinese(line));
				} else if (key.equals("var-0")) {
					sciname = line.substring(0, line.indexOf("("));
				} else if (key.equals("var-1")) {
					rank = RankEnum.var;
					sciname = line.substring(0, getPointUpperCaseIndex(line, 2));
				} else if (key.equals("var-2")) {
					rank = RankEnum.var;
					sciname = line.substring(0, line.indexOf("："));
				} else {
					throw new ValidationException("未定义的key:" + key);
				}
				match = true;
				break;
			}
		}
		if (!match) {
			int indexOfPointUpperCaseWithoutBrackets = indexOfPointUpperCaseWithoutBrackets(line, 2);
			
			if (line.contains("：")) {
				sciname = line.substring(0, line.indexOf("："));
//				logger.info(indexOfPointUpperCaseWithoutBrackets+"__"+sciname+"_____使用：分隔_____" + line);
			} else if (indexOfPointUpperCaseWithoutBrackets != line.length() - 1) {
				sciname = line.substring(0, indexOfPointUpperCaseWithoutBrackets);
				logger.info(sciname + "_____使用第二个大写字母（不包含括号内的）分隔_____" + line);
			} else {
				logger.info("没有匹配：" + line);
			}

		}
		String author = "";// 命名人和年代
		if(StringUtils.isNotEmpty(sciname)) {
			String year = getYear(line);
			if(StringUtils.isNotEmpty(year)) {
				String exceptSciname = line.substring(sciname.length());
				author = exceptSciname.substring(0,exceptSciname.indexOf(year)+year.length());
			}
		}

		map.put(MapConsts.TAXON_SCI_NAME, sciname);
		map.put(MapConsts.TAXON_AUTHOR, author);
		map.put(MapConsts.TAXON_RANK_NAME, rank.getName());
		return map;
	}

	// 项目启动后执行
	@PostConstruct
	public void initregExlist() {
//		regExlist.clear();
		// 小写字母或拉丁文或-
		String EngMixLatinMixShortLine = "(-|[a-z]|[\u00A0-\u00FF]|[\u0100-\u017F]|[\u0180-\u024F])+";
		// 小写字母或拉丁文
		String EngMixLatin = "([a-z]|[\u00A0-\u00FF]|[\u0100-\u017F]|[\u0180-\u024F])+";
		String multChinese = "[\\u4e00-\\u9fa5]+";
		if (regExlist.size() == 0) {
			// 4、genus 属 空格 命名信息首字母大写
			regExlist.put("genus-0", "^[A-Z]{1}" + EngMixLatin + "\\s{1,}[A-Z]{1}(.*?)");
			// genus 属 中文命名信息
			regExlist.put("genus-1", "^[A-Z]{1}" + EngMixLatin + "\\s{0,}" + multChinese + "(.*?)");
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
			// speciese 属(亚属)种加词 中文命名信息
			regExlist.put("species-9", "^[A-Z]{1}" + EngMixLatin + "\\s{0,}\\([A-Z]{1}" + EngMixLatin + "\\)\\s{0,}"
					+ EngMixLatin + "\\s{0,}" + multChinese + "(.*?)");
			// speciese 属 空格 种加词:
			regExlist.put("species-10", "^[A-Z]{1}" + EngMixLatin + "\\s{1,}" + EngMixLatin + "\\s{0,}:(.*?)");
			// speciese属 空格 种加词可能包含- (命名信息首字母大写
			regExlist.put("species-11",
					"^[A-Z]{1}[a-z]{1,}\\s{1,}" + EngMixLatinMixShortLine + "\\s{0,}\\([A-Z]{1,}(.*?)");
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
			// subspecies 属(亚属)种加词 空格 亚种加词 空格 命名信息首字母大写
			regExlist.put("subspecies-5", "^[A-Z]{1}" + EngMixLatin + "\\s{0,}\\([A-Z]{1}" + EngMixLatin + "\\)\\s{0,}"
					+ EngMixLatin + "\\s{1,}" + EngMixLatin + "\\s{1,}[A-Z]{1,}(.*?)");
			// subspecies 属(亚属)种加词 空格 亚种加词 中文命名信息
			regExlist.put("subspecies-6", "^[A-Z]{1}" + EngMixLatin + "\\s{0,}\\([A-Z]{1}" + EngMixLatin + "\\)\\s{0,}"
					+ EngMixLatin + "\\s{1,}" + EngMixLatin + "\\s{0,}" + multChinese + "(.*?)");
			// 11、var 属 空格 种加词 空格可忽略 var. 空格 亚种加词 空格可忽略(命名信息首字母大写
			regExlist.put("var-0", "^[A-Z]{1}" + EngMixLatin + "\\s{1,}" + EngMixLatin + "\\s{0,}var.\\s{1,}"
					+ EngMixLatin + "\\s{0,}\\((.*?)");
			// 12 var 属 空格 种加词 空格可忽略 var. 空格 亚种加词 空格 命名信息首字母大写
			regExlist.put("var-1", "^[A-Z]{1}" + EngMixLatin + "\\s{1,}" + EngMixLatin + "\\s{0,}var.\\s{1,}"
					+ EngMixLatin + "\\s{1,}[A-Z]{1,}(.*?)");
			// var 属 空格 种加词 空格可忽略 var. 空格 亚种加词：
			regExlist.put("var-2", "^[A-Z]{1}" + EngMixLatin + "\\s{1,}" + EngMixLatin + "\\s{0,}var.\\s{1,}"
					+ EngMixLatin + "\\s{0,}：(.*?)");

		}
	}

	@Override
	public int IndexOfFirstChinese(String line) {
		// 找第一个汉字
		for (int index = 0; index <= line.length() - 1; index++) {
			// 将字符串拆开成单个的字符
			String w = line.substring(index, index + 1);
			if (w.compareTo("\u4e00") > 0 && w.compareTo("\u9fa5") < 0) {// \u4e00-\u9fa5 中文汉字的范围
				return index;
			}
		}
		return -1;
	}

	@Override
	public int indexOfPointUpperCaseWithoutBrackets(String line, int point) {
		int index = -1;
		int count = 0;
		boolean meetBrackets = false;
		for (int i = 0; i < line.length(); i++) {
			char charAt = line.charAt(i);
			index++;
			if (charAt == '(') {
				meetBrackets = true;
			} else if (charAt == ')') {
				meetBrackets = false;
			}
			if (meetBrackets == false && charAt >= 'A' && charAt <= 'Z') {
				count++;
				if (count == point) {
					break;
				}
			}
		}
		return index;
	}

	@Override
	public int IndexOfFirstEng(String line) {
		// 找第一个英文
		for (int index = 0; index <= line.length() - 1; index++) {
			// 将字符串拆开成单个的字符
			String w = line.substring(index, index + 1);
			if (CommUtils.isEnglish(w)) {// \u4e00-\u9fa5 中文汉字的范围
				return index;
			}
		}
		return -1;
	}

	@Override
	public int IndexOfFirstNum(String line) {
		// 找第一个数字
		for (int index = 0; index <= line.length() - 1; index++) {
			// 将字符串拆开成单个的字符
			String w = line.substring(index, index + 1);
			if (CommUtils.isNumeric(w)) {// \u4e00-\u9fa5 中文汉字的范围
				return index;
			}
		}
		return -1;
	}

}
