package edu.unsw.comp9321Ass2.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.unsw.comp9321Ass2.exception.EmptyResultException;
import edu.unsw.comp9321Ass2.jdbc.CastDAO;

public class CinemaAddedCommand implements Command {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, CastDAO cast) throws NumberFormatException, EmptyResultException {
		HttpSession session = request.getSession();
		String forwardPage;
		if(cast.checkAdmin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess"))) {
			//TODO: proper checking for adding cinema(like capacity has to be number, etc)
			String location = request.getParameter("location");
			int capacity = Integer.parseInt(request.getParameter("capacity"));
			List<String> amenities = new ArrayList<String>();
			if(request.getParameterValues("amenity") != null) {
				amenities = Arrays.asList(request.getParameterValues("amenity"));
			}
			cast.addCinema(location, capacity, amenities);
			session.setAttribute("message", "Cinema added");
			forwardPage = "redirect1.html";
		} else {
			forwardPage = "reject2.jsp";
		}
		return forwardPage;
	}

}
