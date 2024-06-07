<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.vishesh.task7.*, java.util.*, java.net.URLEncoder" %>    

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>View Info</title>

<link type="text/css" rel="stylesheet" href="styles/viewInfo.css">

<link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">

</head>


<body>


	<%
	
		User user = (User)request.getAttribute("USER_INFO");
		List<String> files = (List<String>)request.getAttribute("FILES");

	
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
				<li><a class="btn" href="add_salary_info.jsp">Add salary Information</a> </li>
			<% } %>
        
            <li><a class="btn" href="upload.jsp">Upload Document</a></li>
            
            <% if(user.getIsAdmin()){ %>
				<li><a class="btn" href="userInfo.jsp">Add Info</a></li> 
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
	
		<table>
			
			<tr>
				<th>Employee id</th>
				<td><%= user.getUid() %></td>
			</tr>
			<tr>
				<th>Email</th>
				<td><%= user.getEmail() %></td>
			</tr>
			<tr>
				<th>Name</th>
				<td><%= user.getName() %></td>
			</tr>
			<tr>
				<th>Age</th>
				<td>
					<% if(user.getAge()==0){ %>
						-
					<% }else{ %>
						<%= user.getAge() %>
					<% } %>
				</td>
			</tr>
			<tr>
				<th>Address</th>
				<td>
					<% if(user.getAddress()==null){ %>
						-
					<% }else{ %>
						<%= user.getAddress() %>
					<% } %>
				</td>
			</tr>
			<tr>
				<th>Department</th>
				<td>
					<% if(user.getDepartment()==null){ %>
						-
					<% }else{ %>
						<%= user.getDepartment() %>
					<% } %>
				</td>
			</tr>
			<tr>
				<th>Phone no.</th>
				<td>
					<% if(user.getPhoneNo()==null){ %>
						-
					<% }else{ %>
						<%= user.getPhoneNo() %>
					<% } %>
				</td>
			</tr>
			<tr>
				<th>Salary amount</th>
				<td><%= user.getSalary_amount() %>/-</td>
			</tr>
			<tr>
				<th>Total salary</th>
				<td><%= user.getSalary() %>/- <span style="color: red;">Taxes included</span></td>
			</tr>
			<tr>
				<th>Files</th>
				<td>
				
				<%
				    for (int i = 0; i < files.size() - 1; i += 2) {
				%>
					
					<%
						String encodedFile = URLEncoder.encode(files.get(i+1),"UTF-8");
					%>
						
				    <a href="ServletFileLoader?path=<%= encodedFile %>" target="_blank"><%= files.get(i) %></a><br>
				<%
				    }
				%>

				</td>
			</tr>
		
		</table>
		
		<a class="btn" href="home.jsp">Go back to home</a>
	
	</div>

</body>
</html>