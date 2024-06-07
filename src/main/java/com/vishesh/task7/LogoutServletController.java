package com.vishesh.task7;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.sql.DataSource;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServletController extends HttpServlet {
	
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
		try {
			logoutUser(request,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void logoutUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		
		User user = (User)session.getAttribute("user");
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		
		userDBUtil.storeLogoutTime(user.getEmail(),dtf.format(now).toString());
		
		session.removeAttribute("user");
		session.invalidate();
		
		 response.sendRedirect(request.getContextPath() + "/login");
		
	}

}
