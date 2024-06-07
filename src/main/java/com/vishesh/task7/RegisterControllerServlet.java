package com.vishesh.task7;

import java.io.IOException;
import java.util.Random;

import javax.sql.DataSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vishesh.task7.Response.ApiResponse;
import com.vishesh.task7.model.UserRegisterData;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/register")
public class RegisterControllerServlet extends HttpServlet{
	
private static UserDBUtil userDBUtil;
	
	@Resource(name="jdbc/user-tracker")
	private DataSource dataSource;
	
	@Override
	public void init() throws ServletException{
		super.init();
		
		try {
			
			userDBUtil = new UserDBUtil(dataSource);
			
		}
		catch(Exception ex) {
			throw new ServletException(ex);
		}
	}
	
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		request.getRequestDispatcher("/register.jsp").forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		try {
			registerUser(request,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
private void registerUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		ObjectMapper objectMapper = new ObjectMapper();
	    UserRegisterData userData = objectMapper.readValue(request.getInputStream(), UserRegisterData.class);
		
		String name = userData.getName();
		String email = userData.getEmail();
		String password = userData.getPassword();
		String confirmPassword = userData.getConfirmPassword();
		
		if(!password.equals(confirmPassword)) {
			
			ApiResponse<Void> apiResponse = new ApiResponse<>(null, "Password and Confirm Password should be same!");
            response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
            return;
			
		}
		
		String default_profile = "https://static.vecteezy.com/system/resources/thumbnails/009/734/564/small_2x/default-avatar-profile-icon-of-social-media-user-vector.jpg";
		
		String[] temp = name.split(" ");
		String uid = String.valueOf(Character.toUpperCase(temp[0].charAt(0)));
		if(temp.length>1) {
			uid += String.valueOf(Character.toUpperCase(temp[1].charAt(0)));
		}
		Random random = new Random();
		uid += String.valueOf(random.nextInt(10000)+1);
		
		User user = new User(uid,name,email,password,default_profile);
		
		boolean res = userDBUtil.registerUser(user);
		
		HttpSession session = request.getSession(true);
		
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
		
		if(!res) {
			
            ApiResponse<User> apiResponse = new ApiResponse<>(null, "Invalid email entered!");
            response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
			return;
			 
		}
	
		response.setStatus(HttpServletResponse.SC_OK);
		ApiResponse<User> apiResponse = new ApiResponse<>(null, "Successfully registered! Please login to continue.");
	    response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
		
	}

}
