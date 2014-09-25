<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="edu.unsw.comp9321Ass2.jdbc.*,java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
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
<% if(request.getAttribute("movie") != null && request.getAttribute("showtimeList") != null && request.getAttribute("cinemaList") != null) { %>
	<% MovieDTO movie = (MovieDTO)request.getAttribute("movie"); %>
	<div align="center">
	<h1>Movie Showtime List</h1>
	<h3><%= movie.getMovieName() %></h3>
	<% String message = (String) request.getSession().getAttribute("message"); 
	if (message != null && !message.equals("")) {%>
		<h2><font color="red"><%= message %></font></h2>
	<% } %>
	<% List<ShowtimeDTO> showtimeList = (List<ShowtimeDTO>)request.getAttribute("showtimeList"); %>
	<% List<CinemaDTO> cinemaList = (List<CinemaDTO>)request.getAttribute("cinemaList"); %>
	<% int index = 0; %>
	<% if(showtimeList.size() > 0) { %>
		<table border="1">
		<col width="150"><col width="100"><col width="100"><col width="100"><col width="100"><col width="200">
		<tr><th>Cinema Location</th><th>Date</th><th>Time</th><th>Available Seat</th><th>Max Capacity</th><th>Booking</th></tr>
		<% for(ShowtimeDTO showtime : showtimeList) { %>
			<% CinemaDTO cinema = cinemaList.get(index); %>
			<tr>
			<td><%= cinema.getLocation() %></td>
			<td><%= showtime.getMovieDate() %></td>
			<td><%= showtime.getMovieTime() %></td>
			<td><%= cinema.getCapacity()-showtime.getBooked() %></td>
			<td><%= showtime.getBooked() %></td>
			<td></td>
			</tr>
			<% index++; %>
		<% } %>
		</table>
	<% } %>
	</div>
<% } %>	
</body>
</html>