package org.big.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.big.entityVO.XmlParamsVO;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReadxmlByDom {
	
	public static  List<XmlParamsVO> Readxml(String filePath) {
		List<XmlParamsVO> list = new ArrayList<>();
		DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder domBuilder = domfac.newDocumentBuilder();
			InputStream is = new FileInputStream(new File(filePath));
			Document doc = domBuilder.parse(is);
			Element root = doc.getDocumentElement();
			NodeList books = root.getChildNodes();
			if (books != null) {
				for (int i = 0; i < books.getLength(); i++) {
					Node book = books.item(i);
					if (book.getNodeType() == Node.ELEMENT_NODE) {
						String seq = book.getAttributes().getNamedItem("seq").getNodeValue();
						String name = book.getAttributes().getNamedItem("name").getNodeValue();
						XmlParamsVO paramsInfo = new XmlParamsVO();
						paramsInfo.setName(name);
						paramsInfo.setSeq(seq);
						for (Node node = book.getFirstChild(); node != null; node = node.getNextSibling()) {
							if (node.getNodeType() == Node.ELEMENT_NODE) {
								if (node.getNodeName().equals("TreeID")) {
									paramsInfo.setTreeID(node.getFirstChild().getNodeValue());
								}
								if (node.getNodeName().equals("loginUser")) {
									paramsInfo.setLoginUser(node.getFirstChild().getNodeValue());
								}
								if (node.getNodeName().equals("taxasetId")) {
									paramsInfo.setTaxasetId(node.getFirstChild().getNodeValue());
								}
								if (node.getNodeName().equals("sourcesid")) {
									paramsInfo.setSourcesid(node.getFirstChild().getNodeValue());
								}
								
								if (node.getNodeName().equals("taxtreeId")) {
									paramsInfo.setTaxtreeId(node.getFirstChild().getNodeValue());
								}
								if (node.getNodeName().equals("IMAGEPATH")) {
									paramsInfo.setImagePath(node.getFirstChild().getNodeValue());
								}
								if (node.getNodeName().equals("inputtimeStr")) {
									paramsInfo.setInputtimeStr(node.getFirstChild().getNodeValue());
								}
									
								}
							
							}
						list.add(paramsInfo);
						}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;

	}
}