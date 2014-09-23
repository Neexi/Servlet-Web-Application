package edu.unsw.comp9321Ass2.jdbc;


public class ReviewDTO {
	private int reviewID;
	private String reviewParagraph;
	private int movieID;
	private int reviewRating;
	private String reviewDate;
	private String reviewName;
	
	public ReviewDTO(int reviewID, String reviewParagraph, int movieID, String reviewDate, int reviewRating, String reviewName) {
		this.reviewID = reviewID;
		this.reviewParagraph = reviewParagraph;
		this.movieID = movieID;
		this.reviewRating = reviewRating;
		this.reviewDate = reviewDate;
		this.reviewName = reviewName;
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
	
	public String getReviewDate() {
		return reviewDate;
	}
	
	public String getReviewName() {
		return reviewName;
	}
}
