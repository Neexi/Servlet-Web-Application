package edu.unsw.comp9321Ass2.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import edu.unsw.comp9321Ass2.common.ServiceLocatorException;
import edu.unsw.comp9321Ass2.exception.EmptyResultException;

public class DerbyDAOImpl implements CastDAO {

	static Logger logger = Logger.getLogger(DerbyDAOImpl.class.getName());
	private Connection connection;
	DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
	
	public DerbyDAOImpl() throws ServiceLocatorException, SQLException{
		connection = DBConnectionFactory.getConnection();
		logger.info("Got connection");
	}
	
	@Override
	
	//TODO:No proper usage other than to check if database is working for now <- Don't forget to delete this line before submitting
	public List<UserDTO> findAllUser() {
		ArrayList<UserDTO> users = new ArrayList<UserDTO>();
		try{
			logger.info("In");
			Statement stmnt = connection.createStatement();
			String query_cast = "SELECT * from TBL_USERS";
			ResultSet res = stmnt.executeQuery(query_cast);
			logger.info("The result set size is "+res.getFetchSize());
			while(res.next()){
				int userID = res.getInt("USER_ID");
				logger.info(" "+userID);
				String username = res.getString("USER_NAME");
				logger.info(username);
				String password = res.getString("USER_PASSWORD");
				logger.info(password);
				String email = res.getString("USER_EMAIL");
				logger.info(email);
				users.add(new UserDTO(userID, username, password, email));
			}
			
			res.close();
			stmnt.close();
			
		}catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
		}
		return users;
	}
	
	/**
	 * Count the number of users
	 * @throws EmptyResultException 
	 * 
	 */
	public int countUser() throws EmptyResultException {
		int count = 0;
		try{
			String count_query = "SELECT COUNT(*) FROM TBL_USERS";
			PreparedStatement count_stmnt = connection.prepareStatement(count_query);
			ResultSet count_res = count_stmnt.executeQuery();
			count_res.next();
			count = count_res.getInt(1);
		}catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
			throw new EmptyResultException();
		}
		return count;
	}
	
	/**
	 * Check if login info provided is legitimate
	 */
	public boolean checkLogin(String username, String password) throws EmptyResultException{
		boolean found = false;
		try{
			String count_query = "SELECT COUNT(*) FROM TBL_USERS WHERE USER_NAME = ? AND USER_PASSWORD = ? AND USER_STATUS = ?";
			PreparedStatement count_stmnt = connection.prepareStatement(count_query);
			count_stmnt.setString(1, username);
			count_stmnt.setString(2, password);
			count_stmnt.setString(3, "active");
			ResultSet count_res = count_stmnt.executeQuery();
			count_res.next();
			int numRows = count_res.getInt(1);
			if(numRows == 1) {
				found = true;
			}
		}catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
			throw new EmptyResultException();
		}
		return found;
	}
	
	/**
	 * Adding user to the database
	 * @param user
	 */
	public void addUser(UserDTO user) {
		
		PreparedStatement stmnt = null; 
		
		try{
			String sqlStr = 
				"INSERT INTO TBL_USERS (USER_ID, USER_NAME, USER_PASSWORD, USER_EMAIL, USER_FIRSTNAME, USER_LASTNAME, USER_STATUS) "+
				"VALUES (?,?,?,?,?,?,?)";
			stmnt = connection.prepareStatement(sqlStr);
			stmnt.setInt(1,user.getID());
			stmnt.setString(2, user.getUsername());
			stmnt.setString(3, user.getPassword());
			stmnt.setString(4, user.getEmail());
			stmnt.setString(5, user.getFirstName());
			stmnt.setString(6, user.getLastName());
			stmnt.setString(7, user.getStatus());
			int result = stmnt.executeUpdate();
			logger.info("Statement successfully executed "+result);
			logger.info(user.getUsername()+" has been registered to the database");
			stmnt.close();
		}catch(Exception e){
			logger.severe("Unable to store comment! ");
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Check if username is already exist
	 * @param username
	 * @return
	 * @throws EmptyResultException 
	 */
	public boolean existUser(String username) throws EmptyResultException {
		boolean exist = false;
		try{
			String count_query = "SELECT COUNT(*) FROM TBL_USERS WHERE USER_NAME = ?";
			PreparedStatement count_stmnt = connection.prepareStatement(count_query);
			count_stmnt.setString(1, username);
			ResultSet count_res = count_stmnt.executeQuery();
			count_res.next();
			int numRows = count_res.getInt(1);
			if(numRows == 1) {
				exist = true;
			}
		}catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
			throw new EmptyResultException();
		}
		return exist;
	}
	
	/**
	 * Check if email is already used
	 * @param email
	 * @return
	 * @throws EmptyResultException
	 */
	public boolean usedEmail(String email) throws EmptyResultException {
		boolean used = false;
		try{
			String count_query = "SELECT COUNT(*) FROM TBL_USERS WHERE USER_EMAIL = ?";
			PreparedStatement count_stmnt = connection.prepareStatement(count_query);
			count_stmnt.setString(1, email);
			ResultSet count_res = count_stmnt.executeQuery();
			count_res.next();
			int numRows = count_res.getInt(1);
			if(numRows == 1) {
				used = true;
			}
		}catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
			throw new EmptyResultException();
		}
		return used;
	}
	
	public void addMovie(MovieDTO movie) {
		//Right now only testing adding a image file called poster
		try {
			String sql = "INSERT INTO TBL_MOVIES (MOVIE_ID,POSTER) values (?,?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1,1);
			statement.setBlob(2, movie.getPoster());
			int result = statement.executeUpdate();
			logger.info("Statement successfully executed "+result);
			statement.close();
		} catch (SQLException e) {
			System.out.println("Caught Exception");
			e.printStackTrace();
		}
	}

}
