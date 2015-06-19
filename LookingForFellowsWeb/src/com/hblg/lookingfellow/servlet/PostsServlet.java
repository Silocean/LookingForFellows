package com.hblg.lookingfellow.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hblg.lookingfellow.dao.PostDAO;
import com.hblg.lookingfellow.entity.Post;

/**
 * Servlet implementation class getPostsServlet
 */
public class PostsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String pro = request.getParameter("province");
		String tag = request.getParameter("page");
		int page = Integer.parseInt(tag);
		PostDAO dao = new PostDAO();
		List<Map<String, Object>> posts = dao.getPosts(pro, page);
		if(posts == null) {
			out.write("error");
		} else {
			String str = constructJson(posts);
			System.out.println(str);
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
	private String constructJson(List<Map<String, Object>> posts) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(int i=0; i<posts.size(); i++) {
			Map<String, Object> map = posts.get(i);
			sb.append("{");
			for(Map.Entry<String, Object> entry : map.entrySet())  {
				sb.append(entry.getKey());
				sb.append(":\"");
				if(entry.getValue() == null) {
					sb.append("");
				} else {
					sb.append(entry.getValue().toString().replaceAll("\"", "\\\\\""));
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
		PrintWriter out = response.getWriter();
		String qq = request.getParameter("qq");
		String title = request.getParameter("title");
		String content = request.getParameter("details");
		String time = request.getParameter("time");
		String imageName = request.getParameter("imageName");
		PostDAO dao = new PostDAO();
		Post post = new Post();
		post.setTitle(title);
		post.setDetails(content);
		post.setTime(time);
		post.setAuthorId(qq);
		post.setImageName(imageName);
		if(dao.insertPost(post)) {
			System.out.println("success");
			out.write("success");
		} else {
			out.write("failed");
		}
		out.flush();
		out.close();
	}

}
