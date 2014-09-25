package edu.unsw.comp9321Ass2.logic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.unsw.comp9321Ass2.exception.EmptyResultException;
import edu.unsw.comp9321Ass2.jdbc.CastDAO;

public class CheckoutCommand implements Command {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, CastDAO cast) throws EmptyResultException {
		String forwardPage;
		if(cast.checkLogin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess"))) {
			int amount = Integer.parseInt(request.getParameter("amount"));
			int showtimeID = Integer.parseInt(request.getParameter("showtimeID"));
			if(cast.findMovieShowtimebyID(showtimeID).getBooked() + amount <= cast.findCinemaByID(cast.findMovieShowtimebyID(showtimeID).getCinemaID()).getCapacity()) {
				int userID = cast.findUser((String)request.getSession().getAttribute("userSess")).getID();
				cast.setBooked(showtimeID, amount);
				cast.addBooking(showtimeID, userID, amount);
				request.getSession().setAttribute("message", "Booking finished");
				forwardPage = "home.jsp";
			} else {
				request.getSession().setAttribute("message", "Problem occured in booking");
				forwardPage = "home.jsp";
			}
		} else {
			forwardPage = "reject1.jsp";
		}
		return forwardPage;
	}

}
