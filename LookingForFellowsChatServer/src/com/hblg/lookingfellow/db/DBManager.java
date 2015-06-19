package com.hblg.lookingfellow.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBManager {

	static Connection conn = null;

	public static Connection getConn() {
		String driverName = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost/test";
		String dbUser = "root";
		String dbPwd = "123456";
		try {
			Class.forName(driverName);
			conn = DriverManager.getConnection(url, dbUser, dbPwd);
			System.out.println("connect mysql successful!");
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
