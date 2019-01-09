package org.big.service;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class LanguageServiceImpl implements LanguageService {
	
	@Override
	public String findLanguageByCode(String code) {
		//加载json文件
		Resource resource = new ClassPathResource("static/json/language.json");
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(resource.getFile());
			JSONArray languages=JSONArray.parseArray(IOUtils.toString(inputStream,"utf8"));
			for(int i=0;i<languages.size();i++){
				if(code.equals(languages.getJSONObject(i).get("id").toString()))
					return languages.getJSONObject(i).get("text").toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
    }
}
