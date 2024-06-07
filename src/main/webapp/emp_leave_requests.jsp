<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.vishesh.task7.*, com.vishesh.task7.model.*, java.util.*, java.text.SimpleDateFormat" %>    
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Employee Information</title>

<link type="text/css" rel="stylesheet" href="/Task7/styles/leave_status.css">

<link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">

</head>
<body>
	

	<%
		User user = (User)session.getAttribute("user");
		List<Leave> leaves = (ArrayList<Leave>)session.getAttribute("leave_status");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");		
		Date currDate = (Date)session.getAttribute("curr_date");
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
				
				<li>
				
					<form action="UserControllerServlet" method="post">
		
						<input type="hidden" name="command" value="UPDATE_SELF_CLICK">
						
						<input type="submit" value="Update Info">
					
					</form>
				
				</li>
				
				<li><a href="attendance.jsp">Mark Leave</a></li>
				
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
	
	
		<h1 class="head">Leave's Status</h1>
	
		<div class="leaves">
		
			<% for(int i=0; i<leaves.size(); i++){ %>
		
			<div class="leave">
			
				<table>
					
					<tr>
						<td>Leave Type</td>
						<td class="right"><%= leaves.get(i).getLeave_type() %></td>
					</tr>
					
					<tr>
						<td>Date Requested</td>
						<td class="right"><%= leaves.get(i).getDateOfRequest() %></td>
					</tr>
					
					<tr>
						<td>Leave from</td>
						<td class="right"><%= leaves.get(i).getFrom_date() %></td>
					</tr>
					
					<tr>
						<td>Leave till</td>
						<td class="right"><%= leaves.get(i).getTo_date() %></td>
					</tr>
					
					<tr>
						<td>Status</td>
						<%
							String clr = "blue_";
							if(leaves.get(i).getStatus().equals("APPROVED")){
								clr = "green_";
							}
							else if(leaves.get(i).getStatus().equals("REJECTED")){
								clr = "red_";
							}
						
						%>
						<td class="right <%= clr %>"><%= leaves.get(i).getStatus() %></td>
					</tr>
					
					<tr>
						<td>Note</td>
						<td class="right">
							<div class="note_container">
								<%= leaves.get(i).getNote() %>
							</div>
						</td>
					</tr>
					
					<tr>
						<td colspan="2" class="center">
							
							<% 
								Date leave_start = sdf.parse(leaves.get(i).getFrom_date());
								if(currDate.before(leave_start)){							
							%>
								<form action="/Task7/api/cancelLeave" method="post">

									<input type="hidden" name="leave_id" value="<%= leaves.get(i).getLeave_id() %>">
									<input type="submit" value="Cancel Leave">
								
								</form>
							
							<% } %>
							
						</td>
					</tr>
				
				</table>
			
			</div>
		
		<% } %>
		
		</div>
	
	</div>
	

</body>
</html>