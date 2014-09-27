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
		this.servletFilePath = getServletContext().getRealPath("/");
    	HashMap<String, Command> commands = new HashMap<String, Command>();
    	commands.put("home",new HomeCommand(servletFilePath));
    	commands.put("login",new LoginCommand(servletFilePath));
    	commands.put("logout",new LogoutCommand());
    	commands.put("edit profile",new EditProfileCommand());
    	commands.put("commit edit",new CommitEditCommand());
    	commands.put("register",new RegisterCommand());
    	commands.put("create account",new CreateAccountCommand());
    	commands.put("activate",new ActivateCommand());
    	commands.put("newMovie",new NewMovieCommand());
    	commands.put("addMovie",new AddMovieCommand());
    	commands.put("return",new ReturnCommand(servletFilePath));
    	commands.put("admin",new AdminCommand());
    	commands.put("add cinema",new AddCinemaCommand());
    	commands.put("cinema added",new CinemaAddedCommand());
    	commands.put("search movie",new SearchMovieCommand());
    	commands.put("movie detail",new MovieDetailCommand(servletFilePath));
    	commands.put("add review",new AddReviewCommand());
    	commands.put("add movie showtime",new AddMovieShowTimeCommand());
    	commands.put("movie showtime added",new MovieShowTimeAddedCommand());
    	commands.put("reject1", new Reject1Command());
    	commands.put("reject2", new Reject2Command());
    	commands.put("check movie showtime",new CheckMovieShowtimeCommand());
    	commands.put("book ticket", new BookTicketCommand());
    	commands.put("checkout", new CheckoutCommand());
    	commands.put("view bookings",new ViewBookingsCommand());
    	this.commands = commands;
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
		
		session.setAttribute("cast", cast);
		session.setAttribute("logged",String.valueOf(cast.checkLogin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess"))));
		//Test for post-redirect-get
		//System.out.println("Next is"+next);
		if(next.matches(".*runPRG$")){
			next = next.replaceAll("runPRG$", "");
			response.sendRedirect(next);
		}else{
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/"+next);
			dispatcher.forward(request, response);
		}
		
		
	}
	
	private Command resolveCommand(HttpServletRequest request) { 
		Command cmd = (Command) commands.get(request.getParameter("action"));
		return cmd;
	}

}
