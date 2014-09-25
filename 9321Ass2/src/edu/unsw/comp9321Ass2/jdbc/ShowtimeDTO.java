package edu.unsw.comp9321Ass2.jdbc;

public class ShowtimeDTO {
	private int showtimeID;
	private int movieID;
	private int cinemaID;
	private java.sql.Date movieDate;
	private java.sql.Time movieTime;
	private int booked;
	
	public ShowtimeDTO(int showtimeID, int movieID, int cinemaID, java.sql.Date movieDate, java.sql.Time movieTime, int booked) {
		this.showtimeID = showtimeID;
		this.movieID = movieID;
		this.cinemaID = cinemaID;
		this.movieDate = movieDate;
		this.movieTime = movieTime;
		this.booked = booked;
	}
	
	public int getShowtimeID() {
		return showtimeID;
	}
	
	public int getMovieID() {
		return movieID;
	}
	
	public int getCinemaID() {
		return cinemaID;
	}
	
	public java.sql.Date getMovieDate() {
		return movieDate;
	}
	
	public java.sql.Time getMovieTime() {
		return movieTime;
	}
	
	public int getBooked() {
		return booked;
	}
}

