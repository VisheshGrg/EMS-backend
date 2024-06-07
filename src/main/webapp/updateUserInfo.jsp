<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.vishesh.task7.*" %>    
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Update Employee Information</title>

<link type="text/css" rel="stylesheet" href="/Task7/styles/updateUserInfo.css">

<link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">

</head>
<body>


	<%
		User user = (User)session.getAttribute("user");
		User update_user = (User)session.getAttribute("update_user");
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
	
		<h2>Update Info</h2>
			
		
		<form action="/Task7/admin/updateInfo" method="post">
		
			<input type="hidden" name="email" value="<%= update_user.getEmail() %>">
			<input type="hidden" name="salary_amount" value="<%= update_user.getSalary_amount() %>">
			<input type="hidden" name="salary_total" value="<%= update_user.getSalary() %>">
			
			<div class="form_cont">
				
				<div class="pers_cont">
				
					<div class="flex">
						<label for="department">Department: </label>
						<input type="text" id="department" class="department" name="department" value="<%= update_user.getDepartment() %>">
					</div>
				
	                <div class="flex">
	                    <label for="age">Age: </label>
	                    <input type="number" id="age" class="age" name="age" value="<%= update_user.getAge() %>">
	                </div>
	
	                <div class="flex">
	                    <label for="bg">Blood Group: </label>
	                    <input type="text" id="bg" class="bg" name="bg" value="<%= update_user.getBlood_group() %>">
	                </div>
	
	                <div class="flex">
	                    <label for="phone">Phone no: </label>
	                    <input type="text" id="phone" class="phone" name="phone" value="<%= update_user.getPhoneNo() %>">
	                </div>
	
	                <div class="flex">
	                    <label for="alt_phone">Alternate Phone no: </label>
	                    <input type="text" id="alt_phone" class="alt_phone" name="alt_phone" value="<%= update_user.getAlt_phoneNo() %>">
	                </div>
	
	                <div class="flex">
	                    <label for="pers_email">Personal Email id: </label>
	                    <input type="text" id="pers_email" class="pers_email" name="pers_email" value="<%= update_user.getPers_email() %>">
	                </div>
	
	                <div class="flex">
	                    <label for="curr_address">Current Address: </label>
	                    <textarea id="curr_address" name="curr_address" class="curr_address"><%= update_user.getAddress() %></textarea>
	                </div>
	
	                <div class="flex">
	                    <label for="perm_address">Permanent Address: </label>
	                    <textarea id="perm_address" name="perm_address" class="perm_address"><%= update_user.getPerm_address() %></textarea>
	                </div>
				
				</div>

            </div>
            
            <div>
                <input type="submit" class="regBtn" value="Update Info">
				<a href="/Task7/api/home">Cancel Update</a>
            </div>
			
		
		</form>
			
	</div>
	
	<%
		session.removeAttribute("update_user");
	%>

</body>
</html>