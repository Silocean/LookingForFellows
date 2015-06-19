package com.hblg.lookingfellow.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hblg.lookingfellow.dao.StudentDAO;

/**
 * Servlet implementation class GetUserInfoServlet
 */
public class GetUserInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String qq = request.getParameter("qq");
		StudentDAO dao = new StudentDAO();
		Map<String, String> map = dao.getStuInfo(qq);
		String str = constructJson(map);
		System.out.println(qq + "=====" + str);
		out.write(str);
		out.flush();
		out.close();
	}

	private String constructJson(Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
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
		sb.deleteCharAt(sb.length() - 1);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
