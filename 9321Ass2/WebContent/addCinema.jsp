<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="edu.unsw.comp9321Ass2.jdbc.*,java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<% DerbyDAOImpl cast = (DerbyDAOImpl)request.getSession().getAttribute("cast"); %>
<% if(!cast.checkAdmin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess"))) { %>
<meta http-equiv="refresh" content="0; url=./control?action=reject2" />
<% } %>
<title>CF Movie Co</title>
</head>
<body>
<form action="control" method="post" enctype="multipart/form-data">
	<div align="center">
	<h1>Add Cinema</h1>
	<% String message = (String) request.getSession().getAttribute("message"); 
	if (message != null && !message.equals("")) {%>
	<h2><font color="red"><%= message %></font></h2>
	<% } %>
	</div>
	<input type="hidden" name="action" value="cinema added">
	Location	:<input type="text" name="location" size="20"><br>
	Capacity	:<input type="text" name="capacity" size="20"><br>
	Amenities	:<br>
	<input type="checkbox" name="amenity" value="ATM">ATM<br>
	<input type="checkbox" name="amenity" value="Widescreen">Widescreen<br>
	<input type="checkbox" name="amenity" value="Snack Bar">Snack Bar<br>
	<input type="checkbox" name="amenity" value="Restaurant">Restaurant<br>
	<input type="submit" VALUE="add">
</form>
</body>
</html>