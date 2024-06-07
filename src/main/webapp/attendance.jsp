<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.vishesh.task7.*,java.util.*" %>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>     
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Mark Leave</title>

<link type="text/css" rel="stylesheet" href="/Task7/styles/attendance.css">

<link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">

</head>
<body>


	<%
		Object u = session.getAttribute("user");
		User user = (User)u;		
		List<Integer> leave_types = (ArrayList<Integer>)session.getAttribute("leave_types");
		
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
            
            <% if(user.getIsAdmin()){ %>
				<li><a href="userInfo.jsp">Add Employee Info</a></li> 
			<% } %>
			
			
            <li>
                <form action="UserControllerServlet" method="post">
		
					<input type="hidden" name="command" value="LOGOUT">
					
					<input type="submit" value="Logout">
				
				</form>
            </li>
            
        </ul>
    </nav>
    
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
	
		<h2>Mark Leave</h2>
	
		<form class="form" action="/Task7/api/markLeave" method="post">
		
			<table>
				
				<tr>
					<td><label for="type">Leave type</label></td>
					<td><select class="type inp" name="leave_type">
					
						<% for(int i=0; i<8; i++){ %>
						
							<% if(leave_types.get(i)==1){ %>
							
								<% if(i==0){ %>
									<option value="PL">Priviledged leave</option>
								<% }else if(i==1){ %>
									<option value="CL">Casual leave</option>
								<% }else if(i==2){ %>
									<option value="SL">Sick leave</option>
								<% }else if(i==3){ %>
									<option value="ML">Maternity leave</option>
								<% }else if(i==4){ %>
									<option value="MrL">Marriage leave</option>
								<% }else if(i==5){ %>
									<option value="PtL">Paternity leave</option>
								<% }else if(i==6){ %>
									<option value="COFF">COFF leave</option>
								<% }else{ %>
									<option value="LOP">LOP</option>
								<% } %>
							
							<% } %>
						
						<% } %>
					
					</select></td>
				</tr>
				
				<tr>
					<td><label for="from">Leave from</label></td>
					<td>
						<input type="date" id="from" name="from_date" class="from">
					</td>
				</tr>
				
				<tr>
					<td><label for="to">Leave till (including)</label></td>
					<td>
						<input type="date" id="to" name="to_date" class="to">
					</td>
				</tr>
				
				<tr>
					<td><label for="note">Note</label></td>
					<td>
						<textarea id="note" class="note" name="note" rows="5" cols="40"></textarea>
					</td>
				</tr>
				
				<tr class="form_btns">
					<td><input type="submit" class="btn" value="Mark"></td>
					<td><a href="/Task7/api/home">Cancel</a></td>
				</tr>
				
			</table>
		
		</form>
	
	</div>

</body>
</html>