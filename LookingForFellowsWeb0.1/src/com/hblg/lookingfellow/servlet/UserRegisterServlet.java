package com.hblg.lookingfellow.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UserRegisterServlet
 */
@WebServlet("/UserRegisterServlet")
public class UserRegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*String qq = request.getParameter("qq");
		String name = request.getParameter("name");
		String hometown = request.getParameter("hometown");
		System.out.println("qq:" + qq);
		System.out.println("name:" + name);
		System.out.println("hometown:" + hometown);*/
		/*UserDAO dao = new UserDAO();
		User user = new User(qq, name, hometown);
		if(dao.save(user)) {
			System.out.println("save successfully");
		} else {
			System.out.println("failed to save");
		}*/
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
