package org.big.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * @Author: WangTianshan(王天山)
 * @Description:
 * @Created Date: 2018/6/23 23:30
 * @Modified By:
 * @Last Modified Date:
 */
public class StrEditer {
    /**
     * Gets the pY.
     *
     * @param cname the cname
     * @return the pY
     * @throws DocumentException the document exception
     */
    public static String getPY(String cname) throws DocumentException
    {
        String str="";
        if(cname.trim().equals(""))
        {
            return str;
        }
        cname=cname.trim();
        System.out.println(cname);
        if(cname.contains("（"))
        {
            cname=cname.replace("（", "(").replace("）", ")");

        }
        cname=cname.replace(" ", "");
        String pyUrl="http://chinese.biodinfo.org/ChineseCharactorWebService.asmx/GetWordPinyinGoogle";
        String param="word="+cname+"&psd=";
        pyUrl=pyUrl+"?"+param;
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(pyUrl);
        if(document!=null){
            Element root = document.getRootElement();
            str=(String) root.getData();}
        return str;
//
//
//        String pyUrl="https://helloacm.com/api/pinyin/";
//        String param="s="+cname+"&t=0";
//        pyUrl=pyUrl+"?"+param;
//
//        try {
//            URL u = new URL("http://route.showapi.com/99-38?showapi_appid=Dmyappid&content=你好&showapi_sign=Dmysecret");
//            InputStream in = u.openStream();
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            try {
//                byte buf[] = new byte[1024];
//                int read = 0;
//                while ((read = in .read(buf)) > 0) {
//                    out.write(buf, 0, read);
//                }
//            } finally {
//                if ( in != null) {
//                    in .close();
//                }
//            }
//            byte b[] = out.toByteArray();
//            System.out.println(new String(b, "utf-8"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return str;
    }


    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            String jsonText = sb.toString();
            JSONObject json = JSON.parseObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
}
