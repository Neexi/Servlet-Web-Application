package edu.unsw.comp9321Ass2.logic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.unsw.comp9321Ass2.exception.EmptyResultException;
import edu.unsw.comp9321Ass2.jdbc.CastDAO;
import edu.unsw.comp9321Ass2.jdbc.UserDTO;

public class EditProfileCommand implements Command {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, CastDAO cast) throws EmptyResultException {
		HttpSession session = request.getSession();
		String forwardPage;
		if(cast.checkLogin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess"))) {
			UserDTO user = cast.findUser((String)session.getAttribute("userSess"));
			request.setAttribute("username", user.getUsername());
			request.setAttribute("email", user.getEmail());
			request.setAttribute("nickName", user.getNickName());
			request.setAttribute("firstName", user.getFirstName());
			request.setAttribute("lastName", user.getLastName());
			forwardPage = "editprofile.jsp";
		} else {
			forwardPage = "reject1.jsp";
		}
		return forwardPage;
	}

}
