<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="com.vishesh.task7.*" %>    
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>        
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Update Employee Information</title>

<link rel="stylesheet" type="text/css" href="/Task7/styles/update_confirm.css">

<link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">

</head>
<body>


	<%
		User user = (User)session.getAttribute("user");
	%>

	
	<nav class="navbar">
        <ul>
        	
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
        
            <li><a href="upload.jsp">Upload Document</a></li>
			
            <li>
                <form action="UserControllerServlet" method="post">
		
					<input type="hidden" name="command" value="LOGOUT">
					
					<input type="submit" value="Logout">
				
				</form>
            </li>
            
        </ul>
    </nav>
    
    <div class="container">
			
		
		<form action="/Task7/admin/updateInfo/confirm" method="post">
			
			<table>
				
				
				<tr>
					<td><label for="uid">Enter employee id to update: </label></td>
					<td><input type="text" id="uid" class="uid" name="uid"></td><br><br>
				</tr>
				
				<tr>
					<td><input type="submit" class="btn" value="Confirm Update"></td>
					<td><a href="/Task7/api/home">Go back to home</a></td>
				</tr>
				
			</table>
			
		
		</form>
			
	</div>
    

	

</body>
</html>