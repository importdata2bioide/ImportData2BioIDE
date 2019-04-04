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

import javax.validation.ValidationException;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.big.common.CommUtils;
import org.big.common.LineAttreEnum;
import org.big.config.HttpConfig;
import org.big.constant.ConfigConsts;
import org.big.constant.DescTypeConsts;
import org.big.constant.MapConsts;
import org.big.entity.Citation;
import org.big.entity.Commonname;
import org.big.entity.Description;
import org.big.entity.Distributiondata;
import org.big.entity.Ref;
import org.big.entity.Taxon;
import org.big.entityVO.BaseParamsForm;
import org.big.entityVO.LineStatus;
import org.big.entityVO.Other;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class ParseWordServiceImpl implements ParseWordService {

	private final static Logger logger = LoggerFactory.getLogger(ParseWordServiceImpl.class);

	@Autowired
	private BatchSubmitService batchSubmitService;
	@Autowired
	private ParseLine parseLineFishWord;
	@Autowired
	private TaxtreeService taxtreeService;
	@Autowired
	private ToolService toolService;

	// 引证的最后一个字符
	Map<String, String> theLastCharacterMap = new HashMap<>();
	private int indentationLeft = 200;
	private int indentationHanging = 200;
	private int fontSize = 10;//磅
	private String fontFamilyChinese = "宋体";
	private String fontFamily = "Calibri";
	int genusbeginCount = 1;
	int speciesbeginCount = 1;
	int orderNum = 1;

	@Override
	public void readExcelAndOutputWord(BaseParamsForm baseParamsForm) throws Exception {
		boolean writeExecute = true;// 重写到word
		boolean insertExecute = false;// 转换成实体类
		String outputfolder = "E:\\003采集系统\\0013鱼类\\20190402输出文件\\";
		String inputfolder = "E:\\003采集系统\\0013鱼类\\20190402\\";
		List<String> fileList = new ArrayList<>();
		fileList.add("3-名录-1盲鳗至鼠喜(伍审阅)-Shao Lab.doc");
		fileList.add("3-名录-2鲤形目-狗鱼目.doc");
		fileList.add("3-名录-3-巨口鱼-狮子鱼(伍审阅)_Shao Lab.doc");
		fileList.add("3-名录-4鲈形目-虾虎鱼前(伍审阅)-Shao Lab.doc");
		fileList.add("3-名录-5虾虎鱼-完(伍审阅)-Shao Lab.doc");
		for (String fileName : fileList) {
			logger.info("操作的word是：" + fileName);
			// 设置datasourceId
			if (insertExecute) {
				setDataSource(baseParamsForm, fileName);
			}
			readAndWrite(writeExecute, insertExecute, outputfolder, inputfolder, fileName, baseParamsForm);
		}
		// 5本书全部操作完，初始化参数
		genusbeginCount = 1;
		speciesbeginCount = 1;
		orderNum = 1;
		// 更新分类树
//		logger.info("开始更新分类树preTaxon");
//		taxtreeService.updatePreTaxonByOrderNum(baseParamsForm.getmTaxtreeId());
	}

	public void readAndWrite(boolean execute, boolean insertExecute, String outputfolder, String inputfolder,
			String fileName, BaseParamsForm baseParamsForm) throws IOException {
		long maxMemory = Runtime.getRuntime().maxMemory();
		System.out.println("堆内存大小：" + maxMemory / 1024 / 1024 + "MB");
		XWPFDocument doc = null;
		OutputStream os = null;
		try {
			// 新建一个文档
			doc = new XWPFDocument();
			// 创建内容
			write(doc, inputfolder + fileName, execute, insertExecute, baseParamsForm);
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
	private void write(XWPFDocument doc, String path, boolean execute, boolean insertExecute,
			BaseParamsForm baseParamsForm) throws Exception {
		List<Taxon> taxonlist = new ArrayList<>();
		List<Citation> citationlist = new ArrayList<>();
		List<Description> desclist = new ArrayList<>();
		List<Commonname> commnamelist = new ArrayList<>();
		List<Distributiondata> distributionlist = new ArrayList<>();
		List<Ref> reflist = new ArrayList<>();
		List<String> readByLine = toolService.readDoc(path);
		LineAttreEnum preAttr = null;
		Taxon preTaxon = null;
		Other other = null;
		// temporary 构建临时状态
		LineStatus thisLineStatus = new LineStatus();
		for (String line : readByLine) {
			if (StringUtils.isEmpty(line)) {
				continue;
			}
//			logger.info("当前行："+line);
			String sourceLine = line;// 未经任何处理的行
			// 替换特殊字符
			line = replaceSpecialChar(line);
			LineAttreEnum currentAttr = parseLineFishWord.isWhat(line, preAttr, sourceLine);
			if (insertExecute) {
				changeDatasourceId(line, baseParamsForm);
			}
			switch (currentAttr) {
			case Class:// 纲
//				System.out.println(line);
				if (execute) {
					printNotRank(other, doc, preAttr);
					writeTitle(doc, line, true);
				}
				// 解析当前行并转换成实体类
				if (insertExecute) {
					Taxon taxon = parseLineFishWord.parseClass(line, baseParamsForm, thisLineStatus);
					taxon.setOrderNum(orderNum);
					orderNum++;
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
				// 解析当前行并转换成实体类
				if (insertExecute) {
					Taxon taxon = parseLineFishWord.parseOrder(line, baseParamsForm, thisLineStatus);
					taxon.setOrderNum(orderNum);
					orderNum++;
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
				if (insertExecute) {
					Taxon taxon = parseLineFishWord.parseFamily(line, baseParamsForm, thisLineStatus);
					taxon.setOrderNum(orderNum);
					orderNum++;
					taxonlist.add(taxon);
					thisLineStatus.setFamily(taxon);
					thisLineStatus.clearUnderFamily();
					preTaxon = taxon;
				}
				preAttr = LineAttreEnum.family;
				break;
			case subfamily:// 亚科
//				System.out.println(line);
				if (execute) {
					printNotRank(other, doc, preAttr);
					writeTitle(doc, line, true);
				}
				if (insertExecute) {
					Taxon taxon = parseLineFishWord.parseSubfamily(line, baseParamsForm, thisLineStatus);
					taxon.setOrderNum(orderNum);
					orderNum++;
					taxonlist.add(taxon);
					thisLineStatus.setSubfamily(taxon);
					thisLineStatus.clearUnderSubfamily();
					preTaxon = taxon;
				}
				preAttr = LineAttreEnum.subfamily;
				break;
			case genus:// 属
//				System.out.println(line);
				Map<String, String> genusMap = parseGenusLine(line);
				if (execute) {
					printNotRank(other, doc, preAttr);
					writeGenusWithStyle(doc, genusMap);
//					writeTitle(doc, line, true);
				}
				if (insertExecute) {
					Taxon taxon = parseLineFishWord.parseGenus(line, baseParamsForm, thisLineStatus, genusMap);
					taxon.setOrderNum(orderNum);
					orderNum++;
					taxonlist.add(taxon);
					thisLineStatus.setGenus(taxon);
					thisLineStatus.clearUnderGenus();
					preTaxon = taxon;
				}
				preAttr = LineAttreEnum.genus;
				break;
			case subgenus:// 亚属
//				System.out.println(line);
				Map<String, String> subgenusMap = parseGenusLine(line);
				if (execute) {
					printNotRank(other, doc, preAttr);
					writeGenusWithStyle(doc, subgenusMap);
//					writeTitle(doc, line, true);
				}
				if (insertExecute) {
					Taxon taxon = parseLineFishWord.parseSubgenus(line, baseParamsForm, thisLineStatus, subgenusMap);
					taxon.setOrderNum(orderNum);
					orderNum++;
					taxonlist.add(taxon);
					thisLineStatus.setSubgenus(taxon);
					thisLineStatus.clearUnderSubgenus();
					preTaxon = taxon;
				}
				preAttr = LineAttreEnum.subgenus;
				break;
			case species:// 种
//				boolean speciesRank = parseLineFishWord.validateSpecies(line);
				if(line.startsWith("（")||line.startsWith("(")||CommUtils.isContainChinese(line.substring(0, 1))) {
					
				}else {
					System.out.println("可能不是种阶元："+line);
				}
				Map<String, String> speciesMap = parseSpeciesLine(line);
//				System.out.println(line);
				if (execute) {
					printNotRank(other, doc, preAttr);
					writeSpeciesWithStyle(doc, speciesMap);
//					writeDesc(true, doc, true, line, indentationHanging, indentationLeft);
				}
				if (insertExecute) {
					Taxon taxon = parseLineFishWord.parseSpecies(line, baseParamsForm, thisLineStatus, speciesMap);
					taxon.setOrderNum(orderNum);
					orderNum++;
					taxonlist.add(taxon);
					thisLineStatus.setSpecies(taxon);
					thisLineStatus.clearUnderSpecies();
					preTaxon = taxon;
				}
				speciesbeginCount++;
				preAttr = LineAttreEnum.species;
				break;
			case subsp:// 亚种
				Map<String, String> subspeciesMap = parseSubspeciesLine(line);
				if (execute) {
//					logger.info("亚种："+line);
					printNotRank(other, doc, preAttr);
					writeSpeciesWithStyle(doc, subspeciesMap);
//					writeDesc(true, doc, true, line, indentationHanging, indentationLeft);
				}
				if (insertExecute) {
					Taxon taxon = parseLineFishWord.parseSubspecies(line, baseParamsForm, thisLineStatus);
					taxon.setOrderNum(orderNum);
					orderNum++;
					taxonlist.add(taxon);
					thisLineStatus.setSubspecies(taxon);
					preTaxon = taxon;
				}
				speciesbeginCount++;
				preAttr = LineAttreEnum.subsp;
				break;
			case ref:// 参考文献
				if (execute) {
					if (other == null) {
						other = new Other();
					}
					other.setRef(line);
				}
//				if (insertExecute) {
//					List<Ref> refs = parseLineFishWord.parseRefs(line, preTaxon, baseParamsForm);
//					if (refs != null) {
//						reflist.addAll(refs);
//					}
//				}
				break;
			case Distribute:// 分布
				if (execute) {
					if (other == null) {
						other = new Other();
					}
					other.setDistribution(line);
				}
//				if (insertExecute) {
//					Description desc = parseLineFishWord.parseDesc(line, preTaxon, baseParamsForm,
//							DescTypeConsts.DISTRIBUTION);
//					desclist.add(desc);
//					Distributiondata distributiondata = parseLineFishWord.parseDistribution(line, preTaxon,
//							baseParamsForm, desc);
//					if (distributiondata != null) {
//						distributionlist.add(distributiondata);
//					}
//				}
				break;
			case protectLevel:// 保护等级
				if (execute) {
					if (other == null) {
						other = new Other();
					}
					other.setProtectLevel(line);
				}
//				if (insertExecute) {
//					Description desc = parseLineFishWord.parseDesc(line, preTaxon, baseParamsForm,
//							DescTypeConsts.PORTECT);
//					desclist.add(desc);
//				}
				break;
			case commonName:// 俗名
				if (execute) {
					if (other == null) {
						other = new Other();
					}
					other.setCommname(line);
				}
				if (insertExecute) {
					List<Commonname> commonames = parseLineFishWord.parseCommonName(line, preTaxon, baseParamsForm);
					commnamelist.addAll(commonames);
				}
				break;
			case ciation:// 引证
				if (execute) {
					// 各条同物异名后加“.”；
					line = parseLineFishWord.handCitationLine(line);
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
				}
				if (insertExecute) {
					Citation citation = parseLineFishWord.parseCitation(line, preTaxon, baseParamsForm);
					citationlist.add(citation);
				}
				break;
			case minglu:// 名录
				if (execute) {
					writeTitle(doc, line, true);
				}
				break;
			default:
				System.out.println(line);
				System.out.println("switchcase:" + line);
				break;
			}

		}
		printNotRank(other, doc, preAttr);
		if (baseParamsForm.isInsert()) {
			// taxon
			logger.info("开始保存taxon :" + taxonlist.size());
			batchSubmitService.saveAll(taxonlist);
			logger.info("开始保存引证 :" + citationlist.size());
			// 引证
			batchSubmitService.saveAll(citationlist);
			logger.info("开始保存俗名：" + commnamelist.size());
			// 俗名
//			batchSubmitService.saveAll(commnamelist);
			logger.info("开始保存描述：" + desclist.size());
			// 描述
//			batchSubmitService.saveAll(desclist);
			logger.info("开始保存分布：" + distributionlist.size());
			// 分布
//			batchSubmitService.saveAll(distributionlist);
			// 参考文献
//			batchSubmitService.saveAll(reflist);
			// 分类树
//			taxtreeService.saveTreeByJsonRemark(taxonlist, baseParamsForm.getmTaxtreeId());
		}

	}

	@SuppressWarnings("unused")
	private Taxon getTaxon(LineAttreEnum preAttr, LineStatus thisLineStatus) {
		Taxon taxon = null;
		switch (preAttr) {
		case Class:
			taxon = thisLineStatus.getClass_();
			break;
		case order:
			taxon = thisLineStatus.getOrder();
			break;
		case family:
			taxon = thisLineStatus.getFamily();
			break;
		case subfamily:
			taxon = thisLineStatus.getSubfamily();
			break;
		case genus:
			taxon = thisLineStatus.getGenus();
			break;
		case subgenus:
			taxon = thisLineStatus.getSubgenus();
			break;
		case species:
			taxon = thisLineStatus.getSpecies();
			break;
		case subsp:
			taxon = thisLineStatus.getSubspecies();
			break;
		default:
			throw new ValidationException("未定义的rank:" + preAttr.getName());
		}
		return taxon;
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
		} // end if

		// 删除{}中间的内容
		if (line.contains("{")) {
			String rgex = "\\{(.*?)\\}";
			List<String> subUtil = CommUtils.getSubUtil(line, rgex);
			for (String oldChar : subUtil) {
				line = line.replace(oldChar, "");
			}
		} // end if

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
			// 引证：悬挂缩进
			if (citation != null && citation.size() > 0) {
				for (String line : citation) {
//					writeDesc(true, doc, false, line, 0, indentationLeft);
					writeCitationWithStyle(doc, line, preAttr);
				}
				other.setCitation(null);
			}
			// 文献：悬挂缩进
			if (StringUtils.isNotEmpty(ref)) {
				writeDesc(true, doc, false, ref, indentationHanging, indentationHanging + indentationLeft);
				other.setRef(null);
			}
			// 别名：悬挂缩进
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
	private void writeGenusWithStyle(XWPFDocument doc, Map<String, String> map) {
		XWPFParagraph paragraph = doc.createParagraph();// 创建段落
		paragraph.setAlignment(ParagraphAlignment.CENTER);// 居中对齐
		writeWithStyle(paragraph, map.get(MapConsts.TAXON_CHNAME), true, false);
		writeWithStyle(paragraph, map.get(MapConsts.TAXON_SCI_NAME), true, true);
		writeWithStyle(paragraph, map.get(MapConsts.TAXON_AUTHOR), true, false);
//		System.out.println(chname+"_____"+sciName+"_____"+author);

	}

	public Map<String, String> parseGenusLine(String line) {
		Map<String, String> genusMap = new HashMap<>();
		String chname;
		String sciName;
		String author;
		int index = CommUtils.indexOfFirstLetter(line);// 第一个英文字母的位置
		chname = line.substring(0, index);
		String sciNameAndAuthor = line.substring(index);
		sciName = toolService.getSciNameFromCitation(sciNameAndAuthor, 1).trim();
		author = CommUtils.cutByStrAfter(sciNameAndAuthor, sciName);
		// 删除旧序号，添加新序号
		if (chname.contains(".")) {
			chname = chname.substring(chname.indexOf(".") + 1);
		}
		chname = genusbeginCount + ". " + chname.trim();
		genusbeginCount++;
		genusMap.put(MapConsts.TAXON_CHNAME, chname);
		genusMap.put(MapConsts.TAXON_SCI_NAME, sciName);
		genusMap.put(MapConsts.TAXON_AUTHOR, author);
		return genusMap;
	}

	Map<String, String> parseSpeciesLine(String line) {
		Map<String, String> speciesMap = new HashMap<>();
		String chname;
		String sciName;
		String author;
		int index = CommUtils.indexOfFirstLetter(line);// 第一个英文字母的位置
		chname = line.substring(0, index);
		String sciNameAndAuthor = line.substring(index);
		Map<String, String> parseMap = toolService.parseSciName(sciNameAndAuthor);
		sciName = parseMap.get(MapConsts.TAXON_SCI_NAME);
		author = CommUtils.cutByStrAfter(line, sciName);
		// 删除旧序号，添加新序号
		String rgex = "\\([0-9]*\\)|\\（[0-9]*\\）";
		List<String> subUtil = CommUtils.getSubUtil(chname, rgex);
		for (String str : subUtil) {
			chname = chname.replace(str, "");
		}
		chname = "（" + speciesbeginCount + "）" + chname;
		speciesMap.put(MapConsts.TAXON_CHNAME, chname);
		speciesMap.put(MapConsts.TAXON_SCI_NAME, sciName);
		speciesMap.put(MapConsts.TAXON_AUTHOR, author);
		return speciesMap;
	}

	private Map<String, String> parseSubspeciesLine(String line) {
		Map<String, String> subspMap = new HashMap<>();
		int indexOfFirstLetter = CommUtils.indexOfFirstLetter(line);
		String chname = line.substring(0, indexOfFirstLetter);
		String sciNameAndAuthor = line.substring(indexOfFirstLetter);
		// 去除中文名中的序号
		if (chname.startsWith("(")) {
			chname = chname.substring(chname.indexOf(")") + 1);
		}
		// 删除旧序号，添加新序号
		String rgex = "\\([0-9]*\\)|\\（[0-9]*\\）";
		List<String> subUtil = CommUtils.getSubUtil(chname, rgex);
		for (String str : subUtil) {
			chname = chname.replace(str, "");
		}
		chname = "（" + speciesbeginCount + "）" + chname.trim();
		
		String sciName = toolService.parseSciName(sciNameAndAuthor).get(MapConsts.TAXON_SCI_NAME);
		String author = CommUtils.cutByStrAfter(sciNameAndAuthor, sciName);
		subspMap.put(MapConsts.TAXON_CHNAME, chname);
		subspMap.put(MapConsts.TAXON_SCI_NAME, sciName);
		subspMap.put(MapConsts.TAXON_AUTHOR, author);
		return subspMap;
	}

	/**
	 * 
	 * @Description 种学名斜体
	 * @param doc
	 * @param line
	 * @author ZXY
	 */
	private void writeSpeciesWithStyle(XWPFDocument doc, Map<String, String> speciesMap) {
//		System.out.println(line);
		XWPFParagraph paragraph = doc.createParagraph();// 创建段落
		paragraph.setAlignment(ParagraphAlignment.LEFT);// 左对齐
		writeWithStyle(paragraph, speciesMap.get(MapConsts.TAXON_CHNAME), true, false);
		writeWithStyleOfSciName(paragraph, speciesMap.get(MapConsts.TAXON_SCI_NAME), true);
//		writeWithStyle(paragraph, speciesMap.get(MapConsts.TAXON_SCI_NAME), true, true);
		writeWithStyle(paragraph, speciesMap.get(MapConsts.TAXON_AUTHOR), true, false);
	}

	/**
	 * 
	 * @Description 斜体 加粗 英文
	 * @param paragraph
	 * @param sciname
	 * @author ZXY
	 */
	private void writeWithStyleOfSciName(XWPFParagraph paragraph, String line, boolean bold) {
//		if (line.contains("（")) {
//			logger.info("学名有括号（01）：" + line);
//		} else if (line.contains("(")) {
//			logger.info("学名有括号（02）：" + line);
//		}
		line = line.replace(" (", "(");
		line = line.replace(") ", ")");
		boolean meetBrackets = false;
		for (int i = 0; i < line.length(); i++) {
			char charAt = line.charAt(i);
			if (charAt == '(') {
				meetBrackets = true;
			} else if (charAt == ')') {
				meetBrackets = false;
			}
			String charAtStr = String.valueOf(charAt);
			XWPFRun run = paragraph.createRun();
			run.setFontFamily(fontFamily);// 字体
			run.setBold(bold);// 加粗
			run.setFontSize(fontSize);// 字号
			run.setText(charAtStr);// 标题内容
			if (meetBrackets || charAt == ')' || charAt == '）') {
				run.setItalic(false);// 非斜体
			} else {
				run.setItalic(true);// 斜体（字体倾斜）
			}
		}

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
		//设置悬挂缩进的段落，左缩进= indentationLeft+indentationHanging
		String sciName = null;
		String other = null;
		XWPFParagraph paragraph = doc.createParagraph();// 创建段落
		paragraph.setAlignment(ParagraphAlignment.LEFT);// 左对齐
		paragraph.setIndentationLeft(indentationLeft+indentationHanging);
		paragraph.setIndentationHanging(indentationHanging);
		if (line.equals("syn. of C. fasciatum Chan, 1966.")) {// 特殊的
			writeSpecialSyn(paragraph);
			return;
		}else if (line.equals("Rasbora sp. 匡溥人(见褚新洛和陈银瑞)，1989，27；.")) {// 特殊的
			writeSpecialCitationItalic(paragraph,"Rasbora",true);
			writeSpecialCitationItalic(paragraph," sp. 匡溥人(见褚新洛和陈银瑞)，1989，27；.",false);
			return;
		}
		Map<String, String> parseMap = toolService.parseSciName(line);
		sciName = parseMap.get(MapConsts.TAXON_SCI_NAME);
		other = CommUtils.cutByStrAfter(line, sciName).trim();
//		System.out.println(sciName+"___"+other);
		writeWithStyleOfSciName(paragraph, sciName, false);
//		writeWithStyle(paragraph, sciName + " ", false, true);
		writeWithStyle(paragraph, other, false, false);

	}

	private void writeSpecialCitationItalic(XWPFParagraph paragraph, String line, boolean Italic) {
		for (int i = 0; i < line.length(); i++) {
			char charAt = line.charAt(i);
			String charAtStr = String.valueOf(charAt);
			XWPFRun run = paragraph.createRun();
			run.setFontFamily(fontFamily);// 字体
			run.setBold(false);// 加粗
			run.setFontSize(fontSize);// 字号
			run.setText(charAtStr);// 标题内容
			run.setItalic(Italic);
		}
		
	}

	private void writeSpecialSyn(XWPFParagraph paragraph) {
		XWPFRun run = paragraph.createRun();
		run.setFontFamily(fontFamily);// 字体
		run.setBold(true);// 加粗
		run.setFontSize(fontSize);// 字号
		run.setText("syn. of C. ");// 标题内容
		run.setItalic(false);// 斜体（字体倾斜）
		XWPFRun runs = paragraph.createRun();
		runs.setFontFamily(fontFamily);// 字体
		runs.setBold(true);// 加粗
		runs.setFontSize(fontSize);// 字号
		runs.setText("fasciatum");// 标题内容
		runs.setItalic(true);// 斜体（字体倾斜）
		XWPFRun runa = paragraph.createRun();
		runa.setFontFamily(fontFamily);// 字体
		runa.setBold(true);// 加粗
		runa.setFontSize(fontSize);// 字号
		runa.setText(" Chan, 1966.");// 标题内容
		runa.setItalic(false);// 斜体（字体倾斜）

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

	private void setDataSource(BaseParamsForm baseParamsForm, String fileName) {
		switch (fileName) {
		case "3-名录-1盲鳗至鼠喜(伍审阅)-Shao Lab.doc":
			baseParamsForm.setmSourcesid("dc596d7c-1681-42e4-a471-d233c783248f");
			break;
		case "3-名录-2鲤形目-狗鱼目.doc":
			baseParamsForm.setmSourcesid("ba288bdf-bfcb-4993-a4f4-1cace376fb0e");
			break;
		case "3-名录-3-巨口鱼-狮子鱼(伍审阅)_Shao Lab.doc":
			baseParamsForm.setmSourcesid("2ae1fcb4-6378-4363-83e7-b3b26c055a9e");
			break;
		case "3-名录-4鲈形目-虾虎鱼前(伍审阅)-Shao Lab.doc":
			baseParamsForm.setmSourcesid("fbc74ba1-3871-4afd-82f8-313a65b06853");
			break;
		case "3-名录-5虾虎鱼-完(伍审阅)-Shao Lab.doc":
			baseParamsForm.setmSourcesid("1f315c8e-3305-43cd-ab9f-18b4498f240b");
			break;
		case "整合.doc":
			baseParamsForm.setmSourcesid("ddd74ba1-3871-4afd-82f8-313a65b06853");
			break;
		default:
			throw new ValidationException("未定义的数据源：" + fileName);
		}

	}

	private void changeDatasourceId(String line, BaseParamsForm baseParamsForm) {
		String fileName = "";
		if (line.contains("盲鳗纲")) {
			fileName = "3-名录-1盲鳗至鼠喜(伍审阅)-Shao Lab.doc";
		} else if (line.contains("鲤形目")) {
			fileName = "3-名录-2鲤形目-狗鱼目.doc";
		} else if (line.contains("巨口鱼目")) {
			fileName = "3-名录-3-巨口鱼-狮子鱼(伍审阅)_Shao Lab.doc";
		} else if (line.contains("鲈形目")) {
			fileName = "3-名录-4鲈形目-虾虎鱼前(伍审阅)-Shao Lab.doc";
		} else if (line.contains("溪鳢科")) {
			fileName = "3-名录-5虾虎鱼-完(伍审阅)-Shao Lab.doc";
		}
		if (StringUtils.isNotEmpty(fileName)) {
			setDataSource(baseParamsForm, fileName);
		}

	}

}
