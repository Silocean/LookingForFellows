package com.hblg.lookingfellow.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hblg.lookingfellow.db.DBManager;
import com.hblg.lookingfellow.entity.Post;

public class PostDAO {
	
	Connection conn = null;
	
	public PostDAO() {
		conn = DBManager.getConn();
	}
	
	/**
	 * 保存帖子
	 * @param post
	 */
	public boolean insertPost(Post post) {
		PreparedStatement pst = null;
		try {
			String sql = "insert into post values(?, ?, ?, ?, 0)";
			pst = conn.prepareStatement(sql);
			pst.setString(1, post.getTitle());
			pst.setString(2, post.getDetails());
			pst.setString(3, post.getTime());
			pst.setString(4, post.getAuthorId());
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
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return false;
	}
			
	/**
	 * 获取帖子条目信息
	 * @return
	 */
	public List<Map<String, Object>> getPosts(int page) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			int total = this.getPostsCount();
			String sql = "select top 20 * from post where postId not in(select top "+ page*20 + " postId from post order by postId desc) order by postId desc";
			pst = conn.prepareStatement(sql);
			//pst.setInt(1, page*20);
			rs = pst.executeQuery();
			while(rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", rs.getInt(1));
				map.put("title", rs.getString(2));
				map.put("details", rs.getString(3));
				map.put("time", rs.getString(4));
				map.put("authorId", rs.getString(5));
				map.put("authorName", this.getAuthorName(rs.getString(5)));
				map.put("replyNum", rs.getString(6));
				list.add(map);
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
	 * 获取数据库中帖子条目总数
	 * @return
	 */
	private int getPostsCount() {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String sql = "select COUNT(postId) from post";
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			if(rs.next()) {
				int postsCount = rs.getInt(1);
				return postsCount;
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
		return 0;
	}
	/**
	 * 根据Id号码获取发帖人名字
	 */
	private String getAuthorName(String authorId) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String sql = "select stuName from student where stuQQ = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, authorId);
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
	 * 测试用例，用于插入帖子条目数据
	 */
	public boolean testInsertPosts() {
		PreparedStatement pst = null;
		try {
			for(int i=1; i<=100; i++) {
				String sql = "insert into post values('" + i + "又有老乡会啦！！！', '明天中午，中门广场，记得准时到啊', '2013-9-27 14:22:34', '3424', 0)";
				pst = conn.prepareStatement(sql);
				int result = pst.executeUpdate();
			}
			return true;
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
