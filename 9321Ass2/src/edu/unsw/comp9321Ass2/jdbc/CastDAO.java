package edu.unsw.comp9321Ass2.jdbc;

import java.util.List;

import edu.unsw.comp9321Ass2.exception.EmptyResultException;

public interface CastDAO {
	
	public List<UserDTO> findAllUser();
	
	public UserDTO findUser(String username) throws EmptyResultException;
	
	public int lastUser() throws EmptyResultException;
	
	public boolean checkLogin(String username, String password) throws EmptyResultException;
	
	public boolean checkAdmin(String username, String password) throws EmptyResultException;
	
	public void addUser(UserDTO user);
	
	public void activateUser(int userID);
	
	public boolean existUser(String username) throws EmptyResultException;
	
	public boolean usedEmail(String email) throws EmptyResultException;
	
	public void editUser(String username, String email, String firstName, String lastName);
	
	public void addCinema(String location, int capacity, List<String> amenities);
	
	public int lastCinema() throws EmptyResultException;
	
	public int lastAmenity() throws EmptyResultException;
	
	public void addMovie(MovieDTO movie);
	
	public int lastMovie() throws EmptyResultException;
	
	public List<MovieDTO> getNowShowing(int noResults);
}
