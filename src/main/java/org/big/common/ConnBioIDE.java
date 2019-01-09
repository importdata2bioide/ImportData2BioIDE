package org.big.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnBioIDE {

	private static Connection connDB;

	private ConnBioIDE() {

	}

	public static Connection getConnDB() {
		if (connDB == null) {
			connDB = ConnSqlserverDB();
		}
		return connDB;
	}

	@SuppressWarnings("unused")
	private static Connection ConnSqlserverDB() {
		// 声明Connection对象
		Connection connection = null;
		// 加载驱动程序
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// 1.getConnection()方法，连接数据库！！
			String url = "jdbc:mysql://159.226.67.14:3306/biodata?rewriteBatchedStatements=true";
			String username = "wts";
			String password = "big@wts_2015";
			connection = DriverManager.getConnection(url, username, password);
			if (!connection.isClosed())
				System.out.println(url + " 数据库连接成功");
			// 2.statement类对象，用来执行SQL语句！！
		} catch (ClassNotFoundException e) {
			System.out.println("找不到 Mysql 连接驱动，无法继续！");
			int i = 1 / 0;
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
			int i = 1 / 0;
		}
		return connection;
	}

}
