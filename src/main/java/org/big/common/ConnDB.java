package org.big.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.validation.ValidationException;

public class ConnDB {

	private static ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();
	private static Connection connDB;

	private ConnDB() {

	}

	private static Connection getConnDB() throws SQLException, IOException {
		connDB = threadLocal.get();
		if (connDB == null || connDB.isClosed()) {
			Configuration configuration = new Configuration();
			configuration.setDriver("com.mysql.jdbc.Driver");
			configuration.setUrl("");
			configuration.setUsername("");
			configuration.setPassword("");
			connDB = ConnDataBase(configuration);
			threadLocal.set(connDB);
		}
		return connDB;
	}

	public static Connection getConnDB(Configuration configuration) throws SQLException, IOException {
		connDB = threadLocal.get();
		if (connDB == null || connDB.isClosed()) {
			if (configuration == null) {//默认读取配置文件中的数据库配置信息
				InputStream in = ConnDB.class.getResourceAsStream("/application-dev.properties");
				Properties props = new Properties();
				InputStreamReader inputStreamReader = new InputStreamReader(in, "UTF-8");
				props.load(inputStreamReader);
				String url = props.getProperty("spring.datasource.druid.url");
				String driver = props.getProperty("spring.datasource.druid.driver-class-name");
				String username = props.getProperty("spring.datasource.druid.username");
				String password = props.getProperty("spring.datasource.druid.password");
				configuration = new Configuration();
				configuration.setUrl(url);
				configuration.setDriver(driver);
				configuration.setUsername(username);
				configuration.setPassword(password);
			}
			connDB = ConnDataBase(configuration);
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

	private static Connection ConnDataBase(Configuration configuration) throws IOException {
		// 声明Connection对象
		Connection connection = null;
		// 加载驱动程序
		try {
			Class.forName(configuration.getDriver());
			// 1.getConnection()方法，连接数据库！！
			connection = DriverManager.getConnection(configuration.getUrl(), configuration.getUsername(),
					configuration.getPassword());
			if (!connection.isClosed())
				System.out.println(configuration.getUrl() + " 数据库连接成功");
			// 2.statement类对象，用来执行SQL语句！！
		} catch (ClassNotFoundException e) {
			throw new ValidationException("找不到 " + configuration.getDriver() + " 连接驱动，无法继续！");
		} catch (SQLException e) {
			throw new ValidationException(e.getMessage());
		}
		return connection;
	}

}
