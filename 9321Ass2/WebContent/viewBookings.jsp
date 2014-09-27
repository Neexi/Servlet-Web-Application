<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Bookings</title>
</head>
<body>
<h1>Your bookings.</h1>
<table border = "1">
	<tr>
		<th>Movie name</th>
		<th>Movie show time</th>
		<th>Movie date</th>
		<th>Bookings made</th>
		<th>Booking card name</th>
		<th>Booking card number</th>
		<th>Booking card CSC</th>
	</tr>
<c:forEach items="${bookings}" var="booking">	
	<tr>
		<td>
			${booking.movieName}
		</td>
		<td>
			${booking.showTime}
		</td>
		<td>
			${booking.showDate}
		</td>
		<td>
			${booking.amount}
		</td>
		<td>
			${booking.cardName}
		</td>
		<td>
			${booking.cardNum}
		</td>
		<td>
			${booking.cardCSC}
		</td>
	</tr>				
</c:forEach>
</table>
<div align="right">
<form action="control" method="get">
	<input type="hidden" name="action" value="return">
	<input type="submit" VALUE="Return to Home Page">
</form>
</div>
</body>
</html>