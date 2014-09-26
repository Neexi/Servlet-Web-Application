package edu.unsw.comp9321Ass2.logic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.unsw.comp9321Ass2.exception.EmptyResultException;
import edu.unsw.comp9321Ass2.jdbc.CastDAO;

public class AddReviewCommand implements Command {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, CastDAO cast) throws EmptyResultException {
		String forwardPage;
		if(cast.checkLogin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess"))) {
			int movieID = Integer.parseInt(request.getParameter("movieID"));
			String review_paragraph = request.getParameter("review paragraph");
			int review_rating = Integer.parseInt(request.getParameter("review rating"));
			String review_name = request.getParameter("review name");
			if(review_name.equals("")) {
				review_name = (String)request.getSession().getAttribute("userSess");
			}
			cast.addReview(movieID, review_paragraph, review_rating, review_name);
			System.out.println("review added");
			forwardPage = "redirect1.html";
		} else {
			forwardPage = "reject1.jsp";
		}
		return forwardPage;
	}

}
