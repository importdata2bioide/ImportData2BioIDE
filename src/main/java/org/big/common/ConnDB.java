package org.big.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.validation.ValidationException;

public class ConnDB {

	private static ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();
	private static Connection connDB;

	private ConnDB() {

	}

	public static Connection getConnDB() throws SQLException {
		connDB = threadLocal.get();
		if (connDB == null || connDB.isClosed()) {
			connDB = ConnSqlserverDB();
			threadLocal.set(connDB);
		}
		return connDB;
	}

	public static void closeConnDB() throws Exception {
		try {
			Connection conn = threadLocal.get();
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			threadLocal.remove();
		}
	}

	private static Connection ConnSqlserverDB() {
		// 声明Connection对象
		Connection connection = null;
		// 加载驱动程序
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// 1.getConnection()方法，连接数据库！！
			String url = "jdbc:mysql://159.226.67.87:3306/biodata?useSSL=false";
			String username = "bioide";
			String password = "big@bioide_2017";
			connection = DriverManager.getConnection(url, username, password);
			if (!connection.isClosed())
				System.out.println(url + " 数据库连接成功");
			// 2.statement类对象，用来执行SQL语句！！
		} catch (ClassNotFoundException e) {
			throw new ValidationException("找不到 SQLSERVER 连接驱动，无法继续！");
		} catch (SQLException e) {
			throw new ValidationException(e.getMessage());
		}
		return connection;
	}

	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 20; i++) {
			new Thread() {
				Connection connDB2 = null;
				public void run() {
					
					try {
						connDB2 = getConnDB();
						System.out.println("第一次："+Thread.currentThread().getId()+"		"+connDB2.hashCode());
						Thread.currentThread().sleep(2000);
						connDB2 = getConnDB();
						System.out.println("第二次："+Thread.currentThread().getId()+"		"+connDB2.hashCode());
						connDB2.close();
						connDB2 = getConnDB();
						System.out.println("第三次："+Thread.currentThread().getId()+"		"+connDB2.hashCode());
						connDB2.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					
				};
			}.start();
		}

	}

}
