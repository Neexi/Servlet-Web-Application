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
       
    /**
     * @throws ServletException 
     * @see HttpServlet#HttpServlet()
     */
    public ControlServlet() throws ServletException {
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
		String action = request.getParameter("action");
		if(action==null){
			forwardPage = "home.jsp";
		} else if(action.equals("login")) { 
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			if(cast.checkLogin(username, password)) {
				//TODO : Set Cookie
				forwardPage = "redirect1.html";
			} else {
				request.setAttribute("message", "Wrong Username or Password, please retry");
				forwardPage = "home.jsp";
			}
		} else if(action.equals("register")) {
			forwardPage = "register.jsp";
		} else if(action.equals("create account")) {
			forwardPage = "redirect.html";
		}
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/"+forwardPage);
		dispatcher.forward(request, response);
	}

}
