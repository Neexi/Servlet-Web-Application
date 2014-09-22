package edu.unsw.comp9321Ass2.logic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.unsw.comp9321Ass2.jdbc.CastDAO;

public interface Command {
	public abstract String execute(HttpServletRequest request, HttpServletResponse response,CastDAO cast);
}
