package edu.unsw.comp9321Ass2.logic;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.unsw.comp9321Ass2.exception.EmptyResultException;
import edu.unsw.comp9321Ass2.jdbc.CastDAO;

public class MovieDetailCommand implements Command {
	
	private String filePath;
	public MovieDetailCommand(String filePath){
		this.filePath = filePath;
	}
	
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, CastDAO cast) throws EmptyResultException {
		String forwardPage;
		if(cast.checkLogin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess"))) {
			int movieID = Integer.parseInt(request.getParameter("movieID"));
			request.setAttribute("movie",cast.findMovieByID(movieID));
			request.setAttribute("reviews", cast.getMovieReview(movieID));
			request.setAttribute("poster", cast.getMoviePoster(movieID,filePath));
			forwardPage = "movieDetail.jsp";
		} else {
			forwardPage = "reject1.jsp";
		}
		return forwardPage;
	}
}
