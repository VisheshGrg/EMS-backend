<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.vishesh.task7.*, java.util.*" %>    
  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Personal</title>

<link rel="stylesheet" type="text/css" href="/Task7/styles/education.css">

<link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">

</head>
<body>
	
	<%
	
		List<List<String>> education_details = (List<List<String>>)session.getAttribute("USER_EDUCATION");
	
	%>

	<div class="cont">
	
		<% if(education_details!=null){ 
				for(List<String> education: education_details){
					String education_id = education.get(0);
					String schoolName = education.get(1);
	                String location = education.get(2);
	                String grade = education.get(3);
	                String degree = education.get(4);
	                String fromDate = education.get(5);
	                String toDate = education.get(6);
		%>
	
		<div class="College">
			<div class="head">Education-<%= education_id %></div>
			<table class="table_content">
				
				<tr>
					<td class="pair"><span class="label">Name of College</span><br><span class="value"><%= schoolName %></span></td>
					<td class="pair"><span class="label">Degree</span><br><span class="value"><%= degree %></span></td>
					<td class="pair"><span class="label">Year of passing</span><br><span class="value"><%= toDate.substring(0, 4) %></span></td>
				</tr>
				<tr>
					<td class="pair"><span class="label">Location</span><br><span class="value"><%= location %></span></td>
					<td class="pair"><span class="label">CGPA</span><br><span class="value"><%= grade %></span></td>
					<td></td>
				</tr>
			</table>		
		</div>
		
		<% }} %>
	
	</div>

</body>
</html>