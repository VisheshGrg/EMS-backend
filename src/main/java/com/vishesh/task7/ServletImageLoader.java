package com.vishesh.task7;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServletImageLoader extends HttpServlet {

	private static final long serialVersionUID = 165472L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		
		String encodedPath = request.getParameter("path");
		
		if(encodedPath != null && !encodedPath.isEmpty()) {
			
			String imgFilePath = URLDecoder.decode(encodedPath,"UTF-8");
			
			response.setContentType("image/jpeg");
			
			try(FileInputStream imageStream = new FileInputStream(imgFilePath);
				ServletOutputStream out = response.getOutputStream()) {
				
				byte[] buffer = new byte[1024];
				int bytesRead;
				while((bytesRead = imageStream.read(buffer)) != -1) {
					out.write(buffer,0,bytesRead);
				}
				
			} catch(FileNotFoundException e) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
			
		}
		else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
		
	}
	
}
