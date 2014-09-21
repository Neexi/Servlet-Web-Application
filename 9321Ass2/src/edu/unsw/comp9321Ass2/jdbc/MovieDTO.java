package edu.unsw.comp9321Ass2.jdbc;

import java.io.InputStream;
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
	private InputStream poster;
	private String genre;
	private String director;
	private String synopsis;
	private String actors; //Is it better to represent as separate dto? //Answer: Nah, unless we really need additional information for the main actor
	private int ageRating;
	
	/*
	 * MovieType(Out now/not out) is not taken because it can be generated from release date.
	 */
	public MovieDTO(int movieID, String movieName, Date releaseDate, InputStream poster, String genre,
					String director,String synopsis,String actors,int ageRating) {
		this.movieID = movieID;
		this.movieName = movieName;
		this.releaseDate = releaseDate;
		this.poster = poster;
		this.genre = genre;
		this.director = director;
		this.synopsis = synopsis;
		this.actors = actors;
		this.ageRating = ageRating;
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
	public InputStream getPoster(){
		return poster;
	}
	
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
	
	public int getAgeRating() {
		return ageRating;
	}
	
	public void setAgeRating(int ageRating) {
		this.ageRating = ageRating;
	}
	
}
	

