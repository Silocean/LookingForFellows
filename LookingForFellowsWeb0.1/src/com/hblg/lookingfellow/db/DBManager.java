package com.hblg.lookingfellow.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBManager {
	
	Connection conn = null;
	PreparedStatement pst = null;
	ResultSet rs = null;
	
	public DBManager() {
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String url = "jdbc:sqlserver://localhost:1433; DatabaseName=lookingfellow";
		try {
			Class.forName(driverName);
			conn = DriverManager.getConnection(url, "sa", "123456");
			System.out.println("connect sqlserver successful!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
