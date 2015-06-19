<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
  <form method="post" action="http://192.168.1.152:8080/lookingfellowWeb0.2/GetImageServlet" enctype="multipart/form-data" >
	标题：<input type="text" name="title"/><br/>
	内容：<input type="text" name="content"/><br/>
	文件：<input type="file" name="image"/><br/>
	<input type="submit" value="提交"/>
  </form>
</body>
</html>