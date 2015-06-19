package com.hblg.lookingfellow.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hblg.lookingfellow.dao.StudentDAO;

/**
 * Servlet implementation class UserLoginServlet
 */
public class UserLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String qq = request.getParameter("qq");
		String password = request.getParameter("password");
		System.out.println("qq:" + qq);
		System.out.println("password:" + password);
		StudentDAO dao = new StudentDAO();
		if(dao.login(qq, password)!=null) {
			System.out.println(dao.login(qq, password));
			System.out.println("��½�ɹ�");
			out.write("login:1"); //1��ʾ��¼�ɹ�
		} else {
			System.out.println("�û������������");
			out.write("login:2"); //2��ʾ�û������������
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
