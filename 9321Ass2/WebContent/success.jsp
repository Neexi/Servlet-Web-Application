<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Action complete!</title>
</head>
<body>

<% String message = (String) request.getSession().getAttribute("message"); 
if (message != null && !message.equals("")) {%>
<div align="center"><h2><%= message %></h2></div>
<% } %>

<div align="center">
	<form action="control" method="get">
		<input type="hidden" name="action" value="home">
		<input type="submit" VALUE="Return to Home Page">
	</form>
</div>
</body>
</html>