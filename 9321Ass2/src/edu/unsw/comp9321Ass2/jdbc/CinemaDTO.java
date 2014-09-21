package edu.unsw.comp9321Ass2.jdbc;

import java.util.List;

public class CinemaDTO {
	private String location;
	private int capacity;
	private List<String> amenities;
	
	public CinemaDTO(String location, int capacity, List<String> amenities) {
		this.location = location;
		this.capacity = capacity;
		this.amenities = amenities;
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
