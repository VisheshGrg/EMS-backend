<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.vishesh.task7.*,java.util.*" %>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>       
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Salary Details</title>

<link rel="stylesheet" type="text/css" href="/Task7/styles/salary_info.css">

<link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">

</head>
<body>

	
	<%
		Object u = session.getAttribute("user");
		User user = (User)u;	
		List<Map<String,String>> earnings = (List<Map<String,String>>)session.getAttribute("earnings");
		List<Map<String,String>> deductions = (List<Map<String,String>>)session.getAttribute("deductions");
	
	%>
	

	<nav class="navbar">
        <ul>
        	
        	<li><a href="home.jsp">Home</a></li>
        	
        	<% if(user.getIsAdmin()){ %>
				<li><a href="add_salary_info.jsp">Add salary Information</a> </li>
			<% } %>
        
            <li><a href="upload.jsp">Upload Document</a></li>
            
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
	
		
		<div class="header">
			Salary Details
		</div>
		
		<div class="search">
		
			<form action="/Task7/api/salaryDetails" method="post">
			
				<span class="month">Month</span>
				
				<select name="month" class="sel_month">
					<option value="January">January</option>
					<option value="February">February</option>
					<option value="March">March</option>
					<option value="April">April</option>
					<option value="May">May</option>
					<option value="June">June</option>
					<option value="July">July</option>
					<option value="August">August</option>
					<option value="September">September</option>
					<option value="October">October</option>
					<option value="November">November</option>
					<option value="December">December</option>
				</select>
				
				<span class="year">Year</span>
				
				<select name="year" class="sel_year">
					<option value="2024">2024</option>
					<option value="2023">2023</option>
					<option value="2022">2022</option>
					<option value="2021">2021</option>
					<option value="2020">2020</option>
					<option value="2019">2019</option>
					<option value="2018">2018</option>
					<option value="2017">2017</option>
					<option value="2016">2016</option>
					<option value="2015">2015</option>
				</select>
				
				<input type="submit" class="btn" value="Go">
			
			</form>
		
		</div>
		
		<div class="result">
		
			<p>Payroll Breakup for the Month of <span class="color"><%= session.getAttribute("month") %> - <%= session.getAttribute("year") %></span></p>
			
			<div class="view_result">
			
				<div class="earnings scroll">
					
					<table class="scroll_list">
					
						<tr class="head"><td colspan="2">Earnings</td></tr>
					
						<% for(Map<String,String> mp: earnings){ %>
						
							<tr class="row">
							
								<td class="left"><%= mp.get("type") %></td> <td class="right"><%= mp.get("amount") %></td>
							
							</tr>
						
						<% } %>
					
					</table>
				
				</div>
				
				<div class="deductions scroll">
				
					<table class="scroll_list">
					
						<tr class="head"><td colspan="2">Deductions</td></tr>
					
						<% for(Map<String,String> mp: deductions){ %>
						
							<tr class="row">
							
								<td class="left"><%= mp.get("type") %></td> <td class="right"><%= mp.get("amount") %></td>
							
							</tr>
						
						<% } %>
					
					</table>
				
				</div>
			
			</div> 
		
		</div>
	
	
	</div>


</body>
</html>