package com.vishesh.task7;

public class User {
	private String name;
	private String email;
	private String pers_email;
	private String password;
	private int age;
	private String blood_group;
	private String address;
	private String perm_address;
	private String department;
	private String phoneNo;
	private String alt_phoneNo;
	private String uid;
	private String salary; // total salary
	private String salary_amount;
	private String photo;
	private boolean isAdmin;
	private String last_processed;
	private String ytd_tax;
	private int total_leaves;
	private double cl;
	private double pl;
	private double sl;
	private double coff;
	private boolean isPunched;
	private String lastPunchedDate;
	private String lastPunchedStatus;
	private String punchStatus;
	private boolean is_leave;
	private boolean is_infoSet;
	
	public User() {
		
	}
	
	public User(String uid, String name, String email, String password) {
		this.uid = uid;
		this.name = name;
		this.email = email;
		this.password = password;
		this.salary = "0.0";
		this.salary_amount = "0.0";
		this.photo="";
		this.isAdmin=false;
		this.last_processed="Not available";
		this.ytd_tax="0";
		this.total_leaves=0;
		this.cl=0.0d;
		this.pl=0.0d;
		this.sl=0.0d;
		this.coff=0.0d;
		this.isPunched=false;
		this.lastPunchedDate="Not Available";
		this.lastPunchedStatus="Not Available";
		this.punchStatus="UNSWIPED";
		this.is_leave=false;
		this.pers_email="Not Available";
		this.blood_group="Not Available";
		this.perm_address="Not Available";
		this.alt_phoneNo="Not Available";
		this.address="Not Available";
		this.is_infoSet=false;
		this.department="Not Available";
		this.phoneNo="Not Available";
	}
	
	public User(String uid, String name, String email, String password, String photo) {
		this.uid = uid;
		this.name = name;
		this.email = email;
		this.password = password;
		this.salary = "0.0";
		this.salary_amount = "0.0";
		this.photo=photo;
		this.isAdmin=false;
		this.last_processed="Not available";
		this.ytd_tax="0";
		this.total_leaves=0;
		this.cl=0.0d;
		this.pl=0.0d;
		this.sl=0.0d;
		this.coff=0.0d;
		this.isPunched=false;
		this.lastPunchedDate="Not Available";
		this.lastPunchedStatus="Not Available";
		this.punchStatus="UNSWIPED";
		this.is_leave=false;
		this.pers_email="Not Available";
		this.blood_group="Not Available";
		this.perm_address="Not Available";
		this.alt_phoneNo="Not Available";
		this.is_infoSet=false;
		this.department="Not Available";
		this.phoneNo="Not Available";
	}
	
	public User(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.salary = "0.0";
		this.salary_amount = "0.0";
		this.photo="";
		this.isAdmin=false;
		this.last_processed="Not available";
		this.ytd_tax="0";
		this.total_leaves=0;
		this.cl=0.0d;
		this.pl=0.0d;
		this.sl=0.0d;
		this.coff=0.0d;
		this.isPunched=false;
		this.lastPunchedDate="Not Available";
		this.lastPunchedStatus="Not Available";
		this.punchStatus="UNSWIPED";
		this.is_leave=false;
		this.pers_email="Not Available";
		this.blood_group="Not Available";
		this.perm_address="Not Available";
		this.alt_phoneNo="Not Available";
		this.is_infoSet=false;
		this.department="Not Available";
		this.phoneNo="Not Available";
	}
	
	public User(String uid, String name, String email, String password, int age, String address, String department, String phoneNo) {
//		super();
		this.uid=uid;
		this.name = name;
		this.email = email;
		this.password = password;
		this.age = age;
		this.address = address;
		this.department = department;
		this.phoneNo = phoneNo;
		this.salary = "0.0";
		this.salary_amount = "0.0";
		this.photo="";
		this.isAdmin=false;
		this.last_processed="Not available";
		this.ytd_tax="0";
		this.total_leaves=0;
		this.cl=0.0d;
		this.pl=0.0d;
		this.sl=0.0d;
		this.coff=0.0d;
		this.isPunched=false;
		this.lastPunchedDate="Not Available";
		this.lastPunchedStatus="Not Available";
		this.punchStatus="UNSWIPED";
		this.is_leave=false;
		this.pers_email="Not Available";
		this.blood_group="Not Available";
		this.perm_address="Not Available";
		this.alt_phoneNo="Not Available";
		this.is_infoSet=false;
	}
	
	public User(String uid, String name, String email, String password, int age, String address, String department, String phoneNo, String photo) {
//		super();
		this.uid=uid;
		this.name = name;
		this.email = email;
		this.password = password;
		this.age = age;
		this.address = address;
		this.department = department;
		this.phoneNo = phoneNo;
		this.salary = "0.0";
		this.salary_amount = "0.0";
		this.photo=photo;
		this.isAdmin=false;
		this.last_processed="Not available";
		this.ytd_tax="0";
		this.total_leaves=0;
		this.cl=0.0d;
		this.pl=0.0d;
		this.sl=0.0d;
		this.coff=0.0d;
		this.isPunched=false;
		this.lastPunchedDate="Not Available";
		this.lastPunchedStatus="Not Available";
		this.punchStatus="UNSWIPED";
		this.is_leave=false;
		this.pers_email="Not Available";
		this.blood_group="Not Available";
		this.perm_address="Not Available";
		this.alt_phoneNo="Not Available";
		this.is_infoSet=false;
	}


	public User(String uid, String name, String email, String password, String pers_email, int age, String blood_group, String curr_address, String perm_address, String department, String phoneNo, String alt_phoneNo, String salary, String salary_amount, String photo) {
//		super();
		this.uid=uid;
		this.name = name;
		this.email = email;
		this.password = password;
		this.age = age;
		this.address = curr_address;
		this.department = department;
		this.phoneNo = phoneNo;
		this.salary = salary;
		this.salary_amount=salary_amount;
		this.photo=photo;
		this.isAdmin=false;
		this.last_processed="Not available";
		this.ytd_tax="0";
		this.total_leaves=0;
		this.cl=0.0d;
		this.pl=0.0d;
		this.sl=0.0d;
		this.coff=0.0d;
		this.isPunched=false;
		this.lastPunchedDate="Not Available";
		this.lastPunchedStatus="Not Available";
		this.punchStatus="UNSWIPED";
		this.is_leave=false;
		this.pers_email=pers_email;
		this.blood_group=blood_group;
		this.perm_address=perm_address;
		this.alt_phoneNo=alt_phoneNo;
		this.is_infoSet=false;
	}
	
	
	
	public String getUid() {
		return uid;
	}
	
	public void setUid(String uid) {
		this.uid=uid;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	
	public String getSalary() {
		return this.salary;
	}
	
	public void setSalary(String salary) {
		this.salary=salary;
	}

	public String getSalary_amount() {
		return salary_amount;
	}

	public void setSalary_amount(String salary_amount) {
		this.salary_amount = salary_amount;
	}
	
	public String getPhoto() {
		return this.photo;
	}
	
	public void setPhoto(String photo) {
		this.photo=photo;
	}
	
	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin=isAdmin;
	}
	
	public boolean getIsAdmin() {
		return this.isAdmin;
	}
	
	public String getLastProcessed() {
		return this.last_processed;
	}
	
	public void setLastProcessed(String last_processed) {
		this.last_processed=last_processed;
	}
	
	public String getYtdTax() {
		return this.ytd_tax;
	}
	
	public void setYtdTax(String ytd_tax) {
		this.ytd_tax=ytd_tax;
	}

	public int getTotal_leaves() {
		return total_leaves;
	}

	public void setTotal_leaves(int total_leaves) {
		this.total_leaves = total_leaves;
	}

	public double getCl() {
		return cl;
	}

	public void setCl(double cl) {
		this.cl = cl;
	}

	public double getPl() {
		return pl;
	}

	public void setPl(double pl) {
		this.pl = pl;
	}

	public double getSl() {
		return sl;
	}

	public void setSl(double sl) {
		this.sl = sl;
	}

	public double getCoff() {
		return coff;
	}

	public void setCoff(double coff) {
		this.coff = coff;
	}
	
	public void setIsPunched(boolean isPunched) {
		this.isPunched=isPunched;
	}
	
	public boolean getIsPunched() {
		return this.isPunched;
	}

	public String getLastPunchedDate() {
		return lastPunchedDate;
	}

	public void setLastPunchedDate(String lastPunchedDate) {
		this.lastPunchedDate = lastPunchedDate;
	}

	public String getLastPunchedStatus() {
		return lastPunchedStatus;
	}

	public void setLastPunchedStatus(String lastPunchedStatus) {
		this.lastPunchedStatus = lastPunchedStatus;
	}

	public String getPunchStatus() {
		return punchStatus;
	}

	public void setPunchStatus(String punchStatus) {
		this.punchStatus = punchStatus;
	}

	public boolean getIs_leave() {
		return is_leave;
	}

	public void setIs_leave(boolean is_leave) {
		this.is_leave = is_leave;
	}

	public String getPers_email() {
		return pers_email;
	}

	public void setPers_email(String pers_email) {
		this.pers_email = pers_email;
	}

	public String getBlood_group() {
		return blood_group;
	}

	public void setBlood_group(String blood_group) {
		this.blood_group = blood_group;
	}

	public String getPerm_address() {
		return perm_address;
	}

	public void setPerm_address(String perm_address) {
		this.perm_address = perm_address;
	}

	public String getAlt_phoneNo() {
		return alt_phoneNo;
	}

	public void setAlt_phoneNo(String alt_phoneNo) {
		this.alt_phoneNo = alt_phoneNo;
	}

	public String getLast_processed() {
		return last_processed;
	}

	public void setLast_processed(String last_processed) {
		this.last_processed = last_processed;
	}

	public String getYtd_tax() {
		return ytd_tax;
	}

	public void setYtd_tax(String ytd_tax) {
		this.ytd_tax = ytd_tax;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public void setPunched(boolean isPunched) {
		this.isPunched = isPunched;
	}

	public boolean getIs_infoSet() {
		return is_infoSet;
	}

	public void setIs_infoSet(boolean is_infoSet) {
		this.is_infoSet = is_infoSet;
	}
	
	
	
	
}
