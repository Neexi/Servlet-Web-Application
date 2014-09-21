package edu.unsw.comp9321Ass2.logic;
//https://Roidi@bitbucket.org/steeeveen/comp9321-ass2.git <- putting this thing here just in case
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import edu.unsw.comp9321Ass2.exception.EmptyResultException;
import edu.unsw.comp9321Ass2.common.ServiceLocatorException;
import edu.unsw.comp9321Ass2.jdbc.CastDAO;
import edu.unsw.comp9321Ass2.jdbc.DerbyDAOImpl;
import edu.unsw.comp9321Ass2.jdbc.MovieDTO;
import edu.unsw.comp9321Ass2.jdbc.UserDTO;

/**
 * Servlet implementation class ControlServlet
 */
@WebServlet({"/Home","/control"})
public class ControlServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(ControlServlet.class.getName());
	private CastDAO cast;
	private int lastUserID;
	private boolean logged; //login status
       
    /**
     * @throws ServletException 
     * @throws EmptyResultException 
     * @see HttpServlet#HttpServlet()
     */
    public ControlServlet() throws ServletException, EmptyResultException {
        super();
        logged = false;
        try {
			cast = new DerbyDAOImpl();
			lastUserID = cast.countUser();
		} catch (ServiceLocatorException e) {
			logger.severe("Trouble connecting to database "+e.getStackTrace());
			throw new ServletException();
		} catch (SQLException e) {
			logger.severe("Trouble connecting to database "+e.getStackTrace());
			throw new ServletException();
		}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			processRequest(request,response);
		} catch (EmptyResultException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			processRequest(request,response);
		} catch (EmptyResultException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Main servlet function of controlling forward page
	 * @throws EmptyResultException 
	 */
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, EmptyResultException {
		String forwardPage = "";
		HttpSession session = request.getSession();
		session.setAttribute("message", ""); //Resetting message session attribute after it has been sent
		String action = request.getParameter("action");
		logger.info("Action" + action);
		if(logged == false) {
			if(cast.checkLogin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess"))) {
				logged = true;
			}
		}
		if(action==null){
			forwardPage = "home.jsp";
		} else if(action.equals("login")) { 
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			if(cast.checkLogin(username, password)) {
				session.setAttribute("userSess",username);
				session.setAttribute("passSess",password);
				//TODO: Proper Session timeout (need discussing)
				logger.info(username+" is now logged in");
				forwardPage = "redirect1.html";
			} else {
				session.setAttribute("message", "Incorrect Username or Password, please retry");
				forwardPage = "home.jsp";
			}
		} else if(action.equals("logout")) {
			logger.info(session.getAttribute("userSess")+" is now logged out");
			session.setAttribute("userSess","");
			session.setAttribute("passSess","");
			logged=false;
			session.setAttribute("message", "You are now logged out");
			forwardPage = "home.jsp";
		} else if(action.equals("edit profile")) {
			forwardPage = "editprofile.jsp";
		} else if(action.equals("commit edit")) {
			String password = request.getParameter("password");
			String passwordC = request.getParameter("passwordC");
			String email = request.getParameter("email");
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String notLegit = legitimateEditProfile(password, passwordC, email);
			if(notLegit.equals("")) {
				//TODO:Editing the database
				session.setAttribute("message", "Your profile has been changed");
				forwardPage = "redirect1.html";
			} else {
				session.setAttribute("message", notLegit);
				forwardPage = "editprofile.jsp";
			}
		} else if(action.equals("register")) { //Moving to the registration page
			forwardPage = "register.jsp";
		} else if(action.equals("create account")) { //Done putting information to create new account
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String email = request.getParameter("email");
			String notLegit = legitimateNewAccount(username, password, email);
			if(notLegit.equals("")) {
				UserDTO newUser = new UserDTO(lastUserID+1, username, password, email);
				lastUserID++;
				cast.addUser(newUser);
				//TODO: Sending email to activate the account
				forwardPage = "redirect.html";
			} else {
				session.setAttribute("message", notLegit);
				forwardPage = "register.jsp";
			}
		}else if(action.equals("newMovie")){
			forwardPage = "addMovie.jsp";
		
		}else if(action.equals("addMovie")){
			logger.info("ADDING A MOVIE");
			InputStream inputStream = null;
			Part filePart = request.getPart("poster");
			 if (filePart != null) {
		            // prints out some information for debugging
		            System.out.println(filePart.getName());
		            System.out.println(filePart.getSize());
		            System.out.println(filePart.getContentType());
		             
		            // obtains input stream of the upload file
		            inputStream = filePart.getInputStream();
		            Date adate = new Date();
		            //Dummy variables for now, just want to test inserting one image.
		            MovieDTO newMovie = new MovieDTO(1, "test", adate, inputStream, "test",
		            		"test","test","test",15);
		            cast.addMovie(newMovie);
					 System.out.println("Added movie");

		     }else{
		    	 System.out.println("Filepart not found");
		     }
			 forwardPage = "register.jsp";
		}else if(action.equals("return")) {
			forwardPage = "home.jsp";
		}
		session.setAttribute("logged",String.valueOf(logged));
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/"+forwardPage);
		dispatcher.forward(request, response);
	}
	
	/**
	 * Returning the problem with the account creation
	 * @param username
	 * @param password
	 * @param email
	 * @return
	 * @throws EmptyResultException
	 */
	private String legitimateNewAccount(String username, String password, String email) throws EmptyResultException {
		String notLegit = "";
		//TODO : More proper parameter check
		if(cast.existUser(username)) {
			notLegit = "Username already exists";
		} else if(username.length() < 5) {
			notLegit = "Username is too short";
		} else if(username.length() > 20) {
			notLegit = "Username is too long";
		} else if(cast.usedEmail(email)) {
			notLegit = "Email is already used";
		}
		return notLegit;
	}
	
	private String legitimateEditProfile(String password, String confirmPassword, String email) throws EmptyResultException {
		String notLegit = "";
		//TODO : More proper parameter check
		if(!password.equals("") && !password.equals(confirmPassword)) {
			notLegit = "Confirmation password does not match the new password";
		} else if(!email.equals("") && cast.usedEmail(email)) {
			notLegit = "Email is already used";
		}
		return notLegit;
	}

}
