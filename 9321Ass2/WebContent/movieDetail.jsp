<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="edu.unsw.comp9321Ass2.jdbc.MovieDTO, java.util.Arrays, java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>CF Movie Co</title>
</head>
<body>
<% MovieDTO movie = (MovieDTO)request.getAttribute("movie"); %>
<% List<String> actors_list = Arrays.asList(movie.getActors().split("\\s*,\\s*")); %>
<% List<String> genres_list = Arrays.asList(movie.getGenre().split("\\s*,\\s*")); %>
<div align="center">
<h1><%= movie.getMovieName()%></h1>
Movie Poster Here<br>
</div>
<b>Release Date	: </b><%= movie.getReleaseDate()%><br><br>
<b>Genre	:</b>
<ul>
  <% for(String genre : genres_list) {%>
  <li><%= genre %></li>
  <% } %>
</ul>
</body>
</html>