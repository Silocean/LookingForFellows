package com.hblg.lookingfellow.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.hblg.lookingfellow.db.DBManager;
import com.hblg.lookingfellow.entity.Message;

public class MessageDAO {

	Connection conn = null;

	public MessageDAO() {
		conn = DBManager.getConn();
	}

	/**
	 * ������Ϣ
	 * 
	 * @param msg
	 * @return
	 */
	public boolean saveMessage(Message msg) {
		PreparedStatement pst = null;
		try {
			String sql = "insert into message values(?, ?, ?, ?, ?)";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, msg.getType());
			pst.setString(2, msg.getSender());
			pst.setString(3, msg.getReceiver());
			pst.setString(4, msg.getDetails());
			pst.setString(5, msg.getTime());
			int result = pst.executeUpdate();
			if (result == 1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
				if (pst != null) {
					pst.close();
					pst = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * ������Ӻ�����Ϣ
	 * 
	 * @param msg
	 * @return
	 */
	public boolean saveRequestAddFriendMessage(Message msg) {
		PreparedStatement pst = null;
		try {
			String sql = "insert into requestAddMsg values(?, ?, ?, ?, ?)";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, msg.getType());
			pst.setString(2, msg.getSender());
			pst.setString(3, msg.getReceiver());
			pst.setString(4, msg.getDetails());
			pst.setString(5, msg.getTime());
			int result = pst.executeUpdate();
			if (result == 1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
				if (pst != null) {
					pst.close();
					pst = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * ���������ѹ�ϵ����Ϣ
	 * 
	 * @param msg
	 */
	public boolean saveunfriendriendMessage(Message msg) {
		PreparedStatement pst = null;
		try {
			String sql = "insert into unfriendMsg values(?, ?, ?, ?, ?)";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, msg.getType());
			pst.setString(2, msg.getSender());
			pst.setString(3, msg.getReceiver());
			pst.setString(4, msg.getDetails());
			pst.setString(5, msg.getTime());
			int result = pst.executeUpdate();
			if (result == 1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
				if (pst != null) {
					pst.close();
					pst = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * �����û�qq�����ѯ����δ����Ϣ
	 * 
	 * @param sender
	 * @return
	 */
	public ArrayList<Map<String, Object>> getMessages(String receiver) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			String sql = "select * from message where msgReceiver = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, receiver);
			rs = pst.executeQuery();
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("msgId", rs.getInt(1));
				map.put("msgType", rs.getInt(2));
				map.put("msgSender", rs.getString(3));
				map.put("msgReceiver", rs.getString(4));
				map.put("msgDetails", rs.getString(5));
				map.put("msgTime", rs.getString(6));
				list.add(map);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
				if (pst != null) {
					pst.close();
					pst = null;
				}
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * �����û�qq�����ѯ����δ����Ӻ�����Ϣ
	 * 
	 * @param sender
	 * @return
	 */
	public ArrayList<Map<String, Object>> getRequestAddFriendMessages(
			String receiver) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			String sql = "select * from requestAddMsg where msgReceiver = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, receiver);
			rs = pst.executeQuery();
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("msgId", rs.getInt(1));
				map.put("msgType", rs.getInt(2));
				map.put("msgSender", rs.getString(3));
				map.put("msgReceiver", rs.getString(4));
				map.put("msgDetails", rs.getString(5));
				map.put("msgTime", rs.getString(6));
				list.add(map);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
				if (pst != null) {
					pst.close();
					pst = null;
				}
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * ɾ�����������ݴ����Ϣ
	 * 
	 * @param receiver
	 * @return
	 */
	public boolean deleteMessages(String receiver) {
		PreparedStatement pst = null;
		try {
			String sql = "delete from message where msgReceiver = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, receiver);
			int result = pst.executeUpdate();
			if (result > 0) {
				System.out.println("��ɾ�����ݿ����ݴ����Ϣ:" + receiver);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
				if (pst != null) {
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
	 * ɾ�����������ݴ��������Ӻ�����Ϣ
	 * 
	 * @param receiver
	 * @return
	 */
	public boolean deleteRequestAddFriendMessages(String receiver) {
		PreparedStatement pst = null;
		try {
			String sql = "delete from requestAddMsg where msgReceiver = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, receiver);
			int result = pst.executeUpdate();
			if (result > 0) {
				System.out.println("��ɾ�����ݿ����ݴ����Ϣ:" + receiver);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
				if (pst != null) {
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
	 * ���ݷ�����qq��ȡ����������
	 * 
	 * @param msgSender
	 * @return
	 */
	public String getMsgSenderName(String msgSender) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String sql = "select stuName from student where stuQQ = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, msgSender);
			rs = pst.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pst != null) {
					pst.close();
					pst = null;
				}
				if (rs != null) {
					rs.close();
					rs = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
