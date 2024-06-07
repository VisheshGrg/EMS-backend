package com.vishesh.task7;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailHandler {
	
	public String getRandomCode() {
		
		Random rand = new Random();
		int code = rand.nextInt(999999);
		return String.format("%06d", code);
	
	}
	
	public boolean sendEmail(String email, String code) throws IOException {
		
		boolean res = false;
		
		String toEmail = email;
		
		FileInputStream fis = new FileInputStream("C:\\Users\\gargv\\eclipse-workspace\\Task7\\src\\main\\java\\com\\vishesh\\task7\\config.properties");
		
		Properties properties = new Properties();
		
		properties.load(fis);          
		
		String fromEmail = properties.getProperty("fromEmail");
		String password = properties.getProperty("email_password");
		
		
		try {
			
			Properties pr = new Properties();
			pr.setProperty("mail.smtp.host", "smtp.gmail.com");
			pr.setProperty("mail.smtp.port", "587");
			pr.setProperty("mail.smtp.auth", "true");
			pr.setProperty("mail.smtp.starttls.enable", "true");
//			pr.put("mail.smtp.socketFactory.port", "587");
//			pr.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			
			
			Session session = Session.getInstance(pr, new Authenticator() {
				
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(fromEmail,password);
				}
				
			});
			
			Message msg = new MimeMessage(session);
			
			msg.setFrom(new InternetAddress(fromEmail));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
			
			msg.setSubject("User Email Verification");
			
			msg.setText("Please verify your account with OTP: "+code);

			Transport.send(msg);
			
			res = true;
			
		}
		catch(Exception e) {
			
			e.printStackTrace();
			
		}
		
		return res;
		
	}
	
}
