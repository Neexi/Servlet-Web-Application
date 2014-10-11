package edu.unsw.comp9321Ass2.jdbc;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import edu.unsw.comp9321Ass2.common.ServiceLocatorException;
import edu.unsw.comp9321Ass2.exception.EmptyResultException;

public class DerbyDAOImpl implements CastDAO {

	static Logger logger = Logger.getLogger(DerbyDAOImpl.class.getName());
	private Connection connection;
	DateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
	
	public DerbyDAOImpl() throws ServiceLocatorException, SQLException, EmptyResultException{
		connection = DBConnectionFactory.getConnection();
		logger.info("Got connection");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// User related function
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	
	//TODO:No proper usage other than to check if database is working for now <- Don't forget to delete this line before submitting
	public List<UserDTO> findAllUser() {
		ArrayList<UserDTO> users = new ArrayList<UserDTO>();
		try{
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
	
	public UserDTO findUser(String username) throws EmptyResultException{
		UserDTO user = null;	
		try{
			
			String query_cast = "SELECT * FROM TBL_USERS WHERE USER_NAME = ?";
			PreparedStatement stmnt = connection.prepareStatement(query_cast);
			stmnt.setString(1, username);
			ResultSet res = stmnt.executeQuery();
			res.next();
			user = new UserDTO(res.getInt("USER_ID"), res.getString("USER_NAME"), res.getString("USER_PASSWORD"), res.getString("USER_EMAIL"), 
					res.getString("USER_NICKNAME"),res.getString("USER_FIRSTNAME"),res.getString("USER_LASTNAME"), res.getString("USER_STATUS"));
		}catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
			throw new EmptyResultException();
		}
		return user;
	}
	
	public int lastIndex(String table, String primary) throws EmptyResultException {
		int index = 0;
		try{
			String count_query = "SELECT MAX("+primary+") FROM "+table;
			PreparedStatement count_stmnt = connection.prepareStatement(count_query);
			ResultSet count_res = count_stmnt.executeQuery();
			count_res.next();
			index = count_res.getInt(1);
		}catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
			throw new EmptyResultException();
		}
		return index;
	}
	
	/**
	 * Check if login info provided is legitimate
	 */
	public boolean checkLogin(String username, String password) throws EmptyResultException{
		boolean found = false;
		try{
			String count_query = "SELECT COUNT(*) FROM TBL_USERS WHERE USER_NAME = ? AND USER_PASSWORD = ? AND (USER_STATUS = ? OR USER_STATUS = ?)";
			PreparedStatement count_stmnt = connection.prepareStatement(count_query);
			count_stmnt.setString(1, username);
			count_stmnt.setString(2, password);
			count_stmnt.setString(3, "active"); //User account has to be activated
			count_stmnt.setString(4, "admin"); //User has to be admin
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
	
	public boolean checkAdmin(String username, String password) throws EmptyResultException{
		boolean found = false;
		try{
			String count_query = "SELECT COUNT(*) FROM TBL_USERS WHERE USER_NAME = ? AND USER_PASSWORD = ? AND USER_STATUS = ?";
			PreparedStatement count_stmnt = connection.prepareStatement(count_query);
			count_stmnt.setString(1, username);
			count_stmnt.setString(2, password);
			count_stmnt.setString(3, "admin"); //User has to be admin
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
				"INSERT INTO TBL_USERS (USER_ID, USER_NAME, USER_PASSWORD, USER_EMAIL, USER_NICKNAME, USER_FIRSTNAME, USER_LASTNAME, USER_STATUS) "+
				"VALUES (?,?,?,?,?,?,?,?)";
			stmnt = connection.prepareStatement(sqlStr);
			stmnt.setInt(1,user.getID());
			stmnt.setString(2, user.getUsername());
			stmnt.setString(3, user.getPassword());
			stmnt.setString(4, user.getEmail());
			stmnt.setString(5, user.getNickName());
			stmnt.setString(6, user.getFirstName());
			stmnt.setString(7, user.getLastName());
			stmnt.setString(8, user.getStatus());
			int result = stmnt.executeUpdate();
			logger.info("Statement successfully executed "+result);
			logger.info(user.getUsername()+" has been registered to the database");
			stmnt.close();
		}catch(Exception e){
			logger.severe("Unable to add user! ");
			e.printStackTrace();
		}
		
	}
	
	public void activateUser(int userID) {
		PreparedStatement stmnt = null; 
		try{
			String sqlStr = 
				"UPDATE TBL_USERS SET USER_STATUS = 'active' "+
				"WHERE USER_ID = ?";
			stmnt = connection.prepareStatement(sqlStr);
			stmnt.setInt(1, userID);
			int result = stmnt.executeUpdate();
			logger.info("Statement successfully executed "+result);
			logger.info("Account activated");
			stmnt.close();
		}catch(Exception e){
			logger.severe("Unable change user profile! ");
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
	
	public void editUser(String username, String email, String nickName, String firstName, String lastName) {
		PreparedStatement stmnt = null; 
		try{
			String sqlStr = 
				"UPDATE TBL_USERS SET USER_EMAIL = ?, USER_NICKNAME = ?, USER_FIRSTNAME = ?, USER_LASTNAME = ? "+
				"WHERE USER_NAME = ?";
			stmnt = connection.prepareStatement(sqlStr);
			stmnt.setString(1, email);
			stmnt.setString(2, nickName);
			stmnt.setString(3, firstName);
			stmnt.setString(4, lastName);
			stmnt.setString(5, username);
			int result = stmnt.executeUpdate();
			logger.info("Statement successfully executed "+result);
			logger.info(username+" profile has been changed");
			stmnt.close();
		}catch(Exception e){
			logger.severe("Unable change user profile! ");
			e.printStackTrace();
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Admin related function
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void addCinema(String location, int capacity, List<String> amenities) throws EmptyResultException {
		PreparedStatement stmnt = null; 
		int cinemaID = lastIndex("TBL_CINEMAS","CINEMA_ID")+1;
		try{
			String sqlStr = 
				"INSERT INTO TBL_CINEMAS (CINEMA_ID, CINEMA_LOCATION, CINEMA_CAPACITY) "+
				"VALUES (?,?,?)";
			stmnt = connection.prepareStatement(sqlStr);
			stmnt.setInt(1,cinemaID);
			stmnt.setString(2, location);
			stmnt.setInt(3, capacity);
			int result = stmnt.executeUpdate();
			logger.info("Statement successfully executed "+result);
			logger.info("Cinema has been registered to the database");
			stmnt.close();
		}catch(Exception e){
			logger.severe("Unable to add cinema! ");
			e.printStackTrace();
		}
		
		for(String amenity : amenities) {
			try{
				String sqlStr = 
					"INSERT INTO TBL_CINEMA_AMENITIES (AMENITY_ID, AMENITY_NAME, AMENITY_CINEMA) "+
					"VALUES (?,?,?)";
				stmnt = connection.prepareStatement(sqlStr);
				stmnt.setInt(1,lastIndex("TBL_CINEMA_AMENITIES","AMENITY_ID")+1);
				stmnt.setString(2, amenity);
				stmnt.setInt(3, cinemaID);
				int result = stmnt.executeUpdate();
				logger.info("Statement successfully executed "+result);
				logger.info("Amenity has been registered to the database");
				stmnt.close();
			}catch(Exception e){
				logger.severe("Unable to add amenity! ");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Returning list of all movie id and its title
	 * @return
	 */
	public List<CinemaDTO> findAllCinema() {
		List<CinemaDTO> allCinemas = new ArrayList<CinemaDTO>();
		try{
			Statement stmnt = connection.createStatement();
			String query_cast = "SELECT * from TBL_CINEMAS";
			ResultSet res = stmnt.executeQuery(query_cast);
			while(res.next()){
				int cinemaID = res.getInt("CINEMA_ID");
				String location = res.getString("CINEMA_LOCATION");
				int capacity = res.getInt("CINEMA_CAPACITY");
				allCinemas.add(new CinemaDTO(cinemaID, location, capacity));
			}
			res.close();
			stmnt.close();
			
		}catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
		}
		return allCinemas;
	}
	
	/**
	 * Return a cinema based on it's ID
	 * @param id
	 * @return
	 * @throws EmptyResultException
	 */
	public CinemaDTO findCinemaByID(int id) throws EmptyResultException {
		CinemaDTO result = null;
		try{
			
			String query_cast = "SELECT * FROM TBL_CINEMAS WHERE CINEMA_ID = ?";
			PreparedStatement stmnt = connection.prepareStatement(query_cast);
			stmnt.setInt(1, id);
			ResultSet res = stmnt.executeQuery();
			res.next();
			int cinemaID = res.getInt("CINEMA_ID");
			String location = res.getString("CINEMA_LOCATION");
			int capacity = res.getInt("CINEMA_CAPACITY");
			result = new CinemaDTO(cinemaID, location, capacity);
		}catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
			throw new EmptyResultException();
		}
		return result;
	}
	
	public void addMovieShowtime(int movieID, int cinemaID, java.sql.Date date, java.sql.Time time) throws ParseException {
		PreparedStatement stmnt = null;
		try{
			String sqlStr = 
				"INSERT INTO TBL_MOVIE_SHOWTIMES (MOVIE_SHOWTIME_ID, MOVIE_ID, CINEMA_ID, MOVIE_DATE, MOVIE_TIME, BOOKED) "+
				"VALUES (?,?,?,?,?,?)";
			stmnt = connection.prepareStatement(sqlStr);
			stmnt.setInt(1,lastIndex("TBL_MOVIE_SHOWTIMES","MOVIE_SHOWTIME_ID")+1);
			stmnt.setInt(2, movieID);
			stmnt.setInt(3, cinemaID);
			stmnt.setDate(4, date);
			stmnt.setTime(5, time);
			stmnt.setInt(6, 0);
			int result = stmnt.executeUpdate();
			logger.info("Statement successfully executed "+result);
			logger.info("Movie Showtime has been registered to the database");
			stmnt.close();
		}catch(Exception e){
			logger.severe("Unable to add Movie Showtime! ");
			e.printStackTrace();
		}
	}
	
	/**
	 * check if the movie time is still available to be added at that specific cinema, date, time
	 */
	public boolean availableMovieShowtime(int cinemaID, java.sql.Date date, java.sql.Time time) throws ParseException {
		boolean available = true;
		PreparedStatement stmnt = null;
		try{
			String sqlStr = 
				"SELECT COUNT(*) FROM TBL_MOVIE_SHOWTIMES WHERE CINEMA_ID = ? AND MOVIE_DATE = ? AND MOVIE_TIME = ?";
			stmnt = connection.prepareStatement(sqlStr);
			stmnt.setInt(1, cinemaID);
			stmnt.setDate(2, date);
			stmnt.setTime(3, time);
			ResultSet count_res = stmnt.executeQuery();
			count_res.next();
			if(count_res.getInt(1) > 0) {
				available = false;
			}
			stmnt.close();
		}catch(Exception e){
			logger.severe("Error in searching movie showtimes! ");
			e.printStackTrace();
		}
		return available;
	}
	
	/**
	 * Return the list of cinema and the showtimes detail which has specific movie
	 * @param id
	 * @return
	 * @throws EmptyResultException
	 */
	public List<ShowtimeDTO> findMovieShowtimebyMovieID(int id) throws EmptyResultException {
		List<ShowtimeDTO> showtimeList = new ArrayList<ShowtimeDTO>();
		try{
			
			String query_cast = "SELECT * FROM TBL_MOVIE_SHOWTIMES WHERE MOVIE_ID = ?";
			PreparedStatement stmnt = connection.prepareStatement(query_cast);
			stmnt.setInt(1, id);
			ResultSet res = stmnt.executeQuery();
			while(res.next()){
				int showtimeID = res.getInt("MOVIE_SHOWTIME_ID");
				int movieID = id;
				int cinemaID = res.getInt("CINEMA_ID");
				java.sql.Date movieDate = res.getDate("MOVIE_DATE");
				java.sql.Time movieTime = res.getTime("MOVIE_TIME");
				int booked = res.getInt("BOOKED");
				showtimeList.add(new ShowtimeDTO(showtimeID, movieID, cinemaID, movieDate, movieTime, booked));
			}
		}catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
			throw new EmptyResultException();
		}
		return showtimeList;
	}
	
	public ShowtimeDTO findMovieShowtimebyID(int id) throws EmptyResultException {
		ShowtimeDTO result = null;
		try{	
			String query_cast = "SELECT * FROM TBL_MOVIE_SHOWTIMES WHERE MOVIE_SHOWTIME_ID = ?";
			PreparedStatement stmnt = connection.prepareStatement(query_cast);
			stmnt.setInt(1, id);
			ResultSet res = stmnt.executeQuery();
			res.next();
			int showtimeID = res.getInt("MOVIE_SHOWTIME_ID");
			int movieID = id;
			int cinemaID = res.getInt("CINEMA_ID");
			java.sql.Date movieDate = res.getDate("MOVIE_DATE");
			java.sql.Time movieTime = res.getTime("MOVIE_TIME");
			int booked = res.getInt("BOOKED");
			result = new ShowtimeDTO(showtimeID, movieID, cinemaID, movieDate, movieTime, booked);
		}catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
			throw new EmptyResultException();
		}
		return result;
	}
	
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Movie related function
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * MOVIE_ID INTEGER CONSTRAINT MOVIE_ID PRIMARY KEY,
	MOVIE_NAME VARCHAR(100),
	MOVIE_TYPE INTEGER,
	RELEASE_DATE DATE,
	POSTER BLOB,
	GENRE VARCHAR(200),
	DIRECTOR VARCHAR(200),
	SYNOPSIS VARCHAR(2000),
	ACTORS VARCHAR(200),
	AGE_RATING INTEGER
	 */
	/**
	 * Adding a movie to the movie table in the database
	 */
	public void addMovie(MovieDTO movie) {
		//Right now only testing adding a image file called poster
		try {
			String sql = "INSERT INTO TBL_MOVIES (MOVIE_ID,RELEASE_DATE,POSTER,GENRE,DIRECTOR,SYNOPSIS,ACTORS,AGE_RATING,MOVIE_NAME) values (?,?,?,?,?,?,?,?,?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1,movie.getMovieID());
			statement.setDate(2,(java.sql.Date) movie.getReleaseDate());
			statement.setBlob(3, movie.getPoster());
			statement.setString(4,movie.getGenre());
			statement.setString(5,movie.getDirector());
			statement.setString(6,movie.getSynopsis());
			statement.setString(7,movie.getActors());
			statement.setInt(8,movie.getAgeRating());
			statement.setString(9,movie.getMovieName());
			int result = statement.executeUpdate();
			logger.info("Statement successfully executed "+result);
			//Converting to list of string
			statement.close();
			List<String> actors_list = Arrays.asList(movie.getActors().split("\\s*,\\s*"));
			addActors(actors_list, movie.getMovieID());
			List<String> genres_list = Arrays.asList(movie.getGenre().split("\\s*,\\s*"));
			addGenres(genres_list, movie.getMovieID());
		} catch (SQLException e) {
			System.out.println("Caught Exception");
			e.printStackTrace();
		}
	}
	
	public MovieDTO findMovieByID(int id) throws EmptyResultException {
		MovieDTO result = null;
		try{
			
			String query_cast = "SELECT * FROM TBL_MOVIES WHERE MOVIE_ID = ?";
			PreparedStatement stmnt = connection.prepareStatement(query_cast);
			stmnt.setInt(1, id);
			ResultSet res = stmnt.executeQuery();
			res.next();
			//TODO: I'm nulling the poster for now
			result = new MovieDTO(res.getInt("MOVIE_ID"), res.getString("MOVIE_NAME"), res.getInt("MOVIE_TYPE"), res.getDate("RELEASE_DATE"), null, res.getString("GENRE"),
					res.getString("DIRECTOR"), res.getString("SYNOPSIS"), res.getString("ACTORS"), res.getInt("AGE_RATING"));
			//MovieDTO(int movieID, String movieName, int movieType, java.sql.Date releaseDate, InputStream poster, String genre,String director,String synopsis,String actors,int ageRating)
		}catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
			throw new EmptyResultException();
		}
		return result;
	}
	
	/**
	 * Returning list of all movie id and its title
	 * @return
	 */
	public List<MovieDTO> findAllMovieTitle() {
		List<MovieDTO> allMovieTitle = new ArrayList<MovieDTO>();
		try{
			Statement stmnt = connection.createStatement();
			String query_cast = "SELECT * from TBL_MOVIES";
			ResultSet res = stmnt.executeQuery(query_cast);
			while(res.next()){
				int id = res.getInt("MOVIE_ID");
				String name = res.getString("MOVIE_NAME");
				allMovieTitle.add(new MovieDTO(id, name));
			}
			res.close();
			stmnt.close();
			
		}catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
		}
		return allMovieTitle;
	}
	
	/**
	 * Add all actors from a movie to the database
	 * @param actors
	 * @param movie_id
	 */
	public void addActors(List<String> actors, int movie_id) {
		PreparedStatement stmnt = null;
		int index = 0;
		for(String actor : actors) {
			try{
				String sqlStr = 
					"INSERT INTO TBL_MOVIE_ACTORS (ACTOR_ID, ACTOR_NAME, ACTOR_STATUS, ACTOR_MOVIE) "+
					"VALUES (?,?,?,?)";
				stmnt = connection.prepareStatement(sqlStr);
				stmnt.setInt(1,lastIndex("TBL_MOVIE_ACTORS","ACTOR_ID")+1);
				stmnt.setString(2, actor);
				if(index == 0) {
					stmnt.setString(3, "main");
				} else {
					stmnt.setString(3, "not main");
				}
				stmnt.setInt(4, movie_id);
				int result = stmnt.executeUpdate();
				logger.info("Statement successfully executed "+result);
				logger.info("Actor has been registered to the database");
				stmnt.close();
			}catch(Exception e){
				logger.severe("Unable to add actor! ");
				e.printStackTrace();
			}
			index++;
		}
	}
	
	/**
	 * Add all genres from a movie to the database
	 * @param genres
	 * @param movie_id
	 */
	public void addGenres(List<String> genres, int movie_id) {
		PreparedStatement stmnt = null;
		for(String genre : genres) {
			try{
				String sqlStr = 
					"INSERT INTO TBL_MOVIE_GENRES (GENRE_ID, GENRE_NAME, GENRE_MOVIE) "+
					"VALUES (?,?,?)";
				stmnt = connection.prepareStatement(sqlStr);
				stmnt.setInt(1,lastIndex("TBL_MOVIE_GENRES","GENRE_ID")+1);
				stmnt.setString(2, genre);
				stmnt.setInt(3, movie_id);
				int result = stmnt.executeUpdate();
				logger.info("Statement successfully executed "+result);
				logger.info("Genre has been registered to the database");
				stmnt.close();
			}catch(Exception e){
				logger.severe("Unable to add genre! ");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Returning list of all genre id, name, and the related movie id
	 * @return
	 */
	public List<GenreDTO> findAllMovieGenre() {
		List<GenreDTO> allMovieGenre = new ArrayList<GenreDTO>();
		try{
			Statement stmnt = connection.createStatement();
			String query_cast = "SELECT * from TBL_MOVIE_GENRES";
			ResultSet res = stmnt.executeQuery(query_cast);
			while(res.next()){
				allMovieGenre.add(new GenreDTO(res.getInt("GENRE_ID"), res.getString("GENRE_NAME"), res.getInt("GENRE_MOVIE")));
			}
			res.close();
			stmnt.close();
			
		}catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
		}
		return allMovieGenre;
	}
	
	/**
	 * Add a review of a movie
	 */
	public void addReview(int movieID, String review_paragraph, int review_rating, String review_name) {
		PreparedStatement stmnt = null;
		try{
			String sqlStr = 
				"INSERT INTO TBL_MOVIE_REVIEWS (REVIEW_ID, REVIEW_PARAGRAPH, MOVIE_ID, REVIEW_RATING, REVIEW_DATE, REVIEW_NAME) "+
				"VALUES (?,?,?,?,?,?)";
			stmnt = connection.prepareStatement(sqlStr);
			stmnt.setInt(1,lastIndex("TBL_MOVIE_REVIEWS","REVIEW_ID")+1);
			stmnt.setString(2, review_paragraph);
			stmnt.setInt(3, movieID);
			stmnt.setInt(4, review_rating);
			java.sql.Date timeNow = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
			stmnt.setDate(5, timeNow);
			stmnt.setString(6, review_name);
			int result = stmnt.executeUpdate();
			logger.info("Statement successfully executed "+result);
			logger.info("Review has been registered to the database");
			stmnt.close();
		}catch(Exception e){
			logger.severe("Unable to add review! ");
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the list of review from a movie
	 */
	public List<ReviewDTO> getMovieReview(int movieID) {
		List<ReviewDTO> reviews = new ArrayList<ReviewDTO>();
		try{
			String query_cast = "SELECT * FROM TBL_MOVIE_REVIEWS WHERE MOVIE_ID = ?";
			PreparedStatement stmnt = connection.prepareStatement(query_cast);
			stmnt.setInt(1, movieID);
			ResultSet res = stmnt.executeQuery();
			while(res.next()){
				reviews.add(new ReviewDTO(res.getInt("REVIEW_ID"), res.getString("REVIEW_PARAGRAPH"), res.getInt("MOVIE_ID"), 
						res.getString("REVIEW_DATE"), res.getInt("REVIEW_RATING"), res.getString("REVIEW_NAME")));
			}
			res.close();
			stmnt.close();
			
		}catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
		}
		return reviews;
	}
	
	/**
	 * Return the average rating of a movie
	 * @param movieID
	 * @return
	 */
	public float getMovieRating(int movieID) {
		float result = 0;
		float total = 0;
		int count = 0;
		try{
			String query_cast = "SELECT * FROM TBL_MOVIE_REVIEWS WHERE MOVIE_ID = ?";
			PreparedStatement stmnt = connection.prepareStatement(query_cast);
			stmnt.setInt(1, movieID);
			ResultSet res = stmnt.executeQuery();
			while(res.next()){
				total += res.getInt("REVIEW_RATING");
				count++;
			}
			res.close();
			stmnt.close();
			
		}catch(Exception e){
			System.out.println("Caught Exception");
			e.printStackTrace();
		}
		if(count > 0) {
			result = total/count;
		}
		return result;
	}
	
	/**
	 * Give id of the last movie in db
	 */
	public int lastMovie() throws EmptyResultException {
		int count = 0;
		try{
			String count_query = "SELECT MAX(MOVIE_ID) FROM TBL_MOVIES";
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
	
	public List<MovieDTO> getNowShowing(int noResults, String path){
		List<MovieDTO> movies = new ArrayList<MovieDTO>();
		try{
			Statement stmnt = connection.createStatement();
			ResultSet results = stmnt.executeQuery("SELECT * FROM TBL_MOVIES WHERE RELEASE_DATE <= current_date ORDER BY RELEASE_DATE ASC");
			while(results.next()){
				//System.out.println("got a movie");
				int movieID = results.getInt("MOVIE_ID");
				String movieName = results.getString("MOVIE_NAME");
				Blob blob = results.getBlob("POSTER");
				InputStream poster = blob.getBinaryStream();
				String filePath = path + "tmpImages/" + movieID + ".jpg";
				OutputStream outputStream = new FileOutputStream(filePath);
				int bytesRead = -1;
                byte[] buffer = new byte[4096];
                while ((bytesRead = poster.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                float rating = getMovieRating(movieID);
				movies.add(new MovieDTO(movieID, movieName, null, poster, null,
		        		null,null,null,1,rating));
			}
			results.close();
			stmnt.close();
		}catch(Exception e){
			System.out.println(e.getMessage());
			logger.severe("Failed to get moveies "+e.getStackTrace());
		}
		if(movies.size() > noResults){	
			movies = movies.subList(1, noResults+1);
		}
		Collections.sort(movies,new Comparator<MovieDTO>() {
	        public int compare(MovieDTO o1, MovieDTO o2) {
	        	if(o2.getRating() > o1.getRating()){
	        		return 1;
	        	}else if(o2.getRating() == o1.getRating()){
	        		return 0;
	        	}else{
	        		return -1;
	        	}	
	            //return (int) (o2.getRating() - o1.getRating());
	        }
	    });
		return movies;
	}
	
	public List<MovieDTO> getComingSoon(int noResults, String path){
		List<MovieDTO> movies = new ArrayList<MovieDTO>();
		try{
			Statement stmnt = connection.createStatement();
			ResultSet results = stmnt.executeQuery("SELECT * FROM TBL_MOVIES WHERE RELEASE_DATE > current_date ORDER BY RELEASE_DATE ASC");
			while(results.next()){
				int movieID = results.getInt("MOVIE_ID");
				String movieName = results.getString("MOVIE_NAME");
				Blob blob = results.getBlob("POSTER");
				InputStream poster = blob.getBinaryStream();
				String filePath = path + "tmpImages/" + movieID + ".jpg";
				OutputStream outputStream = new FileOutputStream(filePath);
				int bytesRead = -1;
                byte[] buffer = new byte[4096];
                while ((bytesRead = poster.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
				movies.add(new MovieDTO(movieID, movieName, null, poster, null,
		        		null,null,null,1,0.0f));
			}
			results.close();
			stmnt.close();
		}catch(Exception e){
			System.out.println(e.getMessage());
			logger.severe("Failed to get moveies "+e.getStackTrace());
		}
		//System.out.println(movies.size());
		//System.out.println(noResults);
		if(movies.size() > noResults){	
			movies = movies.subList(1, noResults+1);
		}
		return movies;
	}
	
	public String getMoviePoster(int movieID,String path){
		try{
			Statement stmnt = connection.createStatement();
			ResultSet results = stmnt.executeQuery("SELECT * FROM TBL_MOVIES WHERE MOVIE_ID = ?");
			while(results.next()){
				Blob blob = results.getBlob("POSTER");
				InputStream poster = blob.getBinaryStream();
				String filePath = path + "tmpImages/" + movieID + ".jpg";
				OutputStream outputStream = new FileOutputStream(filePath);
				int bytesRead = -1;
                byte[] buffer = new byte[4096];
                while ((bytesRead = poster.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
			logger.severe("Failed to get moveies "+e.getStackTrace());
		}
		return movieID+".jpg";
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Search and Comment related stuff
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * search for movies either using a word in the title
	 * @param search
	 * @return
	 * @throws EmptyResultException 
	 */
	public List<MovieDTO> searchMovieTitle(String search) throws EmptyResultException {
		List<MovieDTO> result = new ArrayList<MovieDTO>();
		List<MovieDTO> allMovieTitle = findAllMovieTitle();
		for(MovieDTO movie : allMovieTitle) {
			if(movie.getMovieName().toLowerCase().contains(search.toLowerCase())) {
				result.add(findMovieByID(movie.getMovieID()));
			}
		}
		return result;
	}
	
	/**
	 * Or genre
	 * @throws EmptyResultException 
	 */
	public List<MovieDTO> searchMovieGenre(String search) throws EmptyResultException {
		List<MovieDTO> result = new ArrayList<MovieDTO>();
		List<GenreDTO> allMovieGenre = findAllMovieGenre();
		for(GenreDTO genre : allMovieGenre) {
			if(genre.getGenreName().toLowerCase().equals(search.toLowerCase())) {
				result.add(findMovieByID(genre.getMovieID()));
			}
		}
		return result;
	}
	
	/**
	 * Setting the booked value in movie showtimes table
	 * @param showtimeID
	 * @param amount
	 */
	public void setBooked(int showtimeID, int amount) {
		PreparedStatement stmnt = null; 
		try{
			String sqlStr = 
				"UPDATE TBL_MOVIE_SHOWTIMES SET BOOKED = ? "+
				"WHERE MOVIE_SHOWTIME_ID = ?";
			stmnt = connection.prepareStatement(sqlStr);
			int newAmount = findMovieShowtimebyID(showtimeID).getBooked() + amount;
			stmnt.setInt(1, newAmount);
			stmnt.setInt(2, showtimeID);
			int result = stmnt.executeUpdate();
			logger.info("Statement successfully executed "+result);
			logger.info("Database has been changed");
			stmnt.close();
		}catch(Exception e){
			logger.severe("Failed to change database! ");
			e.printStackTrace();
		}
	}
	
	/**
	 * Add booking to the database
	 * @param showtimeID
	 * @param userID
	 * @param amount
	 * @throws EmptyResultException
	 */
	public void addBooking(int showtimeID, int userID, int amount,String cardName,String cardNum,int cardCSC) throws EmptyResultException {
		PreparedStatement stmnt = null; 
		int bookingID = lastIndex("TBL_BOOKINGS","BOOKING_ID")+1;
		try{
			String sqlStr = 
				"INSERT INTO TBL_BOOKINGS (BOOKING_ID, USER_ID, MOVIE_SHOWTIME_ID, AMOUNT,CARD_NAME,CARD_NUMBER,CARD_CSC) "+
				"VALUES (?,?,?,?,?,?,?)";
			stmnt = connection.prepareStatement(sqlStr);
			stmnt.setInt(1,bookingID);
			stmnt.setInt(2,userID);
			stmnt.setInt(3,showtimeID);
			stmnt.setInt(4,amount);
			stmnt.setString(5,cardName);
			stmnt.setString(6,cardNum);
			stmnt.setInt(7,cardCSC);
			int result = stmnt.executeUpdate();
			logger.info("Statement successfully executed "+result);
			logger.info("Booking has been registered to the database");
			stmnt.close();
		}catch(Exception e){
			logger.severe("Unable to add Booking! ");
			e.printStackTrace();
		}
	}

	public List<BookingDTO> getBookings(int userID) {
		List<BookingDTO> bookings = new ArrayList<BookingDTO>();
		try{
			String query_cast = "select * from TBL_BOOKINGS t1,TBL_MOVIE_SHOWTIMES t2,TBL_MOVIES t3,TBL_CINEMAS t4 "
					+ "where t1.USER_ID = ? and t1.MOVIE_SHOWTIME_ID = t2.MOVIE_SHOWTIME_ID and t2.MOVIE_ID = t3.MOVIE_ID "
					+ "and t2.CINEMA_ID = t4.CINEMA_ID";
			PreparedStatement stmnt = connection.prepareStatement(query_cast);
			stmnt.setInt(1, userID);
			ResultSet results = stmnt.executeQuery();
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy"); 
			
			while(results.next()){
				int amount = results.getInt("AMOUNT");
				String date = df.format(results.getDate("MOVIE_DATE"));  
				String movieName = results.getString("MOVIE_NAME");
				String movieTime = results.getTime("MOVIE_TIME").toString();
				String cardName = results.getString("CARD_NAME");
				String cardNum = results.getString("CARD_NUMBER");
				int cardCSC = results.getInt("CARD_CSC");
				String location = results.getString("CINEMA_LOCATION");
				bookings.add(new BookingDTO(movieName,movieTime,date,amount,cardName,cardNum,cardCSC,location));
			}
			results.close();
			stmnt.close();
		}catch(Exception e){
			System.out.println(e.getMessage());
			logger.severe("Failed to get bookings "+e.getStackTrace());
		}
		
		return bookings;
	}
}
