package org.big.test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.big.common.CommUtils;
import org.big.entity.Citation;
import org.big.entity.User;
import org.big.service.BirdAddDataImpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Test3 {

	public static void main(String[] args) {
//		completePPA();
//		testLinklist();
//		testclone();
//		System.out.println(CommUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
		replaceAllTest();

	}
	private static void replaceAllTest() {
		String s = "石家庄。bb<br>的伤口缝合；舒服的和，";
		s = s.replaceAll("[。，<br>]", "");
		System.out.println(s);
		
	}
	private static void testclone() {
		Citation c = new Citation();
		c.setId("100");
		Citation copy = (Citation)c.clone();
		System.out.println(c.getId());
		System.out.println(copy.getId());
		c.setId("500");
		System.out.println(copy.getId());
		System.out.println(c.getId());
	}
	private static void testLinklist() {
		List<User> list = new LinkedList<>();
		for(int i = 0;i<20;i++) {
			User u = new User();
			u.setId(i+"");
			list.add(u);
		}
		Iterator<User> iter = list.iterator();  
		while(iter.hasNext()){  
		   User user = iter.next();  
		   int id = Integer.parseInt(user.getId());
			if(id%3==0) {
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
		if(patternOfPPA.matcher(citationstr).matches()) {
			System.out.println("----"+citationstr);
			char[] flagsPPA = {'（','（'};
			citationstr = citationstr.substring(0,b.getNotYearflagIndex(flagsPPA, citationstr));
			System.out.println("--------"+citationstr);
		}
		
	}
	/**
	 * 
	 * @Description 字符串转换成JsonArray
	 * @author ZXY
	 */
	private static void testJsonArray() {
		String line = "[{\"refE\": \" 0\", \"refS\": \" 0\", \"refId\": \"FB9F62AA-6BD7-4E6C-B9F0-3D5A6D925BF0\", \"refType\": \"0\"}, {\"refE\": \" 0\", \"refS\": \" 0\", \"refId\": \"43F930D6-45F4-4888-A865-39CC665C5CA1\", \"refType\": \"0\"}, {\"refE\": \" 0\", \"refS\": \" 0\", \"refId\": \"B3CC4E6E-2558-409B-B0BB-844A60F74654\", \"refType\": \"0\"}]";
		JSONArray jsonArray=JSONArray.parseArray(line);
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
		    System.out.println(jsonObject.get("refId")) ;  // 得到 每个对象中的属性值
		  }
		
	}

}
