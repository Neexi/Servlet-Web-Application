<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>CF Movie Co</title>
</head>
<body>
<% String message = (String) request.getSession().getAttribute("message"); 
if (message != null && !message.equals("")) {%>
<div align="center"><h2><%= message %></h2></div>
<% } else {%>
<div align="center"><h1>Welcome to CF Movie</h1></div>
<% } %>
<% String logged = (String) request.getSession().getAttribute("logged");
if(logged == null || logged.equals("false")) {%> 
<form action="control" method="get">
	<div align="right">
	<input type="hidden" name="action" value="login">
	Username	:<input type="text" name="username" size="20"><br>
	Password	:<input type="password" name="password" size="20"><br>
	<input type="submit" VALUE="Login">
	</div>
</form>
<form action="control" method="get">
	<div align="right">
	<input type="hidden" name="action" value="register">
	<input type="submit" VALUE="Create New Account">
	</div>
</form>
<% } else { %>
<div align="right"><h2>Hello, <%= (String) request.getSession().getAttribute("userSess")%>!</h2>
<form action="control" method="get">
	<input type="hidden" name="action" value="edit profile">
	<input type="submit" VALUE="Edit Profile">
</form>
<form action="control" method="get">
	<input type="hidden" name="action" value="admin">
	<input type="submit" VALUE="Admin Page">
</form>
<form action="control" method="get">
	<input type="hidden" name="action" value="logout">
	<input type="submit" VALUE="Log Out">
</form>
</div>
<div align="center">
<form action="control" method="get">
	<h3>Search movie by its title or genre</h3>
	<input type="hidden" name="action" value="search movie">
	<input type="text" name="search" size="50"><br>
	<input type="submit" VALUE="Search Movie">
</form>
</div>
<% } %>


<form action="control" method="get">
<input type="hidden" name="action" value="newMovie">
<input type="submit" VALUE="New movie">
</form>

<h3>Now Showing</h3>

<h3>Coming Soon</h3>
</body>
</html>