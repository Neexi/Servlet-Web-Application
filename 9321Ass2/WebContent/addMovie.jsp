<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<center>
        <h1>Add a new movie!</h1>
        <form method="post" action="control?action=addMovie" enctype="multipart/form-data">
            <table border="0">
                <tr>
                    <td>Movie Title: </td>
                    <td><input type="text" name="title" size="50"/></td>
                </tr>
                <tr>
                    <td>Actors: </td>
                    <td><input type="text" name="actors" size="50"/></td>
                </tr>
                <tr>
                    <td>Genre(s): </td>
                    <td><input type="text" name="genres" size="50"/></td>
                </tr>
                <tr>
                    <td>Director: </td>
                    <td><input type="text" name="director" size="50"/></td>
                </tr>
                <tr>
                    <td>Synopsis: </td>
                    <td><input type="text" name="synopsis" size="50"/></td>
                </tr>
                <tr>
                    <td>Age rating: </td>
                    <td><input type="text" name="age_rating" size="50"/></td>
                </tr>
                <tr>
                    <td>Release date: </td>
                    <td>Date
                    <input type="text" name="release_date" size="2"/>
                    Month
                    <input type="text" name="release_month" size="2"/>
                    Year
                    <input type="text" name="release_year" size="2"/>
                    </td>
                </tr>
                <tr>
                    <td>Poster: </td>
                    <td><input type="file" name="poster" size="50"/></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input type="submit" value="Add to database!">
                    </td>
                </tr>
            </table>
        </form>
    </center>
</body>
</html>