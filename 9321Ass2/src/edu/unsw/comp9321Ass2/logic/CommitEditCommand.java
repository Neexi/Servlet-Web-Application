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
			String notLegit = legitimateEditProfile(email,firstName,lastName);
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
	
	private String legitimateEditProfile(String email,String firstName,String lastName) throws EmptyResultException {
		String notLegit = "";
		if(email == null) {
			notLegit = "Email can't be empty";
		}else if(!isValidEmailAddress(email)){
			notLegit = "Invalid email address";
		}else if(!firstName.matches("^\\w*$")){
			notLegit = "Invalid first name";
		}else if(!lastName.matches("^\\w*$")){
			notLegit = "Invalid last name";
		}
		return notLegit;
	}
	
	//Code taken from stackoverflow.
		//http://stackoverflow.com/questions/624581/what-is-the-best-java-email-address-validation-method
		public boolean isValidEmailAddress(String email) {
	        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
	        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
	        java.util.regex.Matcher m = p.matcher(email);
	        return m.matches();
		}

}
