package com.vishesh.task7.model;

public class Leave {
	
	private String leave_id;
	private String uid;
	private String email;
	private String name;
	private String leave_type;
	private String from_date;
	private String to_date;
	private String note;
	private String dateOfRequest;
	private String status;
	private String photo;
	
	public Leave(String leave_id, String uid, String email, String name, String leave_type, String dateOfRequest, String from_date, String to_date,
			String note) {
		super();
		this.uid = uid;
		this.email = email;
		this.name = name;
		this.leave_type = leave_type;
		this.from_date = from_date;
		this.to_date = to_date;
		this.note = note;
		this.leave_id=leave_id;
		this.dateOfRequest=dateOfRequest;
		this.photo="";
	}
	
	public Leave(String leave_id, String uid, String email, String name, String leave_type, String dateOfRequest, String from_date, String to_date,
			String note, String status) {
		super();
		this.uid = uid;
		this.email = email;
		this.name = name;
		this.leave_type = leave_type;
		this.from_date = from_date;
		this.to_date = to_date;
		this.note = note;
		this.leave_id=leave_id;
		this.dateOfRequest=dateOfRequest;
		this.status=status;
		this.photo="";
	}
	
	public Leave(String leave_id, String uid, String email, String name, String leave_type, String dateOfRequest, String from_date, String to_date,
			String note, String status, String photo) {
		super();
		this.uid = uid;
		this.email = email;
		this.name = name;
		this.leave_type = leave_type;
		this.from_date = from_date;
		this.to_date = to_date;
		this.note = note;
		this.leave_id=leave_id;
		this.dateOfRequest=dateOfRequest;
		this.status=status;
		this.photo=photo;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLeave_type() {
		return leave_type;
	}

	public void setLeave_type(String leave_type) {
		this.leave_type = leave_type;
	}

	public String getFrom_date() {
		return from_date;
	}

	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}

	public String getTo_date() {
		return to_date;
	}

	public void setTo_date(String to_date) {
		this.to_date = to_date;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getLeave_id() {
		return leave_id;
	}

	public void setLeave_id(String leave_id) {
		this.leave_id = leave_id;
	}

	public String getDateOfRequest() {
		return dateOfRequest;
	}

	public void setDateOfRequest(String dateOfRequest) {
		this.dateOfRequest = dateOfRequest;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
}
