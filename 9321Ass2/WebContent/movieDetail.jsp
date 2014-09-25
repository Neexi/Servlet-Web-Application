<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="edu.unsw.comp9321Ass2.jdbc.*,java.util.*,java.text.DateFormat,java.text.SimpleDateFormat"%>
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
<% if(request.getAttribute("movie") != null) { %>
	<% MovieDTO movie = (MovieDTO)request.getAttribute("movie"); %>
	<% List<String> actors_list = Arrays.asList(movie.getActors().split("\\s*,\\s*")); %>
	<% List<String> genres_list = Arrays.asList(movie.getGenre().split("\\s*,\\s*")); %>
	<div align="right">
	<% if(cast.checkAdmin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess")) &&
			!movie.getReleaseDate().after(new Date())) {%>
		<form action="control" method="get">
		<input type="hidden" name="action" value="add movie showtime">
		<input type="hidden" name="movieID" value=<%= movie.getMovieID() %>>
		<input type="submit" VALUE="Add Showtime">
	</form>
	<% } %>
	</div>
	<div align="center">
	<h1><%= movie.getMovieName()%></h1>
	<% String message = (String) request.getSession().getAttribute("message"); 
	if (message != null && !message.equals("")) {%>
	<div align="center"><h2><%= message %></h2></div>
	<% } %>
	Movie Poster Here<br>
	<% if(!movie.getReleaseDate().after(new Date())) { %>
		<form action="control" method="get">
		<input type="hidden" name="action" value="check movie showtime">
		<input type="hidden" name="movieID" value=<%= movie.getMovieID() %>>
		<input type="submit" VALUE="Check Movie Showtime">
		</form>
	<% } %>
	</div>
	<%Date date = movie.getReleaseDate();  // wherever you get this
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	String text = df.format(date); %>
	<b>Release Date	: </b> <%= text%><br><br>
	<b>Genre	:</b>
	<ul>
	  <% for(String genre : genres_list) {%>
	  <li><%= genre %></li>
	  <% } %>
	</ul>
	<b>Directors :</b> <%= movie.getDirector() %><br><br>
	<b>Synopsis :</b><br><%= movie.getSynopsis()%><br><br>
	<b>Actors	:</b>
	<ul>
	  <% for(String actor : actors_list) {%>
	  <li><%= actor %></li>
	  <% } %>
	</ul>
	<b>Age Rating :</b> <%= movie.getAgeRating()%><br><br>
	<% if(!movie.getReleaseDate().after(new Date())) { %>
	<form action='control' method='get'>
			<input type='hidden' name='action' value='add review'>
			<input type="hidden" name="movieID" value="<%= movie.getMovieID()%>">
			<b>Review : </b><br>
			<textarea name="review paragraph" cols="80" rows="5"></textarea><br>
			<b>Rating : </b><br>
			<input type="radio" name="review rating" value="1">1
			<input type="radio" name="review rating" value="2">2
			<input type="radio" name="review rating" value="3" checked>3
			<input type="radio" name="review rating" value="4">4
			<input type="radio" name="review rating" value="5">5<br>
			<b>Review as : </b><input type="text" name="review name" size="50"><br>
			<button type='submit'>Submit Review</button>
	</form>
	<% } %>
	<% List<ReviewDTO> reviews = (List<ReviewDTO>)request.getAttribute("reviews"); %>
	<%if(reviews.size() > 0) {%>
		<h3>Review List</h3>
		<%for(ReviewDTO review : reviews) {%>
			Rated <%= review.getReviewRating() %> by <%= review.getReviewName() %> at <%= review.getReviewDate() %><br>
			<b>Review:</b>
			<%= review.getReviewParagraph() %><br><br>
		<% } %>
	<% } else {%>
		<h3>No review yet</h3>
	<% } %>
<% } %>
</body>
</html>