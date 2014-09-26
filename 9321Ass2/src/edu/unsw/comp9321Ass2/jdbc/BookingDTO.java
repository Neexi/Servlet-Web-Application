package edu.unsw.comp9321Ass2.jdbc;

public class BookingDTO {
	/*
	 * BOOKING_ID INTEGER CONSTRAINT BOOKING_PK PRIMARY KEY,
	USER_ID INTEGER CONSTRAINT BOOKING_FK REFERENCES TBL_USERS(USER_ID),
	MOVIE_SHOWTIME_ID INTEGER CONSTRAINT BOOKING_FK_2 REFERENCES TBL_MOVIE_SHOWTIMES(MOVIE_SHOWTIME_ID),
	AMOUNT INTEGER NOT NULL,
	CARD_NAME VARCHAR(200) NOT NULL,
	CARD_NUMBER INTEGER,
	CARD_CSC INTEGER
	 */
	private String movieName;
	private String showTime;
	private String showDate;
	private int amount;
	private String cardName;
	private String cardNum;
	private int cardCSC;
	
	public BookingDTO(String movieName,String showTime,String showDate,int amount,String cardName,String cardNum,int cardCSC){
		this.movieName = movieName;
		this.showTime = showTime;
		this.showDate = showDate;
		this.amount = amount;
		this.cardName = cardName;
		this.cardNum = cardNum;
		this.cardCSC = cardCSC;
	}
	
	public String getMovieName(){
		return movieName;
	}
	public String getShowTime(){
		return showTime;
	}
	public String getShowDate(){
		return showDate;
	}
	public int getAmount(){
		return amount;
	}
	public String getCardName(){
		return cardName;
	}
	public String getCardNum(){
		return cardNum;
	}
	public int getCardCSC(){
		return cardCSC;
	}
}
