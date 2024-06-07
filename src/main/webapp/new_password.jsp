<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Reset Password</title>

<link rel="stylesheet" type="text/css" href="/Task7/styles/styles.css">

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
	
		<h2>Reset Password</h2>
	
		<form action="/Task7/login/resetPassword" method="post">
		
			<input type="hidden" name="email" value="<%= session.getAttribute("user_email") %>">
			
			<table>
			
				<tr>
					<td>New Password:</td>
					<td><input type="password" name="password" class="password"></td>
				</tr>
				<tr>
					<td>Confirm New Password:</td>
					<td><input type="password" name="confirm_password" class="confirm_password"></td>
				</tr>
				<tr>
					<td><input type="submit" value="Reset Password"></td>
					<td><a href="/Task7/login">Cancel</a></td>
				</tr>
			
			</table>
		
		</form>
	
	</div>
	

</body>
</html>