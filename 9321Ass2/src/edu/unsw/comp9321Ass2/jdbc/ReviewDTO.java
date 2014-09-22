package edu.unsw.comp9321Ass2.jdbc;

public class ReviewDTO {
	private int reviewID;
	private String reviewParagraph;
	private int movieID;
	private int reviewRating;
	
	public ReviewDTO(int reviewID, String reviewParagraph, int movieID, int reviewRating) {
		this.reviewID = reviewID;
		this.reviewParagraph = reviewParagraph;
		this.movieID = movieID;
		this.reviewRating = reviewRating;
	}
	
	public int getReviewID() {
		return reviewID;
	}
	
	public String getReviewParagraph() {
		return reviewParagraph;
	}
	
	public int getMovieID() {
		return movieID;
	}
	
	public int getReviewRating() {
		return reviewRating;
	}
}
