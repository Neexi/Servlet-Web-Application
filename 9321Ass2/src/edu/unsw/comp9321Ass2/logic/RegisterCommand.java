package edu.unsw.comp9321Ass2.logic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.unsw.comp9321Ass2.jdbc.CastDAO;

public class RegisterCommand implements Command {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, CastDAO cast) {
		String forwardPage;
		forwardPage = "register.jsp";
		return forwardPage;
	}

}
