package org.big.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.big.common.CommUtils;
import org.big.common.ConstFile;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Tes {

	public static void main(String[] args) {
		
		String cutChinese = CommUtils.cutChinese("水稻细菌性条斑病菌Xanthomonas oryzae pv. oryzicola (Fang et al.) Swings et al.");
		System.out.println(cutChinese);
		System.out.println(CommUtils.cutByStrAfter("水稻细菌性条斑病菌Xanthomonas oryzae pv. oryzicola (Fang et al.) Swings et al.", cutChinese));
		//		System.out.println(s.substring(s.indexOf(" ")+1));
//		String imagePath ="E:\\003采集系统\\0010四册版\\真菌\\图\\image2.png";
//		String[] split = imagePath.split("\\\\");
//		String imageNameAndSuffix = split[split.length-1];
//		System.out.println(imageNameAndSuffix);
//		String onlyName = imageNameAndSuffix.split("\\.")[0];
//		System.out.println(onlyName);
//		String catalog = "马铃薯银屑病菌Helminthosporium solani Durieu et Mont.	11";
//		catalog = catalog.substring(0, catalog.indexOf("	")).trim();
//		System.out.println(catalog);
//		String importPath = "E:\\003采集系统\\0010四册版\\测试\\1.docx";
//		readWord(importPath);
//		readWordWithTextAndPic();

	}

	private static void readWordWithTextAndPic() {
		Map<String, String> map = new HashMap<String, String>();
		String importPath = "E:\\003采集系统\\0010四册版\\原核生物及病毒类\\原核生物及病毒类.docx";
		String absolutePath = "E:\\003采集系统\\0010四册版\\原核生物及病毒类\\图";
		try {
			FileInputStream inputStream = new FileInputStream(importPath);
			XWPFDocument xDocument = new XWPFDocument(inputStream);
			List<XWPFParagraph> paragraphs = xDocument.getParagraphs();
			List<XWPFPictureData> pictures = xDocument.getAllPictures();
			int picCount = 0;
			for (XWPFPictureData picture : pictures) {
				picCount++;
				String id = picture.getParent().getRelationId(picture);// 这里获取图片在word中唯一标识
				
				File folder = new File(absolutePath);
				if (!folder.exists()) {
					folder.mkdirs();
				}
				String rawName = picture.getFileName();
//				System.out.println("id = "+id+",rawName = "+rawName);
				String fileExt = rawName.substring(rawName.lastIndexOf("."));
				String newName = picCount + fileExt;

				File saveFile = new File(absolutePath + File.separator + rawName);
				@SuppressWarnings("resource")
				FileOutputStream fos = new FileOutputStream(saveFile);
				fos.write(picture.getData());
				fos.flush();
				fos.close();
//				System.out.println("id = "+id+",rawName = "+rawName);
//				System.out.println(saveFile.getAbsolutePath());
				map.put(id, saveFile.getAbsolutePath());
			}
//			System.out.println("图片总数：" + picCount);
			//

			String text = "";
			for (XWPFParagraph paragraph : paragraphs) {
				List<XWPFRun> runs = paragraph.getRuns();
				for (XWPFRun run : runs) {
					// 为什么这里判断<w:drawing>？如果你输出了runXmlText就会知道，word中是按照xml标签组织展示docx文档
					if (run.getCTR().xmlText().indexOf("<w:drawing>") != -1) {
						String runXmlText = null;
						try {
							runXmlText = run.getCTR().xmlText();
							
							int rIdIndex = runXmlText.indexOf("r:embed");
							int rIdEndIndex = runXmlText.indexOf("/>", rIdIndex);
							String rIdText = runXmlText.substring(rIdIndex, rIdEndIndex);
							String id = rIdText.split("\"")[1];
							String value = map.get(id);
							if (StringUtils.isNotEmpty(value)) {
								text = text + "<img src = '" + map.get(id) + "'/>";
							} else {
								text = text + "<img src = '未知'/>";
							}
//							System.out.println("runXmlText T="+runXmlText);
							map.remove(id);
						} catch (Exception e) {
//							System.out.println("runXmlText F="+runXmlText);
							text = text + "<img src = '未知2'/>";

						}

					} else {
						text = text + run;
					}
				}
			}
			System.out.println("开始");
			System.out.println(text);
			System.out.println("结束，你要把这个text拷贝到一个txt文件中，然后读取");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("---------找不到的-----------");
//		Iterator<Entry<String, String>> entries = map.entrySet().iterator();
//		while (entries.hasNext()) {
//			Entry<String, String> entry = entries.next();
//			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//		}

	}

	public void insertUser() throws SQLException {
		Connection connection = null;
		PreparedStatement pstmt = null;
		int entitiesSize = 0;
		try {
			// 连接数据库,字段个数20
			connection = null;
			String insertSql = " INSERT INTO user (id,user_name,password,email,phone,role,adddate,avatar,dtime,idnum,level,mark,mobile,nickname,profile_picture,resetmark,resettime,score,status,uploadnum) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
			pstmt = connection.prepareStatement(insertSql);
			for (int i = 0; i < entitiesSize; i++) {
				pstmt.setString(1, null);
				pstmt.setString(2, null);
				pstmt.setString(3, null);
				pstmt.setString(4, null);
				pstmt.setString(5, null);
				pstmt.setString(6, null);
				pstmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
				pstmt.setString(8, null);
				pstmt.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
				pstmt.setInt(10, 1);
				pstmt.setInt(11, 1);
				pstmt.setString(12, null);
				pstmt.setString(13, null);
				pstmt.setString(14, null);
				pstmt.setString(15, null);
				pstmt.setString(16, null);
				pstmt.setTimestamp(17, new Timestamp(System.currentTimeMillis()));
				pstmt.setInt(18, 1);
				pstmt.setInt(19, 1);
				pstmt.setInt(20, 1);
				pstmt.addBatch();// 添加到同一个批处理中;
			} // for end
			pstmt.executeBatch();// 执行批处理
		} catch (Exception e) {
			connection.rollback();
			e.printStackTrace();
		} finally {
			// do something
			if (pstmt != null) {
				pstmt.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
	}

	/**
	 * 读取word文档，然后将信息保存为定义的xml格式
	 * 
	 * @param filePath
	 * @return
	 */
	public static Document readWord(String filePath) {

		Document document = DocumentHelper.createDocument();
		Element wordE = DocumentHelper.createElement("word");
		document.add(wordE);

		Map<String, String> picMap = new HashMap<String, String>();// 保存图片文件的word中id和生成的UUID作为文件名

		String text = "";
		File file = new File(filePath);
		if (file.getName().endsWith(".docx")) { // 2007
			try {
				OPCPackage oPCPackage = POIXMLDocument.openPackage(filePath);
				XWPFDocument xwpf = new XWPFDocument(oPCPackage);
//				POIXMLTextExtractor ex = new XWPFWordExtractor(xwpf);

				Iterator<IBodyElement> iter = xwpf.getBodyElementsIterator();
				int curT = 0;// 当前操作表格对象索引
				int curP = 0;// 当前操作文字对象索引
				StringBuffer content = new StringBuffer();
				int imageCount = 0;
				while (iter.hasNext()) {
					IBodyElement ibe = iter.next();
					if (ibe.getElementType().equals(BodyElementType.TABLE)) {// 处理表格
						XWPFTable table = ibe.getBody().getTableArray(curT);

						Element tableE = DocumentHelper.createElement(ConstFile.TABLE);

						List<XWPFTableRow> rowList = table.getRows();
						if (rowList != null && rowList.size() > 0) {
							for (XWPFTableRow row : rowList) {

								Element rowE = DocumentHelper.createElement(ConstFile.ROW);

								List<XWPFTableCell> cellList = row.getTableCells();
								if (cellList != null && cellList.size() > 0) {
									for (XWPFTableCell cell : cellList) {

										Element colE = DocumentHelper.createElement(ConstFile.COL);
										colE.setText(cell.getText());
//										colE.addAttribute("color", cell.getColor()); //此处本来想提取table中文字颜色，发现返回基本是同一个，提取失败
										rowE.add(colE);

									}
								}

								tableE.add(rowE);
							}
						}

						wordE.add(tableE);

						curT++;
					} else if (ibe.getElementType().equals(BodyElementType.PARAGRAPH)) {// 处理文字
						boolean isPic = false;// 表示是否是图片标志
						XWPFParagraph p = ibe.getBody().getParagraphArray(curP);
						curP++;
						// 下面的代码来确定图片所在位置
						List<XWPFRun> runList = p.getRuns();
						if (runList != null && runList.size() > 0) {
							for (XWPFRun run : runList) {
								String runXmlText = run.getCTR().xmlText();
								// 图片索引
								if (runXmlText.indexOf("<w:drawing") != -1) {
									int rIdIndex = runXmlText.indexOf("r:embed=");
									int ridEndIndex = runXmlText.indexOf("/>", rIdIndex);
									/*
									 * rIdText格式： rId4 rId5
									 */
									String rIdText = runXmlText.substring(rIdIndex + "r:embed=".length() + 1,
											ridEndIndex - 1);
									imageCount++;
									String pfName = imageCount+"";
									picMap.put(rIdText, pfName);
									isPic = true;

									Element pictureE = DocumentHelper.createElement(ConstFile.PICTURE);
									pictureE.setText(pfName);
									wordE.add(pictureE);
								} else if (runXmlText.indexOf("<w:t") != -1) {// 文字
									Element paragraphE = DocumentHelper.createElement(ConstFile.PARAGRAPH);
//									paragraphE.addAttribute("color",
//											OtherUtil.isNotEmpty(run.getColor()) ? run.getColor() : "000000");// 格式：0000FF
//									paragraphE.addAttribute("fontSize", String.valueOf(run.getFontSize()));
									paragraphE.setText(run.toString());
									wordE.add(paragraphE);
								}
							}
						}

					}
				}
				// 这里输出只是为了看效果，不是工具类的一部分
				System.out.println(content.toString());
//				OutputFormat opf = new OutputFormat(" ",true);
//		    	opf.setEncoding("UTF-8");
//		    	XMLWriter xmlW = new XMLWriter(new FileOutputStream("d:\\tmp\\person.xml"),opf);
//		    	xmlW.write(document);
//		    	xmlW.close();
				// 对word中读取生成一个xml格式
				/*
				 * 读取图片所在位置，然后保存图片 输出格式： rId4 rId5
				 */
				List<XWPFPictureData> xwpfPicList = xwpf.getAllPictures();
				for (XWPFPictureData p : xwpfPicList) {
					String id = p.getParent().getRelationId(p);
					String pfName = picMap.get(id);
					byte[] pb = p.getData();
					String pName = p.getFileName();
					FileOutputStream fos = new FileOutputStream(
							ConstFile.PIC_PATH + File.separator + pfName + pName.substring(pName.indexOf(".")));
					fos.write(pb);
					fos.flush();
					fos.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return document;
	}

}
