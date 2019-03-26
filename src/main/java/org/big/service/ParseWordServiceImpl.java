package org.big.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.big.common.CommUtils;
import org.big.common.LineAttreEnum;
import org.big.entity.Taxon;
import org.big.entityVO.BaseParamsForm;
import org.big.entityVO.LineStatus;
import org.big.entityVO.Other;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ParseWordServiceImpl implements ParseWordService {
	@Autowired
	private BatchSubmitService batchSubmitService;
	@Autowired
	private ParseLine parseLineFishWord;
	
	// 引证的最后一个字符
	Map<String, String> theLastCharacterMap = new HashMap<>();
	private int indentationLeft = 200;
	private int indentationHanging = 200;
	private int fontSize = 10;
	private String fontFamilyChinese = "宋体";
	private String fontFamily = "Calibri";
	int genusbeginCount = 1;
	int speciesbeginCount = 1;
	
	
	

	@Override
	public void readExcelAndOutputWord(BaseParamsForm baseParamsForm) throws Exception {
		boolean writeExecute = true;//重写到word
		boolean insertExecute = false;//存储到数据库
		String outputfolder = "E:\\003采集系统\\0013鱼类\\输出文件\\";
		String inputfolder = "E:\\003采集系统\\0013鱼类\\19-03-21-鱼类名录\\";
		String fileName = "3-名录-1盲鳗至鼠喜(伍审阅)-Shao Lab.doc";
//		String fileName = "3-名录-2鲤形目-狗鱼目.doc";
//		String fileName = "3-名录-3-巨口鱼-狮子鱼(伍审阅)_Shao Lab.doc";
//		String fileName = "3-名录-4鲈形目-虾虎鱼前(伍审阅)-Shao Lab.doc";
//		String fileName = "3-名录-5虾虎鱼-完(伍审阅)-Shao Lab.doc";
		readAndWrite(writeExecute, insertExecute, outputfolder, inputfolder, fileName,baseParamsForm);

	}

	public void readAndWrite(boolean execute, boolean insertExecute, String outputfolder, String inputfolder,
			String fileName,BaseParamsForm baseParamsForm) throws IOException {
		long maxMemory = Runtime.getRuntime().maxMemory();
		System.out.println("堆内存大小：" + maxMemory / 1024 / 1024 + "MB");
		XWPFDocument doc = null;
		OutputStream os = null;
		try {
			// 新建一个文档
			doc = new XWPFDocument();
			// 创建内容
			write(doc, inputfolder + fileName, execute, insertExecute,baseParamsForm);
			// 写文件
			if (execute) {
				os = new FileOutputStream(outputfolder + fileName);
				doc.write(os);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (doc != null) {
				doc.close();
			}
			if (os != null) {
				os.close();
			}
			System.out.println("执行结束");
		}
		System.out.println("控制台输出 引证的最后一个字符");
		Set<Entry<String, String>> entrySet = theLastCharacterMap.entrySet();
		for (Entry<String, String> entry : entrySet) {
			System.out.println(entry.getKey());
		}

	}

	@SuppressWarnings("unused")
	private void write(XWPFDocument doc, String path, boolean execute, boolean insertExecute,BaseParamsForm baseParamsForm) throws Exception {
		List<Taxon> taxonlist = new ArrayList<>();
		// 读取文件
		List<String> readByLine = readByLine(path);
		LineAttreEnum preAttr = null;
		Taxon preTaxon = null;
		Other other = null;
		// temporary 构建临时状态
		LineStatus thisLineStatus = new LineStatus();
		for (String line : readByLine) {
			if (StringUtils.isEmpty(line)) {
				continue;
			}
			//替换特殊字符
			line = replaceSpecialChar(line);
			LineAttreEnum currentAttr = isWhat(line, preAttr);
			switch (currentAttr) {
			case Class:// 纲
//				System.out.println(line);
				if (execute) {
					printNotRank(other, doc, preAttr);
					writeTitle(doc, line, true);
				}
				//解析当前行并转换成实体类
				if(insertExecute) {
					Taxon taxon = parseLineFishWord.parseClass(line,baseParamsForm,thisLineStatus);
					taxonlist.add(taxon);
					thisLineStatus.setClass_(taxon);
					thisLineStatus.clearUnderClass();
					preTaxon = taxon;
				}
				preAttr = LineAttreEnum.Class;
				break;
			case order:// 目
//				System.out.println(line);
				if (execute) {
					printNotRank(other, doc, preAttr);
					writeTitle(doc, line, true);
				}
				//解析当前行并转换成实体类
				if(insertExecute) {
					Taxon taxon = parseLineFishWord.parseOrder(line,baseParamsForm,thisLineStatus);
					taxonlist.add(taxon);
					thisLineStatus.setOrder(taxon);
					thisLineStatus.clearUnderOrder();
					preTaxon = taxon;
				}
				preAttr = LineAttreEnum.order;
				break;
			case family:// 科
//				System.out.println(line);
				if (execute) {
					printNotRank(other, doc, preAttr);
					writeTitle(doc, line, true);
				}
				if(insertExecute) {
					Taxon taxon = parseLineFishWord.parseFamily(line,baseParamsForm,thisLineStatus);
					taxonlist.add(taxon);
					thisLineStatus.setFamily(taxon);
					thisLineStatus.clearUnderFamily();
					preTaxon = taxon;
				}
				preAttr = LineAttreEnum.family;
				break;
			case subfamily://亚科
//				System.out.println(line);
				if (execute) {
					printNotRank(other, doc, preAttr);
					writeTitle(doc, line, true);
				}
				if(insertExecute) {
					Taxon taxon = parseLineFishWord.parseSubfamily(line,baseParamsForm,thisLineStatus);
					taxonlist.add(taxon);
					thisLineStatus.setFamily(taxon);
					thisLineStatus.clearUnderFamily();
					preTaxon = taxon;
				}
				preAttr = LineAttreEnum.subfamily;
				break;
			case genus:// 属
//				System.out.println(line);
				if (execute) {
					printNotRank(other, doc, preAttr);
					writeGenusWithStyle(doc, line);
//					writeTitle(doc, line, true);
				}
				preAttr = LineAttreEnum.genus;
				break;
			case subgenus:// 亚属
//				System.out.println(line);
				if (execute) {
					printNotRank(other, doc, preAttr);
					writeGenusWithStyle(doc, line);
//					writeTitle(doc, line, true);
				}
				preAttr = LineAttreEnum.subgenus;
				break;
			case species:// 种
//				System.out.println(line);
				if (execute) {
					printNotRank(other, doc, preAttr);
					writeSpeciesWithStyle(doc, line);
//					writeDesc(true, doc, true, line, indentationHanging, indentationLeft);
				}
				preAttr = LineAttreEnum.species;
				break;
			case ref:// 参考文献
//				System.out.println(line);
				if (other == null) {
					other = new Other();
				}
				other.setRef(line);
				break;
			case Distribute:// 分布
//				System.out.println(line);
				if (other == null) {
					other = new Other();
				}
				other.setDistribution(line);
				break;
			case protectLevel:// 保护等级
//				System.out.println(line);
				if (other == null) {
					other = new Other();
				}
				other.setProtectLevel(line);
				break;
			case commonName:// 俗名
//				System.out.println(line);
				if (other == null) {
					other = new Other();
				}
				other.setCommname(line);
				break;
			case ciation:// 引证
//				System.out.println(line);
				// 各条同物异名后加“.”；
				line = handCitationLine(line);
				List<String> clist = null;
				if (other == null) {
					other = new Other();
					clist = new ArrayList<>();
				} else {
					clist = other.getCitation();
				}
				if (clist == null) {
					clist = new ArrayList<>();
				}
				clist.add(line);
				other.setCitation(clist);
				try {
					theLastCharacterMap.put(line.substring(line.length() - 1), line);
				} catch (Exception e) {
					System.out.println(line);
					e.printStackTrace();
				}
				break;
			case minglu:// 名录
				writeTitle(doc, line, true);
				break;
			default:
				System.out.println(line);
				System.out.println("switchcase:" + line);
				break;
			}

		}
		printNotRank(other, doc, preAttr);
		if(baseParamsForm.isInsert()) {
			batchSubmitService.saveAll(taxonlist);
		}

	}
	

	

	private String replaceSpecialChar(String line) {
		line = line.replace("()", "").trim();
		line = line.replace("（）", "").trim();
		if (line.contains("")) {// 替换特殊字符
			line = line.replace("", "{").trim();
			line = line.replace("", "}").trim();
			if (line.endsWith("\"")) {
				line = line + " }";
			}
			if (line.startsWith("XE")) {
				line = "{" + line;
			}
		}//end if
		
		//删除{}中间的内容
		if(line.contains("{")) {
			String rgex = "\\{(.*?)\\}";  
			List<String> subUtil = CommUtils.getSubUtil(line, rgex);
			for (String str : subUtil) {
				String oldChar = "{"+str+"}";
				line = line.replace(oldChar, "");
			}
		}// end if 
		
		return line;
	}


	private String handCitationLine(String line) {
		String theLastCharacter = line.substring(line.length() - 1);
		if (theLastCharacter.equals("。") || theLastCharacter.equals(".")) {
			line = line.substring(0, line.length() - 1);// 删除最后一个句号
		}
		line = line.trim() + ".";
		return line;
	}

	/**
	 * 
	 * @Description 1. 各条同物异名后加“.”； 2. “文献(reference)：”移至同物异名下，或维持现状； 3.
	 *              “文献(reference)：”如有两行以上，第二行起（有的话）后退一格； 4. “别名(common
	 *              name)：”有两行以上，第二行开始也退一格，
	 * 
	 * @param other
	 * @param doc
	 * @author ZXY
	 * @throws Exception
	 */
	private void printNotRank(Other other, XWPFDocument doc, LineAttreEnum preAttr) throws Exception {
		if (other != null) {
			List<String> citation = other.getCitation();
			String ref = other.getRef();
			String commname = other.getCommname();
			String distribution = other.getDistribution();
			String protectLevel = other.getProtectLevel();
			// 引证
			if (citation != null && citation.size() > 0) {
				for (String line : citation) {
//					writeDesc(true, doc, false, line, 0, indentationLeft);
					writeCitationWithStyle(doc, line, preAttr);
				}
				other.setCitation(null);
			}
			// 文献
			if (StringUtils.isNotEmpty(ref)) {
				writeDesc(true, doc, false, ref, indentationHanging, indentationHanging + indentationLeft);
				other.setRef(null);
			}
			// 别名
			if (StringUtils.isNotEmpty(commname)) {
				writeDesc(true, doc, false, commname, indentationHanging, indentationHanging + indentationLeft);
				other.setCommname(null);
			}
			// 分布
			if (StringUtils.isNotEmpty(distribution)) {
				writeDesc(true, doc, false, distribution, 0, indentationLeft);
				other.setDistribution(null);
			}
			// 保护等级
			if (StringUtils.isNotEmpty(protectLevel)) {
				writeDesc(true, doc, false, protectLevel, 0, indentationLeft);
				other.setProtectLevel(null);
			}

		}

	}

	private LineAttreEnum isWhat(String line, LineAttreEnum preAttr) {
		if (line.startsWith("文献")) {
			return LineAttreEnum.ref;
		}
		if (line.startsWith("别名")) {
			return LineAttreEnum.commonName;
		}
		if (line.startsWith("分布")) {
			return LineAttreEnum.Distribute;
		}
		if (line.startsWith("保护等级") || line.startsWith("保护类型")) {
			return LineAttreEnum.protectLevel;
		}
		if (line.equals("名   录")) {
			return LineAttreEnum.minglu;
		}
		String chinese = CommUtils.cutChinese(line);
		if (chinese.endsWith("纲")) {
			return LineAttreEnum.Class;
		}
		if (chinese.endsWith("目")) {
			return LineAttreEnum.order;
		}
		if (chinese.contains("亚属")) {
			return LineAttreEnum.subgenus;
		}
		if (chinese.endsWith("属")) {
			return LineAttreEnum.genus;
		}
		if (line.contains("亚科")) {
			return LineAttreEnum.subfamily;
		}
		if (chinese.endsWith("科")) {
			return LineAttreEnum.family;
		}
		if (isEnglish(getChartASC(line, 2))) {
			return LineAttreEnum.ciation;
		}
		return LineAttreEnum.species;

	}

	/**
	 * 
	 * @Description 判断是否为中文
	 * @param text
	 * @return
	 * @author ZXY
	 */
	public boolean isChinese(String text) {
		String regEx = "[\\u4e00-\\u9fa5]+";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(text);
		if (m.find())
			return true;
		else
			return false;
	}

	/**
	 * 
	 * @Description 判断是否为英文
	 * @param text
	 * @return
	 * @author ZXY
	 */
	public boolean isEnglish(String text) {
		return text.matches("^[a-zA-Z]*");
	}

	/**
	 * 
	 * @Description 截取字符串的前几位
	 * @param text
	 * @param num
	 * @return
	 * @author ZXY
	 */
	public String getChartASC(String text, int num) {
		String result = "";
		if (StringUtils.isNotEmpty(text)) {
			result = text.substring(0, num);
		}
		return result;
	}

	@SuppressWarnings("resource")
	public List<String> readByLine(String path) {
		List<String> thisList = new ArrayList<>();
		try {
			InputStream is = new FileInputStream(new File(path));
			WordExtractor ex = new WordExtractor(is);
			// Line
			String paraTexts[] = ex.getParagraphText();
			for (int i = 0; i < paraTexts.length; i++) {
				String line = paraTexts[i];
				if (org.apache.commons.lang.StringUtils.isNotEmpty(line)) {
					thisList.add(i, line.trim());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return thisList;
	}

	public String getSciNameFromCitation(String line, int count) {
		int index = 0;
		int flagCount = 0;
		for (int i = 0; i < line.length(); i++) {
			String str = String.valueOf(line.charAt(i));
			index++;
			if (str.equals(" ") || str.equals("：") || str.equals(":") || str.equals("(")||str.equals("（") || str.equals("{")) {
				flagCount++;
				if (count == flagCount) {
					return line.substring(0, index - 1);
				}
			}
		}
		return line;
	}

	public void writeTitle(XWPFDocument doc, String title, boolean withStyle) {
		XWPFParagraph paragraph = doc.createParagraph();// 创建段落
		paragraph.setAlignment(ParagraphAlignment.CENTER);// 居中对齐
		if (withStyle) {
			writeWithStyle(paragraph, title, true, false);
		} else {
			XWPFRun run = paragraph.createRun();
			run.setFontFamily(fontFamily);// 字体
			run.setBold(true);// 加粗
			run.setFontSize(fontSize);// 字号
			run.setText(title);// 标题内容
		}

	}

	/**
	 * 
	 * @Description
	 * @param paragraph
	 * @param line
	 * @author ZXY
	 */
	private void writeWithStyle(XWPFParagraph paragraph, String line, boolean bold, boolean italic) {
		for (int i = 0; i < line.length(); i++) {
			String charAt = String.valueOf(line.charAt(i));
			if (isChinese(charAt)) {// 中文：宋体
				XWPFRun run = paragraph.createRun();
				run.setFontFamily(fontFamilyChinese);// 字体
				run.setBold(bold);// 加粗
				run.setFontSize(fontSize);// 字号
				run.setText(charAt);// 标题内容
			} else {// 其他：Calibri
				XWPFRun run = paragraph.createRun();
				run.setFontFamily(fontFamily);// 字体
				run.setBold(bold);// 加粗
				run.setFontSize(fontSize);// 字号
				run.setText(charAt);// 标题内容
				run.setItalic(italic);// 斜体（字体倾斜）
			}

		}

	}

	public void writeDesc(boolean withStyle, XWPFDocument doc, boolean bold, String text, int indentationHanging,
			int indentationLeft) {
		XWPFParagraph paragraph = doc.createParagraph();// 创建段落
		paragraph.setIndentationHanging(indentationHanging);// 首行前进,指定的缩进量，应通过第一行回到开始的文本流的方向上移动缩进从父段的第一行中删除。
		paragraph.setAlignment(ParagraphAlignment.LEFT);// 左对齐
		paragraph.setIndentationLeft(indentationLeft);
		if (withStyle) {
			writeWithStyle(paragraph, text, bold, false);
		} else {
			XWPFRun run = paragraph.createRun();
			run.setFontFamily(fontFamily);// 字体
			run.setBold(bold);// 加粗
			run.setFontSize(fontSize);// 字号
			run.setText(text);// 段落内容
		}

	}

	/**
	 * 
	 * @Description 属学名斜体
	 * @param doc
	 * @param line
	 * @author ZXY
	 */
	private void writeGenusWithStyle(XWPFDocument doc, String line) {
		String chname;
		String sciName;
		String author;
		int index = CommUtils.indexOfFirstLetter(line);// 第一个英文字母的位置
		chname = line.substring(0, index);
		String sciNameAndAuthor = line.substring(index);
		sciName = getSciNameFromCitation(sciNameAndAuthor, 1).trim();
		author = CommUtils.cutByStrAfter(sciNameAndAuthor, sciName);
		XWPFParagraph paragraph = doc.createParagraph();// 创建段落
		paragraph.setAlignment(ParagraphAlignment.CENTER);// 居中对齐
		//删除旧序号，添加新序号
		if(chname.contains(".")) {
			chname = chname.substring(chname.indexOf(".")+1);
		}
		chname = genusbeginCount+". "+chname.trim();
		genusbeginCount++;
		writeWithStyle(paragraph, chname, true, false);
		writeWithStyle(paragraph, sciName, true, true);
		writeWithStyle(paragraph, author, true, false);
//		System.out.println(chname+"_____"+sciName+"_____"+author);

	}

	/**
	 * 
	 * @Description 种学名斜体
	 * @param doc
	 * @param line
	 * @author ZXY
	 */
	private void writeSpeciesWithStyle(XWPFDocument doc, String line) {
//		System.out.println(line);
		String chname;
		String sciName;
		String author;
		int index = CommUtils.indexOfFirstLetter(line);// 第一个英文字母的位置
		chname = line.substring(0, index);
		String sciNameAndAuthor = line.substring(index);
		sciName = getSciNameFromCitation(sciNameAndAuthor, 2).trim();
		author = CommUtils.cutByStrAfter(sciNameAndAuthor, sciName);
		XWPFParagraph paragraph = doc.createParagraph();// 创建段落
		paragraph.setAlignment(ParagraphAlignment.LEFT);// 左对齐
		//删除旧序号，添加新序号
		System.out.println(chname);
		String rgex = "\\([0-9]*\\)|\\（[0-9]*\\）";  
		List<String> subUtil = CommUtils.getSubUtil(chname , rgex);
		for (String str : subUtil) {
			chname = chname.replace(str, "");
		}
		chname = "（"+speciesbeginCount+"）"+chname.trim();
		writeWithStyle(paragraph, chname, true, false);
		writeWithStyle(paragraph, sciName, true, true);
		writeWithStyle(paragraph, author, true, false);
	}

	/**
	 * 
	 * @Description 引证学名斜体
	 * @param doc
	 * @param line
	 * @author ZXY
	 * @throws Exception
	 */
	private void writeCitationWithStyle(XWPFDocument doc, String line, LineAttreEnum preAttr) throws Exception {
		String sciName = null;
		String other = null;
		switch (preAttr) {
		case genus:
			sciName = getSciNameFromCitation(line, 1).trim();
			break;
		case subgenus:
			sciName = getSciNameFromCitation(line, 1).trim();
			break;
		case species:
			sciName = getSciNameFromCitation(line, 2).trim();
			break;
		default:
			System.out.println(line);
			throw new Exception("未定义的preAttr:" + preAttr.getName());
		}
		other = CommUtils.cutByStrAfter(line, sciName).trim();
//		System.out.println(sciName+"___"+other);
		XWPFParagraph paragraph = doc.createParagraph();// 创建段落
		paragraph.setAlignment(ParagraphAlignment.LEFT);// 左对齐
		paragraph.setIndentationLeft(indentationLeft);
		writeWithStyle(paragraph, sciName + " ", false, true);
		writeWithStyle(paragraph, other, false, false);

	}

	@SuppressWarnings("unused")
	private void writetest(XWPFDocument doc) {
		XWPFParagraph paragraph = doc.createParagraph();// 创建段落
		paragraph.setIndentationHanging(indentationHanging);// 首行前进,指定的缩进量，应通过第一行回到开始的文本流的方向上移动缩进从父段的第一行中删除。
		paragraph.setAlignment(ParagraphAlignment.LEFT);// 左对齐
		paragraph.setIndentationLeft(indentationLeft + indentationHanging);
		XWPFRun runS = paragraph.createRun();
		runS.setFontFamily(fontFamily);// 字体
		runS.setFontSize(fontSize);// 字号
		runS.setText("段落标签");// 段落内容
		runS.setItalic(true);// 斜体（字体倾斜）
		runS.setText("石家庄");// 段落内容
		runS.setItalic(false);// 斜体（字体倾斜）

	}

}
