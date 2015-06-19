package com.hblg.lookingfellow.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hblg.lookingfellow.dao.StudentDAO;
import com.hblg.lookingfellow.entity.Student;

/**
 * Servlet implementation class UserRegisterServlet
 */
public class UserRegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String qq = request.getParameter("qq");
		String name = request.getParameter("name");
		String hometown = request.getParameter("hometown");
		String password = request.getParameter("password");
		System.out.println("qq:" + qq);
		System.out.println("name:" + name);
		System.out.println("hometown:" + hometown);
		System.out.println("password:" + password);
		StudentDAO dao = new StudentDAO();
		Student student = new Student(qq, name, hometown, password);
		boolean flag = checkQQ(qq);
		if(flag) {
			if(dao.save(student)) {
				System.out.println("save successfully");
			} else {
				System.out.println("failed to save");
				out.write("error:1"); //1表示服务器端出现问题
				out.flush();
				out.close();
			}
		} else {
			out.write("error:2"); //2表示qq号已被注册
			out.flush();
			out.close();
		}
	}

	private boolean checkQQ(String qq) {
		StudentDAO dao = new StudentDAO();
		List<String> list = dao.getAllStuQQ();
		for(int i=0; i<list.size(); i++) {
			if(qq.equals(list.get(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
