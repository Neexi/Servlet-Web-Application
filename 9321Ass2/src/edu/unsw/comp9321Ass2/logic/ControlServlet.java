package edu.unsw.comp9321Ass2.logic;
//https://Roidi@bitbucket.org/steeeveen/comp9321-ass2.git <- putting this thing here just in case
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
	private HashMap<String, Command> commands;
	private String servletFilePath;
	
	public void init() throws ServletException{
		super.init();
    	HashMap<String, Command> commands = new HashMap<String, Command>();
    	commands.put("home",new HomeCommand(servletFilePath));
    	commands.put("login",new LoginCommand());
    	commands.put("logout",new LogoutCommand());
    	commands.put("edit profile",new EditProfileCommand());
    	commands.put("commit edit",new CommitEditCommand());
    	commands.put("register",new RegisterCommand());
    	commands.put("create account",new CreateAccountCommand());
    	commands.put("activate",new ActivateCommand());
    	commands.put("newMovie",new NewMovieCommand());
    	commands.put("addMovie",new AddMovieCommand());
    	commands.put("return",new ReturnCommand());
    	commands.put("admin",new AdminCommand());
    	commands.put("add cinema",new AddCinemaCommand());
    	commands.put("cinema added",new CinemaAddedCommand());
    	commands.put("search movie",new SearchMovieCommand());
    	commands.put("movie detail",new MovieDetailCommand());
    	commands.put("add review",new AddReviewCommand());
    	commands.put("add movie showtime",new AddMovieShowTimeCommand());
    	commands.put("movie showtime added",new MovieShowTimeAddedCommand());
    	commands.put("reject1", new Reject1Command());
    	commands.put("reject2", new Reject2Command());
    	commands.put("check movie showtime",new CheckMovieShowtimeCommand());
    	this.commands = commands;
    	this.servletFilePath = getServletContext().getRealPath("/");
    	System.out.println(this.servletFilePath);
    }
       
    /**
     * @throws ServletException 
     * @throws EmptyResultException 
     * @see HttpServlet#HttpServlet()
     */
    public ControlServlet() throws ServletException, EmptyResultException {
        super();
        try {
			cast = new DerbyDAOImpl();
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
			try {
				processRequest(request,response);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Main servlet function of controlling forward page
	 * @throws EmptyResultException 
	 * @throws ParseException 
	 * @throws IllegalStateException 
	 */
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, EmptyResultException, IllegalStateException, ParseException {
		HttpSession session = request.getSession();
		session.setAttribute("message", ""); //Resetting message session attribute after it has been sent
		String action = request.getParameter("action");
		String next;
		Command cmd;
		if(action==null){
			cmd = new HomeCommand(servletFilePath);
		}else{
			cmd = resolveCommand(request);
		}
		logger.info("Action is " + action);
		next = cmd.execute(request, response,cast);
		
		
		/*
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
			if(cast.checkLogin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess"))) {
				logger.info(session.getAttribute("userSess")+" is now logged out");
				session.setAttribute("userSess","");
				session.setAttribute("passSess","");
				session.setAttribute("message", "You are now logged out");
			} else {
				forwardPage = "reject1.jsp";
			}
			forwardPage = "home.jsp";
		} else if(action.equals("edit profile")) {
			if(cast.checkLogin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess"))) {
				UserDTO user = cast.findUser((String)session.getAttribute("userSess"));
				request.setAttribute("username", user.getUsername());
				request.setAttribute("email", user.getEmail());
				request.setAttribute("firstName", user.getFirstName());
				request.setAttribute("lastName", user.getLastName());
				forwardPage = "editprofile.jsp";
			} else {
				forwardPage = "reject1.jsp";
			}
		} else if(action.equals("commit edit")) {
			if(cast.checkLogin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess"))) {
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
			} else {
				forwardPage = "reject1.jsp";
			}
		} else if(action.equals("register")) { //Moving to the registration page
			forwardPage = "register.jsp";
		} else if(action.equals("create account")) { //Done putting information to create new account
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String email = request.getParameter("email");
			String notLegit = legitimateNewAccount(username, password, email);
			if(notLegit.equals("")) {
				UserDTO newUser = new UserDTO(cast.lastUser()+1, username, password, email);
				cast.addUser(newUser);
				//TODO : Can't check the email sending since my localhost has to support SMTP, the URL works manually however
				session.setAttribute("userSess", username);
				session.setAttribute("passSess", password);
				sendEmail(email, cast.lastUser());
				forwardPage = "redirect.html";
			} else {
				session.setAttribute("message", notLegit);
				forwardPage = "register.jsp";
			}
		} else if(action.equals("activate")) { 
			int userID = Integer.parseInt(request.getParameter("id"));
			cast.activateUser(userID);
			if(cast.checkLogin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess"))) {
				UserDTO user = cast.findUser((String)session.getAttribute("userSess"));
				request.setAttribute("username", user.getUsername());
				request.setAttribute("email", user.getEmail());
				request.setAttribute("firstName", user.getFirstName());
				request.setAttribute("lastName", user.getLastName());
				session.setAttribute("message", "Your account has been activated, you now can update your profile");
				forwardPage = "editprofile.jsp";
			} else {
				session.setAttribute("message", "You have been logged out from your previous session, please relog");
				forwardPage = "home.jsp";
			}
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
		} else if(action.equals("return")) {
			forwardPage = "home.jsp";
		} 
		//Admin part after this step
		else if(action.equals("admin")) { 
			if(cast.checkAdmin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess"))) {
				forwardPage = "admin.jsp";
			} else {
				forwardPage = "reject2.jsp";
			}
		} else if(action.equals("add cinema")) {
			if(cast.checkAdmin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess"))) {
				forwardPage = "addCinema.jsp";
			} else {
				forwardPage = "reject2.jsp";
			}
		} else if(action.equals("cinema added")) {
			if(cast.checkAdmin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess"))) {
				//TODO: proper checking for adding cinema(like capacity has to be number, etc)
				String location = request.getParameter("location");
				int capacity = Integer.parseInt(request.getParameter("capacity"));
				List<String> amenities = Arrays.asList(request.getParameterValues("amenity"));
				cast.addCinema(location, capacity, amenities);
				session.setAttribute("message", "Cinema added");
				forwardPage = "admin.jsp";
			} else {
				forwardPage = "reject2.jsp";
			}
		}
		*/
		session.setAttribute("cast", cast);
		session.setAttribute("logged",String.valueOf(cast.checkLogin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess"))));
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/"+next);
		dispatcher.forward(request, response);
	}
	
	private Command resolveCommand(HttpServletRequest request) { 
		Command cmd = (Command) commands.get(request.getParameter("action"));
		return cmd;
	}

}
