package edu.unsw.comp9321Ass2.logic;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.unsw.comp9321Ass2.exception.EmptyResultException;
import edu.unsw.comp9321Ass2.jdbc.CastDAO;
import edu.unsw.comp9321Ass2.jdbc.MovieDTO;

public class SearchMovieCommand implements Command {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, CastDAO cast) throws EmptyResultException {
		String forwardPage;
		if(cast.checkLogin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess"))) {
			String search = (String)request.getParameter("search");
			List<MovieDTO> titleMatch = cast.searchMovieTitle(search);
			List<MovieDTO> genreMatch = cast.searchMovieGenre(search);
			request.setAttribute("titleMatch", titleMatch);
			request.setAttribute("genreMatch", genreMatch);
			request.setAttribute("search", search);
			request.setAttribute("cast", cast);
			forwardPage = "searchresult.jsp";
		} else {
			forwardPage = "reject1.jsp";
		}
		return forwardPage;
	}

}

