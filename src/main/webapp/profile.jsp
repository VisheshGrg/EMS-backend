<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.vishesh.task7.*, java.util.*" %>    
<%@page import="java.net.URLEncoder" %>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Profile</title>

<link rel="stylesheet" type="text/css" href="/Task7/styles/profile.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>

	$(document).ready(function(){
		function loadContent(pageUrl){
			$.ajax({
				url: pageUrl,
				type: 'POST',
				success: function(data){
					$('.dynamic').html(data);
				},
				error: function(xhr,status,error){
					console.log('Error in loading page!', error);
				}
			})
		}
		
		$('#Personal').click(function(e){
			e.preventDefault();
			loadContent('/Task7/personal.jsp');
		});
		
		$('#Education').click(function(e){
			e.preventDefault();
			loadContent('/Task7/education.jsp');
		});
		
		$('#Documents').click(function(e){
			e.preventDefault();
			loadContent('/Task7/documents.jsp');
		});
		
		$('#Files').click(function(e){
			e.preventDefault();
			loadContent('/Task7/files.jsp');
		});
		
		$('#Personal').trigger('click');
		
	})

</script>

</head>
<body>

	<%
	
		User user = (User)request.getAttribute("USER_INFO");
		List<String> files = (List<String>)request.getAttribute("FILES");
		List<List<String>> education_details = (List<List<String>>)request.getAttribute("USER_EDUCATION");
		List<String> office_docs = (List<String>)request.getAttribute("OFFICE_DOCS");
		
		session.setAttribute("USER_INFO",user);
		session.setAttribute("FILES",files);
		session.setAttribute("USER_EDUCATION",education_details);
		session.setAttribute("OFFICE_DOCS",office_docs);
		String department = user.getDepartment();
	
	%>

	
	<nav class="navbar">
        <ul>
        	
        	<li><a class="btn" href="home.jsp">Home</a></li>
        	
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
    
    <div class="header">
    	
   		<div class="name"><%= user.getName() %></div>
   		<div class="links">
   		
   			<ul class="links_list">
   				<li><a id="Personal" class="lnk" href="#">Personal</a></li>
   				<li><a id="Education" class="lnk" href="#">Education</a></li>
   				<li><a id="Documents" class="lnk" href="#">Documents</a></li>
   				<li><a id="Files" class="lnk" href="#">File Management</a></li>
   			</ul>
   		
   		</div>
   		
   	</div>
   
    <div class="container">
    	
    	<div class="static">
    		
   			<div class="profile_img">
   			
				
					<%
						String encodedPhoto = URLEncoder.encode(user.getPhoto(), "UTF-8");
					%>
				
					<img src="/Task7/ServletImageLoader?path=<%=encodedPhoto%>" alt="profile" class="profile_photo">
   			
   			</div>
   			<div class="domain"><%= department!=null && !department.equals("")?user.getDepartment():"*Department not alloted*" %></div>
   			<div class="email"><i class="fa fa-envelope" style="font-size:15px;color:#0096c7;margin-right:5px;"></i><%= user.getEmail() %></div>
   			<div class="update_buttons">
   				<% if(user.getIs_infoSet()){ %>
   					<span class="space">
				
						<a href="/Task7/api/updateSelf" class="btn">Update Info</a>
					
					</span>
				<% }else{ %>
					<span class=""><a class="btn" href="/Task7/api/addInfo">Add Info</a></span> 
   				<% } %>
   			</div>
   		
   		</div>
   		<div class="dynamic">
   		
   			
   		
   		</div>
    	
    </div>

</body>
</html>