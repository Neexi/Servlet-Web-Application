package edu.unsw.comp9321Ass2.logic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import edu.unsw.comp9321Ass2.jdbc.CastDAO;
import edu.unsw.comp9321Ass2.jdbc.MovieDTO;

public class AddMovieCommand implements Command {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, CastDAO cast) throws IllegalStateException, IOException, ServletException {
		// TODO Auto-generated method stub
		//logger.info("ADDING A MOVIE");
		String forwardPage;
		InputStream inputStream = null;
		Part filePart = request.getPart("poster");
		if (filePart != null) {
		        // prints out some information for debugging
		        System.out.println(filePart.getName());
		        System.out.println(filePart.getSize());
		        System.out.println(filePart.getContentType());
		         
		        // obtains input stream of the upload file
		        inputStream = filePart.getInputStream();
		        Date adate = new Date();
		        //Dummy variables for now, just want to test inserting one image.
		        MovieDTO newMovie = new MovieDTO(1, "test", adate, inputStream, "test",
		        		"test","test","test",15);
		        cast.addMovie(newMovie);
				 System.out.println("Added movie");
		
		 } else{
			 System.out.println("Filepart not found");
		 }
		 forwardPage = "home.jsp";
		 return forwardPage;
	}

}
