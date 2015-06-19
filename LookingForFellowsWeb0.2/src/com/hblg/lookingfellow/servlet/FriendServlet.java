package com.hblg.lookingfellow.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hblg.lookingfellow.dao.FriendDAO;

/**
 * Servlet implementation class AddFriendServlet
 */
public class FriendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String tag = request.getParameter("tag");
		if(tag.equals("myfriends")) { // 如果是获取用户自己的好友
			String qq = request.getParameter("qq");
			FriendDAO dao = new FriendDAO();
			ArrayList<Map<String, String>> friendsInfo = dao.getMyFriendsInfo(qq);
			String str = constructJson(friendsInfo);
			out.write(str);
		} else if(tag.equals("searchfriends")) { // 如果是获取用户的老乡
			String hometown = request.getParameter("hometown");
			String qq = request.getParameter("qq");
			FriendDAO dao = new FriendDAO();
			ArrayList<Map<String, String>> fellowsInfo = dao.getFellowsInfo(qq, hometown);
			String str = constructJson(fellowsInfo);
			out.write(str);
		}
		out.flush();
		out.close();
	}
	
	/**
	 * 构造json格式数据
	 * @param posts
	 * @return
	 */
	private String constructJson(ArrayList<Map<String, String>> firendsInfo) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(int i=0; i<firendsInfo.size(); i++) {
			Map<String, String> map = firendsInfo.get(i);
			sb.append("{");
			for(Map.Entry<String, String> entry : map.entrySet())  {
				sb.append(entry.getKey());
				sb.append(":\"");
				if(entry.getValue() == null) {
					sb.append("");
				} else {
					sb.append(entry.getValue());
				}
				sb.append("\",");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append("},");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("]");
		return sb.toString();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
