package com.hblg.lookingfellow.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hblg.lookingfellow.dao.StudentDAO;

/**
 * Servlet implementation class ModifyUserInfoServlet
 */
public class ModifyUserInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		StudentDAO dao = new StudentDAO();
		String qq = "";
		Enumeration enu = request.getParameterNames();
		List<String> list = new ArrayList<String>();
		while(enu.hasMoreElements()) {
			String param = (String) enu.nextElement();
			list.add(param);
			if(param.equals("qq")) {
				qq = request.getParameter("qq");
			}
		}
		for(int i=0; i<list.size(); i++) {
			String param = list.get(i);
			if(param.equals("mobile")) {
				String mobile = request.getParameter("mobile");
				if(dao.modifyMobile(mobile, qq)) {
					out.write("true");
					out.flush();
					out.close();
					return;
				}
			} else if(param.equals("name")) {
				String name = request.getParameter("name");
				if(dao.modifyName(name, qq)) {
					out.write("true");
					out.flush();
					out.close();
					return;
				}
			} else if(param.equals("sex")) {
				String sex = request.getParameter("sex");
				if(dao.modifySex(sex, qq)) {
					out.write("true");
					out.flush();
					out.close();
					return;
				}
			} else if(param.equals("hometown")) {
				String hometown = request.getParameter("hometown");
				if(dao.modifyHometown(hometown, qq)) {
					out.write("true");
					out.flush();
					out.close();
					return;
				}
			} else if(param.equals("signs")) {
				String signs = request.getParameter("signs");
				if(dao.modifySigns(signs, qq)) {
					out.write("true");
					out.flush();
					out.close();
					return;
				}
			} else if(param.equals("password")) {
				String password = request.getParameter("password");
				if(dao.modifyPassword(password, qq)) {
					out.write("true");
					out.flush();
					out.close();
					return;
				}
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
