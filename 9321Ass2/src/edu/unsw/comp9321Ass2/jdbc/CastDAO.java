package edu.unsw.comp9321Ass2.jdbc;

import java.util.List;

import edu.unsw.comp9321Ass2.exception.EmptyResultException;

public interface CastDAO {
	
	public List<UserDTO> findAllUser();
	
	public boolean checkLogin(String username, String password) throws EmptyResultException;

}
