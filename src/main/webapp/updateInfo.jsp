<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.vishesh.task7.*, java.util.*" %>  
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Update Information</title>

<link type="text/css" rel="stylesheet" href="/Task7/styles/updateUserInfo.css">

<link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

<script>
    $(document).ready(function(){
        let educationIndex = $('.education-entry').length;

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
                '</div>';

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
                    const newName = name.replace(/\[\d+\]/, '['+newIndex+']');
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
		User update_user = (User)session.getAttribute("update_user");
		List<List<String>> update_education_details = (List<List<String>>)session.getAttribute("update_user_education");
		List<String> update_office_docs = (List<String>)session.getAttribute("update_office_docs");
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
	
		<h2>Update Info</h2>
			
		
		<form action="/Task7/api/updateSelf" method="post" enctype="multipart/form-data">
			
			<div class="form_cont">
			
				<p class="title">Personal Details</p>

	            <div class="pers_cont">
	                <div class="flex">
	                    <label for="age">Age: </label>
	                    <input type="number" id="age" class="age" name="age" value="<%= update_user.getAge() %>">
	                </div>
	
	                <div class="flex">
	                    <label for="bg">Blood Group: </label>
	                    <input type="text" id="bg" class="bg" name="bg" value="<%= update_user.getBlood_group() %>">
	                </div>
	
	                <div class="flex">
	                    <label for="phone">Phone no: </label>
	                    <input type="text" id="phone" class="phone" name="phone" value="<%= update_user.getPhoneNo() %>">
	                </div>
	
	                <div class="flex">
	                    <label for="alt_phone">Alternate Phone no: </label>
	                    <input type="text" id="alt_phone" class="alt_phone" name="alt_phone" value="<%= update_user.getAlt_phoneNo() %>">
	                </div>
	
	                <div class="flex">
	                    <label for="pers_email">Personal Email id: </label>
	                    <input type="text" id="pers_email" class="pers_email" name="pers_email" value="<%= update_user.getPers_email() %>">
	                </div>
	
	                <div class="flex">
	                    <label for="curr_address">Current Address: </label>
	                    <textarea id="curr_address" name="curr_address" class="curr_address"><%= update_user.getAddress() %></textarea>
	                </div>
	
	                <div class="flex">
	                    <label for="perm_address">Permanent Address: </label>
	                    <textarea id="perm_address" name="perm_address" class="perm_address"><%= update_user.getPerm_address() %></textarea>
	                </div>
	
	                <div class="flex">
	                    <label for="photo">Upload photo: </label>
	                    <input type="file" id="photo" class="photo" name="photo" value="<%= update_user.getPhoto() %>">
	                </div>
	            </div>
	            
	            <p class="title">Education Details</p>
				
				<div class="edu_cont">
				    <div id="educationContainer">
				        <%
				            for (int i = 0; i < update_education_details.size(); i++) {
				                List<String> education = update_education_details.get(i);
				        %>
				        <div class="education-entry" id="education-<%= (i + 1) %>">
				            <h4>Education <%= (i + 1) %></h4>
				            <div class="flex">
				                <label>School/College Name:</label>
				                <input type="text" name="education[<%= (i + 1) %>][school]" required value="<%= education.get(1) %>">
				            </div>
				            <div class="flex">
				                <label>Location:</label>
				                <input type="text" name="education[<%= (i + 1) %>][location]" required value="<%= education.get(2) %>">
				            </div>
				            <div class="flex">
				                <label>Grade:</label>
				                <input type="text" name="education[<%= (i + 1) %>][grade]" required value="<%= education.get(3) %>">
				            </div>
				            <div class="flex">
				                <label>Degree:</label>
				                <input type="text" name="education[<%= (i + 1) %>][degree]" required value="<%= education.get(4) %>">
				            </div>
				            <div class="flex">
				                <label>From Date:</label>
				                <input type="date" name="education[<%= (i + 1) %>][from_date]" required value="<%= education.get(5) %>">
				            </div>
				            <div class="flex">
				                <label>To Date:</label>
				                <input type="date" name="education[<%= (i + 1) %>][to_date]" required value="<%= education.get(6) %>">
				            </div>
				            <button class="removeEducation" data-index="<%= (i + 1) %>">Remove</button>
				            <hr>
				        </div>
				        <%
				            }
				        %>
				    </div>
				    <button id="addEducation">Add Education</button>
				</div>
				
				<p class="title">Office Documents</p>
	            
	            <div class="emp_doc">	
	                <div class="flex">
	                    <label for="pan">Update PAN: </label>
	                    <input type="file" id="pan" class="pan" name="pan" value="<%= update_office_docs.get(0) %>">
	                </div>
	                
	                <div class="flex">
						<label for="emp_photo">Update Photo: </label>
						<input type="file" id="emp_photo" class="emp_photo" name="emp_photo" value="<%= update_office_docs.get(1) %>">
					</div>
	
	                <div class="flex">
	                    <label for="marksheet_10">Update 10th Marksheet: </label>
	                    <input type="file" id="marksheet_10" class="marksheet_10" name="marksheet_10" value="<%= update_office_docs.get(2) %>">
	                </div>
	
	                <div class="flex">
	                    <label for="marksheet_12">Update 12th Marksheet: </label>
	                    <input type="file" id="marksheet_12" class="marksheet_12" name="marksheet_12" value="<%= update_office_docs.get(3) %>">
	                </div>
	            </div>
	            
	            <div>
	                <input type="submit" class="regBtn" value="Update Info">
					<a href="/Task7/api/home">Cancel Update</a>
	            </div>
				
			
			</div>
			
		
		</form>
			
	</div>

</body>
</html>