package com.hblg.lookingfellow.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hblg.lookingfellow.db.DBManager;
import com.hblg.lookingfellow.entity.Reply;
import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH;

public class ReplyDAO {
	Connection conn = null;
	
	public ReplyDAO() {
		conn = DBManager.getConn();
	}
	/**
	 * 根据postId获取该帖子的所有跟帖
	 * @param postId
	 * @return
	 */
	public List<Map<String, Object>> getReply(String postId, int page) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			String sql = "select top 5 * from reply where postId = ? and replyId not in(select top " + page*5 + " replyId from reply where postId = ?)";
			pst = conn.prepareStatement(sql);
			pst.setString(1, postId);
			pst.setString(2, postId);
			rs = pst.executeQuery();
			while(rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", rs.getInt(1));
				map.put("details", rs.getString(2));
				map.put("time", rs.getString(3));
				map.put("fromId", rs.getString(4));
				map.put("toId", rs.getString(5));
				map.put("fromName", this.getStuName(rs.getString(4)));
				map.put("toName", this.getStuName(rs.getString(5)));
				map.put("postId", rs.getInt(5));
				list.add(map);
			}
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
		return list;
	}
	/**
	 * 根据Id号码获取发帖人名字
	 */
	private String getStuName(String stuId) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String sql = "select stuName from student where stuQQ = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, stuId);
			rs = pst.executeQuery();
			if(rs.next()) {
				return rs.getString(1);
			}
			
			if(pst != null) {
				pst.close();
				pst = null;
			}
			if(rs != null) {
				rs.close();
				rs = null;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 保存回复
	 * @param reply
	 * @return
	 */
	public boolean insertReply(Reply reply) {
		PreparedStatement pst = null;
		try {
			String sql = "insert into reply values (?, ?, ?, ?, ?)";
			pst = conn.prepareStatement(sql);
			pst.setString(1, reply.getDetails());
			pst.setString(2, reply.getTime());
			pst.setString(3, reply.getFromId());
			pst.setString(4, reply.getToId());
			pst.setInt(5, reply.getPostId());
			int result = pst.executeUpdate();
			if(result == 1) {
				return true;
			}
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
