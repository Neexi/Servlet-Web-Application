package edu.unsw.comp9321Ass2.jdbc;

/**
 * Database constructor for User Information
 *
 */
public class UserDTO {
	private int userID;
	private String username;
	private String password;
	private String email;
	private String nickName;
	private String firstName;
	private String lastName;
	private String status;
	
	public UserDTO(int userID, String username, String password, String email) {
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.email = email;
		nickName = "";
		firstName = "";
		lastName = "";
		status = "inactive";
	}
	
	public UserDTO(int userID, String username, String password, String email, String nickName, String firstName, String lastName, String status) {
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.email = email;
		this.nickName = nickName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.status = status;
	}
	
	public int getID() {
		return userID;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getNickName() {
		return nickName;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void activateUser() {
		status = "active";
	}
}
