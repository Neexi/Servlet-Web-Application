package edu.unsw.comp9321Ass2.logic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.unsw.comp9321Ass2.exception.EmptyResultException;
import edu.unsw.comp9321Ass2.jdbc.CastDAO;

public class CommitEditCommand implements Command {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, CastDAO cast) throws EmptyResultException {
		HttpSession session = request.getSession();
		String forwardPage;
		if(cast.checkLogin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess"))) {
			String username = (String)session.getAttribute("userSess");
			String email = request.getParameter("email");
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String notLegit = legitimateEditProfile(email);
			request.setAttribute("username", username);
			request.setAttribute("email", email);
			request.setAttribute("firstName", firstName);
			request.setAttribute("lastName", lastName);
			if(notLegit.equals("")) {
				cast.editUser(username, email, firstName, lastName);
				session.setAttribute("message", "Your profile has been changed");
				forwardPage = "editprofile.jsp";
			} else {
				session.setAttribute("message", notLegit);
				forwardPage = "editprofile.jsp";
			}
		} else {
			forwardPage = "reject1.jsp";
		}
		return forwardPage;
	}
	
	private String legitimateEditProfile(String email) throws EmptyResultException {
		String notLegit = "";
		//TODO : More proper parameter check
		return notLegit;
	}

}
