<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="edu.unsw.comp9321Ass2.jdbc.*,java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<% DerbyDAOImpl cast = (DerbyDAOImpl)request.getSession().getAttribute("cast"); %>
<% if(!cast.checkLogin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess"))) { %>
<meta http-equiv="refresh" content="0; url=./control?action=reject1" />
<% } %>
<title>CF Movie Co</title>
</head>
<body>
<div align="right">
<form action="control" method="get">
	<input type="hidden" name="action" value="return">
	<input type="submit" VALUE="Return to Home Page">
</form>
</div>
<div align="center">
<h1>Checkout</h1>
<h4>Please enter your credit card details in order to checkout.</h4>
<form action="control" method="post">
	<input type="hidden" name="action" value="checkout">
	<input type="hidden" name="showtimeID" value="<%= request.getAttribute("showtimeID") %>">
	<input type="hidden" name="amount" value="<%= request.getAttribute("amount") %>">
	<input type="hidden" name="userID" value="<%= request.getAttribute("userID") %>">
	Name on card:
	<input type="text" name="cardName"><br>
	Credit card number:
	<input type="text" name="cardNo"><br>
	CSC(Card Security Code):
	<input type="text" name="cardCSC"><br>
	<input type="submit" VALUE="Check Out">
</form>
</div>
</body>
</html>