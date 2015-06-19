package com.hblg.lookingfellow.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hblg.lookingfellow.db.DBManager;
import com.hblg.lookingfellow.entity.Student;

public class StudentDAO {
	
	Connection conn = null;
	
	public StudentDAO() {
		conn = DBManager.getConn();
	}
	
	/*
	 * 注册用户信息
	 */
	public boolean save(Student stu) {
		PreparedStatement pst = null;
		try {
			String sql = "insert into student (stuQQ, stuName, stuPro, stuCity, stuPassword) values(?, ?, ?, ?, ?)";
			pst = conn.prepareStatement(sql);
			pst.setString(1, stu.getQq().trim());
			pst.setString(2, stu.getName().trim());
			pst.setString(3, stu.getProvince().trim());
			pst.setString(4, stu.getCity().trim());
			pst.setString(5, stu.getPassword().trim());
			int result = pst.executeUpdate();
			if(result == 1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(conn != null) {
					conn.close();
					conn = null;
				}
				if(pst != null) {
					pst.close();
					pst = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	/**
	 * 获取所有已注册学生QQ号
	 */
	public List<String> getAllStuQQ() {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			List<String> list = new ArrayList<String>();
			String sql = "select stuQQ from student";
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while(rs.next()) {
				list.add(rs.getString(1));
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
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
		return null;
	}
	
	/**
	 * 登录
	 * @param qq 
	 * @param password
	 * @return
	 */
	public Student login(String qq, String password) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			Student stu = null;
			String sql = "select * from student where stuQQ = ? and stuPassword = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, qq);
			pst.setString(2, password);
			rs = pst.executeQuery();
			while(rs.next())  {
				stu = new Student();
				stu.setQq(rs.getString(1));
				stu.setPassword(rs.getString(4));
			}
			return stu;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 根据QQ号码获取用户全部信息
	 */
	public Map<String, String> getStuInfo(String qq) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			Map<String, String> map = new HashMap<String, String>();
			String sql = "select * from student where stuQQ = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, qq);
			rs = pst.executeQuery();
			while(rs.next()) {
				map.put("stuQQ", qq);
				map.put("stuName", rs.getString(2));
				map.put("stuPro", rs.getString(3));
				map.put("stuCity", rs.getString(4));
				map.put("stuPassword", rs.getString(5));
				map.put("stuSex", rs.getString(6));
				map.put("stuSigns", rs.getString(7));
				map.put("stuPhone", rs.getString(8));
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
		return null;
	}
	/**
	 * 根据qq号码更改用户手机号
	 * @param mobile
	 * @param qq
	 */
	public boolean modifyMobile(String mobile, String qq) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String sql = "update student set stuPhone = ? where stuQQ = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, mobile);
			pst.setString(2, qq);
			int result = pst.executeUpdate();
			if(result == 1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
		return false;
	}
	/**
	 * 根据qq号码更改用户名字
	 * @param name
	 * @param qq
	 * @return
	 */
	public boolean modifyName(String name, String qq) {
		PreparedStatement pst = null;
		try {
			String sql = "update student set stuName = ? where stuQQ = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, name);
			pst.setString(2, qq);
			int result = pst.executeUpdate();
			if(result == 1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(conn != null) {
					conn.close();
					conn = null;
				}
				if(pst != null) {
					pst.close();
					pst = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	/**
	 * 根据qq号码更改用户性别
	 * @param sex
	 * @param qq
	 * @return
	 */
	public boolean modifySex(String sex, String qq) {
		PreparedStatement pst = null;
		try {
			String sql = "update student set stuSex = ? where stuQQ = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, sex);
			pst.setString(2, qq);
			int result = pst.executeUpdate();
			if(result == 1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(conn != null) {
					conn.close();
					conn = null;
				}
				if(pst != null) {
					pst.close();
					pst = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	/**
	 * 根据qq号码更改用户家乡
	 * @param hometown
	 * @param qq
	 * @return
	 */
	public boolean modifyHometown(String pro, String city, String qq) {
		PreparedStatement pst = null;
		try {
			String sql = "update student set stuPro = ? , stuCity = ? where stuQQ = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, pro);
			pst.setString(2, city);
			pst.setString(3, qq);
			int result = pst.executeUpdate();
			if(result == 1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(conn != null) {
					conn.close();
					conn = null;
				}
				if(pst != null) {
					pst.close();
					pst = null;
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	/**
	 * 根据qq号码更改用户个性签名
	 * @param signs
	 * @param qq
	 * @return
	 */
	public boolean modifySigns(String signs, String qq) {
		PreparedStatement pst = null;
		try {
			String sql = "update student set stuSigns = ? where stuQQ = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, signs);
			pst.setString(2, qq);
			int result = pst.executeUpdate();
			if(result == 1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(conn != null) {
					conn.close();
					conn = null;
				}
				if(pst != null) {
					pst.close();
					pst = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	/**
	 * 根据qq号码更改用户密码
	 * @param password
	 * @param qq
	 * @return
	 */
	public boolean modifyPassword(String password, String qq) {
		PreparedStatement pst = null;
		try {
			String sql = "update student set stuPassword = ? where stuQQ = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, password);
			pst.setString(2, qq);
			int result = pst.executeUpdate();
			if(result == 1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(conn != null) {
					conn.close();
					conn = null;
				}
				if(pst != null) {
					pst.close();
					pst = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
