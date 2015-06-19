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
		proMap.put("公告板", "post_all");
		proMap.put("北京", "post_beijing");
		proMap.put("天津", "post_tianjin");
		proMap.put("上海", "post_shanghai");
		proMap.put("重庆", "post_chongqing");
		proMap.put("河北", "post_hebei");
		proMap.put("山西", "post_shanxi");
		proMap.put("陕西", "post_shanxi2");
		proMap.put("台湾", "post_taiwan");
		proMap.put("辽宁", "post_liaoning");
		proMap.put("吉林", "post_jilin");
		proMap.put("黑龙江", "post_heilongjiang");
		proMap.put("江苏", "post_jiangsu");
		proMap.put("浙江", "post_zhejiang");
		proMap.put("安徽", "post_anhui");
		proMap.put("福建", "post_fujian");
		proMap.put("山东", "post_shandong");
		proMap.put("河南", "post_henan");
		proMap.put("湖北", "post_hubei");
		proMap.put("湖南", "post_hunan");
		proMap.put("广东", "post_guangdong");
		proMap.put("甘肃", "post_gansu");
		proMap.put("四川", "post_sichuan");
		proMap.put("贵州", "post_guizhou");
		proMap.put("海南", "post_hainan");
		proMap.put("云南", "post_yunnan");
		proMap.put("青海", "post_qinghai");
		proMap.put("西藏", "post_xizang");
		proMap.put("宁夏", "post_ningxia");
		proMap.put("新疆", "post_xinjiang");
		proMap.put("内蒙古", "post_neimenggu");
		proMap.put("澳门", "post_aomen");
		proMap.put("香港", "post_xianggang");
	}

	/**
	 * 保存帖子
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
	 * 获取帖子条目信息(从视图中)
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
	 * 获取某一省份的的所有用户Id
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
	 * 获取数据库中帖子条目总数
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
	 * 测试用例，用于插入帖子条目数据
	 */
	public boolean testInsertPosts() {
		PreparedStatement pst = null;
		try {
			for (int i = 1; i <= 100; i++) {
				String sql = "insert into post values('"
						+ i
						+ "又有老乡会啦！！！', '明天中午，中门广场，记得准时到啊', '2013-9-27 14:22:34', '3424', 0)";
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
