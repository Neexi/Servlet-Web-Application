package edu.unsw.comp9321Ass2.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.unsw.comp9321Ass2.exception.EmptyResultException;
import edu.unsw.comp9321Ass2.jdbc.CastDAO;
import edu.unsw.comp9321Ass2.jdbc.CinemaDTO;
import edu.unsw.comp9321Ass2.jdbc.MovieDTO;
import edu.unsw.comp9321Ass2.jdbc.ShowtimeDTO;

public class CheckMovieShowtimeCommand implements Command {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, CastDAO cast) throws EmptyResultException {
		String forwardPage;
		if(cast.checkLogin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess"))) {
			int movieID = Integer.parseInt(request.getParameter("movieID"));
			MovieDTO movie = cast.findMovieByID(movieID);
			if(!movie.getReleaseDate().after(new Date())) {
				List<ShowtimeDTO> showtimeList = cast.findMovieShowtimebyMovieID(movieID);
				List<CinemaDTO> cinemaList = new ArrayList<CinemaDTO>();
				for(ShowtimeDTO showtime : showtimeList) {
					cinemaList.add(cast.findCinemaByID(showtime.getCinemaID()));
				}
				request.setAttribute("movie", movie);
				request.setAttribute("showtimeList", showtimeList);
				request.setAttribute("cinemaList", cinemaList);
				forwardPage = "checkMovieShowtime.jsp";
			} else {
				request.getSession().setAttribute("message", "Movie is not yet released");
				forwardPage = "home.jsp";
			}
		} else {
			forwardPage = "reject2.jsp";
		}
		return forwardPage;
	}

}
