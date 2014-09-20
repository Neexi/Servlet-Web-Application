package edu.unsw.comp9321Ass2.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
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
	
	public boolean checkLogin(String username, String password) throws EmptyResultException{
		boolean found = false;
		try{
			String count_query = "SELECT COUNT(*) FROM TBL_USERS WHERE USER_NAME = ? AND USER_PASSWORD = ?";
			PreparedStatement count_stmnt = connection.prepareStatement(count_query);
			count_stmnt.setString(1, username);
			count_stmnt.setString(2, password);
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

}
