package com.vishesh.task7;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServletFileLoader extends HttpServlet{
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		
		String encodedPath = request.getParameter("path");
		if(encodedPath != null && !encodedPath.isEmpty()) {
			
			String filePath = URLDecoder.decode(encodedPath,"UTF-8");
			
			response.setContentType("application/pdf");
//			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition","inline; filename=\""+new File(filePath).getName()+"\"");
			
			try (FileInputStream inputStream = new FileInputStream(filePath);
				ServletOutputStream outputStream = response.getOutputStream()) {
				
				byte[] buffer = new byte[4096];
				int bytesRead;
				while((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}
				
			} catch(IOException e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			
		}else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
		
	}
	
}
