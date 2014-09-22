package edu.unsw.comp9321Ass2.logic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.unsw.comp9321Ass2.exception.EmptyResultException;
import edu.unsw.comp9321Ass2.jdbc.CastDAO;

public class LoginCommand implements Command {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, CastDAO cast) throws EmptyResultException {
		String forwardPage;
		HttpSession session = request.getSession();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if(cast.checkLogin(username, password)) {
			session.setAttribute("userSess",username);
			session.setAttribute("passSess",password);
			//TODO: Proper Session timeout (need discussing)
			//logger.info(username+" is now logged in");
			forwardPage = "redirect1.html";
		} else {
			session.setAttribute("message", "Incorrect Username or Password, please retry");
			forwardPage = "home.jsp";
		}
		return forwardPage;
	}

}
