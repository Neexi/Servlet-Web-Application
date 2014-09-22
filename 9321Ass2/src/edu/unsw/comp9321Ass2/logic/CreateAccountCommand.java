package edu.unsw.comp9321Ass2.logic;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.unsw.comp9321Ass2.exception.EmptyResultException;
import edu.unsw.comp9321Ass2.jdbc.CastDAO;
import edu.unsw.comp9321Ass2.jdbc.UserDTO;

public class CreateAccountCommand implements Command {

	private CastDAO cast;
	@Override
	public String execute(HttpServletRequest request,
			HttpServletResponse response, CastDAO cast)
			throws IllegalStateException, IOException, ServletException,
			EmptyResultException {
		this.cast = cast;
		HttpSession session = request.getSession();
		String forwardPage;
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
		return forwardPage;
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
