<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.vishesh.task7.*,com.vishesh.task7.model.*, java.util.*" %>    
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Employee Information</title>

<link type="text/css" rel="stylesheet" href="/Task7/styles/leave_requests.css">

<link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">

</head>
<body>
	

	<%
		User user = (User)session.getAttribute("user");
		List<Leave> leaves = (ArrayList<Leave>)session.getAttribute("employee_leaves");
	%>

	
	<nav class="navbar">
        <ul>
        	
        	<li><a href="add_salary_info.jsp">Add salary Information</a> </li>
        
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
	
	
		<h1 class="head">Employee Leave Requests</h1>
	
		<div class="leaves">
		
			<% for(int i=0; i<leaves.size(); i++){ %>
		
			<div class="leave">
			
				<div class="emp_info">
				
					<p class="name"><%= leaves.get(i).getName() %></p>
					<p class="uid">Employee id: <%= leaves.get(i).getUid() %></p>
					<p class="email"><%= leaves.get(i).getEmail() %></p>
					
					<div class="btns">
					
						<form action="/Task7/admin/leaveRequests/approve" method="post" class="btn">

							<input type="hidden" name="leave_id" value="<%= leaves.get(i).getLeave_id() %>">
							
							<input type="submit" value="Approve" class="btn_approve">
					
						</form>
						
						<form action="/Task7/admin/leaveRequests/reject" method="post" class="btn">
		
							<input type="hidden" name="leave_id" value="<%= leaves.get(i).getLeave_id() %>">
							
							<input type="submit" value="Reject" class="btn_reject">
				
						</form>
						
					</div>
				
				</div>
				<div class="leave_info">
				
				
					<table>
					
						<tr>
							<td>Leave Type</td>
							<td><%= leaves.get(i).getLeave_type() %></td>
						</tr>
						
						<tr>
							<td>Leave from</td>
							<td><%= leaves.get(i).getFrom_date() %></td>
						</tr>
						
						<tr>
							<td>Leave till</td>
							<td><%= leaves.get(i).getTo_date() %></td>
						</tr>
						
						<tr>
							<td>Note</td>
							<td>
								<div class="note_container">
									<%= leaves.get(i).getNote() %>
								</div>
							</td>
						</tr>
					
					</table>
				
				</div>

			
			</div>
		
		<% } %>
		
		</div>
	
	</div>
	

</body>
</html>