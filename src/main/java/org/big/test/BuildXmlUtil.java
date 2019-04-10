package org.big.test;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class BuildXmlUtil {
	public static void main(String[] args) throws Exception {
		buildxml();
	}

	public static void buildxml() throws ParserConfigurationException, TransformerException {

		// step1:获得一个DocumentBuilderFactory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// step2:获得一个DocumentBuilder
		DocumentBuilder db = factory.newDocumentBuilder();
		// step3:新建一个Document对象
		Document document = db.newDocument();
		// step4:创建一个根节点
		Element rootElement = document.createElement("engineering");
		for (int i = 0; i < 3; i++) {
			// step5:创建一个节点
			Element person = document.createElement("engineering");
			// step6:为该节点设定属性
			person.setAttribute("id", "id_");
			Element name = document.createElement("name");
			// 为节点设定文本内容
			name.setTextContent("name_");
			Element author = document.createElement("author");
			author.setTextContent("author_");
			Element addressUrl = document.createElement("addressUrl");
			addressUrl.setTextContent("addressUrl_");
			person.appendChild(name);
			person.appendChild(author);
			person.appendChild(addressUrl);
			// step7:为某一元素节点设立子节点
			rootElement.appendChild(person);
		}
		// step8:把刚刚建立的根节点添加到document对象中
		document.appendChild(rootElement);
		
		
		
		// step9:获得一个TransformerFactory对象
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		// step10:获得一个Transformer对象
		Transformer transformer = transformerFactory.newTransformer();
		// step11:把document对象用一个DOMSource对象包装起来
		Source xmlSource = new DOMSource(document);
		System.out.println(xmlSource);
		// step12:建立一个存储目标对象
		Result outputTarget = new StreamResult(new File("C:\\Users\\BIGIOZ\\Desktop\\persons.xml"));
		// step13:生成相应的xml文件
		transformer.transform(xmlSource, outputTarget);

	}
}
