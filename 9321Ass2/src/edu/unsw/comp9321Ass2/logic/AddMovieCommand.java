package edu.unsw.comp9321Ass2.logic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import edu.unsw.comp9321Ass2.exception.EmptyResultException;
import edu.unsw.comp9321Ass2.jdbc.CastDAO;
import edu.unsw.comp9321Ass2.jdbc.MovieDTO;

public class AddMovieCommand implements Command {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, CastDAO cast) throws IllegalStateException, IOException, ServletException, EmptyResultException {
		// TODO Auto-generated method stub
		//logger.info("ADDING A MOVIE");
		String forwardPage;
		InputStream inputStream = null;
		
		String title = request.getParameter("title");
		String actors = request.getParameter("actors");
		String genres = request.getParameter("genres");
		String director = request.getParameter("director");
		String synopsis = request.getParameter("synopsis");
		int age_rating = Integer.parseInt(request.getParameter("age_rating"));
		int releaseDate = Integer.parseInt(request.getParameter("release_date"));
		int releaseMmonth = Integer.parseInt(request.getParameter("release_month"));
		int releaseYear = Integer.parseInt(request.getParameter("release_year")) - 1901; //offset for some reason..
		@SuppressWarnings("deprecation")
		java.sql.Date date = new java.sql.Date(releaseYear,releaseMmonth,releaseDate);
		
		//TODO : I change the else to allow me putting movie without poster for now, change it back later if i forget
		Part filePart = request.getPart("poster");
		if (filePart != null) {
	        // prints out some information for debugging
	        System.out.println(filePart.getName());
	        System.out.println(filePart.getSize());
	        System.out.println(filePart.getContentType());
	        // obtains input stream of the upload file
	        inputStream = filePart.getInputStream();
	        //Dummy variables for now, just want to test inserting one image.
	        MovieDTO newMovie = new MovieDTO(cast.lastMovie()+1, title, date, inputStream, genres,
	        		director,synopsis,actors,age_rating);
	        cast.addMovie(newMovie);
	        System.out.println("Added movie");
		 } else{
			 MovieDTO newMovie = new MovieDTO(cast.lastMovie()+1, title, date, null, genres,
		        		director,synopsis,actors,age_rating);
		     cast.addMovie(newMovie);
			 //System.out.println("Filepart not found");
		 }
		 forwardPage = "home.jsp";
		 return forwardPage;
	}

}
