

import com.alibaba.fastjson.JSONObject;

public class TESTJSONArray {
	
	public static void main(String[] args) {
		JSONObject js = new JSONObject();
		js.put("remark", "31321");
		js.put("source", "12312343");
		System.out.println(js.getString("remark"));
		System.out.println(String.valueOf(js));
//		JSONArray jsonArray = new JSONArray();
//		for(int i = 0;i<3;i++) {
//			JSONObject jsonObject = new JSONObject();
//			jsonObject.put("refE"," 0");
//			jsonObject.put("refS"," 0");
//			jsonObject.put("refId",""+i);
//			jsonObject.put("refType","0");
//			jsonArray.add(jsonObject);
//		}
//		System.out.println(jsonArray.size());
//		
//		String jsonString = jsonArray.toJSONString();
//		System.out.println(jsonString);
	}
	

}
