package edu.unsw.comp9321Ass2.logic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.unsw.comp9321Ass2.exception.EmptyResultException;
import edu.unsw.comp9321Ass2.jdbc.CastDAO;
import edu.unsw.comp9321Ass2.jdbc.CinemaDTO;
import edu.unsw.comp9321Ass2.jdbc.MovieDTO;

public class MovieShowTimeAddedCommand implements Command {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, CastDAO cast) throws EmptyResultException, ParseException {
		String forwardPage;
		if(!cast.checkAdmin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess"))) {
			int movieID = Integer.parseInt(request.getParameter("movieID"));
			MovieDTO movie = cast.findMovieByID(movieID);
			int cinemaID = Integer.parseInt(request.getParameter("cinemaID"));
			String date = request.getParameter("date");
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
			java.util.Date utilDate = sdf1.parse(date);
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime()); 
			if(cast.availableMovieShowtime(cinemaID, sqlDate) && !movie.getReleaseDate().after(new Date())) {
				cast.addMovieShowtime(movieID, cinemaID, sqlDate);
				request.getSession().setAttribute("message", "Movie showtimes added to the cinema");
			} else if(movie.getReleaseDate().after(new Date())) {
				request.getSession().setAttribute("message", "Movie is not yet released");
			} else {
				request.getSession().setAttribute("message", "Cinema is already in use in that date");
			}
			List<CinemaDTO> cinemas = cast.findAllCinema();
			request.setAttribute("movie", movie);
			request.setAttribute("cinemas", cinemas);
			forwardPage = "addMovieShowtime.jsp";
		} else {
			forwardPage = "reject2.jsp";
		}
		return forwardPage;
	}

}

