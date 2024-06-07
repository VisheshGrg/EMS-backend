<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.vishesh.task7.*, java.util.*, java.net.URLEncoder" %>      
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>File Management</title>

<link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="/Task7/styles/files.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

</head>
<body>

	<%
	
		List<String> files = (List<String>)session.getAttribute("FILES");
	
	%>
	
	<div class="cont">
	
		<%
		    for (int i = 0; i < files.size() - 1; i += 2) {
		%>
			
			<%
				String encodedFile = URLEncoder.encode(files.get(i+1),"UTF-8");
				String f = files.get(i);
				String pr = f.substring(0,Math.min(20,f.length()-1));
			%>
				
		    <div class="file"><i class="fa fa-file" style="font-size:18px;color:#bc6c25;margin-right:7px;"></i><a  href="/Task7/ServletFileLoader?path=<%= encodedFile %>" target="_blank"><abbr title="<%= f %>"><%= pr %>...</abbr></a></div>
		<%
		    }
		%>
	
	</div>

</body>
</html>