package edu.unsw.comp9321Ass2.jdbc;

import java.util.List;

public class CinemaDTO {
	private int cinemaID;
	private String location;
	private int capacity;
	private List<String> amenities;
	
	public CinemaDTO(int cinemaID, String location, int capacity, List<String> amenities) {
		this.location = location;
		this.capacity = capacity;
		this.amenities = amenities;
	}
	
	public CinemaDTO(int cinemaID, String location, int capacity) {
		this.cinemaID = cinemaID;
		this.location = location;
		this.capacity = capacity;
	}
	
	public int getCinemaID() {
		return cinemaID;
	}
	
	public String getLocation() {
		return location;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public List<String> getAmenities() {
		return amenities;
	}
}
