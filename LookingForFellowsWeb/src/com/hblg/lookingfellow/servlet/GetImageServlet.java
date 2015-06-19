package com.hblg.lookingfellow.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class GetImageServlet
 */
public class GetImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doGet");
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			PrintWriter out = response.getWriter();
			// Check that we have a file upload request
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (isMultipart) {
				// Create a factory for disk-based file items
				FileItemFactory factory = new DiskFileItemFactory();
				// Create a new file upload handler
				ServletFileUpload upload = new ServletFileUpload(factory);
				// Parse the request
				List<FileItem> items = upload.parseRequest(request);
				String dir = null;
				String imageName = null;
				for (FileItem item : items) {
					if (item.isFormField()) { // 如果是文本类型参数
						String name = item.getFieldName();
						String value = item.getString();
						if (name.equals("tag")) {
							if (value.equals("head")) {
								dir = request.getSession().getServletContext()
										.getRealPath("/head");
							} else if (value.equals("headbg")) {
								dir = request.getSession().getServletContext()
										.getRealPath("/headbg");
							} else if (value.equals("post")) {
								dir = request.getSession().getServletContext()
										.getRealPath("/post");
							}
							File dirFile = new File(dir);
							if (!dirFile.exists()) {
								dirFile.mkdirs();
							}
						} else if (name.equals("imageName")) {
							imageName = value;
						}
						// System.out.println(name + " " + value);
					} else { // 如果是文件类型参数
						System.out.println(dir);
						File file = new File(dir, imageName);
						item.write(file);
						out.write("sueecss");
						out.flush();
						out.close();
					}
				}
			} else {
				System.out.println("doPost");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
