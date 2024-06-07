<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.vishesh.task7.*, java.util.*, java.net.URLEncoder" %> 
 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Documents</title>

<link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="/Task7/styles/documents.css">

</head>
<body>

	<%
	
		List<String> files = (List<String>)session.getAttribute("OFFICE_DOCS");
	
	%>

	<div class="cont">
	
		<% if(files!=null){ %>
			<% if(files.get(0)!=null && !files.get(0).equals("")){ %>
				<div class="block">
					<%
						String encodedFile1 = URLEncoder.encode(files.get(0),"UTF-8");
					%>
						
				    <a href="/Task7/ServletFileLoader?path=<%= encodedFile1 %>" target="_blank" class="link"></a><br>
				    <div class="file_name">Pan Card<br><span class="light">(Click to Open)</span></div>
				</div>
			<% } %>
			<% if(files.get(1)!=null && !files.get(1).equals("")){ %>
				<div class="block">
					<%
						String encodedFile2 = URLEncoder.encode(files.get(1),"UTF-8");
					%>
						
				    <a href="/Task7/ServletFileLoader?path=<%= encodedFile2 %>" target="_blank" class="link"></a><br>
				    <div class="file_name">Photo<br><span class="light">(Click to Open)</span></div>
				</div>
			<% } %>
			<% if(files.get(2)!=null && !files.get(2).equals("")){ %>
				<div class="block">
					<%
						String encodedFile3 = URLEncoder.encode(files.get(2),"UTF-8");
					%>
						
				    <a href="/Task7/ServletFileLoader?path=<%= encodedFile3 %>" target="_blank" class="link"></a><br>
				    <div class="file_name">12th Marksheet<br><span class="light">(Click to Open)</span></div>
				</div>
			<% } %>
			<% if(files.get(3)!=null && !files.get(3).equals("")){ %>
				<div class="block">
					<%
						String encodedFile4 = URLEncoder.encode(files.get(3),"UTF-8");
					%>
						
				    <a href="/Task7/ServletFileLoader?path=<%= encodedFile4 %>" target="_blank" class="link"></a><br>
				    <div class="file_name">10th Marksheet<br><span class="light">(Click to Open)</span></div>
				</div>
			<% } %>
		<% } %>
	
	</div>

</body>
</html>