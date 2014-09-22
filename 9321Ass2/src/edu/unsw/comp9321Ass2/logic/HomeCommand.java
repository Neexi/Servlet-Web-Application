package edu.unsw.comp9321Ass2.logic;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.unsw.comp9321Ass2.exception.EmptyResultException;
import edu.unsw.comp9321Ass2.jdbc.CastDAO;

public class HomeCommand implements Command {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, CastDAO cast)
			throws IllegalStateException, IOException, ServletException,
			EmptyResultException {
		String forwardPage = "home.jsp";
		request.setAttribute("nowShowing", cast.getNowShowing(3));
		return forwardPage;
	}

}
