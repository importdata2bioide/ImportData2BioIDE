package org.big.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.big.common.CommUtils;
import org.big.common.UUIDUtils;
import org.big.entity.Ref;
import org.big.entityVO.LanguageEnum;
import org.big.repository.RefRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * refs 导入参考文献 文献存在txt文件中
 * 
 * @author BIGIOZ
 *
 */
@Controller
public class RefsController {

	@Autowired
	private RefRepository refRepository;
	String inputer = "";

	List<Ref> reflist = new ArrayList<Ref>();

	@RequestMapping(value = "importRefs")
	public void ImportRefs(HttpServletResponse response, HttpServletRequest request)
			throws IOException {
		// 解析
		paresRefs(response, request);
		// 保存
		System.out.println("save 保存条数 ： " + reflist.size());
		refRepository.saveAll(reflist);
		System.out.println("save finish  ： " + reflist.size());

	}

	public void paresRefs(HttpServletResponse response, HttpServletRequest request)
			throws IOException {
		reflist.clear();
		String txtUrl = "";
		String code = "utf-8";
		List<String> txt = CommUtils.readTxt(txtUrl, code);

		System.out.println("txtUrl:" + txtUrl + " || 解析 txt 行数：" + txt.size());
		String line = "";

		for (int i = 0; i < txt.size(); i++) {
			try {
				Ref ref = new Ref();
				line = txt.get(i);
				int pos = line.indexOf("[");// 文献中是否中英混合
				String cnline = "";// 中文
				if (pos > -1 && isChineseChar(line.substring(pos + 1, pos + 2)))// 包含中文
				{
					cnline = line.substring(pos).replace("[", "");// 截取中文文献
					cnline = cnline.replace("]", "");
					line = line.substring(0, pos);// line剔除中文，只剩英文
				}

				// 解析英文文献
				if (CommUtils.isStrNotEmpty(line)) {
					parseLineChineseOrEng(ref, line);
					ref.setId(UUIDUtils.getUUID32());
					ref.setRefstr(line);
					ref.setLanguages(LanguageEnum.English.getName());
					reflist.add(CommUtils.initRef(ref));
					System.out
							.println("Eng： " + ref.getAuthor() + "\t||\t" + ref.getPyear() + "\t||\t" + ref.getTitle());// 控制台输出英文文献内容
				}
				// 解析中文文献
				if (!cnline.equals("")) {
					Ref refcn = new Ref();
					ref.setId(UUIDUtils.getUUID32());
					parseLineChineseOrEng(refcn, cnline);
					refcn.setRefstr(cnline);
					refcn.setLanguages(LanguageEnum.chinese.getName());
					reflist.add(CommUtils.initRef(refcn));
					System.out.println("chinese : " + refcn.getAuthor() + "\t||\t" + refcn.getPyear() + "\t||\t"
							+ refcn.getTitle());// 控制台输出中文文献内容
				}
			} catch (Exception e) {
				System.out.println("error line \t||\t" + line);
				e.printStackTrace();
			}
		}

	}

	/**
	 * parse 处理文献
	 * 
	 * @param ref
	 * @param line
	 * @param inputer
	 * @param relatedID
	 */
	public Map<String, String> parseLineChineseOrEng(Ref ref, String line){
		 Map<String, String> map = new HashMap<>();
		int spos = getYearStart(line);
		String author = null;
//		try {
		author = line.substring(0, spos).trim();
//		} catch (Exception e) {
//			System.out.println(line+"||"+spos);
//			e.printStackTrace();
//		}
		if (author.endsWith(",") || author.endsWith("，")) {
			author = author.substring(0, author.length() - 1);
		}
		String year = line.substring(spos, spos + 4).trim();
		String title = line.substring(spos + 4).trim();
		if (title.startsWith("-")) {
			year = line.substring(spos, spos + 9).trim();
			title = line.substring(spos + 10).trim();
		}
//    	else if(title.startsWith("."))
//    	{
//    		title=line.substring(spos+6).trim();
//    		year=line.substring(spos,spos+5).trim();
//    	}
		else {
			title = line.substring(spos + 5).trim();
		}

		if (title.startsWith(".")) {
			title = line.substring(spos + 6).trim();
			year = line.substring(spos, spos + 6).trim();
		}
		year = year.replace(".", "");

		String language = null;
		if (isChineseChar(author)) {
			language = String.valueOf(LanguageEnum.chinese.getIndex());
			author = author.replace(".", "");
		} else {
			author = author.replace(".", "");
			language = String.valueOf(LanguageEnum.English.getIndex());
		}
		if (ref != null) {
			ref.setAuthor(author);
			ref.setPyear(year);
			ref.setRemark(line);
			ref.setTitle(title);
			ref.setLanguages(language);
			if (CommUtils.isStrNotEmpty(inputer)) {
				ref.setInputer(inputer);
			}
		}
		map.put("author", author);
		map.put("year", year);
		map.put("line", line);
		map.put("title", title);
		map.put("language", language);
		map.put("inputer", inputer);
		return map;

	}

	/**
	 * chinese refs
	 * 
	 * @param line
	 * @return
	 */
	private String tmpAuthor = "";

	public void parseLine(Ref ref, String line, String inputer) {
		line = line.substring(line.indexOf("]") + 1).trim();
		int spos = getYearStart(line);
		// System.out.println(line);
		String author = line.substring(0, spos).trim();
		if (author.contains("——") || author.contains("--")) {
			author = tmpAuthor;
		} else
			tmpAuthor = author;
		String year = line.substring(spos, spos + 4).trim();
		String title = line.substring(spos + 4).trim();
		if (title.startsWith("-")) {
			year = line.substring(spos, spos + 10).trim();
			title = line.substring(spos + 10).trim();
		} else
			title = line.substring(spos + 5).trim();
		ref.setAuthor(author);
		ref.setPyear(year);
		ref.setRemark(line);
		ref.setTitle(title);
		ref.setId(UUIDUtils.getUUID32());
		ref.setInputer(inputer);
		if (isChineseChar(author)) {
			ref.setLanguages("中文");
		} else {
			ref.setLanguages("英文");
		}

	}

	public int getYearStart(String line) {
		int start = -1;
		for (int i = 0; i < line.length() - 4; i++) {
			String tmp = line.substring(i, i + 4);
			if (isNumeric(tmp)) {
				// System.out.println(tmp);
				start = i;
				break;
			}
		}
		return start;

	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	public boolean isChineseChar(String str) {
		boolean temp = false;
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			temp = true;
		}
		return temp;
	}

}
