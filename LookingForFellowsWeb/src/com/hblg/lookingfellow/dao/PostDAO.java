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
import com.hblg.lookingfellow.entity.Post;

public class PostDAO {

	Connection conn = null;

	Map<String, String> proMap = new HashMap<String, String>();

	public PostDAO() {
		conn = DBManager.getConn();
		this.initProMap();
	}

	public void initProMap() {
		proMap.put("�����", "post_all");
		proMap.put("����", "post_beijing");
		proMap.put("���", "post_tianjin");
		proMap.put("�Ϻ�", "post_shanghai");
		proMap.put("����", "post_chongqing");
		proMap.put("�ӱ�", "post_hebei");
		proMap.put("ɽ��", "post_shanxi");
		proMap.put("����", "post_shanxi2");
		proMap.put("̨��", "post_taiwan");
		proMap.put("����", "post_liaoning");
		proMap.put("����", "post_jilin");
		proMap.put("������", "post_heilongjiang");
		proMap.put("����", "post_jiangsu");
		proMap.put("�㽭", "post_zhejiang");
		proMap.put("����", "post_anhui");
		proMap.put("����", "post_fujian");
		proMap.put("ɽ��", "post_shandong");
		proMap.put("����", "post_henan");
		proMap.put("����", "post_hubei");
		proMap.put("����", "post_hunan");
		proMap.put("�㶫", "post_guangdong");
		proMap.put("����", "post_gansu");
		proMap.put("�Ĵ�", "post_sichuan");
		proMap.put("����", "post_guizhou");
		proMap.put("����", "post_hainan");
		proMap.put("����", "post_yunnan");
		proMap.put("�ຣ", "post_qinghai");
		proMap.put("����", "post_xizang");
		proMap.put("����", "post_ningxia");
		proMap.put("�½�", "post_xinjiang");
		proMap.put("���ɹ�", "post_neimenggu");
		proMap.put("����", "post_aomen");
		proMap.put("���", "post_xianggang");
	}

	/**
	 * ��������
	 * 
	 * @param post
	 */
	public boolean insertPost(Post post) {
		PreparedStatement pst = null;
		try {
			String sql = "insert into post (title, content, authorId, time, replyNum, imageName) values(?, ?, ?, ?, 0, ?)";
			pst = conn.prepareStatement(sql);
			pst.setString(1, post.getTitle());
			pst.setString(2, post.getDetails());
			pst.setString(3, post.getAuthorId());
			pst.setString(4, post.getTime());
			pst.setString(5, post.getImageName());
			int result = pst.executeUpdate();
			if (result == 1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pst != null) {
					pst.close();
					pst = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean updatePostReplyCount(int postId) {
		PreparedStatement pst = null;
		try {
			String sql = "update post set replyNum = replyNum + 1 where postId = ?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, postId);
			int result = pst.executeUpdate();
			if (result == 1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pst != null) {
					pst.close();
					pst = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * ��ȡ������Ŀ��Ϣ(����ͼ��)
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getPosts(String pro, int page) {
		String postView = proMap.get(pro);
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			String sql = "select * from " + postView + " limit " + page * 20;
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", rs.getInt(1));
				map.put("title", rs.getString(2));
				map.put("details", rs.getString(3));
				map.put("authorId", rs.getString(4));
				map.put("time", rs.getString(5));
				map.put("authorName", this.getAuthorName(rs.getString(4)));
				map.put("replyNum", rs.getInt(6));
				map.put("imageName", rs.getString(7));
				list.add(map);
			}
			return list;
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
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
				if (proMap != null) {
					proMap.clear();
					proMap = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * ��ȡĳһʡ�ݵĵ������û�Id
	 * 
	 * @param pro
	 * @return
	 */
	public List<String> getAuthorIdByPro(String pro) {
		List<String> list = new ArrayList<String>();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String sql = "select stuQQ from student where stuPro like ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, pro);
			rs = pst.executeQuery();
			while (rs.next()) {
				list.add(rs.getString(1));
			}
			return list;
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
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * ��ȡ���ݿ���������Ŀ����
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	private int getPostsCount() {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String sql = "select COUNT(postId) from post";
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			if (rs.next()) {
				int postsCount = rs.getInt(1);
				return postsCount;
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
		return 0;
	}

	/**
	 * ����Id�����ȡ����������
	 */
	private String getAuthorName(String authorId) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String sql = "select stuName from student where stuQQ = ?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, authorId);
			rs = pst.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
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
		return null;
	}

	/**
	 * �������������ڲ���������Ŀ����
	 */
	public boolean testInsertPosts() {
		PreparedStatement pst = null;
		try {
			for (int i = 1; i <= 100; i++) {
				String sql = "insert into post values('"
						+ i
						+ "�����������������', '�������磬���Ź㳡���ǵ�׼ʱ����', '2013-9-27 14:22:34', '3424', 0)";
				pst = conn.prepareStatement(sql);
				@SuppressWarnings("unused")
				int result = pst.executeUpdate();
			}
			return true;
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

}
