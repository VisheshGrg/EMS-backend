package com.vishesh.task7;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vishesh.task7.model.UserJwt;
import com.vishesh.task7.model.UserLoginData;
import com.vishesh.task7.model.UserRegisterData;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login/*")
public class LoginControllerServlet extends HttpServlet {
	
	private static final long serialVersionUID = 134123123L;

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
		
		String action = request.getPathInfo();
		if(action==null) {
			action="";
		}
		
		try {
			switch(action) {
			}
		}
		catch(Exception e) {
			throw new ServletException(e);
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String action = request.getPathInfo();
		if(action==null) {
			action="";
		}
		
		try {
			
			switch(action) {
			case "":
				loginUser(request,response);
				break;
			case "/forgetPasswordEmail":
				verifyEmail(request,response);
				break;
			case "/verifyOTP":
				verifyOTP(request,response);
				break;
			case "/loginOTPEmail":
				loginWithOTP(request,response);
				break;
			case "/resetPassword":
				resetPassword(request,response);
				break;
			}
			
		}
		catch(Exception e) {
			throw new ServletException(e);
		}
	}
	
	private void loginUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ObjectMapper objectMapper = new ObjectMapper();
	    UserLoginData loginData = objectMapper.readValue(request.getInputStream(), UserLoginData.class);
		
		String email = loginData.getEmail();
		String password = loginData.getPassword();
		
		UserJwt jwt = userDBUtil.loginUser(email,password);
		
		Map<String,Object> responseData = new HashMap();
		
		if(jwt!=null) {
			
			responseData.put("jwt", jwt);
			responseData.put("message","Welcome back "+jwt.getName());
			
			response.setStatus(HttpServletResponse.SC_OK);
	        response.setContentType("application/json");
	        
	        objectMapper = new ObjectMapper();
	        objectMapper.writeValue(response.getWriter(), responseData);
			
		}
		else {
			
			responseData.put("message", "Invalid email or password");
			responseData.put("jwt", null);
			
			objectMapper = new ObjectMapper();
	        objectMapper.writeValue(response.getWriter(), responseData);
			
		}
		
	}

	private void verifyEmail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,Object> responseData = new HashMap();
		ObjectMapper om = new ObjectMapper();
		
		Map<String, Object> requestBodyMap = om.readValue(request.getInputStream(), Map.class);
	    Object empEmailObj = requestBodyMap.get("email");
	    
	    String email = String.valueOf(empEmailObj);

	    if (email == null) {
	        responseData.put("message", "Invalid email. Please try again later.");
	        responseData.put("email", null);
	        responseData.put("authCode", null);
	        responseData.put("type", null);
	        response.setContentType("application/json");
	        om.writeValue(response.getWriter(), responseData);
	        return;
	    }
		
		User usr = userDBUtil.findUser(email);
		
		if(usr==null) {
			responseData.put("message", "Invalid email. Please try again.");
	        responseData.put("email", null);
	        responseData.put("authCode", null);
	        responseData.put("type", null);
	        response.setContentType("application/json");
	        om.writeValue(response.getWriter(), responseData);
	        return;
		}
		
		EmailHandler eh = new EmailHandler();
		String code = eh.getRandomCode();
		
		boolean res = eh.sendEmail(email, code);
		
		if(res) {
			responseData.put("message", "Invalid email. Please try again later.");
	        responseData.put("email", email);
	        responseData.put("authCode", code);
	        responseData.put("type", "reset");
	        response.setContentType("application/json");
	        om.writeValue(response.getWriter(), responseData);
	        return;
		}
		else {
			responseData.put("message", "");
	        responseData.put("email", null);
	        responseData.put("authCode", null);
	        responseData.put("type", null);
	        response.setContentType("application/json");
	        om.writeValue(response.getWriter(), responseData);
	        return;
		}
		
	}
	
	private void loginWithOTP(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,Object> responseData = new HashMap();
		ObjectMapper om = new ObjectMapper();
		
		Map<String, Object> requestBodyMap = om.readValue(request.getInputStream(), Map.class);
	    Object empEmailObj = requestBodyMap.get("email");
	    
	    String email = String.valueOf(empEmailObj);

	    if (email == null) {
	        responseData.put("message", "Invalid email. Please try again later.");
	        responseData.put("email", null);
	        responseData.put("authCode", null);
	        responseData.put("type", null);
	        response.setContentType("application/json");
	        om.writeValue(response.getWriter(), responseData);
	        return;
	    }
		
	    User usr = userDBUtil.findUser(email);
		
		if(usr==null) {
			responseData.put("message", "Invalid email. Please try again.");
	        responseData.put("email", null);
	        responseData.put("authCode", null);
	        responseData.put("type", null);
	        response.setContentType("application/json");
	        om.writeValue(response.getWriter(), responseData);
	        return;
		}
		
		EmailHandler eh = new EmailHandler();
		String code = eh.getRandomCode();
		
		boolean res = eh.sendEmail(email, code);
		
		if(res) {
			responseData.put("message", "Invalid email. Please try again later.");
	        responseData.put("email", email);
	        responseData.put("authCode", code);
	        responseData.put("type", "login");
	        response.setContentType("application/json");
	        om.writeValue(response.getWriter(), responseData);
	        return;
		}
		else {
			responseData.put("message", "");
	        responseData.put("email", null);
	        responseData.put("authCode", null);
	        responseData.put("type", null);
	        response.setContentType("application/json");
	        om.writeValue(response.getWriter(), responseData);
	        return;
		}
		
		
	}
	
	private void verifyOTP(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,Object> responseData = new HashMap();
		ObjectMapper om = new ObjectMapper();
		
		Map<String, Object> requestBodyMap = om.readValue(request.getInputStream(), Map.class);
		String code = (String)requestBodyMap.get("otp");
	    String user_email = (String)requestBodyMap.get("email");
	    String orig_code = (String)requestBodyMap.get("authCode");
	    String type = (String)requestBodyMap.get("funcType");
		
		if(code.equals(orig_code)) {
			
			User user = userDBUtil.findUser(user_email);
			
			if(user == null) {
				responseData.put("message", "Invalid email. Please try again later.");
		        responseData.put("status", "N");
		        responseData.put("jwt", null);
		        responseData.put("email", null);
		        response.setContentType("application/json");
		        om.writeValue(response.getWriter(), responseData);
		        return;
			}
			
			if(type.equals("login")) {
				
				UserJwt jwt = new UserJwt(user.getUid(), user.getName(), user.getEmail());
				
				responseData.put("message", "Welcome back!");
		        responseData.put("status", "Y");
		        responseData.put("email", null);
		        responseData.put("jwt", jwt);
		        response.setContentType("application/json");
		        om.writeValue(response.getWriter(), responseData);
		        return;
				
			}
			else {
				
				responseData.put("message", "");
		        responseData.put("status", "Y");
		        responseData.put("email", user_email);
		        responseData.put("jwt", null);
		        response.setContentType("application/json");
		        om.writeValue(response.getWriter(), responseData);
		        return;
				
			}
			
		}
		else {
			
			responseData.put("message", "Invalid OTP. Please try again!");
	        responseData.put("status", "N");
	        responseData.put("email", null);
	        responseData.put("jwt", null);
	        response.setContentType("application/json");
	        om.writeValue(response.getWriter(), responseData);
	        return;
			
		}
		
	}

	
	private void resetPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,Object> responseData = new HashMap();
		ObjectMapper om = new ObjectMapper();
		
		Map<String, Object> requestBodyMap = om.readValue(request.getInputStream(), Map.class);
	    String email = (String)requestBodyMap.get("email");
	    String new_password = (String)requestBodyMap.get("password");
	    String confirm_password = (String)requestBodyMap.get("confirmPassword");
		
	    if(email==null) {
	    	responseData.put("message", "Something went wrong. Please try again.");
	        response.setContentType("application/json");
	        om.writeValue(response.getWriter(), responseData);
	        return;
	    }
	    
		if(new_password==null || !new_password.equals(confirm_password)) {
			responseData.put("message", "Invalid password details!");
	        response.setContentType("application/json");
	        om.writeValue(response.getWriter(), responseData);
	        return;
		}
		
		userDBUtil.resetPassword(email,new_password);
		
		responseData.put("message", "Password reset successfull");
        response.setContentType("application/json");
        om.writeValue(response.getWriter(), responseData);
        return;
		
	}
}
