<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Registration form</title>

<link type="text/css" rel="stylesheet" href="styles/styles.css">

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
	
		<h2>SignUp</h2>
		
		
		
		<form action="/Task7/register" method="post">
			
			<table>
		
				<tr>
					<td><label for="name">Name: </label></td>
					<td><input type="text" id="name" class="name" name="name"></td><br><br>
				</tr>
				
				<tr>
					<td><label for="email">Email: </label></td>
					<td><input type="email" id="email" class="email" name="email"></td><br><br>
				</tr>
				
				<tr>
					<td><label for="pass">Password: </label></td>
					<td><input type="password" id="pass" class="pass" name="password"></td><br><br>
				</tr>
				
				<tr>
					<td><label for="confPass">Confirm Password: </label></td>
					<td><input type="password" id="confPass" class="confPass" name="confPass"></td><br><br>
				</tr>
				
				<tr>
					<td><input type="submit" class="regBtn" value="SignUp"></td>
					<td><a href="/Task7/login">Already have an account? Login</a></td>
				</tr>
			</table>
		
		</form>
	
	</div>

</body>
</html>