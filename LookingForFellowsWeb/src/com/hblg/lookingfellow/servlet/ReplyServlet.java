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
import com.hblg.lookingfellow.dao.ReplyDAO;
import com.hblg.lookingfellow.entity.Reply;

/**
 * Servlet implementation class ReplyServlet
 */
public class ReplyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String postId = request.getParameter("postId");
		System.out.println(postId);
		if(postId != null) {
			int page = Integer.valueOf(request.getParameter("page"));
			ReplyDAO dao = new ReplyDAO();
			List<Map<String, Object>> reply = dao.getReply(postId, page);
			String str = constructJson(reply);
			out.write(str);
			out.flush();
			out.close();
		} else {
			System.out.println("未获得postId");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String fromId = request.getParameter("fromId");
		String toId = request.getParameter("toId");
		String time = request.getParameter("time");
		String details = request.getParameter("details");
		String postId = request.getParameter("postId");
		System.out.println("fromId：" + fromId);
		System.out.println("toId:" + toId);
		System.out.println("time" + time);
		System.out.println("details：" + details);
		System.out.println("toPostId：" + postId);
		ReplyDAO dao = new ReplyDAO();
		Reply reply = new Reply();
		reply.setFromId(fromId);
		reply.setToId(toId);
		reply.setDetails(details);
		reply.setTime(time);
		reply.setPostId(Integer.valueOf(postId));
		PostDAO dao2 = new PostDAO();
		if(dao.insertReply(reply) && dao2.updatePostReplyCount(Integer.parseInt(postId))) {
			System.out.println("success");
			out.write("success");
		} else {
			out.write("failed");
		}
		out.flush();
		out.close();
	}
	/**
	 * 构造json格式数据
	 * @param posts
	 * @return
	 */
	private String constructJson(List<Map<String, Object>> reply) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(int i=0; i<reply.size(); i++) {
			Map<String, Object> map = reply.get(i);
			sb.append("{");
			for(Map.Entry<String, Object> entry : map.entrySet())  {
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

}
