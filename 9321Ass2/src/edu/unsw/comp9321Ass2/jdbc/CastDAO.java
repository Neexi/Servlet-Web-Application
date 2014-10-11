package edu.unsw.comp9321Ass2.jdbc;

import java.text.ParseException;
import java.util.List;

import edu.unsw.comp9321Ass2.exception.EmptyResultException;

public interface CastDAO {
	
	public List<UserDTO> findAllUser();
	
	public UserDTO findUser(String username) throws EmptyResultException;
	
	public int lastIndex(String table, String primary) throws EmptyResultException;
	
	public boolean checkLogin(String username, String password) throws EmptyResultException;
	
	public boolean checkAdmin(String username, String password) throws EmptyResultException;
	
	public void addUser(UserDTO user);
	
	public void activateUser(int userID);
	
	public boolean existUser(String username) throws EmptyResultException;
	
	public boolean usedEmail(String email) throws EmptyResultException;
	
	public void editUser(String username, String email, String nickName, String firstName, String lastName);
	
	public void addCinema(String location, int capacity, List<String> amenities) throws EmptyResultException;
	
	public List<CinemaDTO> findAllCinema();
	
	public CinemaDTO findCinemaByID(int id) throws EmptyResultException;
	
	public void addMovieShowtime(int movieID, int cinemaID, java.sql.Date date, java.sql.Time time) throws ParseException ;
	
	public boolean availableMovieShowtime(int cinemaID, java.sql.Date date, java.sql.Time time) throws ParseException;
	
	public ShowtimeDTO findMovieShowtimebyID(int id) throws EmptyResultException;
	
	public List<ShowtimeDTO> findMovieShowtimebyMovieID(int id) throws EmptyResultException;
	
	public void addMovie(MovieDTO movie);
	
	public MovieDTO findMovieByID(int id) throws EmptyResultException;
	
	public int lastMovie() throws EmptyResultException;
	
	public void addReview(int movieID, String review_paragraph, int review_rating, String review_name);
	
	public List<MovieDTO> getNowShowing(int noResults,String path);
	
	public List<MovieDTO> getComingSoon(int noResults, String path);
	
	public List<MovieDTO> searchMovieTitle(String search) throws EmptyResultException;
	
	public List<MovieDTO> searchMovieGenre(String search) throws EmptyResultException;

	public List<ReviewDTO> getMovieReview(int movieID);
	
	public float getMovieRating(int movieID);
	
	public void setBooked(int showtimeID, int amount);
		
	public void addBooking(int showtimeID, int userID, int amount,String cardName,String cardNum,int cardCSC) throws EmptyResultException;
	
	public List<BookingDTO> getBookings (int userID);

	public String getMoviePoster(int movieID, String filePath);
}
