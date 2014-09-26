package edu.unsw.comp9321Ass2.logic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.unsw.comp9321Ass2.exception.EmptyResultException;
import edu.unsw.comp9321Ass2.jdbc.CastDAO;

public class CheckoutCommand implements Command {

	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, CastDAO cast) throws EmptyResultException {
		
		String forwardPage;
		if(cast.checkLogin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess"))) {
			int amount = Integer.parseInt(request.getParameter("amount"));
			int showtimeID = Integer.parseInt(request.getParameter("showtimeID"));
			String cardName = request.getParameter("cardName");
			String cardNum = request.getParameter("cardNo");
			int cardCSC = Integer.parseInt(request.getParameter("cardCSC"));
			
			if(cast.findMovieShowtimebyID(showtimeID).getBooked() + amount <= 
			   cast.findCinemaByID(cast.findMovieShowtimebyID(showtimeID).getCinemaID()).getCapacity()
			   && checkParams(request).equals("Success")){
				int userID = cast.findUser((String)request.getSession().getAttribute("userSess")).getID();
				cast.setBooked(showtimeID, amount);
				cast.addBooking(showtimeID, userID, amount,cardName,cardNum,cardCSC);
				request.getSession().setAttribute("message", "Booking finished");
				forwardPage = "home.jsp";
			} else {
				
				request.getSession().setAttribute("message", "Problem occured in booking:"+ checkParams(request));
				forwardPage = "home.jsp";
			}
		} else {
			forwardPage = "reject1.jsp";
		}
		return forwardPage;
	}
	
	private String checkParams(HttpServletRequest request){
		String output = "Success";
		String cardName = (String) request.getParameter("cardName");
		String cardNo = (String) request.getParameter("cardNo");
		String cardCSC = (String) request.getParameter("cardCSC");
		
		
		if(cardName == null){
			output = "Please enter a card name.";
		}else if(cardNo == null){
			output = "Please enter a card number.";
		}else if(cardCSC == null){
			output = "Please enter a card csc.";
		}else if(!cardNo.matches("^\\d{16}$")){
			output = "Please enter a valid card number.";
		}else if(!cardCSC.matches("^\\d{3}$")){
			output = "Please enter a valid card csc.";
		}else if(cardName.length() > 200){
			output = "Card name is too long.";
		}
		return output;
	}

}
