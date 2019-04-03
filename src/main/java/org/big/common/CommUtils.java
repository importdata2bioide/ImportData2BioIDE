package org.big.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.big.entity.Ref;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class CommUtils {

	// 线程池核心线程数:20;线程池最大数:200;空闲线程存活时间:10s;时间单位:SECONDS;线程池所使用的缓冲队列20
	// 废弃，使用ThreadConfig替换
	public static ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 200, 10, TimeUnit.SECONDS,
			new ArrayBlockingQueue<Runnable>(20));

	public static String uploadPath = "upload/";// 文件在新采集系统的保存路径

	public static String imageUploadPath = uploadPath + "images/";// 文件在新采集系统的保存路径

	public static final String TAXON_REMARK_FROM_FILE = "originalText";
	public static final String TAXON_REMARK_PARENT_ID = "parentId";
	public static final String TAXON_REMARK_PARENT_NAME = "parentName";
	

	
	public static String cutNumber(String line) {
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(line);
		return m.replaceAll("").trim();
	}
	/**
	 * 
	 * @Description  计算指定字符在字符串中出现的次数
	 * @param src
	 * @param find
	 * @return
	 * @author ZXY
	 */
	public static int getOccur(String src, String find) {
		int num = 0;
		int index = -1;
		while ((index = src.indexOf(find, index)) > -1) {
			++index;
			++num;
		}
		return num;
	}

	/**
	 * 
	 * @Description 正则表达式匹配两个指定字符串中间的内容
	 * @param str
	 * @param rgex
	 * @return
	 * @author ZXY
	 */
	public static List<String> getSubUtil(String str, String rgex) {
		List<String> list = new ArrayList<String>();
		Pattern pattern = Pattern.compile(rgex);// 匹配的模式
		Matcher m = pattern.matcher(str);
		while (m.find()) {
			String group = m.group();
			list.add(group);
		}
		return list;
	}

	/**
	 * 
	 * @Description 第一个英文字母的位置
	 * @param line
	 * @return
	 * @author ZXY
	 */
	public static int indexOfFirstLetter(String line) {
		int index = -1;
		for (int i = 0; i < line.length(); i++) {
			char charAt = line.charAt(i);
			if (String.valueOf(charAt).matches("[a-zA-Z]")) {
				return index + 1;

			}
			index++;
		}
		return index;

	}

	public static String removeChinese(String line) {
		String REGEX_CHINESE = "[\u4e00-\u9fa5]";// 中文正则
		Pattern pat = Pattern.compile(REGEX_CHINESE);
		Matcher mat = pat.matcher(line);
		return mat.replaceAll("");
	}

	public static String getKeyString(Map<String, String> map) {
		StringBuffer str = new StringBuffer();
		for (Entry<String, String> entry : map.entrySet()) {
			str.append(entry.getKey() + "&");
		}
		String result = str.toString();
		if (result.endsWith("&")) {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}

	/**
	 * 
	 * @param chinese 汉字转为拼音
	 * @return
	 */
	public static String ToPinyin(String chinese) {
		String pinyinStr = "";
		char[] newChar = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < newChar.length; i++) {
			if (newChar[i] > 128) {
				try {
					pinyinStr += PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat)[0];
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pinyinStr += newChar[i];
			}
		}
		return pinyinStr;
	}

	/**
	 * 
	 * @Description 首字母大写
	 * @param name
	 * @return
	 * @author ZXY
	 */
	public static String captureName(String name) {
		if (StringUtils.isEmpty(name)) {
			return null;
		}
		name = name.trim();
		if (isStartWithEnglish(name)) {
			name = name.substring(0, 1).toUpperCase() + name.substring(1);
		}
		return name;

	}

	/**
	 * 
	 * @Description 去除数据的空格、回车、换行符、制表符
	 * @param str
	 * @return
	 * @author ZXY
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			// 空格\t、回车\n、换行符\r、制表符\t
			Pattern p = Pattern.compile("\\s*|\t|\r|\n|　| ");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	public static void printList(List<String> list) {
		StringBuffer sb = new StringBuffer();
		for (String str : list) {
			sb.append("[" + str + "] ");
		}
		System.out.println(sb.toString());
	}

	/**
	 * get fileType and count title: CommUtils.java
	 * 
	 * @param list
	 * @return
	 * @author ZXY
	 */
	public static Map<String, Integer> getSuffix(List<String> list) {
		Map<String, Integer> map = new HashMap<>();
		for (String str : list) {
			String suffix = str.substring(str.lastIndexOf("."), str.length());
			Integer count = map.get(suffix);
			if (count != null) {
				map.put(suffix, count + 1);
			} else {
				map.put(suffix, 1);
			}
		}
		Iterator<Map.Entry<String, Integer>> entries = map.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<String, Integer> entry = entries.next();
			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}
		return map;

	}

	/**
	 * title: CommUtils.java
	 * 
	 * @param localFile 本地文件（要上传的文件）
	 * @param realPath  图片的保存路径（绝对路径）
	 * @return
	 * @author ZXY
	 */
	public static String saveImage(File localFile, String realPath) {
		// localFile 转换成 MultipartFile
		MultipartFile file = null;
		try {
			FileInputStream in_file = new FileInputStream(localFile);
			file = new MockMultipartFile(localFile.getName(), in_file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (null == file || file.isEmpty()) {
			System.out.println("文件为空：" + localFile.getName());
		} else {
			// limit image size
			if (file.getSize() > 100 * 1024 * 1024) {
				System.out.println("文件超过限制大小：100 * 1024 * 1024 " + localFile.getName());
			}
			String name = localFile.getName();
			String[] split = name.split("\\.");
			String suffix = split[1]; // 后缀名
			String newFileName = UUIDUtils.getUUID32() + "." + suffix; // rename
			try {
				// save to local
				FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath, newFileName));
				return newFileName;
			} catch (Exception e1) {
				e1.printStackTrace();
				System.out.println("文件保存到本地发生异常:" + e1.getMessage() + localFile.getName());
			}
		}
		return null;
	}

	/**
	 * get all file title: CommUtils.java
	 * 
	 * @param path
	 * @author ZXY
	 */
	public static List<String> getAllFiles(String path, List<String> list) {
		if (list == null) {
			list = new LinkedList<>();
		}
		File file = new File(path);
		// 如果这个路径是文件夹
		if (file.isDirectory()) {
			// 获取路径下的所有文件
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				// 如果还是文件夹 递归获取里面的文件 文件夹
				if (files[i].isDirectory()) {
					// 目录
					getAllFiles(files[i].getPath(), list);
				} else {
					// 文件
					list.add(files[i].getPath());
				}

			}
		} else {
			list.add(file.getPath());
		}
		return list;
	}

	/**
	 * 利用正则表达式判断字符串是否是数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * get获取查询的字符串 将匹配的字符串取出 title: CommUtils.java
	 * 
	 * @param str
	 * @param regx
	 * @return
	 * @author ZXY
	 */
	public static List<String> getMatchGroup(String str, String regx) {
		List<String> list = new LinkedList<>();
		// 1.将正在表达式封装成对象Patten 类来实现
		Pattern pattern = Pattern.compile(regx);
		// 2.将字符串和正则表达式相关联
		Matcher matcher = pattern.matcher(str);
		// 3.String 对象中的matches 方法就是通过这个Matcher和pattern来实现的。
//		System.out.println(matcher.matches());
		// 查找符合规则的子串
		while (matcher.find()) {
			// 获取 字符串
			String group = matcher.group();
			list.add(group);
			// 获取的字符串的首位置和末位置
//			System.out.println(matcher.start() + "--" + matcher.end());
		}
		return list;
	}

	public static Ref initRef(Ref r) {
		r.setStatus(1);
		r.setInputer("95c24cdc24794909bd140664e2ee9c3b");// 王天山的账户
		r.setInputtime(getTimestamp("2018-11-16 01:01:01"));
		r.setSynchstatus(0);
		r.setSynchdate(getTimestamp("2018-11-16 01:01:01"));
		r.setRemark("中国鸟类分类与分布名录第三版参考文献");
		return r;
	}

	public static JSONObject strToJSONObject(String str) {
		if (isStrNotEmpty(str)) {
			return JSONObject.parseObject(str);
		}
		return new JSONObject();

	}

	public static String getCitationYear(String line) {
		if (isStrEmpty(line)) {
			return null;
		}
		int firstNumIndex = -1;
		// 第一个数字出现的位置
		for (int i = 0; i < line.length(); i++) {
			char a = line.charAt(i);
			if (a >= '1' && a <= '9') {
				firstNumIndex = i;
				break;
			}
		}
		// 截取后三位
		String year = null;
		try {
			year = line.substring(firstNumIndex, firstNumIndex + 4);
			Integer.parseInt(year);
		} catch (Exception e) {

		}
		return year;
	}

	/**
	 * 删除所有的标点符号和空格 title: CommUtils.java
	 * 
	 * @param str
	 * @return
	 * @author ZXY
	 */
	public static String replaceAllPunctuation(String str) {
		if (isStrEmpty(str)) {
			return "";
		} else {
			return str.replaceAll("[\\pP+~$`^=|<>～｀＄＾＋＝｜＜＞￥× ]", "");
		}
	}

	public static String handleDistributionLine(String line) {
		if (isStrEmpty(line)) {
			return "";
		} else {
			line = line.replace("分布", "");
			// replaceAll 的第一个参数是正则表达式，故而要经过两次转义，一次Java、一次正则。因此就需要四个反斜杠才可以匹配一个反斜杠
			line = line.replaceAll("[～｀＄＾＋＝｜￥×|，,.。；;：:?？\\\\]", "、").trim();
		}
		return line;

	}

	/**
	 * chinese 截取所有汉字
	 * 
	 * @param str
	 * @return
	 */
	public static String cutChinese(String str) {
		String reg = "[^\u4e00-\u9fa5]";
		str = str.replaceAll(reg, "");
		return str;
	}

	public static int getLastIndexOfChinese(String line) {
		int i = -1;
		for (int index = 0; index <= line.length() - 1; index++) {
			// 将字符串拆开成单个的字符
			String w = line.substring(index, index + 1);
			if (w.compareTo("\u4e00") > 0 && w.compareTo("\u9fa5") < 0) {// \u4e00-\u9fa5 中文汉字的范围
				i = index;
				System.out.println("第一个中文的索引位置:" + index + ",值是：" + w);
			} else {
				break;
			}
		}
		return i;
	}

	/**
	 * 判断字符串中是否包含中文
	 * 
	 * @param str 待校验字符串
	 * @return 是否为中文
	 * @warn 不能校验是否为中文标点符号
	 */
	public static boolean isContainChinese(String str) {
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

	/**
	 * 截取字符串指定字符之前(不包含)的所有
	 * 
	 * @param text
	 * @return
	 */
	public static String cutByStrBefore(String line, String str) {
		String result = "";
		if (isStrNotEmpty(line)) {
			if (line.indexOf(str) >= 0)
				result = line.substring(0, line.indexOf(str));
			else
				result = line;

		}
		return result;
	}

	/**
	 * 截取字符串指定字符之前(包含)的所有
	 * 
	 * @param text
	 * @return
	 */
	public static String cutByStrBeforeInclude(String line, String str) {
		String result = "";
		if (isStrNotEmpty(line)) {
			result = line.substring(0, line.indexOf(str) + str.length());
		}
		return result;
	}

	/**
	 * 截取字符串指定字符之后(不包含)的所有
	 * 
	 * @param text
	 * @return
	 */
	public static String cutByStrAfter(String line, String str) {
		String result = "";
		if (isStrNotEmpty(line)) {
			result = line.substring(line.indexOf(str) + str.length());
		}
		return result;
	}

	/**
	 * 截取字符串指定字符之后(包含)的所有
	 * 
	 * @param text
	 * @return
	 */
	public static String cutByStrAfterInclude(String text, String str) {
		String result = "";
		if (isStrNotEmpty(text)) {
			result = text.substring(text.indexOf(str));
		}
		return result;
	}

	/**
	 * 截取字符串的前几位
	 * 
	 * @param text
	 * @return
	 */
	public static String getChartASC(String line, int num) {
		String result = "";
		if (isStrNotEmpty(line)) {
			result = line.substring(0, num);
		}
		return result;
	}

	/**
	 * 首字母是否为英文
	 * 
	 * @param line
	 * @return
	 */
	public static boolean isStartWithEnglish(String line) {
		if (CommUtils.isStrEmpty(line)) {
			return false;
		}
		if (isEnglish(getChartASC(line, 1))) {
			return true;
		}
		return false;
	}

	public static boolean isEnglish(String text) {
		return text.matches("^[a-zA-Z]*");
	}

	/**
	 * isStartWithSeq 是否符合(1) (12) 开头
	 * 
	 * @param line
	 * @return
	 */
	public static boolean isStartWithSeq(String line) {
		Pattern pattern = Pattern.compile("^\\(\\d{1,}\\).*?");
		Matcher matcher = pattern.matcher(line);
		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * isStartWithNum 是否以数字开头
	 * 
	 * @param content
	 * @return
	 */
	public static boolean isStartWithNum(String line) {
		Pattern pattern = Pattern.compile("^(\\d+)(.*)");
		Matcher matcher = pattern.matcher(line);
		if (matcher.matches()) {// 数字开头
			return true;
		}
		return false;
	}

	public static boolean isStrNotEmpty(String str) {
		if (str != null && str.length() != 0 && !str.equals("") && !str.equals(" ")) {
			return true;
		}
		return false;
	}

	/**
	 * read txt
	 * 
	 * @param path 文件路径
	 * @param code 编码 GBK UTF-8...
	 * @return
	 * @throws IOException
	 */
	public static List<String> readTxt(String path, String code) throws IOException {
		/* 读取数据 */
		List<String> thisList = new ArrayList<>(1000);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)), code));
			String lineTxt = null;
			while ((lineTxt = br.readLine()) != null) {
				if (CommUtils.isStrEmpty(lineTxt)) {// 空白行
					continue;
				}
				if (CommUtils.isStrEmpty(lineTxt.trim())) {// 空白行
					continue;
				}
				lineTxt.replace("（", "(");
				lineTxt.replace("）", ")");
				thisList.add(lineTxt.trim());
			}

		} catch (Exception e) {
			System.err.println("CommUtils : read txt errors :" + e);
		} finally {
			if (br != null) {
				br.close();
			}

		}
		return thisList;
	}

	public static boolean isStrEmpty(String str) {
		if (str == null || str.length() == 0 || str.equals("") || str.equals(" ") || str.equals("  ")
				|| str.equals("   ")) {
			return true;
		}
		return false;

	}

	/**
	 * str to Timestamp
	 * 
	 * @param inputtimeStr
	 * @return
	 */
	public static Timestamp getTimestamp(String inputtimeStr) {
		Timestamp date = new Timestamp(System.currentTimeMillis());
		try {
			date = Timestamp.valueOf(inputtimeStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * str to java.sql.Date
	 * 
	 * @param inputtimeStr
	 * @return
	 * @throws ParseException
	 */
	public static Date getSqlDate(String inputtimeStr) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date d = null;
		try {
			d = format.parse(inputtimeStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		java.sql.Date date = new java.sql.Date(d.getTime());
		return date;

	}

	/**
	 * str to java.util.Date
	 * 
	 * @param inputtimeStr
	 * @return
	 * @throws ParseException
	 */
	public static java.util.Date getUtilDate(String inputtimeStr) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date d = null;
		try {
			d = format.parse(inputtimeStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return d;

	}

	/**
	 * yyyy-MM-dd HH:mm:ss格式（当前时间） title: CommUtils.java
	 * 
	 * @return
	 * @author ZXY
	 */
	public static String getCurrentDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String string = format.format(new java.util.Date());
		return string;
	}

	/**
	 * getCurrentDate(自定义格式，如 yyyy-MM-dd HH:mm:ss ) title: CommUtils.java
	 * 
	 * @param formatStr
	 * @return
	 * @author ZXY
	 */
	public static String getCurrentDate(String formatStr) {
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		String string = format.format(new java.util.Date());
		return string;
	}

	public static List<List<String>> groupList(List<String> all, int i) {
		int size = all.size();
		int groupCount = (int) Math.ceil(size / i);// 向上取整，只要有小数都+1
		List<List<String>> resultList = new ArrayList<>(groupCount + 20);
		if (size <= i) {
			resultList.add(all);
			return resultList;
		}
		List<String> part = new ArrayList<>(i + 20);
		// allFiles 拆分为多个数组
		for (String line : all) {
			part.add(line);
			if (part.size() == i) {
				resultList.add(part);
				part = new ArrayList<>(i + 20);
			}
		}
		if (part.size() > 0) {
			resultList.add(part);
		}
		return resultList;
	}
	
	

}
