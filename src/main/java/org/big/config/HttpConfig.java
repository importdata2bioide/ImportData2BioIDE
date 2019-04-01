package org.big.config;

import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;

public class HttpConfig {
	private static volatile Map<String, String> urlMap = null;

	private HttpConfig() {

	}

	public static Map<String, String> getInstance() throws SQLException {
		if (urlMap == null) {
			urlMap = new HashedMap<>();
			urlMap.put("COLCHINA", "http://127.0.0.1:8088/api/");
		}
		return urlMap;
	}

}
