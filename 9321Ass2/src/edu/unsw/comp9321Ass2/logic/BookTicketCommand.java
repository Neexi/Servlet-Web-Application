package edu.unsw.comp9321Ass2.logic;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.unsw.comp9321Ass2.exception.EmptyResultException;
import edu.unsw.comp9321Ass2.jdbc.CastDAO;
import edu.unsw.comp9321Ass2.jdbc.CinemaDTO;
import edu.unsw.comp9321Ass2.jdbc.MovieDTO;
import edu.unsw.comp9321Ass2.jdbc.ShowtimeDTO;

public class BookTicketCommand implements Command {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, CastDAO cast) throws EmptyResultException {
		String forwardPage;
		if(cast.checkLogin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess"))) {
			int movieID = Integer.parseInt(request.getParameter("movieID"));
			String amountStr = request.getParameter("ticket amount");
			int amount;
			if(amountStr != null && !amountStr.equals("") && amountStr.matches("^\\d*$")){
				amount = Integer.parseInt(amountStr);
			}else{
				amount = 0;
			}
			int showtimeID = Integer.parseInt(request.getParameter("showtimeID"));
			//If overbook don't accept booking
			if(amount > 0 && cast.findMovieShowtimebyID(showtimeID).getBooked() + amount <= cast.findCinemaByID(cast.findMovieShowtimebyID(showtimeID).getCinemaID()).getCapacity()) {
				int userID = cast.findUser((String)request.getSession().getAttribute("userSess")).getID();
				request.setAttribute("showtimeID", showtimeID);
				request.setAttribute("amount", amount);
				request.setAttribute("userID", userID);
				forwardPage = "checkout.jsp";
			}else{
				if(amount == 0){
					request.getSession().setAttribute("message","Please enter how many tickets you need.");
				}else{
					request.getSession().setAttribute("message","Not enough seat available");
				}
				MovieDTO movie = cast.findMovieByID(movieID);
				List<ShowtimeDTO> showtimeList = cast.findMovieShowtimebyMovieID(movieID);
				List<CinemaDTO> cinemaList = new ArrayList<CinemaDTO>();
				for(ShowtimeDTO showtime : showtimeList) {
					cinemaList.add(cast.findCinemaByID(showtime.getCinemaID()));
				}
				request.setAttribute("movie", movie);
				request.setAttribute("showtimeList", showtimeList);
				request.setAttribute("cinemaList", cinemaList);
				request.setAttribute("movieID",movieID);
				forwardPage = "checkMovieShowtime.jsp";
			}
		} else {
			forwardPage = "reject1.jsp";
		}
		return forwardPage;
	}

}
