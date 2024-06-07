<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.vishesh.task7.*" %>    
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Employee Information</title>

<link type="text/css" rel="stylesheet" href="/Task7/styles/userInfo.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

<script>

	$(document).ready(function(){
		let educationIndex = 0;
		
		$('#addEducation').click(function(e){
			e.preventDefault();
			educationIndex++;
			
			const educationFields = 
				'<div class="education-entry" id="education-'+educationIndex+'">'+
					'<h4>Education '+educationIndex+'</h4>'+
					'<div class="flex">'+
						'<label>School/College Name:</label>'+
                    	'<input type="text" name="education['+educationIndex+'][school]" required>'+
                    '</div>'+
                    '<div class="flex">'+
	                    '<label>Location:</label>'+
	                    '<input type="text" name="education['+educationIndex+'][location]" required>'+
                    '</div>'+
                    '<div class="flex">'+
	                    '<label>Grade:</label>'+
	                    '<input type="text" name="education['+educationIndex+'][grade]" required>'+
                    '</div>'+
                    '<div class="flex">'+
	                    '<label>Degree:</label>'+
	                    '<input type="text" name="education['+educationIndex+'][degree]" required>'+
                    '</div>'+
                    '<div class="flex">'+
	                    '<label>From Date:</label>'+
	                    '<input type="date" name="education['+educationIndex+'][from_date]" required>'+
                    '</div>'+
                    '<div class="flex">'+
	                    '<label>To Date:</label>'+
	                    '<input type="date" name="education['+educationIndex+'][to_date]" required>'+
                    '</div>'+
                    '<button class="removeEducation" data-index="'+educationIndex+'">Remove</button>'+
                    '<hr>'+
				'</div>'
			;
			
			$('#educationContainer').append(educationFields);
		});
		
		$(document).on('click','.removeEducation',function(e){
			e.preventDefault();
			const index = $(this).data('index');
			$('#education-'+index).remove();
			reindexEducationEntries();
		});
		
		function reindexEducationEntries() {
	        $('#educationContainer .education-entry').each(function(index) {
	            const newIndex = index + 1;
	            $(this).attr('id', 'education-' + newIndex);
	            $(this).find('h4').text('Education ' + newIndex);
	            $(this).find('.removeEducation').data('index', newIndex);
	            $(this).find('input, textarea').each(function() {
	                const name = $(this).attr('name');
	                const newName = name.replace(/\[.*?\]/, `[${newIndex}]`);
	                $(this).attr('name', newName);
	            });
	        });
	        educationIndex = $('#educationContainer .education-entry').length;
	    }
	});

</script>

</head>
<body>


	<%
		User user = (User)session.getAttribute("user");
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
			
            <li>
                <form action="UserControllerServlet" method="post">
		
					<input type="hidden" name="command" value="LOGOUT">
					
					<input type="submit" value="Logout">
				
				</form>
            </li>
            
        </ul>
    </nav>


	<div class="container">
	
		<h1>Add Info</h1>
			
		
		<form action="/Task7/api/addInfo" method="post" enctype="multipart/form-data">
			
			<div class="form_cont">
			
				<p class="title">Personal Details</p>
			
				<div class="pers_cont">
					
					<div class="flex">
						<label for="age">Age: </label>
						<input type="number" id="age" class="age" name="age">
					</div>
					
					<div class="flex">
						<label for="bg">Blood Group: </label>
						<input type="text" id="bg" class="bg" name="bg">
					</div>
					

					<div class="flex">
						<label for="phone">Phone no: </label>
						<input type="text" id="phone" class="phone" name="phone">
					</div>
					
					
					<div class="flex">
						<label for="alt_phone">Alternate Phone no: </label>
						<input type="text" id="alt_phone" class="alt_phone" name="alt_phone">
					</div>
					
					<div class="flex">
						<label for="pers_email">Personal Email id: </label>
						<input type="text" id="pers_email" class="pers_email" name="pers_email">
					</div>
					
					<div class="flex">
						<label for="curr_address">Current Address: </label>
						<textarea id="curr_address" name="curr_address" class="curr_address"></textarea>
					</div>
					
					<div class="flex">
						<label for="perm_address">Permanent Address: </label>
						<textarea id="perm_address" name="perm_address" class="perm_address"></textarea>
					</div>
					
					<div class="flex">
						<label for="photo">Upload photo: </label>
						<input type="file" id="photo" class="photo" name="photo">
					</div>
					
					
				</div>
				
				<p class="title">Education Details</p>
				
				<div class="edu_cont">
					<div id="educationContainer">
				        
				    </div>
				    <button id="addEducation">Add Education</button>
				</div>
				
				<p class="title">Employee Documents</p>
				
				<div class="emp_doc">
				
					<div class="flex">
						<label for="pan">Upload PAN Card: </label>
						<input type="file" id="pan" class="pan" name="pan">
					</div>
					
					<div class="flex">
						<label for="emp_photo">Upload Photo: </label>
						<input type="file" id="emp_photo" class="emp_photo" name="emp_photo">
					</div>
					
					
					<div class="flex">
						<label for="marksheet_12">12th Marksheet: </label>
						<input type="file" id="marksheet_12" class="marksheet_12" name="marksheet_12">
					</div>
					
					<div class="flex">
						<label for="marksheet_10">10th Marksheet: </label>
						<input type="file" id="marksheet_10" class="marksheet_10" name="marksheet_10">
					</div>
					
				</div>
			    
			    <div>
					<input type="submit" class="regBtn" value="Add Info">
					<a href="/Task7/api/home">Go back to home</a>
				</div>
			
			</div>
			
		
		</form>
			
	</div>

</body>
</html>