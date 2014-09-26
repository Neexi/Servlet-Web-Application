package edu.unsw.comp9321Ass2.logic;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.unsw.comp9321Ass2.exception.EmptyResultException;
import edu.unsw.comp9321Ass2.jdbc.CastDAO;
import edu.unsw.comp9321Ass2.jdbc.CinemaDTO;
import edu.unsw.comp9321Ass2.jdbc.MovieDTO;
import edu.unsw.comp9321Ass2.jdbc.ShowtimeDTO;
import edu.unsw.comp9321Ass2.jdbc.UserDTO;

public class ViewBookingsCommand implements Command {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, CastDAO cast)
			throws IllegalStateException, IOException, ServletException,
			EmptyResultException, ParseException {
		String forwardPage = null;
		if(cast.checkLogin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess"))) {
			
			String username = (String) request.getSession().getAttribute("userSess");
			UserDTO user = cast.findUser(username);
			int userid = user.getID();
			
			request.setAttribute("bookings", cast.getBookings(userid));
			forwardPage = "viewBookings.jsp";
		} else {
			forwardPage = "reject1.jsp";
		}
		return forwardPage;
	}

}
