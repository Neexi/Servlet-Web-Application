package edu.unsw.comp9321Ass2.logic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.unsw.comp9321Ass2.exception.EmptyResultException;
import edu.unsw.comp9321Ass2.jdbc.CastDAO;

public class LoginCommand implements Command {

	private String filePath;
	public LoginCommand(String filePath){
		this.filePath = filePath;
	}
	
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, CastDAO cast) throws EmptyResultException {
		String forwardPage;
		HttpSession session = request.getSession();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if(checkParams(request).equals("Success")){
			if(cast.checkLogin(username, password)) {
				session.setAttribute("userSess",username);
				session.setAttribute("passSess",password);
				//TODO: Proper Session timeout (need discussing)
				forwardPage = "redirect1.html";
			} else {
				session.setAttribute("message", "Incorrect Username or Password, please retry");
				forwardPage = "home.jsp";
			}
			
		}else{
			session.setAttribute("message", checkParams(request));
			forwardPage = "home.jsp";
		}
		request.setAttribute("nowShowing", cast.getNowShowing(10,filePath));
		request.setAttribute("comingSoon", cast.getComingSoon(10,filePath));
		return forwardPage+"";
	}
	
	private String checkParams(HttpServletRequest request){
		String output = "Success";
		String username = (String) request.getParameter("username");
		String password = (String) request.getParameter("password");		
		if(username == null){
			output = "Please enter a username.";
			return output;
		}else if(password == null){
			output = "Please enter a password.";
			return output;
		}else if(!username.matches("^.{1,25}$")){
			output = "Username too short or long.";
			return output;
		}else if(!username.matches("^(\\w|\\s|\\d)*$")){
			output = "Invalid characters in username.";
			return output;
		}else if(!password.matches("^.{1,50}$")){
			output = "Password is too short or long.";
			return output;
		}else if(!password.matches("^(\\w|\\s|\\d)*$")){
			output = "Invalid characters in password.";
			return output;
		}
		return output;
	}

}
