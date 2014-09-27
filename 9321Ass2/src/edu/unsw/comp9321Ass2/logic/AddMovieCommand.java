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

	private boolean error = false;
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, CastDAO cast) throws IllegalStateException, IOException, ServletException, EmptyResultException {
		String message = "Successfully added movie";
		String forwardPage;
		InputStream inputStream = null;
		String title = getParam(request,"title");
		String actors = getParam(request,"actors");
		String genresList[]= request.getParameterValues("genres");
		String genres = convertList(genresList);
		String director = getParam(request,"director");
		String synopsis = getParam(request,"synopsis");
		String age_rating_str = getParam(request,"age_rating");
		int age_rating;
		//Age rating not an integer
		if(!age_rating_str.matches("^\\d*$")){
			age_rating = 0;
			error = true;
			request.getSession().setAttribute("message", "Age rating must be an integer.");
		}else{
			age_rating = Integer.parseInt(getParam(request,"age_rating"));
		}
		int releaseDate = Integer.parseInt(getIntParam(request,"release_date"));
		int releaseMmonth = Integer.parseInt(getIntParam(request,"release_month"));
		int releaseYear = Integer.parseInt(getIntParam(request,"release_year")) - 1901; //offset because java.
		@SuppressWarnings("deprecation")
		java.sql.Date date = new java.sql.Date(releaseYear,releaseMmonth,releaseDate);
		System.out.println("error"+error);
		//Dealing with adding a movie with a poster or no poster
		if(!error){
			System.out.println("No error detected.");
			Part filePart = request.getPart("poster");
			if (filePart != null) {
				System.out.println("Adding movie with poster");
		        inputStream = filePart.getInputStream();
		        MovieDTO newMovie = new MovieDTO(cast.lastMovie()+1, title, date, inputStream, genres,
		        		director,synopsis,actors,age_rating,null);
		        cast.addMovie(newMovie);
		        request.getSession().setAttribute("message", message);
			 } else{
				 MovieDTO newMovie = new MovieDTO(cast.lastMovie()+1, title, date, null, genres,
			        		director,synopsis,actors,age_rating,null);
			     cast.addMovie(newMovie);
			     request.getSession().setAttribute("message", message);
			 }
		}
	    forwardPage = "success.jsprunPRG";
	    return forwardPage;
	}
	
	//Makes sure there is no null value returned.
	private String getParam(HttpServletRequest request,String param){
		String output;
		output = request.getParameter(param);
		if(output == null || output.equals("")){
			output = "-1";
			request.getSession().setAttribute("message", "Error: Missing fields.");
			error = true;
		}
		return output;
	}
	
	private String getIntParam(HttpServletRequest request,String param){
		String output;
		output = request.getParameter(param);
		if(output == null || output.equals("") || !output.matches("^\\d*$")){
			output = "-1";
			request.getSession().setAttribute("message", "Error: Missing fields or date field is not correct");
			error = true;
		}
		return output;
	}
	
	private String convertList(String[] list){
		String listAsStr = "";
		if(list != null){
			for(String s:list){
				listAsStr = listAsStr + "," + s;
			}
		}
		//Remove leading comma
		if(listAsStr.length() > 0){
			listAsStr = listAsStr.substring(1);
		}
		return listAsStr;
	}

}
