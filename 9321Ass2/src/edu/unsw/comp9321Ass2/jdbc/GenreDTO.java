package edu.unsw.comp9321Ass2.jdbc;

public class GenreDTO {
	private int genreID;
	private String genreName;
	private int movieID;
	
	public GenreDTO(int genreID, String genreName, int movieID) {
		this.genreID = genreID;
		this.genreName = genreName;
		this.movieID = movieID;
	}
	
	public int getGenreID() {
		return genreID;
	}
	
	public String getGenreName() {
		return genreName;
	}
	
	public int getMovieID() {
		return movieID;
	}
}
