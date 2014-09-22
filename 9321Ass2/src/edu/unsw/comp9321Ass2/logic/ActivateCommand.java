package edu.unsw.comp9321Ass2.logic;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.unsw.comp9321Ass2.exception.EmptyResultException;
import edu.unsw.comp9321Ass2.jdbc.CastDAO;
import edu.unsw.comp9321Ass2.jdbc.UserDTO;

public class ActivateCommand implements Command {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, CastDAO cast)
			throws IllegalStateException, IOException, ServletException,
			EmptyResultException {
		HttpSession session = request.getSession();
		String forwardPage;
		int userID = Integer.parseInt(request.getParameter("id"));
		cast.activateUser(userID);
		if(cast.checkLogin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess"))) {
			UserDTO user = cast.findUser((String)session.getAttribute("userSess"));
			request.setAttribute("username", user.getUsername());
			request.setAttribute("email", user.getEmail());
			request.setAttribute("firstName", user.getFirstName());
			request.setAttribute("lastName", user.getLastName());
			session.setAttribute("message", "Your account has been activated, you now can update your profile");
			forwardPage = "editprofile.jsp";
		} else {
			session.setAttribute("message", "You have been logged out from your previous session, please relog");
			forwardPage = "home.jsp";
		}
		return forwardPage;
	}

}
