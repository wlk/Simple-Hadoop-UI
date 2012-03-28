package pl.wroc.pwr.zpi.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static final String DB_HOST = "10.10.1.29";
	private static final String DB_PORT = "3306"; //domyślny - nie używany
	private static final String DB_NAME = "zpi";
	private static final String DB_USER = "root";
	private static final String DB_PASS = "root";

	public static Connection getConnection(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(getConnectionURL());
			return connection;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String getConnectionURL() {
		return "jdbc:mysql://" + DB_HOST + "/" + DB_NAME + "?user=" + DB_USER + "&password=" + DB_PASS;
		}

}
