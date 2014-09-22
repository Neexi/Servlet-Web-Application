package edu.unsw.comp9321Ass2.logic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.unsw.comp9321Ass2.exception.EmptyResultException;
import edu.unsw.comp9321Ass2.jdbc.CastDAO;

public class LogoutCommand implements Command {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, CastDAO cast) throws EmptyResultException {
		HttpSession session = request.getSession();
		String forwardPage;
		if(cast.checkLogin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess"))) {
			//logger.info(session.getAttribute("userSess")+" is now logged out");
			session.setAttribute("userSess","");
			session.setAttribute("passSess","");
			session.setAttribute("message", "You are now logged out");
		} else {
			forwardPage = "reject1.jsp";
		}
		forwardPage = "home.jsp";
		return forwardPage;
	}

}
