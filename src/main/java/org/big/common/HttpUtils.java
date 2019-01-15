package org.big.common;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpUtils {
	/**
	 * 
	 * @Description 发送post请求
	 * @param url 请求路径
	 * @param data 请求数据
	 * @return
	 * @author ZXY
	 */
	public static String doPost(String url, String data) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(20000)
				.setConnectionRequestTimeout(10000).build();
		httpPost.setConfig(requestConfig);
		String context = StringUtils.EMPTY;
		if (!StringUtils.isEmpty(data)) {
			StringEntity body = new StringEntity(data, "utf-8");
			httpPost.setEntity(body);
		}
		// 设置回调接口接收的消息头
		httpPost.addHeader("Content-Type", "application/json");
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			context = EntityUtils.toString(entity, StandardCharsets.UTF_8);
		} catch (Exception e) {
			e.getStackTrace();
		} finally {
			try {
				response.close();
				httpPost.abort();
				httpClient.close();
			} catch (Exception e) {
				e.getStackTrace();
			}
		}
		return context;
	}
	/**
	 * 
	 * @Description 发送get请求
	 * @param urlStr
	 * @param paramsStr
	 * @return
	 * @author ZXY
	 */
	public static String doGet(String urlStr,String paramsStr) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
       
        try {
        	URL url = new URL(urlStr+"?"+paramsStr);
    		URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
            // 创建httpget.
            HttpGet httpget = new HttpGet(uri);
            // 执行get请求.
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                String responseStr = EntityUtils.toString(entity);
                if (entity != null) {
                	return responseStr;
                }
            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		return null;
    }
}
