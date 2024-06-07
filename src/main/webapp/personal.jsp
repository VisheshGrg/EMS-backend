<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.vishesh.task7.*, java.util.*" %>    
  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Personal</title>

<link rel="stylesheet" type="text/css" href="/Task7/styles/personal.css">

<link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">

</head>
<body>
	
	<%
	
		User user = (User)session.getAttribute("USER_INFO");
		String department = user.getDepartment();
		String age = String.valueOf(user.getAge());
		String bg = user.getBlood_group();
		String pers_email = user.getPers_email();
		String phone = user.getPhoneNo();
		String alt_phone = user.getAlt_phoneNo();
		String curr_address = user.getAddress();
		String perm_address = user.getPerm_address();
	
	%>

	<div class="cont">
	
		<div class="personal">
			<div class="head">PERSONAL INFO</div>
			<table class="table_content">
				<tr>
					<td class="pair"><span class="label">Name</span><br><span class="value"><%= user.getName() %></span></td>
					<td class="pair"><span class="label">Age</span><br><span class="value"><%= age!=null && !age.equals("") ? age:"Not Available" %></span></td>
					<td class="pair"><span class="label">Department</span><br><span class="value"><%= department!=null && !department.equals("") ? department:"Not Available" %></span></td>
				</tr>
				<tr>
					<td class="pair"><span class="label">UId</span><br><span class="value"><%= user.getUid() %></span></td>
					<td class="pair"><span class="label">Blood Group</span><br><span class="value"><%= bg!=null && !bg.equals("") ? bg:"Not Available" %></span></td>
					<td class="pair"></td>
				</tr>
			</table>		
		</div>
		
		<div class="contact">
			<div class="head">CONTACT INFO</div>
			<table class="table_content">
				<tr>
					<td class="pair"><span class="label">Official Email ID</span><br><span class="value"><%= user.getEmail() %></span></td>
					<td class="pair"><span class="label">Personal Email ID</span><br><span class="value"><%= pers_email!=null && !pers_email.equals("") ? pers_email:"Not Available" %></span></td>
					
				</tr>
				<tr>
					<td class="pair"><span class="label">Phone Number</span><br><span class="value"><%= phone!=null && !phone.equals("") ? phone:"Not Available" %></span></td>
					<td class="pair"><span class="label">Alternate Phone Number</span><br><span class="value"><%= alt_phone!=null && !alt_phone.equals("") ? alt_phone:"Not Available" %></span></td>
				</tr>
			</table>
		</div>
		
		<div class="address">
			<div class="head">ADDRESSES</div>
			<table class="table_content">
				<tr>
					<td><span class="label">Current Address</span><br><span class="value"><%= curr_address!=null && !curr_address.equals("") ? curr_address:"Not Available" %></span></td>
					<td><span class="label">Permanent Address</span><br><span class="value"><%= perm_address!=null && !perm_address.equals("") ? perm_address:"Not Available" %></span></td>
				</tr>
			</table>
		</div>
	
	</div>

</body>
</html>