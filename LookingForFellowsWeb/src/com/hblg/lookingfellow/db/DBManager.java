package com.hblg.lookingfellow.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBManager {

	static Connection conn = null;

	public static Connection getConn() {
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String url = "jdbc:sqlserver://localhost:1433; DatabaseName=lookingfellow";
		try {
			Class.forName(driverName);
			conn = DriverManager.getConnection(url, "sa", "123456");
			System.out.println("connect sqlserver successful!");
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
