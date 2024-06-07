<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.vishesh.task7.*" %>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Salary Information</title>

<link type="text/css" rel="stylesheet" href="/Task7/styles/styles.css">

<link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">

</head>
<body>


	<% 
	
		Object msg = session.getAttribute("message");
		Object color = session.getAttribute("color");
	
		if(msg!=null){ 
	
	%>
	
		<div class="msg <%= (String)color %>">
			<span><%= (String)msg %></span>
		</div>
		
	<% 
		session.removeAttribute("message");
		session.removeAttribute("color");
		} 
	%>


	<div class="container">
	
		<h2>Salary Info</h2>
		
		<%
			User user = (User)session.getAttribute("user");
		%>
		
		
		<form action="/Task7/admin/addSalaryInfo" method="post">
			
			<table>
				
				<tr>
					<td><label for="sid">Salary id: </label></td>
					<td><input type="text" id="sid" class="sid" name="sid"></td><br><br>
				</tr>
				
				<tr>
					<td><label for="uid">Employee id: </label></td>
					<td><input type="text" id="uid" class="uid" name="uid"></td><br><br>
				</tr>
				
				<tr>
					<td><label for="salary_amount">Salary amount: </label></td>
					<td><input type="text" id="salary_amount" class="salary_amount" name="salary_amount"></td><br><br>
				</tr>
				
				<tr>
					<td><label for="salary_type">Salary type: </label></td>
					<td>
						<select name="salary_type" id="salary_type">
							<option value="Base">Base salary</option>
							<option value="Bonus">Bonus salary</option>
							<option value="Overtime">Overtime salary</option>
							<option value="Income Tax">Income Tax</option>
							<option value="Professional Tax">Professional Tax</option>
							<option value="Leave Deduction">Leave Deduction</option>
						</select>
					</td><br><br>
				</tr>
				
				<tr>
					<td><label for="salary_des">Salary Description: </label></td>
					<td><textarea id="salary_des" class="salary_des" name="salary_des"></textarea></td><br><br>
				</tr>
				
				
				<tr>
					<td><input type="submit" class="btn" value="Add Salary Info"></td>
					<td><a href="/Task7/api/home">Cancel</a></td>
				</tr>
				
			</table>
			
		
		</form>
			
	</div>

</body>
</html>