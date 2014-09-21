package edu.unsw.comp9321Ass2.jdbc;

import java.util.List;

import edu.unsw.comp9321Ass2.exception.EmptyResultException;

public interface CastDAO {
	
	public List<UserDTO> findAllUser();
	
	public int countUser() throws EmptyResultException;
	
	public boolean checkLogin(String username, String password) throws EmptyResultException;
	
	public void addUser(UserDTO user);
	
	public boolean existUser(String username) throws EmptyResultException;
	
	public boolean usedEmail(String email) throws EmptyResultException;
	
	public void addMovie(MovieDTO movie);
}
