<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.vishesh.task7.*" %>
<%@page import="java.net.URLEncoder" %>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Home</title>

<link rel="stylesheet" type="text/css" href="/Task7/styles/home.css">
<link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">

</head>
<body>

	<%
		Object u = session.getAttribute("user");
		User user = (User)u;
		
		Object msg = session.getAttribute("message");
		Object color = session.getAttribute("color");
	
	%>


	<nav class="navbar">
        <ul>
        	
        	<% if(!user.getIsAdmin()){ %>
				<li><a href="/Task7/api/salaryDetails">Salary Details</a></li>
				
				<li><a href="/Task7/api/markLeave">Mark Leave</a></li>
				
				<li><a href="/Task7/api/leaveStatus">Leave Status</a></li>
				
				<li><a href="/Task7/api/upload">Upload Document</a></li>
				
			<% } %>
        	
        	<% if(user.getIsAdmin()){ %>
				<li><a href="/Task7/admin/addSalaryInfo">Add salary Information</a> </li>
				
				<li><a href="/Task7/admin/updateInfo/confirm">Update Employee Info</a></li>
				
				<li><a href="/Task7/admin/leaveRequests">Leave Requests</a></li>
				
				<li><a href="/Task7/admin/changeLeaveTypes">Manage Leave Types</a></li>
				
			<% } %>
			
            <li><a href="/Task7/logout">Logout</a></li>
            
        </ul>
    </nav>

	
	<div class="container">
	
		<% if(msg!=null){ %>
		
			<div class="msg <%= (String)color %>">
				<span><%= (String)msg %></span>
			</div>
			
		<% 
			session.removeAttribute("message");
			session.removeAttribute("color");
			} 
		%>
	
		<div class="top">
		
			<div class="profile">
		
				<div class="profile_img">
				
					<%
						String encodedPhoto = URLEncoder.encode(user.getPhoto(), "UTF-8");
					%>
				
					<img src="/Task7/ServletImageLoader?path=<%=encodedPhoto%>" alt="profile" class="profile_photo">
				</div>
		
				<div class="profile_data">
					<p class="profile_name"><%= user.getName() %></p>
					<p class="profile_id">Employee id: <%= user.getUid() %></p>
					
					<% if(user.getDepartment()!=null){ %>
						<p class="profile_dep"><%= user.getDepartment() %></p>
					<% } %>
					
					<p class="profile_social"><%= user.getEmail() %></p>
					
					<% if(!user.getIsAdmin()){ %>
						<div class="btns">
							
							<a href="/Task7/api/profile" class="btn btn-primary profile_info">View Profile</a>
							
							<c:if test="${not user.getIsPunched() and not user.getIsAdmin() and not user.getIs_leave()}">
							    
							    <form action="/Task7/api/punch" method="post">
							    	
							    	<input type="hidden" name="latitude" id="latitudeInput" value="">
	        						<input type="hidden" name="longitude" id="longitudeInput" value="">
	        							
	        						<input type="submit" value="Punch" id="punchButton">
							    
							    </form>
							    
							</c:if>
						
						</div>
					<% } %>
				</div>
			
			</div>
			
			<%
				if(!user.getIsAdmin()){
			%>
			
				<div class="salary">
			
					<div class="salary1">
					
						<div class="img">
							<img class="salary_logo" src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS_xc2Yqo6b6QoGAQMKUIkg8t9fULBI7I76JA&usqp=CAU">
						</div>
						
						<div class="salary_data">
							<p class="temp_salary">Last Salary Processed</p>
							<p class="salary_val"><%= user.getLastProcessed() %></p>
						</div>
					
					</div>
					
					<div class="salary2">
					
						<div class="img">
							<img class="salary_logo" src="data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAoHCBUQFBQTERQYFxcYFxQXFBcXFxEXFxcUFxcYGBcXFxcaICwjGhwoHRcXJDUkKC0yMjIyGSI4PTgwPCwxMi8BCwsLDw4PHRERHC8oICgxMTExMTExMTExMTExMTExMTExMTExMTExMTExLzExMTExMTExMTExMTExMTExMTExMf/AABEIAMIBAwMBIgACEQEDEQH/xAAcAAEAAQUBAQAAAAAAAAAAAAAABAEDBQYHAgj/xABFEAACAQICBgcEBQsDBAMAAAABAgADEQQhBRIxQVFhBhMicYGRoQcyscEjQlJichQzQ4KSorKzwtHhJGPwFYOT8SU0c//EABoBAQADAQEBAAAAAAAAAAAAAAACAwQBBQb/xAAzEQACAQIEAQoFBAMAAAAAAAAAAQIDEQQSITEFEzJBUWFxgZGxwSKh0eHwFDNicjRD8f/aAAwDAQACEQMRAD8A7NERAEREAREQBERAEREAtVawXLfLX5QeUx5r3ZieJ+MqK3OAZWnUDbPES5MRQr2deZt5zLwBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQBERAEREAREQDWdK0zRYt9RiSDwJzKmRBixxm21KYYEMAQdoIuD3iQf+jYcHW6scdrW8r2g7cxej6gLdYxsin9p9yjid/hMymLJz1beOchGkKjBiLKuVNRkAONuJl9stkArU0oq5G15ew2PWpykKjgqdVm6xATYZ3YG2zcfWQ8TgHw/ap3entI2so+Y5wDZImGwekchfZMnRxCvsOfDfBwvREQBERAEREAREQBERAEREAREQBERAKxEQBERAEREAREhY6pYqO8wC/Wq6tsr3M9owIymOWtPdOtYg+fdAMhLOJ9xu63nlKiuvGWsSwNgDvuYBDxFUIsi0cWH2GR9LUqlVGamLqp7VtpG02G+2XnMZgqhFrQTNmwb2cc8v8AnlMrNeo1Nh8fGZ6lU1lDcReCBhNIKKbONXI9peB+0Bzv8RIejMUrhUp3Lsc+X+AJsWLwwqrqnLepG1W3ETzhcDTpZoqgm2swABbvtB0lREQcEREAREQBERAEREAREQBERAEREArERAEREAREQBImMwvWWIaxF91wQdxkuIBhXwFUbNQ+LA+VvnLRw1cfo79zJ8zM/KQDXepxA/RH9qn/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">
						</div>
						
						<div class="salary_data">
							<p class="temp_salary">YTD Tax</p>
							<p class="salary_val"><%= user.getYtdTax() %> INR</p>
						</div>
					
					</div>
				
				</div>
				
			<%
				}
			%>
			
		</div>
		
		<% if(!user.getIsAdmin()){ %>
		
			<div class="bottom">
		
				<div class="attendance_stats">
				
					<p class="att_item_head">ATTENDANCE STATS</p>
					
					<table class="att_item">
						<tr><td>Last Punch Date</td><td>Last Punch status</td><td>Punch status</td></tr>
						<tr><td><%= user.getLastPunchedDate() %></td><td><%= user.getLastPunchedStatus() %></td><td><%= user.getPunchStatus() %></td></tr>
					</table>
					
					<table class="att_item">
						<tr><td>Holiday's this Year</td> <td class="right">20</td></tr>
						<tr><td>Total leaves</td> <td class="right"><%= user.getTotal_leaves() %></td></tr>
					</table>
				
				</div>
				
				<div class="leave_percentages">
				
					<div class="leave_type">
						<div class="label">CL:</div>
						<div class="bar">
							<div class="fill" style="width: ${user.getCl()*100}%"></div>
						</div>
						<div class="perc">${user.getCl()*100}%</div>
					</div>
					
					<div class="leave_type">
				        <div class="label">SL:</div>
				        <div class="bar">
				            <div class="fill" style="width: ${user.getSl()*100}%"></div>
				        </div>
				        <div class="perc">${user.getSl()*100}%</div>
				    </div>
				
				    <div class="leave_type">
				        <div class="label">PL:</div>
				        <div class="bar">
				            <div class="fill" style="width: ${user.getPl()*100}%"></div>
				        </div>
				        <div class="perc">${user.getPl()*100}%</div>
				    </div>
				
				    <div class="leave_type">
				        <div class="label">COFF:</div>
				        <div class="bar">
				            <div class="fill" style="width: ${user.getCoff()*100}%"></div>
				        </div>
				        <div class="perc">${user.getCoff()*100}%</div>
				    </div>
				
				</div>
			
			</div>
		
		<% } %>
		
	
	</div>
	

	<script>
	    function sendLocationToForm(position) {
	        var latitude = position.coords.latitude;
	        var longitude = position.coords.longitude;
	
	        document.getElementById('latitudeInput').value = latitude;
	        document.getElementById('longitudeInput').value = longitude;
	
	        document.getElementById('locationForm').submit();
	    }
	
	    navigator.geolocation.getCurrentPosition(sendLocationToForm);
	</script>
	
	
</body>
</html>