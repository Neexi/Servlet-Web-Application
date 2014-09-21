package edu.unsw.comp9321Ass2.logic;
//https://Roidi@bitbucket.org/steeeveen/comp9321-ass2.git <- putting this thing here just in case
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
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
@MultipartConfig(maxFileSize = 16177215) // upload file's size up to 16MB

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
		logger.info("Action is " + action);
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
			UserDTO user = cast.findUser((String)session.getAttribute("userSess"));
			request.setAttribute("username", user.getUsername());
			request.setAttribute("email", user.getEmail());
			request.setAttribute("firstName", user.getFirstName());
			request.setAttribute("lastName", user.getLastName());
			forwardPage = "editprofile.jsp";
		} else if(action.equals("commit edit")) {
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
				//TODO : Can't check the email sending since my localhost has to support SMTP, the URL works manually however
				sendEmail(email, lastUserID);
				forwardPage = "redirect.html";
			} else {
				session.setAttribute("message", notLegit);
				forwardPage = "register.jsp";
			}
		} else if(action.equals("activate")) { 
			int userID = Integer.parseInt(request.getParameter("id"));
			cast.activateUser(userID);
			forwardPage = "activated.jsp";
		} else if(action.equals("newMovie")){
			forwardPage = "addMovie.jsp";
		
		} else if(action.equals("addMovie")){
			//logger.info("ADDING A MOVIE");
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

		     } else{
		    	 System.out.println("Filepart not found");
		     }
			 forwardPage = "home.jsp";
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
		} else if(username.length() < 4) {
			notLegit = "Username is too short";
		} else if(username.length() > 25) {
			notLegit = "Username is too long";
		} else if(password.length() < 4) {
			notLegit = "Password is too short";
		} else if(password.length() > 50) {
			notLegit = "Password is too long";
		} else if(email == null) {
			notLegit = "Email can't be empty";
		} else if(password.length() > 50) {
			notLegit = "Email is too long";
		} 
		return notLegit;
	}
	
	private String legitimateEditProfile(String email) throws EmptyResultException {
		String notLegit = "";
		//TODO : More proper parameter check
		return notLegit;
	}
	
	/**
	 * Sending activation email of specific user
	 * @param email
	 * @param userID
	 */
	private void sendEmail(String email, int userID) {
		// Recipient's email ID needs to be mentioned.
	      String to = email;

	      // Sender's email ID needs to be mentioned
	      String from = "CFMovieCo.net";

	      // Assuming you are sending email from localhost
	      String host = "localhost";

	      // Get system properties
	      Properties properties = System.getProperties();

	      // Setup mail server
	      properties.setProperty("mail.smtp.host", host);

	      // Get the default Session object.
	      Session session = Session.getDefaultInstance(properties);

	      try{
	         // Create a default MimeMessage object.
	         MimeMessage message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(from));

	         // Set To: header field of the header.
	         message.addRecipient(Message.RecipientType.TO,
	                                  new InternetAddress(to));

	         // Set Subject: header field
	         message.setSubject("Activation Email");
	         String messageBody = ("Please activate your account using following link : <br>" + "<a href =\'http://localhost:8080/9321Ass2/control?action=activate&id="+userID+"\'>");
	         // Send the actual HTML message, as big as you like
	         message.setContent(messageBody,
	                            "text/html" );

	         // Send message
	         Transport.send(message);
	         System.out.println("Sent message successfully....");
	      }catch (MessagingException mex) {
	         mex.printStackTrace();
	      }
	}

}
