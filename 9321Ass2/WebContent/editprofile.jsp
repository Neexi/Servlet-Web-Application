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
	<table border="0">
	<tr><td>Username		:</td><td><input type="text" name="username" value="<%= request.getAttribute("username") %>" size="40" disabled></td></tr>
	<tr><td>Email		:</td><td><input type="text" name="email" value="<%= request.getAttribute("email") %>" size="40"></td></tr>
	<tr><td>First Name		:</td><td><input type="text" name="firstName" value="<%= request.getAttribute("firstName") %>" size="40"></td></tr>
	<tr><td>Last Name		:</td><td><input type="text" name="lastName" value="<%= request.getAttribute("lastName") %>" size="40"></td></tr>
	</table>
	<input type="submit" VALUE="Edit">
	</div>
</form>
<form action="control" method="get">
	<div align="center">
	<input type="hidden" name="action" value="return">
	<input type="submit" VALUE="Return to Home Page">
	</div>
</form>
</body>
</html>