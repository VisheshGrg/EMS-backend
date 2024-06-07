package com.vishesh.task7;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vishesh.task7.model.AddSalary;
import com.vishesh.task7.model.Leave;
import com.vishesh.task7.model.UserLoginData;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/*")
public class AdminServletController extends HttpServlet{

	private static final long serialVersionUID = 197123L;

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
			case "/changeLeaveTypes":
				manageLeaveTypes(request,response);
				break;
			case "/leaveRequests":
				viewLeaveRequests(request,response);
				break;
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
			case "/changeLeaveTypes":
				saveLeaveTypes(request,response);
				break;
			case "/leaveRequests/approve":
				approveLeave(request,response);
				break;
			case "/leaveRequests/reject":
				rejectLeave(request,response);
				break;
			case "/addSalaryInfo":
				addSalaryInfo(request,response);
				break;
			case "/updateInfo":
				updateInfo(request,response);
				break;
			case "/updateInfo/confirm":
				updateConfirm(request,response);
				break;
			}
			
		}
		catch(Exception e) {
			throw new ServletException(e);
		}
	}
	
	
	private void manageLeaveTypes(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,Object> responseData = new HashMap();
		ObjectMapper om = new ObjectMapper();
		
		String userHeader = request.getHeader("User");
		if(userHeader==null) {
			responseData.put("message", "Something went wrong. Please try again later!");
			responseData.put("leaveTypes", null);
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
	        return;
		}
	
		User user = om.readValue(userHeader,User.class);
		
		if(!user.getIsAdmin()) {
			responseData.put("message", "You are not allowed to do that!");
			responseData.put("leaveTypes", null);
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
	        return;
		}
		
		List<Integer> res = userDBUtil.retrieve_leave_types();
		
		responseData.put("message", "");
		responseData.put("leaveTypes", res);
		response.setContentType("application/json");
		om = new ObjectMapper();
        om.writeValue(response.getWriter(), responseData);
        return;
		
	}

	private void saveLeaveTypes(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,Object> responseData = new HashMap();
		ObjectMapper om = new ObjectMapper();
		
		String userHeader = request.getHeader("User");
		if(userHeader==null) {
			responseData.put("message", "Something went wrong. Please try again later!");
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
	        return;
		}
	
		User user = om.readValue(userHeader,User.class);
		
		if(!user.getIsAdmin()) {
			responseData.put("message", "You are not allowed to do that!");
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
	        return;
		}
		
		Map<String, Boolean> leaveTypes = om.readValue(request.getInputStream(), Map.class);

	    List<Integer> selectedLeaveTypes = new ArrayList<>();
	    selectedLeaveTypes.add(leaveTypes.getOrDefault("Privileged Leave", false) ? 1 : 0);
	    selectedLeaveTypes.add(leaveTypes.getOrDefault("Casual Leave", false) ? 1 : 0);
	    selectedLeaveTypes.add(leaveTypes.getOrDefault("Sick Leave", false) ? 1 : 0);
	    selectedLeaveTypes.add(leaveTypes.getOrDefault("Maternity Leave", false) ? 1 : 0);
	    selectedLeaveTypes.add(leaveTypes.getOrDefault("Marriage Leave", false) ? 1 : 0);
	    selectedLeaveTypes.add(leaveTypes.getOrDefault("Paternity Leave", false) ? 1 : 0);
	    selectedLeaveTypes.add(leaveTypes.getOrDefault("Compensatory Off Leave", false) ? 1 : 0);
	    selectedLeaveTypes.add(leaveTypes.getOrDefault("LOP Leave", false) ? 1 : 0);
		
        userDBUtil.save_leave_types(selectedLeaveTypes);
        
        responseData.put("message", "");
		response.setContentType("application/json");
		om = new ObjectMapper();
        om.writeValue(response.getWriter(), responseData);
		return;
        
	}
	
	private void viewLeaveRequests(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,Object> responseData = new HashMap();
		ObjectMapper om = new ObjectMapper();
		
		String userHeader = request.getHeader("User");
		if(userHeader==null) {
			responseData.put("message", "Something went wrong. Please try again later!");
			responseData.put("leaveRequests", null);
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
	        return;
		}
	
		User user = om.readValue(userHeader,User.class);
		
		if(!user.getIsAdmin()) {
			responseData.put("message", "You are not allowed to do that!");
			responseData.put("leaveRequests", null);
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
	        return;
		}
		
		List<Leave> leaves = userDBUtil.retrieveLeaves();
		
		for(int i=0; i<leaves.size(); i++) {
			String email = leaves.get(i).getEmail();
			String photo = userDBUtil.getPhoto(email);
			leaves.get(i).setPhoto(photo);
		}
		
		responseData.put("message", "");
		responseData.put("leaveRequests", leaves);
		response.setContentType("application/json");
		om = new ObjectMapper();
        om.writeValue(response.getWriter(), responseData);
		
	}
	
	private void approveLeave(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,Object> responseData = new HashMap();
		ObjectMapper om = new ObjectMapper();
		
		String userHeader = request.getHeader("User");
		if(userHeader==null) {
			responseData.put("message", "Something went wrong. Please try again later!");
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
	        return;
		}
	
		User user = om.readValue(userHeader,User.class);
		
		if(!user.getIsAdmin()) {
			responseData.put("message", "You are not allowed to do that!");
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
	        return;
		}
		
		Map<String, Object> requestBodyMap = om.readValue(request.getInputStream(), Map.class);
	    Object leaveIdObj = requestBodyMap.get("leave_id");

	    if (leaveIdObj == null) {
	        responseData.put("message", "Leave ID is missing.");
	        response.setContentType("application/json");
	        om.writeValue(response.getWriter(), responseData);
	        return;
	    }

	    int leave_id;
	    try {
	        leave_id = Integer.parseInt(leaveIdObj.toString());
	    } catch (NumberFormatException e) {
	        responseData.put("message", "Invalid Leave ID format.");
	        response.setContentType("application/json");
	        om.writeValue(response.getWriter(), responseData);
	        return;
	    }
		
		userDBUtil.approveLeave(leave_id);
		
		responseData.put("message", "Leaved approved successfully!");
		response.setContentType("application/json");
		om = new ObjectMapper();
        om.writeValue(response.getWriter(), responseData);
		return;
		
	}

	private void rejectLeave(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,Object> responseData = new HashMap();
		ObjectMapper om = new ObjectMapper();
		
		String userHeader = request.getHeader("User");
		if(userHeader==null) {
			responseData.put("message", "Something went wrong. Please try again later!");
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
	        return;
		}
	
		User user = om.readValue(userHeader,User.class);
		
		if(!user.getIsAdmin()) {
			responseData.put("message", "You are not allowed to do that!");
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
	        return;
		}
		
		Map<String, Object> requestBodyMap = om.readValue(request.getInputStream(), Map.class);
	    Object leaveIdObj = requestBodyMap.get("leave_id");

	    if (leaveIdObj == null) {
	        responseData.put("message", "Leave ID is missing.");
	        response.setContentType("application/json");
	        om.writeValue(response.getWriter(), responseData);
	        return;
	    }

	    int leave_id;
	    try {
	        leave_id = Integer.parseInt(leaveIdObj.toString());
	    } catch (NumberFormatException e) {
	        responseData.put("message", "Invalid Leave ID format.");
	        response.setContentType("application/json");
	        om.writeValue(response.getWriter(), responseData);
	        return;
	    }
		
		userDBUtil.rejectLeave(leave_id);
		
		responseData.put("message", "Leaved approved successfully!");
		response.setContentType("application/json");
		om = new ObjectMapper();
        om.writeValue(response.getWriter(), responseData);
		return;
		
	}
	
	private void addSalaryInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,Object> responseData = new HashMap();
		ObjectMapper om = new ObjectMapper();
		
		String userHeader = request.getHeader("User");
		if(userHeader==null) {
			responseData.put("message", "Something went wrong. Please try again later!");
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
	        return;
		}
	
		User user = om.readValue(userHeader,User.class);
		
		ObjectMapper objectMapper = new ObjectMapper();
	    AddSalary data = objectMapper.readValue(request.getInputStream(), AddSalary.class);
		
		String sid = data.getSid();
		String uid = data.getUid();
		String salary_amount = data.getSalary_amount();
		String salary_type = data.getSalary_type();
		String salary_des = data.getSalary_des();
		
		if(!user.getIsAdmin()) {
			responseData.put("message", "You are not allowed to do that!");
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
	        return;
		}
		else {
			
			String email = userDBUtil.retrieveEmail(uid);
			
			if(email=="") {				
				responseData.put("message", "Invalid Employee Id entered. Please try again with the correct Id.");
				response.setContentType("application/json");
				om = new ObjectMapper();
		        om.writeValue(response.getWriter(), responseData);
		        return;
			}
			
			LocalDate currDate = LocalDate.now();
			Date sqlDate = Date.valueOf(currDate);
			
			userDBUtil.addSalaryInfo(email,sid,salary_amount,salary_type,salary_des,sqlDate);
			
			
			String ytd_tax = userDBUtil.findYtdTax(email,sqlDate);
			String ytd_salary = userDBUtil.findYtdSalary(email,sqlDate);
			
			if(ytd_salary==null) {
				ytd_salary="0";
			}
			if(ytd_tax==null) {
				ytd_tax="0";
			}
			
			userDBUtil.updateSalaryExtraInfo(email,ytd_salary,ytd_tax,sqlDate);
			userDBUtil.updateSalaryInfo(email,ytd_salary,ytd_tax);
			
			responseData.put("message", "Successfully added salary!");
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
			
		}
		
		
	}
		
	private void updateConfirm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,Object> responseData = new HashMap();
		ObjectMapper om = new ObjectMapper();
		
		String userHeader = request.getHeader("User");
		if(userHeader==null) {
			responseData.put("message", "Something went wrong. Please try again later!");
			responseData.put("empInfo", null);
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
	        return;
		}
	
		User user = om.readValue(userHeader,User.class);
			
		if(!user.getIsAdmin()) {
			responseData.put("message", "You are not allowed to do that!");
			responseData.put("empInfo", null);
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
			return;
		}
		
		Map<String, Object> requestBodyMap = om.readValue(request.getInputStream(), Map.class);
	    Object emp_id = requestBodyMap.get("uid");

	    if (emp_id == null) {
	        responseData.put("message", "Employee ID is missing.");
	        responseData.put("empInfo", null);
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
	        return;
	    }
	    
	    String uid = String.valueOf(emp_id);

		User usr = userDBUtil.retrieveUser(uid);
		
		if(usr==null) {
			responseData.put("message", "Invalid Employee Id. Please try again later.");
	        responseData.put("empInfo", null);
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
	        return;
		}
		
		responseData.put("message", "");
        responseData.put("empInfo", usr);
		response.setContentType("application/json");
		om = new ObjectMapper();
        om.writeValue(response.getWriter(), responseData);
        return;

	}

	private void updateInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,Object> responseData = new HashMap();
		ObjectMapper om = new ObjectMapper();
		
		String userHeader = request.getHeader("User");
		if(userHeader==null) {
			responseData.put("message", "Something went wrong. Please try again later!");
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
	        return;
		}
	
		User user = om.readValue(userHeader,User.class);
			
		if(!user.getIsAdmin()) {
			responseData.put("message", "You are not allowed to do that!");
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
			return;
		}
		
		Map<String, Object> payload = om.readValue(request.getInputStream(), Map.class);

	    String email = (String) payload.get("email");
	    String pers_email = (String) payload.get("pers_email");
	    int age = (int) payload.get("age");
	    String blood_group = (String) payload.get("bg");
	    String curr_address = (String) payload.get("curr_address");
	    String perm_address = (String) payload.get("perm_address");
	    String department = (String) payload.get("department");
	    String phone = (String) payload.get("phone");
	    String alt_phone = (String) payload.get("alt_phone");
	    String salary_amount = (String) payload.get("salary_amount");
	    String salary_total = (String) payload.get("salary_total");
		
		if(email=="" || email.isEmpty()) {

			responseData.put("message", "Something went wrong. Please try again later.");
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
			return;
		}
		
		userDBUtil.updateUserInfo(email,pers_email,age,blood_group,curr_address,perm_address,department,phone,alt_phone,salary_amount,salary_total);

		responseData.put("message", "Successfully updated employee Info!");
		response.setContentType("application/json");
		om = new ObjectMapper();
        om.writeValue(response.getWriter(), responseData);
		return;
		
		
	}

}
