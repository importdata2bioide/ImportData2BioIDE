package org.big.test;

import java.util.List;

import org.big.common.ReadxmlByDom;
import org.big.entityVO.XmlParamsVO;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TestDom4J {

	public static void main(String[] args) {
		
		List<XmlParamsVO> readxml = ReadxmlByDom.Readxml("E:\\book1.xml");
		System.out.println(readxml.size());
//		try { // 创建解析器工厂 
//			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
//			DocumentBuilder db = factory.newDocumentBuilder(); 
//			Document document = db.newDocument(); 
//			// 不显示standalone="no" 
//			document.setXmlStandalone(true); 
//			Element root = document.createElement("root"); 
//			// 向root根节点中添加子节点
//			addChildren(document,root);
//			
//			document.appendChild(root); 
//			TransformerFactory tff = TransformerFactory.newInstance(); 
//			Transformer tf = tff.newTransformer(); 
//			tf.setOutputProperty(OutputKeys.INDENT, "yes"); 
//			tf.transform(new DOMSource(document), new StreamResult(new File("E:\\book1.xml"))); 
//			System.out.println("生成book1.xml成功"); 
//			} catch (Exception e) { 
//				e.printStackTrace(); 
//				System.out.println("生成book1.xml失败"); } 
		}

	@SuppressWarnings("unused")
	private static void addChildren(Document document,Element root) {
		Element params = document.createElement("Params"); 
		//为params添加子节点
		Element treeID = document.createElement("TreeID"); 
		treeID.setTextContent("雷神"); 
		params.appendChild(treeID); 
		
		Element loginUser = document.createElement("loginUser"); 
		loginUser.setTextContent("雷神"); 
		params.appendChild(loginUser); 
		
		Element taxasetId = document.createElement("taxasetId"); 
		taxasetId.setTextContent("雷神"); 
		params.appendChild(taxasetId); 
		
		Element sourcesid = document.createElement("sourcesid"); 
		sourcesid.setTextContent("雷神"); 
		params.appendChild(sourcesid); 
		
		
		Element taxtreeId = document.createElement("taxtreeId"); 
		taxtreeId.setTextContent("雷神"); 
		params.appendChild(taxtreeId); 
		
		Element IMAGEPATH = document.createElement("IMAGEPATH"); 
		IMAGEPATH.setTextContent("雷神"); 
		params.appendChild(IMAGEPATH); 
		
		Element inputtimeStr = document.createElement("inputtimeStr"); 
		inputtimeStr.setTextContent("雷神"); 
		params.appendChild(inputtimeStr); 
		
		
		
		
		// 为params节点添加属性 
		params.setAttribute("id", "1"); 
		// 将params节点添加到bookstore根节点中 
		root.appendChild(params); 
		
	}
			
	


}
