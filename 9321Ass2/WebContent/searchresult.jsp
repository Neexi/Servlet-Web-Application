<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.ArrayList, java.util.*,edu.unsw.comp9321Ass2.jdbc.MovieDTO,edu.unsw.comp9321Ass2.jdbc.DerbyDAOImpl"%>
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
<form action="control" method="get">
	<div align="right">
	<input type="hidden" name="action" value="return">
	<input type="submit" VALUE="Return to Home Page">
	</div>
</form>
<div align="center">
<form action="control" method="get">
	<h3>Search</h3>
	<input type="hidden" name="action" value="search movie">
	<input type="text" name="search" value="<%= request.getParameter("search")%>" size="50"><br>
	<input type="submit" VALUE="Search Movie">
</form>
<% if(request.getParameter("search") != null && request.getParameter("search").trim().length() > 0) { %>
	<% List<MovieDTO> matchTitle = new ArrayList<MovieDTO>(); %>
	<% if(request.getAttribute("titleMatch") != null) matchTitle = (List<MovieDTO>)request.getAttribute("titleMatch"); %>
	<% if(matchTitle.size() > 0) { %>
		<h3>Matches Title</h3>
		<table border=1>
		<col width="200"><col width="100"><col width="200"><col width="200"><col width="50"><col width="100">
		<tr><b><th>Title</th><th>Poster</th><th>Genre</th><th>Actor</th><th>Rating</th><th>Detail</th></b></tr>
		<% for(MovieDTO movie : matchTitle) { %>
			<% float rating = cast.getMovieRating(movie.getMovieID());%>
			<tr><td><%= movie.getMovieName() %></td>
			<td></td>
			<td><%= movie.getGenre() %></td>
			<td><%= movie.getActors() %></td>
			<td><% if(rating > 0) { %>
			<%= rating%>
			<% } %>
			</td>
			<td><a href="./control?action=movie+detail&movieID=<%= movie.getMovieID()%>">Detail</a></td></tr>
		<% } %>
		</table>
	<% } %>
	<% List<MovieDTO> matchGenre = new ArrayList<MovieDTO>(); %>
	<% if(request.getAttribute("genreMatch") != null) matchGenre = (List<MovieDTO>)request.getAttribute("genreMatch"); %>
	<% if(matchGenre.size() > 0) { %>
		<h3>Matches Genre</h3>
		<table border=1>
		<col width="200"><col width="100"><col width="200"><col width="200"><col width="50"><col width="100">
		<tr><b><th>Title</th><th>Poster</th><th>Genre</th><th>Actor</th><th>Rating</th><th>Detail</th></b></tr>
		<% for(MovieDTO movie : matchGenre) { %>
			<tr><td><%= movie.getMovieName() %></td>
			<td></td>
			<td><%= movie.getGenre() %></td>
			<td><%= movie.getActors() %></td>
			<td></td>
			<td><a href="./control?action=movie+detail&movieID=<%= movie.getMovieID()%>">Detail</a></td></tr>
		<% } %>
		</table>
	<% } %>
	<% if(matchTitle.size() == 0 && matchGenre.size() == 0) {%>
		<h3>No result found</h3>
	<% } %>
<% } else { %>
	<h3>Please insert the search criteria</h3>
<% } %>
</div>
</body>
</html>