package org.big.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class JDBCTools {

	public static Connection getConnection(String driver, String url, String username, String password)
			throws Exception {
		// 声明Connection对象
		Connection connection = null;
		connection = DriverManager.getConnection(url, username, password);
		if (!connection.isClosed()) {
			System.out.println(url + " 数据库连接成功");
		} else {
			throw new Exception("数据库连接失败");
		}
		return connection;
	}
	

	public static void closeConnection(Connection connection, PreparedStatement preparedStatement) throws Exception {
		if(preparedStatement != null) {
			preparedStatement.close();
		}
		if(connection != null) {
			connection.close();
		}
		
	}
	
	

}
