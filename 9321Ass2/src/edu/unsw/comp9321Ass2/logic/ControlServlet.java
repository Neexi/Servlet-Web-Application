package edu.unsw.comp9321Ass2.logic;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.unsw.comp9321Ass2.exception.EmptyResultException;
import edu.unsw.comp9321Ass2.exception.InvalidActionException;
import edu.unsw.comp9321Ass2.common.ServiceLocatorException;
import edu.unsw.comp9321Ass2.jdbc.CastDAO;
import edu.unsw.comp9321Ass2.jdbc.DerbyDAOImpl;

/**
 * Servlet implementation class ControlServlet
 */
@WebServlet({"/Home","/control"})
public class ControlServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(ControlServlet.class.getName());
	private CastDAO cast;
	private boolean logged; //login status
       
    /**
     * @throws ServletException 
     * @see HttpServlet#HttpServlet()
     */
    public ControlServlet() throws ServletException {
        super();
        logged = false;
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
		String username = "";
		String password = "";
		HttpSession session = request.getSession();
		String action = request.getParameter("action");
		logger.info("Login is "+logged);
		if(logged == false) {
			if(cast.checkLogin((String)request.getSession().getAttribute("userSess"),(String)request.getSession().getAttribute("passSess"))) {
				logged = true;
			}
		}
		if(action==null){
			forwardPage = "home.jsp";
		} else if(action.equals("login")) { 
			username = request.getParameter("username");
			password = request.getParameter("password");
			if(cast.checkLogin(username, password)) {
				session.setAttribute("userSess",username);
				session.setAttribute("passSess",password);
				//TODO : Session timeout
				forwardPage = "redirect1.html";
			} else {
				request.setAttribute("message", "Wrong Username or Password, please retry");
				forwardPage = "home.jsp";
			}
		} else if(action.equals("logout")) {
			session.setAttribute("userSess","");
			session.setAttribute("passSess","");
			logged=false;
			request.setAttribute("message", "You are now logged out");
			forwardPage = "home.jsp";
		} else if(action.equals("register")) {
			forwardPage = "register.jsp";
		} else if(action.equals("create account")) {
			forwardPage = "redirect.html";
		}
		
		session.setAttribute("logged",String.valueOf(logged));
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/"+forwardPage);
		dispatcher.forward(request, response);
	}

}
