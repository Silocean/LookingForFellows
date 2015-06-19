package com.hblg.lookingfellow.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.hblg.lookingfellow.entity.User;

public class UserDAO {
	
	Connection conn = null;
	PreparedStatement pst = null;
	Statement stmt = null;
	ResultSet rs = null;
	
	public UserDAO() {
		this.connectDatebase();
	}
	
	private void connectDatebase() {
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String url = "jdbc:sqlserver://localhost:1433; DatabaseName=lookingfellow";
		try {
			Class.forName(driverName);
			conn = DriverManager.getConnection(url, "sa", "123456");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * 注册用户信息
	 */
	public boolean save(User user) {
		try {
			String sql = "insert into user values(?, ?, ?)";
			pst = conn.prepareStatement(sql);
			pst.setString(1, user.getQq().trim());
			pst.setString(2, user.getName().trim());
			pst.setString(3, user.getHometown().trim());
			int result = pst.executeUpdate();
			if(result == 1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.closeSource();
		}
		return false;
	}
	/*
	 * 关闭资源
	 */
	private void closeSource() {
		try {
			if(conn != null) {
				conn.close();
				conn = null;
			}
			if(pst != null) {
				pst.close();
				pst = null;
			}
			if(rs != null) {
				rs.close();
				rs = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
