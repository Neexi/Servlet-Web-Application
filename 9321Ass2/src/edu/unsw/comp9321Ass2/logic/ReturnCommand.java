package edu.unsw.comp9321Ass2.logic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.unsw.comp9321Ass2.jdbc.CastDAO;

public class ReturnCommand implements Command {
	
	private String filePath;
	public ReturnCommand(String filePath){
		this.filePath = filePath;
	}
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, CastDAO cast) {
		// TODO Auto-generated method stub
		request.setAttribute("nowShowing", cast.getNowShowing(10,filePath));
		request.setAttribute("comingSoon", cast.getComingSoon(10,filePath));
		return "home.jsp";
	}

}
