<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Verify</title>

<link rel="stylesheet" href="/Task7/styles/styles.css" type="text/css">

<link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">

</head>
<body>

	
	<%
	
		String type = (String)session.getAttribute("type");
	
	%>
	
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
	
		<span>We sent an OTP to your mail.</span>
		
		<form action="/Task7/login/verifyEmail" method="post">
			
			<table>
			
				<tr>
					<td><label for="authCode">OTP: </label></td>
					<td><input type="text" id="authCode" name="authCode"></td>
				</tr>
				
				<tr>
					<td><input type="submit" class="btn" value="Verify"></td>
					<td>
						<a href="/Task7/login">Go back to login page</a>
					</td>
				</tr>
			
			</table>
		
		</form>
		
							
		<% if(type.equals("login")){ %>
			<form action="/Task7/login/loginOTPEmail" method="post">
				<input type="submit" class="btn" value="Send OTP again">
			</form>
		<% }else{ %>
			<form action="/Task7/login/resetPasswordEmail" method="post">
				<input type="submit" class="btn" value="Send OTP again">
			</form>
		<% } %>	
		   
			
	
	</div>

</body>
</html>