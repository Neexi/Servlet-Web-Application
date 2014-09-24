package edu.unsw.comp9321Ass2.logic;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.unsw.comp9321Ass2.exception.EmptyResultException;
import edu.unsw.comp9321Ass2.jdbc.CastDAO;
import edu.unsw.comp9321Ass2.jdbc.CinemaDTO;
import edu.unsw.comp9321Ass2.jdbc.MovieDTO;

public class AddMovieShowTimeCommand implements Command {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, CastDAO cast) throws EmptyResultException {
		String forwardPage;
		if(!cast.checkAdmin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess"))) {
			int movieID = Integer.parseInt(request.getParameter("movieID"));
			MovieDTO movie = cast.findMovieByID(movieID);
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
