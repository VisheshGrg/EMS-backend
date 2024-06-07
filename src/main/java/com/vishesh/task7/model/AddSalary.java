package com.vishesh.task7.model;

public class AddSalary {
	private String sid;
	private String uid;
	private String salary_amount;
	private String salary_des;
	private String salary_type;
	
	public AddSalary() {
		
	}
	
	public AddSalary(String sid, String uid, String salary_amount, String salary_des, String salary_type) {
		super();
		this.sid = sid;
		this.uid = uid;
		this.salary_amount = salary_amount;
		this.salary_des = salary_des;
		this.salary_type = salary_type;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getSalary_amount() {
		return salary_amount;
	}

	public void setSalary_amount(String salary_amount) {
		this.salary_amount = salary_amount;
	}

	public String getSalary_des() {
		return salary_des;
	}

	public void setSalary_des(String salary_des) {
		this.salary_des = salary_des;
	}

	public String getSalary_type() {
		return salary_type;
	}

	public void setSalary_type(String salary_type) {
		this.salary_type = salary_type;
	}
	
	
}
