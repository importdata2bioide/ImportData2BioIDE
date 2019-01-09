package org.big.testJava;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class TestConnMysql {
	
	
	public static void main(String[] args) throws SQLException {
		Connection cc=TestConnMysql.getConnection();
	    if(!cc.isClosed())
	    	System.out.println("Succeeded connecting to the Database!");
	   
	    else {
	    	System.out.println("failed connecting to the Database!");
	    	
	    }
	    
	    cc.close();
	}

	public static Connection getConnection() {
		String driver = "com.mysql.jdbc.Driver"; // 获取mysql数据库的驱动类
		String url = "jdbc:mysql://159.226.67.14:3306/biodata?useSSL=false&rewriteBatchedStatements=true"; // 连接数据库（kucun是数据库名）
		String name = "wts";// 连接mysql的用户名
		String pwd = "big@wts_2015";// 连接mysql的密码
		try {
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, name, pwd);// 获取连接对象
			return conn;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
