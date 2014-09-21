package edu.unsw.comp9321Ass2.jdbc;

import java.util.Date;

public class MovieDTO {
	/**
	 * Database constructor for Movie Information
	 *
	 */
	
	private int movieID;
	private String movieName;
	private int movieType;
	private Date releaseDate;
	//private Image poster; Deal with this later
	private String genre;
	private String director;
	private String synopsis;
	private String actors; //Is it better to represent as separate dto?
	
	/*
	 * MovieType(Out now/not out) is not taken because it can be generated from release date.
	 */
	public MovieDTO(int movieID, String movieName, Date releaseDate, String genre,
					String director,String synopsis,String actors) {
		this.movieID = movieID;
		this.movieName = movieName;
		this.releaseDate = releaseDate;
		this.genre = genre;
		this.director = director;
		this.synopsis = synopsis;
		this.actors = actors;
	}
	
	
	public int getMovieID() {
		return movieID;
	}
	
	public String getMovieName() {
		return movieName;
	}
	
	public void setMovieName(String movieName){
		this.movieName = movieName;
	}
	
	public int getMovieType(){
		return movieType;
	}
	
	public void setMovieType(int movieType){
		this.movieType = movieType;
	}
	
	public Date getReleaseDate() {
		return releaseDate;
	}
	
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	
	//Add poster getter/setter later
	
	
	public String getGenre() {
		return genre;
	}
	
	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	public String getDirector() {
		return director;
	}
	
	public void setDirector(String director) {
		this.director = director;
	}
	
	public String getSynopsis() {
		return synopsis;
	}
	
	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}
	
	public String getActors() {
		return actors;
	}
	
	public void setActors(String actors) {
		this.actors = actors;
	}
	
}
	

