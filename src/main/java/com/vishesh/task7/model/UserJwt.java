package com.vishesh.task7.model;

public class UserJwt {
	private String employeeId;
	private String name;
	private String email;
	
	public UserJwt() {
		
	}
	
	public UserJwt(String employeeId, String name, String email) {
		super();
		this.employeeId = employeeId;
		this.name = name;
		this.email = email;
	}
	
	
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
