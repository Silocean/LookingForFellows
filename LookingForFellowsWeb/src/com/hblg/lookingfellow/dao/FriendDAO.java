package com.hblg.lookingfellow.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.hblg.lookingfellow.db.DBManager;

public class FriendDAO {
	
	Connection conn = null;
	
	public FriendDAO() {
		conn = DBManager.getConn();
	}
	/**
	 * 根据用户qq号码查询他所有好友的详细信息
	 * @param qq
	 * @return
	 */
	public ArrayList<Map<String, String>> getMyFriendsInfo(String qq) {
		ArrayList<String> friendsQq = this.getMyFriends(qq);
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList<Map<String, String>> friendsInfo = new ArrayList<Map<String,String>>();
		try {
			for(int i=0; i<friendsQq.size(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				String sql = "select * from student where stuQQ = ?";
				pst = conn.prepareStatement(sql);
				pst.setString(1, friendsQq.get(i));
				rs = pst.executeQuery();
				while(rs.next()) {
					map.put("friendQq", friendsQq.get(i));
					map.put("friendName", rs.getString(2));
					map.put("friendHometown", rs.getString(3));
					map.put("friendSex", rs.getString(5));
					map.put("friendSigns", rs.getString(6));
					map.put("friendPhone", rs.getString(7));
				}
				friendsInfo.add(map);
			}
			return friendsInfo;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pst != null) {
					pst.close();
					pst = null;
				}
				if(rs != null) {
					rs.close();
				}
				if(conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * 根据用户qq号码查询他所有好友的qq号码
	 * @param qq
	 * @return
	 */
	public ArrayList<String> getMyFriends(String qq) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList<String> list = null;
		try {
			list = new ArrayList<String>();
			String sql = "select friend_qq from friend where user_qq = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, qq);
			rs = pst.executeQuery();
			while(rs.next()) {
				list.add(rs.getString(1));
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pst != null) {
					pst.close();
					pst = null;
				}
				if(rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * 根据用qq号码查找跟他同省份的老乡
	 * @param qq
	 * @param hometown
	 * @return
	 */
	public ArrayList<Map<String, String>> getFellowsInfo(String qq, String hometown) {
		ArrayList<String> myFriends = this.getMyFriends(qq);
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList<Map<String, String>> fellowsInfo = new ArrayList<Map<String,String>>();
		try {
			String sql = "select * from student where stuHometown like '" + hometown.split(" ")[0] + "%' and stuQQ != ?" ;
			pst = conn.prepareStatement(sql);
			pst.setString(1, qq);
			rs = pst.executeQuery();
			while(rs.next()) {
				boolean flag = false;
				Map<String, String> map = new HashMap<String, String>();
				map.put("friendQq", rs.getString(1));
				map.put("friendName", rs.getString(2));
				map.put("friendHometown", hometown);
				map.put("friendSex", rs.getString(5));
				map.put("friendSigns", rs.getString(6));
				map.put("friendPhone", rs.getString(7));
				for(int i=0; i<myFriends.size(); i++) {
					if(myFriends.get(i).equals(rs.getString(1))) {
						flag = true; // 表示此人已被我加为好友
					}
				}
				if(!flag) {
					fellowsInfo.add(map);
				}
			}
			return fellowsInfo;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pst != null) {
					pst.close();
					pst = null;
				}
				if(rs != null) {
					rs.close();
				}
				if(conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public boolean addFriend(String qq, String friendQq) {
		PreparedStatement pst = null;
		try {
			String sql = "insert into friend values (?, ?)";
			pst = conn.prepareStatement(sql);
			pst.setString(1, qq);
			pst.setString(2, friendQq);
			pst.executeUpdate();
			pst.setString(1, friendQq);
			pst.setString(2, qq);
			pst.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pst != null) {
					pst.close();
					pst = null;
				}
				if(conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
}
