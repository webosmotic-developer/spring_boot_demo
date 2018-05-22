package com.wo.demo.jwt.model;

public class JwtUser {
	Long id;
	String userName;
	String role;

	public JwtUser(long id, String userName, String role) {
		this.id = id;
		this.userName = userName;
		this.role = role;
	}

	public JwtUser() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
