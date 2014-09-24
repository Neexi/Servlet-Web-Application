package edu.unsw.comp9321Ass2.logic;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.unsw.comp9321Ass2.exception.EmptyResultException;
import edu.unsw.comp9321Ass2.jdbc.CastDAO;

public interface Command {
	public abstract String execute(HttpServletRequest request, HttpServletResponse response,CastDAO cast) throws IllegalStateException, IOException, ServletException, EmptyResultException, ParseException;
}
