<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.vishesh.task7.*, java.util.*" %>    
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Manage Leave Types</title>

<link type="text/css" rel="stylesheet" href="/Task7/styles/manage_leave_types.css">

<link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">

</head>
<body>
	

	<%
		User user = (User)session.getAttribute("user");
		List<Integer> lst = (ArrayList<Integer>)session.getAttribute("leave_types");
	%>

	
	<nav class="navbar">
        <ul>
        	
        	<li><a href="add_salary_info.jsp">Add salary Information</a> </li>
				
			<li><a href="userInfo.jsp">Add Employee Info</a></li> 
			
			<li><a href="update_confirm.jsp">Update Employee Info</a></li>
			
			<li>
			
				<form action="UserControllerServlet" method="post">
	
					<input type="hidden" name="command" value="LEAVE_REQUESTS">
					
					<input type="submit" value="Leave Requests">
				
				</form>
			
			</li>
       
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
	
	
		<h1 class="head">Manage Leave Types</h1>
	
		<div class="leaves_types">
		
			<form class="form" action="/Task7/admin/changeLeaveTypes" method="post">
			
				<div class="space"><input type="checkbox" name="PL" value="1" <% if (lst.get(0)==1) out.print("checked"); %>>  Priviledged Leave</div>
				<div class="space" ><input type="checkbox" name="CL" value="1" <% if (lst.get(1)==1) out.print("checked"); %>>  Casual Leave</div>
				<div class="space" ><input type="checkbox" name="SL" value="1" <% if (lst.get(2)==1) out.print("checked"); %>>  Sick Leave</div>
				<div class="space" ><input type="checkbox" name="ML" value="1" <% if (lst.get(3)==1) out.print("checked"); %>>  Maternity Leave</div>
				<div class="space" ><input type="checkbox" name="MrL" value="1" <% if (lst.get(4)==1) out.print("checked"); %>>  Marriage Leave</div>
				<div class="space" ><input type="checkbox" name="PtL" value="1" <% if (lst.get(5)==1) out.print("checked"); %>>  Paternity Leave</div>
				<div class="space" ><input type="checkbox" name="COFF" value="1" <% if (lst.get(6)==1) out.print("checked"); %>>  Compensatory Off Leave</div>
				<div class="space" ><input type="checkbox" name="LOP" value="1" <% if (lst.get(7)==1) out.print("checked"); %>>  LOP Leave</div>
				
				<input type="submit" value="Save changes">
			
			</form>
		
		</div>
	
	</div>
	

</body>
</html>