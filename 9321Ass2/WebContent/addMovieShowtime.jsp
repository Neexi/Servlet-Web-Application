<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="edu.unsw.comp9321Ass2.jdbc.*,java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<% DerbyDAOImpl cast = (DerbyDAOImpl)request.getSession().getAttribute("cast"); %>
<% if(!cast.checkAdmin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess"))) { %>
<meta http-equiv="refresh" content="0; url=./reject2.jsp" />
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
<% MovieDTO movie = (MovieDTO)request.getAttribute("movie"); %>
	<div align="center">
	<h1>Add Movie Showtime</h1>
	<h3><%= movie.getMovieName() %></h3>
	<% String message = (String) request.getSession().getAttribute("message"); 
	if (message != null && !message.equals("")) {%>
	<h2><font color="red"><%= message %></font></h2>
	<% } %>
	</div>
<h3>Select Cinema, (Date format is dd/MM/YYYY)</h3>
<% List<CinemaDTO> cinemas = (List<CinemaDTO>)request.getAttribute("cinemas"); %>
<% for(CinemaDTO cinema : cinemas) { %>
	<%= cinema.getLocation()%> cinema with capacity of <%= cinema.getCapacity()%><br>
		<form action="control" method="post">
		<input type="hidden" name="action" value="movie showtime added">
		<input type="hidden" name="cinemaID" value="<%= cinema.getCinemaID()%>">
		<input type="hidden" name="movieID" value="<%= movie.getMovieID()%>">
		<input type="text" name="date" size="50">
		<input type="submit" VALUE="Add">
		<br>
		</form>
<% } %>	
</body>
</html>