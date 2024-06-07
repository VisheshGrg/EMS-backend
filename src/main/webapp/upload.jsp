<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.vishesh.task7.*" %>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>      
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Upload Document</title>

<link rel="stylesheet" type="text/css" href="/Task7/styles/upload.css">

<link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">

</head>
<body>

	<%
	
		User user = (User)session.getAttribute("user");
	
	%>
	
	<nav class="navbar">
        <ul>
        	
        	<li><a href="home.jsp">Home</a></li>
        	
        	<% if(!user.getIsAdmin()){ %>
				<li>
				
					<form action="UserControllerServlet" method="post">
		
						<input type="hidden" name="command" value="VIEW_SALARY_INFO">
						
						<input type="submit" value="View Salary Details">
					
					</form>
				
				</li>
			<% } %>
        	
        	<% if(user.getIsAdmin()){ %>
				<li><a href="add_salary_info.jsp">Add salary Information</a> </li>
			<% } %>
            
            <% if(user.getIsAdmin()){ %>
				<li><a href="userInfo.jsp">Add Info</a></li> 
			<% } %>
			
            <li>
                <form action="UserControllerServlet" method="post">
		
					<input type="hidden" name="command" value="LOGOUT">
					
					<input type="submit" value="Logout">
				
				</form>
            </li>
            
        </ul>
    </nav>

	<div class="container">
	
		<form class="form" action="/Task7/api/upload" method="POST" enctype="multipart/form-data">
		
			<label class="file_label" for="file">Click to upload</label> <br><br>
			<input type="file" id="file" class="file" name="file">
		
			<br><br>
			
			<input type="submit" value="Upload">
			
			<br><br>
		
		</form>
	
	</div>

</body>
</html>