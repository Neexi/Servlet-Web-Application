<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="edu.unsw.comp9321Ass2.jdbc.DerbyDAOImpl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<% DerbyDAOImpl cast = (DerbyDAOImpl)request.getSession().getAttribute("cast"); %>
<% if(!cast.checkAdmin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess"))) { %>
<meta http-equiv="refresh" content="0; url=./control?action=reject2" />
<% } %>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>CF Movie Co</title>
</head>
<body>
<div align="center">
<h1>Administrator Page</h1><br>

<form action="control" method="get">
	<input type="hidden" name="action" value="add cinema">
	<input type="submit" VALUE="Add new Cinema">
</form>

<form action="control" method="get">
<input type="hidden" name="action" value="newMovie">
<input type="submit" VALUE="New movie">
</form>

</div>
<div align="right">
<form action="control" method="get">
	<input type="hidden" name="action" value="return">
	<input type="submit" VALUE="Return to Home Page">
</form>
</div>
</body>
</html>