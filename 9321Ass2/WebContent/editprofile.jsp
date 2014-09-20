<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>CF Movie Co</title>
</head>
<body>
<form action="control" method="get">
	<div align="center">
	<h1>Edit Your Profile</h1>
	<% String message = (String) request.getSession().getAttribute("message"); 
	if (message != null && !message.equals("")) {%>
	<h2><font color="red"><%= message %></font></h2>
	<% } %>
	<input type="hidden" name="action" value="commit edit">
	New Password	:<input type="password" name="password" size="20"><br>
	Confirm Password	:<input type="password" name="passwordC" size="20"><br>
	New Email		:<input type="text" name="email" size="20"><br>
	New First Name		:<input type="text" name="firstname" size="20"><br>
	New Last Name		:<input type="text" name="lastname" size="20"><br>
	<input type="submit" VALUE="Edit">
	</div>
</form>
</body>
</html>