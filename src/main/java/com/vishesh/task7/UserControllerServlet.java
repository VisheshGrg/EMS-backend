package com.vishesh.task7;

import jakarta.annotation.Resource;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.security.Timestamp;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.sql.DataSource;

//import org.apache.tomcat.jakartaee.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.ObjectUtils;

import com.cloudinary.Cloudinary;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vishesh.task7.Config.CloudinaryConfig;
import com.vishesh.task7.model.Leave;
import com.vishesh.task7.model.UserJwt;


@MultipartConfig
@WebServlet("/api/*")
public class UserControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1235231412L;

	private static UserDBUtil userDBUtil;
	
	private static final double EARTH_RADIUS = 6371.0;
	
	private static Map<String,String> mp = new HashMap<>();
	private static Map<String,String> reverse_mp = new HashMap<>();
	
	
	@Resource(name="jdbc/user-tracker")
	private DataSource dataSource;
	
	@Override
	public void init() throws ServletException{
		super.init();
		
		try {
			
			userDBUtil = new UserDBUtil(dataSource);
			
			mp.put("January", "01");
			mp.put("February", "02");
			mp.put("March", "03");
			mp.put("April", "04");
			mp.put("May", "05");
			mp.put("June", "06");
			mp.put("July", "07");
			mp.put("August", "08");
			mp.put("September", "09");
			mp.put("October", "10");
			mp.put("November", "11");
			mp.put("December", "12");
			
			reverse_mp.put("01", "January");
			reverse_mp.put("02", "February");
			reverse_mp.put("03", "March");
			reverse_mp.put("04", "April");
			reverse_mp.put("05", "May");
			reverse_mp.put("06", "June");
			reverse_mp.put("07", "July");
			reverse_mp.put("08", "August");
			reverse_mp.put("09", "September");
			reverse_mp.put("10", "October");
			reverse_mp.put("11", "November");
			reverse_mp.put("12", "December");
			
		}
		catch(Exception ex) {
			throw new ServletException(ex);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getPathInfo();
		
		try {
			
			switch(action) {
			case "/getInfo":
				getUserInfo(request,response);
				break;
			case "/profile":
				viewUserInfo(request,response);
				break;
			case "/leaveStatus":
				viewLeaveStatus(request,response);
				break;
			case "/markLeave":
				viewMarkLeavePage(request,response);
				break;
			case "/salaryDetails":
				viewSalaryInfo(request,response);
				break;
			}
				
			
		}
		catch(Exception e) {
			throw new ServletException(e);
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String action = request.getPathInfo();
		
		try {
			
			switch(action) {
			case "/updateSelf":
				updateSelfInfoFinal(request,response);
				break;
			case "/addInfo":
				addUserInfo(request,response);
				break;
			case "/punch":
				checkLocation(request,response);
				break;
			case "/upload":
				uploadFile(request,response);
				break;
			case "/cancelLeave":
				cancelLeave(request,response);
				break;
			case "/markLeave":
				markLeave(request,response);
				break;
			case "/salaryDetails":
				viewSalaryInfo(request,response);
				break;
			}
				
		}
		catch(Exception e) {
			throw new ServletException(e);
		}
		
	}
	
	private void getUserInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		Map<String,Object> responseData = new HashMap();
		ObjectMapper om = new ObjectMapper();
		
		String userHeader = request.getHeader("Authorization");
		if(userHeader==null) {
			responseData.put("message", "Something went wrong. Please try again later!");
			responseData.put("user", null);
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
		}
	
		UserJwt jwt = om.readValue(userHeader,UserJwt.class);
		
		User user = userDBUtil.getUserInfo(jwt.getEmail());
		String email = user.getEmail();
		
		FileInputStream fis = new FileInputStream("C:\\Users\\gargv\\eclipse-workspace\\Task7\\src\\main\\java\\com\\vishesh\\task7\\config.properties");
		
		Properties properties = new Properties();
		
		properties.load(fis);
		
		if(email.equals(properties.getProperty("admin"))) {
			user.setIsAdmin(true);
		}
		else {
			int total_leaves = userDBUtil.findTotalLeaves(email);
			List<Double> leaves_percentages = userDBUtil.findLeavesPercentage(email,total_leaves);
			
			user.setTotal_leaves(total_leaves);
			user.setCl(Math.round(leaves_percentages.get(0)*100.0)/100.0);
			user.setSl(Math.round(leaves_percentages.get(1)*100.0)/100.0);
			user.setPl(Math.round(leaves_percentages.get(2)*100.0)/100.0);
			user.setCoff(Math.round(leaves_percentages.get(3)*100.0)/100.0);
			
			LocalDate currDate = LocalDate.now();
			Date sqlDate = Date.valueOf(currDate);
			
			boolean isPunched = userDBUtil.checkIsPunched(email,sqlDate);
			
			if(isPunched) {
				user.setIsPunched(isPunched);
				user.setPunchStatus("SWIPED");
			}
			
			List<String> res = userDBUtil.lastPunchedData(user.getEmail());
			user.setLastPunchedDate(res.get(0));
			user.setLastPunchedStatus(res.get(1));
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
			LocalDateTime now = LocalDateTime.now();  
			
			userDBUtil.storeLoginTime(email,dtf.format(now).toString());
			
			boolean isLeave = userDBUtil.checkIsLeave(sqlDate,user.getEmail());
			
			user.setIs_leave(isLeave);
			
			User user_info = userDBUtil.viewUserInfo(user);
			if(user_info.getBlood_group()!=null && user_info.getBlood_group()!="Not Available") {
				user.setIs_infoSet(true);
			}
			else {
				user.setIs_infoSet(false);
			}
		}
		user.setPassword("");
		
		responseData.put("message", "");
		responseData.put("user", user);
		response.setContentType("application/json");
		om = new ObjectMapper();
        om.writeValue(response.getWriter(), responseData);
		
	}
	
	private void viewUserInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
//		HttpSession session = request.getSession();
//		User user = (User)session.getAttribute("user");
		
		Map<String,Object> responseData = new HashMap();
		ObjectMapper om = new ObjectMapper();
			
		String userHeader = request.getHeader("User");
		if(userHeader==null) {
			responseData.put("message", "Something went wrong. Please try again later!");
			responseData.put("userInfo", null);
			responseData.put("userFiles", null);
			responseData.put("userEducation", null);
			responseData.put("userDocuments", null);
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
		}
		
		User user = om.readValue(userHeader,User.class);
		
		if(user.getIsAdmin()) {
			responseData.put("message", "You are not allowed to do that!");
			responseData.put("userInfo", null);
			responseData.put("userFiles", null);
			responseData.put("userEducation", null);
			responseData.put("userDocuments", null);
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
			return;
		}
		
		
		User u = userDBUtil.viewUserInfo(user);
		u.setPassword("");
		List<String> files = userDBUtil.retrieveUserFiles(user.getEmail());
		List<List<String>> educationDetails = userDBUtil.retrieveUserEducation(user.getEmail());
		List<String> officeDocs = userDBUtil.retrieveOfficeDocs(user.getEmail());
		
		if(u.getBlood_group()!=null && u.getBlood_group()!="Not Available") {
			u.setIs_infoSet(true);
		}
		
		responseData.put("message", "");
		responseData.put("userInfo", u);
		responseData.put("userFiles", files);
		responseData.put("userEducation", educationDetails);
		responseData.put("userDocuments", officeDocs);
		response.setContentType("application/json");
		om = new ObjectMapper();
        om.writeValue(response.getWriter(), responseData);
		
	}
	
	private void updateSelfInfoFinal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,Object> responseData = new HashMap();
		ObjectMapper om = new ObjectMapper();
		
		String userHeader = request.getHeader("User");
		if(userHeader==null) {
			responseData.put("message", "Something went wrong. Please try again later!");
			responseData.put("userInfo", null);
			responseData.put("userFiles", null);
			responseData.put("userEducation", null);
			responseData.put("userDocuments", null);
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
		}
	
		User user = om.readValue(userHeader,User.class);
		
		if(user.getIsAdmin()) {
			responseData.put("message", "You are not allowed to do that!");
			responseData.put("userInfo", null);
			responseData.put("userFiles", null);
			responseData.put("userEducation", null);
			responseData.put("userDocuments", null);
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
			return;
		}
		
		int age = Integer.parseInt(request.getParameter("age"));
		String bloodGroup = request.getParameter("bg");
		String phone = request.getParameter("phone");
        String altPhone = request.getParameter("alt_phone");
        String personalEmail = request.getParameter("pers_email");
        String currAddress = request.getParameter("curr_address");
        String permAddress = request.getParameter("perm_address");
//		String department = request.getParameter("department");
        
        
        Map<Integer, Map<String, String>> educationDetails = new HashMap<>();
        int educationIndex = 0;
        while (true) {
            String school = request.getParameter("education[" + educationIndex + "][1]");
            if (school == null) break; // No more education entries
            String location = request.getParameter("education[" + educationIndex + "][2]");
            String grade = request.getParameter("education[" + educationIndex + "][3]");
            String degree = request.getParameter("education[" + educationIndex + "][4]");
            String fromDate = request.getParameter("education[" + educationIndex + "][5]");
            String toDate = request.getParameter("education[" + educationIndex + "][6]");

            Map<String, String> eduEntry = new HashMap<>();
            eduEntry.put("school", school);
            eduEntry.put("location", location);
            eduEntry.put("grade", grade);
            eduEntry.put("degree", degree);
            eduEntry.put("from_date", fromDate);
            eduEntry.put("to_date", toDate);

            educationDetails.put(educationIndex+1, eduEntry);
            educationIndex++;
        }
		
		Part photoPart = request.getPart("photo");
		Part panPart = request.getPart("pan");
        Part empPhotoPart = request.getPart("emp_photo");
        Part marksheet12Part = request.getPart("marksheet_12");
        Part marksheet10Part = request.getPart("marksheet_10");
		
		String photo = extractFileName(photoPart);
		String pan = extractFileName(panPart);
		String empPhoto = extractFileName(empPhotoPart);
		String marksheet12 = extractFileName(marksheet12Part);
		String marksheet10 = extractFileName(marksheet10Part);
        
		String panPath=uploadToCloudinaryFile(panPart, pan);
		String empPhotoPath=uploadToCloudinaryFile(empPhotoPart, empPhoto);
		String marksheet12Path=uploadToCloudinaryFile(marksheet12Part, marksheet12);
		String marksheet10Path=uploadToCloudinaryFile(marksheet10Part, marksheet10);
		String filePath=uploadToCloudinaryImage(photoPart, photo);
		
		List<String> update_office_docs = userDBUtil.retrieveOfficeDocs(user.getEmail());
		
		if(filePath==null || filePath.equals("")) {
			filePath = user.getPhoto();
		}
		if(panPath==null || panPath.equals("")) {
			panPath = update_office_docs.get(0);
		}
		if(empPhotoPath==null || empPhotoPath.equals("")) {
			empPhotoPath = update_office_docs.get(1);
		}
		if(marksheet12Path==null || marksheet12Path.equals("")) {
			marksheet12Path = update_office_docs.get(2);
		}
		if(marksheet10Path==null || marksheet10Path.equals("")) {
			marksheet10Path = update_office_docs.get(3);
		}
		
		
		userDBUtil.updateInfoSelf(user.getEmail(),personalEmail,age,bloodGroup,currAddress,permAddress,phone,altPhone,filePath);
		userDBUtil.updateEducationDetails(user.getEmail(),educationDetails);
		userDBUtil.updateOfficeDocs(user.getEmail(),panPath,empPhotoPath,marksheet12Path,marksheet10Path);
		
		User u = userDBUtil.viewUserInfo(user);
		u.setPassword("");
		List<String> files = userDBUtil.retrieveUserFiles(user.getEmail());
		List<List<String>> updatedEducationDetails = userDBUtil.retrieveUserEducation(user.getEmail());
		List<String> officeDocs = userDBUtil.retrieveOfficeDocs(user.getEmail());
		
		responseData.put("message", "Successfully updated Information!");
		responseData.put("userInfo", u);
		responseData.put("userFiles", files);
		responseData.put("userEducation", updatedEducationDetails);
		responseData.put("userDocuments", officeDocs);
		response.setContentType("application/json");
		om = new ObjectMapper();
        om.writeValue(response.getWriter(), responseData);
		
	}

	private String extractFileName(Part part) {
		
		String contentDisp = part.getHeader("content-disposition");
		
		String[] items = contentDisp.split(";");
		
		for(String item: items) {
			if(item.trim().startsWith("filename")) {
				String temp = item.substring(item.indexOf("=")+2,item.length()-1);
				String[] tempStr = temp.split("\\\\");
				return tempStr[tempStr.length-1];
			}
		}
		
		return null;
		
	}

	private String uploadToCloudinaryImage(Part filePart, String file) throws Exception{
		if(file==null) {
			return null;
		}
		
		Cloudinary cloudinary = CloudinaryConfig.getCloudinary();
        File tempFile = File.createTempFile("upload", filePart.getSubmittedFileName());

        try (InputStream fileStream = filePart.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fileStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            Map<String, String> uploadParams = new HashMap<>();
            uploadParams.put("folder", "EMS");
            
            Map uploadResult = cloudinary.uploader().upload(tempFile, uploadParams);
            return (String) uploadResult.get("url");
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to upload file to Cloudinary", e);
        } finally {
            Files.deleteIfExists(tempFile.toPath());
        }
	}
	
	private String uploadToCloudinaryFile(Part filePart, String file) throws Exception{
		if(file==null) {
			return null;
		}
		
		Cloudinary cloudinary = CloudinaryConfig.getCloudinary();
        File tempFile = File.createTempFile("upload", filePart.getSubmittedFileName());

        try (InputStream fileStream = filePart.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fileStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            Map<String, String> uploadParams = new HashMap<>();
            uploadParams.put("folder", "EMS");
            uploadParams.put("resource_type", "raw");
            
            Map uploadResult = cloudinary.uploader().upload(tempFile, uploadParams);
            return (String) uploadResult.get("url");
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to upload file to Cloudinary", e);
        } finally {
            Files.deleteIfExists(tempFile.toPath());
        }
	}
	
	private void addUserInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		Map<String,Object> responseData = new HashMap();
		ObjectMapper om = new ObjectMapper();
		
		String userHeader = request.getHeader("User");
		if(userHeader==null) {
			responseData.put("message", "Something went wrong. Please try again later!");
			responseData.put("user", null);
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
		}
	
		User user = om.readValue(userHeader,User.class);
		
		
		int age = Integer.parseInt(request.getParameter("age"));
		String bloodGroup = request.getParameter("bg");
		String phone = request.getParameter("phone");
        String altPhone = request.getParameter("alt_phone");
        String personalEmail = request.getParameter("pers_email");
        String currAddress = request.getParameter("curr_address");
        String permAddress = request.getParameter("perm_address");
        
        Map<Integer, Map<String, String>> educationDetails = new HashMap<>();
        int educationIndex = 0;
        while (true) {
            String school = request.getParameter("education[" + educationIndex + "][1]");
            if (school == null) break; // No more education entries
            String location = request.getParameter("education[" + educationIndex + "][2]");
            String grade = request.getParameter("education[" + educationIndex + "][3]");
            String degree = request.getParameter("education[" + educationIndex + "][4]");
            String fromDate = request.getParameter("education[" + educationIndex + "][5]");
            String toDate = request.getParameter("education[" + educationIndex + "][6]");

            Map<String, String> eduEntry = new HashMap<>();
            eduEntry.put("school", school);
            eduEntry.put("location", location);
            eduEntry.put("grade", grade);
            eduEntry.put("degree", degree);
            eduEntry.put("from_date", fromDate);
            eduEntry.put("to_date", toDate);

            educationDetails.put(educationIndex+1, eduEntry);
            educationIndex++;
        }
		
        Part photoPart = request.getPart("photo");
		Part panPart = request.getPart("pan");
        Part empPhotoPart = request.getPart("emp_photo");
        Part marksheet12Part = request.getPart("marksheet_12");
        Part marksheet10Part = request.getPart("marksheet_10");
		
		String photo = extractFileName(photoPart);
		String pan = extractFileName(panPart);
		String empPhoto = extractFileName(empPhotoPart);
		String marksheet12 = extractFileName(marksheet12Part);
		String marksheet10 = extractFileName(marksheet10Part);
		

		if(user.getIsAdmin()) {
			responseData.put("message", "You are not allowed to do that!");
			responseData.put("userInfo", null);
			responseData.put("userFiles", null);
			responseData.put("userEducation", null);
			responseData.put("userDocuments", null);
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
	        return;
		}
		else {
			
			User usr = userDBUtil.findUser(user.getEmail());
			
			if(usr.getAddress()!=null) {
				responseData.put("message", "Employee information already added!");
				responseData.put("userInfo", null);
				responseData.put("userFiles", null);
				responseData.put("userEducation", null);
				responseData.put("userDocuments", null);
				response.setContentType("application/json");
				om = new ObjectMapper();
		        om.writeValue(response.getWriter(), responseData);
				return;
			}
			
			User infoUser = usr;
		
			String panPath=uploadToCloudinaryFile(panPart, pan);
			String empPhotoPath=uploadToCloudinaryFile(empPhotoPart, empPhoto);
			String marksheet12Path=uploadToCloudinaryFile(marksheet12Part, marksheet12);
			String marksheet10Path=uploadToCloudinaryFile(marksheet10Part, marksheet10);
			String filePath=uploadToCloudinaryImage(photoPart, photo);
			
			if(filePath==null) {
				filePath="https://i.pinimg.com/736x/0d/64/98/0d64989794b1a4c9d89bff571d3d5842.jpg";
			}
			
			
			userDBUtil.addUserInfo(user.getEmail(),age,bloodGroup,phone,altPhone,"",personalEmail,currAddress,permAddress,filePath);
			userDBUtil.addEducationDetails(user.getEmail(),educationDetails);
			userDBUtil.addOfficeDocs(user.getEmail(), panPath,empPhotoPath,marksheet12Path,marksheet10Path);
			
			user.setIs_infoSet(true);
			
			User u = userDBUtil.viewUserInfo(user);
			u.setPassword("");
			u.setIs_infoSet(true);
			List<String> files = userDBUtil.retrieveUserFiles(user.getEmail());
			List<List<String>> updatedEducationDetails = userDBUtil.retrieveUserEducation(user.getEmail());
			List<String> officeDocs = userDBUtil.retrieveOfficeDocs(user.getEmail());
			
			responseData.put("message", "Successfully updated Information!");
			responseData.put("userInfo", u);
			responseData.put("userFiles", files);
			responseData.put("userEducation", updatedEducationDetails);
			responseData.put("userDocuments", officeDocs);
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
			
		}
	
	}
	
	private void checkLocation(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,Object> responseData = new HashMap();
		ObjectMapper om = new ObjectMapper();
		
		String userHeader = request.getHeader("User");
		if(userHeader==null) {
			responseData.put("message", "Something went wrong. Please try again later!");
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
		}
	
		User user = om.readValue(userHeader,User.class);
		
		Map<String, Object> requestBodyMap = om.readValue(request.getInputStream(), Map.class);
	    Object latitude = requestBodyMap.get("latitude");
	    Object longitude = requestBodyMap.get("longitude");
	 

	    if (latitude == null || longitude==null) {
	        responseData.put("message", "Coordinates are missing!");
	        response.setContentType("application/json");
	        om.writeValue(response.getWriter(), responseData);
	        return;
	    }
	    if(user.getIsPunched()) {
	    	responseData.put("message", "Punching already done!");
	        response.setContentType("application/json");
	        om.writeValue(response.getWriter(), responseData);
	        return;
	    }
		if(user.getIs_leave()) {
			responseData.put("message", "Something went wrong. Please try again later.");
	        response.setContentType("application/json");
	        om.writeValue(response.getWriter(), responseData);
	        return;
		}
		
		FileInputStream fis = new FileInputStream("C:\\Users\\gargv\\eclipse-workspace\\Task7\\src\\main\\java\\com\\vishesh\\task7\\config.properties");
		
		Properties properties = new Properties();
		
		properties.load(fis);
		
		String center_latitude = properties.getProperty("center_latitude");
		String center_longitude = properties.getProperty("center_longitude");
		
		double lat = (double)latitude;
		double lon = (double)longitude;
		double cent_lat = Double.parseDouble(center_latitude);
		double cent_lon = Double.parseDouble(center_longitude);
		
		lat = Math.toRadians(lat);
		lon = Math.toRadians(lon);
		cent_lat = Math.toRadians(cent_lat);
		cent_lon = Math.toRadians(cent_lon);
		
		double dLat = Math.abs(cent_lat - lat);
		double dLon = Math.abs(cent_lon - lon);
		
		double a = Math.pow(Math.sin(dLat/2),2) + Math.cos(lat)*Math.cos(cent_lat)*Math.pow(Math.sin(dLon/2),2);
		double c = 2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
		
		double distance = EARTH_RADIUS * c;
		
		LocalDate currDate = LocalDate.now();
		Date sqlDate = Date.valueOf(currDate);
		
		LocalTime time = LocalTime.now();
		String currentTime = time.toString();
		
		if(distance<=15) {
			userDBUtil.storePunchResult(user.getEmail(),sqlDate,currentTime,"SWIPED");
			
			responseData.put("message", "Punching successfull.");
		}	
		else {
			userDBUtil.storePunchResult(user.getEmail(),sqlDate,currentTime,"UNSWIPED");
			
			responseData.put("message", "Not able to punch. Please reach the office and try again!");
		}
		
		responseData.put("message", "");
        response.setContentType("application/json");
        om.writeValue(response.getWriter(), responseData);
        return;

	}
		
	private void uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,Object> responseData = new HashMap();
		ObjectMapper om = new ObjectMapper();
		
		String userHeader = request.getHeader("User");
		if(userHeader==null) {
			responseData.put("message", "Something went wrong. Please try again later!");
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
		}
	
		User user = om.readValue(userHeader,User.class);
		
		if(user.getIsAdmin()) {
			responseData.put("message", "You are not allowed to do that!");
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
			return;
		}
		else {
			
			String uid = user.getUid();
			String name = user.getName();
			String email = user.getEmail();
			
			Collection<Part> parts = request.getParts();
			
			int flag = 0;
			
			for(Part part: parts) {
				
				String fileName = extractFileName(part);
				
				if(fileName != null && !fileName.isEmpty()) {
					
					flag=1;
					
					String filePath = uploadToCloudinaryFile(part,fileName);
					
					userDBUtil.uploadFile(email,fileName,filePath);
				}
			}
			
			if(flag==0) {
				responseData.put("message", "Invalid file!");
				response.setContentType("application/json");
				om = new ObjectMapper();
		        om.writeValue(response.getWriter(), responseData);		
			}
			else {
				responseData.put("message", "Successfully uploaded files! (Please visit profile page to view files.)");
				response.setContentType("application/json");
				om = new ObjectMapper();
		        om.writeValue(response.getWriter(), responseData);
			}
			
		}
		
	}
	
	private void viewLeaveStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,Object> responseData = new HashMap();
		ObjectMapper om = new ObjectMapper();
		
		String userHeader = request.getHeader("User");
		if(userHeader==null) {
			responseData.put("message", "Something went wrong. Please try again later!");
			responseData.put("leavesStatus", null);
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
		}
	
		User user = om.readValue(userHeader,User.class);
		
		if(user.getIsAdmin()) {
			responseData.put("message", "You are not allowed to do that!");
			responseData.put("leavesStatus", null);
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
			return;
		}
		
		String email = user.getEmail();
		
		LocalDate currDate = LocalDate.now();
		Date sqlDate = Date.valueOf(currDate);
		
		List<Leave> leave_status = userDBUtil.retrieveEmployeeLeaves(sqlDate,email);
		
		
		responseData.put("message", "");
		responseData.put("leavesStatus", leave_status);
		response.setContentType("application/json");
		om = new ObjectMapper();
        om.writeValue(response.getWriter(), responseData);
		
	}
	
	private void cancelLeave(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,Object> responseData = new HashMap();
		ObjectMapper om = new ObjectMapper();
		
		String userHeader = request.getHeader("User");
		if(userHeader==null) {
			responseData.put("message", "Something went wrong. Please try again later!");
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
		}
	
		User user = om.readValue(userHeader,User.class);
		
		if(user.getIsAdmin()) {
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
		
		LocalDate currDate = LocalDate.now();
		Date sqlDate = Date.valueOf(currDate);
		
//		boolean isLeave = userDBUtil.checkIsLeave(sqlDate,user.getEmail());
//		user.setIs_leave(isLeave);
		
		
		userDBUtil.cancel_leave(leave_id);
		
		responseData.put("message", "Leave cancelled successfully. Please check leave status page to confirm.");
		response.setContentType("application/json");
		om = new ObjectMapper();
        om.writeValue(response.getWriter(), responseData);
		
	}
		
	private void viewMarkLeavePage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,Object> responseData = new HashMap();
		ObjectMapper om = new ObjectMapper();
		
		String userHeader = request.getHeader("User");
		if(userHeader==null) {
			responseData.put("message", "Something went wrong. Please try again later!");
			responseData.put("leaveTypes", null);
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
		}
	
		User user = om.readValue(userHeader,User.class);
		
		if(user.getIsAdmin()) {
			responseData.put("message", "You are not allowed to do that!");
			responseData.put("leaveTypes", null);
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
			return;
		}
		
		List<Integer> lst = userDBUtil.retrieve_leave_types();
		
		responseData.put("message", "");
		responseData.put("leaveTypes", lst);
		response.setContentType("application/json");
		om = new ObjectMapper();
        om.writeValue(response.getWriter(), responseData);
		return;
		
	}

	private void markLeave(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,Object> responseData = new HashMap();
		ObjectMapper om = new ObjectMapper();
		
		String userHeader = request.getHeader("User");
		if(userHeader==null) {
			responseData.put("message", "Something went wrong. Please try again later!");
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
		}
	
		User user = om.readValue(userHeader,User.class);
		
		if(user.getIsAdmin()) {
			responseData.put("message", "You are not allowed to do that!");
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
		}
			
		String uid = user.getUid();
		BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
	    StringBuilder sb = new StringBuilder();
	    String line;
	    while ((line = reader.readLine()) != null) {
	        sb.append(line);
	    }
	    String requestBody = sb.toString();

	    Map<String, String> requestBodyMap = om.readValue(requestBody, Map.class);
	    String leave_type = requestBodyMap.get("leave_type");
	    String from_date = requestBodyMap.get("from_date");
	    String to_date = requestBodyMap.get("to_date");
	    String note = requestBodyMap.get("note");
		
		
		String email = userDBUtil.retrieveEmail(uid);
		
		if(email==null) {
			responseData.put("message", "Something went wrong. Please try again later!");
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
			return;
		}
		
		LocalDate currDate = LocalDate.now();
		Date sqlDate = Date.valueOf(currDate);
		
		userDBUtil.markLeave(email,leave_type,sqlDate,from_date,to_date,note);
		
		responseData.put("message", "");
		response.setContentType("application/json");
		om = new ObjectMapper();
        om.writeValue(response.getWriter(), responseData);
		
	}
	
	@SuppressWarnings("deprecation")
	private void viewSalaryInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String,Object> responseData = new HashMap();
		ObjectMapper om = new ObjectMapper();
		
		String userHeader = request.getHeader("User");
		if(userHeader==null) {
			responseData.put("message", "Something went wrong. Please try again later!");
			responseData.put("earningsList", null);
			responseData.put("taxList", null);
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
		}
	
		User user = om.readValue(userHeader,User.class);
		
		if(user.getIsAdmin()) {
			responseData.put("message", "You are not allowed to do that!");
			responseData.put("earningsList", null);
			responseData.put("taxList", null);
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
	        return;
		}
		else {
			
			
//			String year = request.getParameter("year");
//			String month = request.getParameter("month");
//			
//			LocalDate currDate = LocalDate.now();
//			Date sqlDate = Date.valueOf(currDate);
//
//			
//			if(year==null) {
//				year = Integer.toString(sqlDate.getYear()+1900);
//			}
//			
//			if(month==null) {
//				if(sqlDate.getMonth()+1 < 10) {
//					month = "0"+Integer.toString(sqlDate.getMonth()+1);
//				}
//				else {
//					month = Integer.toString(sqlDate.getMonth()+1);
//				}
//			}
//			else {
//				month = mp.get(month);
//			}
//			
//			final String finalYear = year;
//			final String finalMonth = month;
			
			List<Map<String,String>> earnings_lst = userDBUtil.viewEarningsSalaryInfo(user.getEmail());
			List<Map<String,String>> tax_lst = userDBUtil.viewTaxSalaryInfo(user.getEmail());
			
			
//			List<Map<String, String>> filtered_earnings_list = earnings_lst.stream()
//	                .filter(map -> map.containsKey("date") && isDateMatch(map.get("date"), finalYear, finalMonth))
//	                .collect(Collectors.toList());
//			
//			List<Map<String, String>> filtered_tax_list = tax_lst.stream()
//	                .filter(map -> map.containsKey("date") && isDateMatch(map.get("date"), finalYear, finalMonth))
//	                .collect(Collectors.toList());
			
			
//			session.setAttribute("earnings", filtered_earnings_list);
//			session.setAttribute("deductions", filtered_tax_list);
//			session.setAttribute("month", reverse_mp.get(month));
//			session.setAttribute("year", finalYear);
			
			responseData.put("message", "");
			responseData.put("earningsList", earnings_lst);
			responseData.put("taxList", tax_lst);
			response.setContentType("application/json");
			om = new ObjectMapper();
	        om.writeValue(response.getWriter(), responseData);
		}
		
	}

	private static boolean isDateMatch(String date, String desiredYear, String desiredMonth) {
	    String[] parts = date.split("-");
	    
	    if (parts.length == 3) {
	        String storedYear = parts[0];
	        String storedMonth = parts[1];

	        return storedYear.equals(desiredYear) && storedMonth.equals(desiredMonth);
	    }
	    
	    return false;
	}
	

}
