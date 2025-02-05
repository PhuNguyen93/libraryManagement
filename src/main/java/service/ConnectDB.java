package service;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectDB {
	public static String getURL() {
		String url = null;

		try (var input = new FileInputStream("db.properties");) {
			var prop = new Properties();
			prop.load(input);
			url = prop.getProperty("url") + prop.getProperty("serverName") + ":" + prop.getProperty("port")
					+ "; databaseName=" + prop.getProperty("databaseName") + "; user=" + prop.getProperty("user")
					+ "; password=" + prop.getProperty("password") + "; encrypt=" + prop.getProperty("encrypt")
					+ "; trustServerCertificate=" + prop.getProperty("trustServerCertificate");
		} catch (Exception e) {
			e.printStackTrace();
			url = null;
		}
		return url;
	}

	public static Connection getCon() {

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(getURL());
		} catch (Exception e) {
			e.printStackTrace();
			conn = null;
		}
		return conn;
	}
}