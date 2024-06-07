package com.vishesh.task7;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.sql.DataSource;

import com.vishesh.task7.model.Leave;
import com.vishesh.task7.model.UserJwt;

public class UserDBUtil {

	private DataSource dataSource;
	private static final String SECRET_KEY="vishesh@01";
	private static String SALT = "abcd@dcba";;
	
	public UserDBUtil(DataSource dataSource) {
		this.dataSource=dataSource;
	}
	
	public User getUserInfo(String email) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet res = null;
	
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "select * from user join user_info on user.email=user_info.email where user.email=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			
			res = stmt.executeQuery();
			
			if(res.next()) {
				
				String uid = res.getString("uid");
				String name = res.getString("name");
				String orig_password = res.getString("password");
				String orig_email = res.getString("email");
				String photo = res.getString("photo");
				if(photo==null) {
					photo = "https://static.vecteezy.com/system/resources/thumbnails/009/734/564/small_2x/default-avatar-profile-icon-of-social-media-user-vector.jpg";
				}
				int age = res.getInt("age");
				String pers_email = res.getString("personal_email");
				String blood_group = res.getString("blood_group");
				String curr_address = res.getString("curr_address");
				String perm_address = res.getString("perm_address");
				String department = res.getString("department");
				String phone = res.getString("phone");
				String alt_phone = res.getString("alt_phone");
				String salary_amount = res.getString("salary_amount");
				String total_salary = res.getString("total_salary");
	
				
				User usr =  new User(uid,name,orig_email,orig_password,pers_email,age,blood_group,curr_address,perm_address,department,phone,alt_phone,total_salary,salary_amount,photo);
				
				sql = "select * from salary_extra_info where email=?";
				
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, email);
				
				res = stmt.executeQuery();
				
				if(res.next()) {
					
					String ytd_tax = res.getString("ytd_tax");
					String last_processed = res.getString("last_processed");
					
					usr.setYtdTax(ytd_tax);
					usr.setLastProcessed(last_processed);
					
				}
				
				return usr;
			}
			else{
				
				sql = "select * from user where user.email=?";
				
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, email);
				
				res = stmt.executeQuery();
				
				if(res.next()) {
					
					String uid = res.getString("uid");
					String name = res.getString("name");
					String orig_password = res.getString("password");
					String orig_email = res.getString("email");
					String photo = "C:\\Users\\gargv\\eclipse-workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\Task7\\uploads\\profile.jpg";			
					
					User usr = new User(uid,name,orig_email,orig_password,photo);
					
					sql = "select * from salary_extra_info where email=?";
					
					stmt = conn.prepareStatement(sql);
					stmt.setString(1, email);
					
					res = stmt.executeQuery();
					
					if(res.next()) {
						
						String ytd_tax = res.getString("ytd_tax");
						String last_processed = res.getString("last_processed");
						
						usr.setYtdTax(ytd_tax);
						usr.setLastProcessed(last_processed);
						
					}
					
					return usr;
				}
				
			}
			return null;
			
		}
		finally {
			
			close(conn,stmt,res);
			
		}
		
	}
	
	protected String getPhoto(String email) throws Exception{
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet res = null;
		
		try {
			conn = dataSource.getConnection();
			
			String sql = "select photo from user_info where email=?";
			stmt=conn.prepareStatement(sql);
			stmt.setString(1, email);
			
			res = stmt.executeQuery();
			
			if(res.next()) {
				return res.getString("photo");
			}
		}finally{
			close(conn,stmt,res);
		}
		
		return "";
	}
	
	protected boolean registerUser(User user) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet res = null;
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "select * from user where email=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, user.getEmail());
			
			res = stmt.executeQuery();
			
			if(res.next()) {
				return false;
			}
			
			sql = "select * from user where uid=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, user.getUid());
			
			res = stmt.executeQuery();
			
			if(res.next()) {
				return false;
			}
			
			sql = "insert into user values(?,?,?,?)";
			
			String encrypted_password = encrypt(user.getPassword());
			
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, user.getUid());
			stmt.setString(2, user.getName());
			stmt.setString(3, user.getEmail());
			stmt.setString(4, encrypted_password);
			
			stmt.execute();
			
		}
		finally {
			
			close(conn,stmt,null);
			
		}
		
		return true;
		
	}

	private void close(Connection conn, PreparedStatement stmt, ResultSet res) {
		
		try {
			if(conn!=null) {
				conn.close();
			}
			if(stmt!=null) {
				stmt.close();
			}
			if(res!=null) {
				res.close();
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}

	public UserJwt loginUser(String email, String password) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet res = null;
	
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "select * from user where email=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			
			res = stmt.executeQuery();
			
			if(res.next()) {
				
				String uid = res.getString("uid");
				String name = res.getString("name");
				String orig_password = res.getString("password");
				String orig_email = res.getString("email");
				
				String decrypted_password = decrypt(orig_password);
				
				if(decrypted_password.equals(password)) {
					UserJwt usr =  new UserJwt(uid,name,orig_email);
					
					return usr;
				
				}
				else {
					return null;
				}
			}
			
			return null;
			
		}
		finally {
			
			close(conn,stmt,res);
			
		}
		
	}

	public void addUserInfo(String email, int age, String bloodGroup, String phone, String altPhone, String department, String personalEmail, String currAddress, String permAddress, String photo) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "insert into user_info values(?,?,?,?,?,?,?,?,?,?,?,?)";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			stmt.setString(2, personalEmail);
			stmt.setInt(3, age);
			stmt.setString(4, bloodGroup);
			stmt.setString(5, currAddress);
			stmt.setString(6, permAddress);
			stmt.setString(7, department);
			stmt.setString(8, phone);
			stmt.setString(9, altPhone);
			stmt.setString(10, "0.0");
			stmt.setString(11, "0.0");
			stmt.setString(12, photo);
			
			stmt.execute();
			
		}
		finally {
			
			close(conn,stmt,null);
			
		}
		
	}

	public User viewUserInfo(User user) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet res = null;
		User u = null;
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "select * from user join user_info on user.email = user_info.email where user.email=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, user.getEmail());
			
			res = stmt.executeQuery();
			
			if(res.next()) {
				
				u = new User(res.getString("uid"),res.getString("name"),res.getString("email"),res.getString("password"),res.getString("personal_email"),res.getInt("age"),res.getString("blood_group"),res.getString("curr_address"),res.getString("perm_address"),res.getString("department"),res.getString("phone"),res.getString("alt_phone"),res.getString("total_salary"),res.getString("salary_amount"),res.getString("photo"));
				
			}
			else {
				u = new User(user.getUid(),user.getName(),user.getEmail(),user.getPhoto());
			}
			
			
		}
		finally {
			
			close(conn,stmt,res);

		}
		
		return u;
	}

	public void resetPassword(String email, String new_password) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "update user set password=? where email=? ";
			
			String encrypted_password = encrypt(new_password);
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(2, email);
			stmt.setString(1, encrypted_password);
			
			stmt.execute();
			
		}
		finally {
			
			close(conn,stmt,null);
			
		}
		
	}

	public void uploadFile(String email, String fileName, String filePath) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "insert into user_files values(?,?,?)";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			stmt.setString(2, fileName);
			stmt.setString(3, filePath);
			
			stmt.execute();
			
		}
		finally {
			
			close(conn,stmt,null);
			
		}
		
	}

	public List<String> retrieveUserFiles(String email) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet res = null;
		List<String> files = null;
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "select filename,filepath from user_files where email=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			
			res = stmt.executeQuery();
			
			files = new ArrayList<>();
			
			while(res.next()) {
				
				files.add(res.getString("filename"));
				files.add(res.getString("filepath"));
				
			}
			
		}
		finally {
			
			close(conn,stmt,null);
			
		}
		
		return files;
		
	}

	public void addSalaryInfo(String email, String sid, String salary_amount, String salary_type, String salary_des, Date sqlDate) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "insert into user_salary values(?,?,?,?,?,?)";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, sid);
			stmt.setString(2, email);
			stmt.setString(3, salary_amount);
			stmt.setString(4, salary_type);
			stmt.setString(5, salary_des);
			stmt.setDate(6, sqlDate);
			
			stmt.execute();
			
		}
		finally {
			
			close(conn,stmt,null);
			
		}
		
	}

	public void updateSalaryInfo(String email, String ytd_salary, String ytd_tax) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet res = null;
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "update user_info set salary_amount=? where email=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, ytd_salary);
			stmt.setString(2, email);
			
			stmt.execute();
			
			Double salary_amount = Double.parseDouble(ytd_salary);
			Double salary_tax = Double.parseDouble(ytd_tax);
			Double total_salary = salary_amount - salary_tax;     //change tax amount
			
			sql = "update user_info set total_salary=? where email=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, total_salary.toString());
			stmt.setString(2, email);
			
			stmt.execute();
			
		}
		finally {
			
			close(conn,stmt,res);
			
		}
		
	}
	
	public static String encrypt(String password) throws Exception{
		
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		SecretKey secretKey = generateKey();
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(new byte[16]));
		byte[] encrypted = cipher.doFinal(password.getBytes());
		return Base64.getEncoder().encodeToString(encrypted);
		
	}
	
	private String decrypt(String orig_password) throws Exception {
		
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		SecretKey secretKey = generateKey();
		cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(new byte[16]));
		byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(orig_password));
		return new String(decrypted);
		
	}
	
	private static SecretKey generateKey() throws Exception{
		
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		PBEKeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
		SecretKey tmp = factory.generateSecret(spec);
		return new SecretKeySpec(tmp.getEncoded(), "AES");
		
	}

	public User findUser(String email) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet res = null;
		User user = null;
		
		try {
					
			conn = dataSource.getConnection();
			
			String sql = "select * from user join user_info on user.email=user_info.email where user.email=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			
			res = stmt.executeQuery();
			
			if(res.next()) {
				
				String uid = res.getString("uid");
				String name = res.getString("name");
				String password = res.getString("password");
				int age = Integer.parseInt(res.getString("age"));
				String address = res.getString("curr_address");
				String department=res.getString("department");
				String phone = res.getString("phone");
				String photo = res.getString("photo");
				
				User usr = new User(uid,name,email,password,age,address,department,phone,photo);
				
				usr.setDepartment(department);
				
				sql = "select * from salary_extra_info where email=?";
				
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, email);
				
				res = stmt.executeQuery();
				
				if(res.next()) {
					
					String ytd_tax = res.getString("ytd_tax");
					String last_processed = res.getString("last_processed");
					
					usr.setYtdTax(ytd_tax);
					usr.setLastProcessed(last_processed);
					
				}
				
				user = usr;
			}
			else{
				
				sql = "select * from user where user.email=?";
				
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, email);
				
				res = stmt.executeQuery();
				
				if(res.next()) {
					
					String uid = res.getString("uid");
					String name = res.getString("name");
					String password = res.getString("password");
					String photo = "C:\\Users\\gargv\\eclipse-workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\Task7\\uploads\\profile.jpg";
					
					User usr = new User(uid,name,email,password,photo);
					
					sql = "select * from salary_extra_info where email=?";
					
					stmt = conn.prepareStatement(sql);
					stmt.setString(1, email);
					
					res = stmt.executeQuery();
					
					if(res.next()) {
						
						String ytd_tax = res.getString("ytd_tax");
						String last_processed = res.getString("last_processed");
						
						usr.setYtdTax(ytd_tax);
						usr.setLastProcessed(last_processed);
						
					}
					
					
					user = usr;
				}
				
			}
			
		}
		finally {
			
			close(conn,stmt,res);
			
		}
		
		return user;
		
	}
	
	public String retrieveEmail(String uid) throws Exception {
		
		Connection conn = null;
		ResultSet res = null;
		PreparedStatement stmt = null;
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "select * from user where uid=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, uid);
			
			res = stmt.executeQuery();
			
			if(res.next()) {
				
				String email = res.getString("email");
				return email;
				
			}
			else {
				return "";
			}
			
		}
		finally {
			
			close(conn,stmt,res);
			
		}
		
	}

	public String findYtdTax(String email, Date sqlDate) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet res = null;
		String ytdTax="0";
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "select sum(salary_amount) as tax_amount from user_salary where salary_user_email=? and salary_type in ('Income Tax','Professional Tax', 'Leave Deduct') and year(date)=?";
			
			@SuppressWarnings("deprecation")
			int year = sqlDate.getYear();
			year+=1900;
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			stmt.setInt(2, year);
			
			res = stmt.executeQuery();
			
			if(res.next()) {
				ytdTax = res.getString("tax_amount");
			}
			
		}
		finally {
			
			close(conn,stmt,res);
			
		}
		
		return ytdTax;
		
	}

	public String findYtdSalary(String email, Date sqlDate) throws Exception {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet res = null;
		String ytdSalary="0";
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "select sum(salary_amount) as total_amount from user_salary where salary_user_email=? and salary_type not in ('Income Tax','Professional Tax', 'Leave Deduct') and YEAR(date)=?";
			
			@SuppressWarnings("deprecation")
			int year = sqlDate.getYear();
			year+=1900;
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			stmt.setInt(2, year);
			
			res = stmt.executeQuery();
			
			if(res.next()) {
				ytdSalary = res.getString("total_amount");
			}
			
		}
		finally {
			
			close(conn,stmt,res);
			
		}
		
		return ytdSalary;
		
	}

	public void updateSalaryExtraInfo(String email, String ytd_salary, String ytd_tax, Date sqlDate) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet res = null;
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "select * from salary_extra_info where email=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			
			res = stmt.executeQuery();
			
			if(!res.next()) {
				
				sql = "insert into salary_extra_info values(?,?,?,?)";
				
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, email);
				stmt.setDate(2, sqlDate);
				stmt.setString(3, ytd_tax);
				stmt.setString(4, ytd_salary);
				
				stmt.execute();
				
			}
			else {
				
				sql = "update salary_extra_info set last_processed=?, ytd_tax=?, ytd_salary=? where email=? ";
				
				stmt = conn.prepareStatement(sql);
				stmt.setDate(1, sqlDate);
				stmt.setString(2, ytd_tax);
				stmt.setString(3, ytd_salary);
				stmt.setString(4, email);
				
				stmt.execute();
				
			}
			
		}
		finally {
			
			close(conn,stmt,null);
			
		}
		
	}
	
public List<Map<String, String>> viewEarningsSalaryInfo(String email) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet res = null;
		List<Map<String,String>> lst = new ArrayList<>();
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "select * from user_salary where salary_user_email=? and salary_type not in ('Income Tax','Professional Tax', 'Leave Deduct')";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			
			res = stmt.executeQuery();
			
			while(res.next()) {
				
				Map mp = new HashMap<String,String>();
				mp.put("amount", res.getString("salary_amount"));
				mp.put("type", res.getString("salary_type"));
				mp.put("date", res.getString("date"));
				
				lst.add(mp);
				
			}
			
		}
		finally {
			
			close(conn,stmt,res);
			
		}
		
		return lst;
		
	}

	public List<Map<String, String>> viewTaxSalaryInfo(String email) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet res = null;
		List<Map<String,String>> lst = new ArrayList<>();
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "select * from user_salary where salary_user_email=? and salary_type in ('Income Tax','Professional Tax', 'Leave Deduct')";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			
			res = stmt.executeQuery();
			
			while(res.next()) {
				
				Map mp = new HashMap<String,String>();
				mp.put("amount", res.getString("salary_amount"));
				mp.put("type", res.getString("salary_type"));
				mp.put("date", res.getString("date"));
				
				lst.add(mp);
				
			}
			
		}
		finally {
			
			close(conn,stmt,res);
			
		}
		
		return lst;
		
	}

	public void markLeave(String email, String leave_type, Date curr_date, String from_date, String to_date, String note) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		Date from = Date.valueOf(from_date);
		Date to = Date.valueOf(to_date);
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "insert into user_leave(email,leave_type,date_of_request,from_date,to_date,note) values(?,?,?,?,?,?)";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			stmt.setString(2, leave_type);
			stmt.setDate(3, curr_date);
			stmt.setDate(4, from);
			stmt.setDate(5, to);
			stmt.setString(6, note);
			
			stmt.execute();
			
		}
		finally {
			
			close(conn,stmt,null);
			
		}
		
	}

	public int findTotalLeaves(String user_email) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet res = null;
		int total_leaves = 0;
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "select count(*) as total_leaves from user_leave where email=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, user_email);
			
			
			res = stmt.executeQuery();
			
			if(res.next()) {
				
				total_leaves = res.getInt("total_leaves");
				
			}
			else {
				return 0;
			}
			
		}
		finally {
			
			close(conn,stmt,res);
			
		}
		
		return total_leaves;
		
	}

	public List<Double> findLeavesPercentage(String user_email, int total_leaves) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet res = null;
		List<Double> lst = new ArrayList<>();
		int cl_leave=0, sl_leave=0, pl_leave=0, coff_leave=0;
		Double totalLeaves = Double.valueOf(total_leaves);
		
		if(total_leaves==0) {
			
			lst.add(0.0);
			lst.add(0.0);
			lst.add(0.0);
			lst.add(0.0);
			
		}
		else {
			
			try {
				
				conn = dataSource.getConnection();
				
				String sql = "select count(case when leave_type='CL' then 1 end) as cl_count, count(case when leave_type='PL' then 1 end) as pl_count, count(case when leave_type='SL' then 1 end) as sl_count, count(case when leave_type='COFF' then 1 end) as coff_count from user_leave where email=? group by email";
				
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, user_email);
				
				res = stmt.executeQuery();
				
				if(res.next()) {
					
					cl_leave = res.getInt("cl_count");
					sl_leave = res.getInt("sl_count");
					pl_leave = res.getInt("pl_count");
					coff_leave = res.getInt("coff_count");
					
				}
				
				lst.add((double) (cl_leave/totalLeaves));
				lst.add((double) (sl_leave/totalLeaves));
				lst.add((double) (pl_leave/totalLeaves));
				lst.add((double) (coff_leave/totalLeaves));

				
			}
			finally {
				
				close(conn,stmt,res);
				
			}
			
		}
		
		return lst;
		
	}

	public void storePunchResult(String email, Date sqlDate, String currentTime, String result) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "insert into user_punches values(?,?,?,?)";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			stmt.setDate(2, sqlDate);
			stmt.setString(3, currentTime);
			stmt.setString(4, result);
			
			stmt.execute();
			
		}
		finally {
			
			close(conn,stmt,null);
			
		}
		
	}

	public boolean checkIsPunched(String email, Date date) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet res = null;
		boolean isPunched = false;
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "select * from user_punches where date=? and email=? and result='SWIPED'";
			
			stmt = conn.prepareStatement(sql);
			stmt.setDate(1, date);
			stmt.setString(2, email);
			
			res = stmt.executeQuery();
			
			if(res.next()) {
				
				isPunched = true;
				
			}
			
		}
		finally {
			
			close(conn,stmt,res);
			
		}
		
		return isPunched;
		
	}

	public List<String> lastPunchedData(String email) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet res = null;
		List<String> lst = new ArrayList<>();
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "select date,result from user_punches where email=? order by date desc limit 1";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			
			res = stmt.executeQuery();
			
			if(res.next()) {
				
				lst.add(res.getDate("date").toString());
				lst.add(res.getString("result"));
				
			}
			else {
				
				lst.add("Not available");
				lst.add("Not available");
				
			}
	
		}
		finally {
			
			close(conn,stmt,null);
			
		}
		
		return lst;
		
	}

	public void storeLoginTime(String email, String time) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "Insert into user_visits values(?,?,'')";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1,email);
			stmt.setString(2, time);
			
			stmt.execute();
			
		}
		finally {
			
			close(conn,stmt,null);
			
		}
		
	}

	public void storeLogoutTime(String email, String time) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "update user_visits set logout=? where email=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1,time);
			stmt.setString(2, email);
			
			stmt.execute();
			
		}
		finally {
			
			close(conn,stmt,null);
			
		}
		
	}

	public User retrieveUser(String uid) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet res = null;
		User user = null;
		
		try {
			
			 conn = dataSource.getConnection();
			 
			 String sql = "select * from user inner join user_info on user.email=user_info.email where user.uid=?";
			 
			 stmt = conn.prepareStatement(sql);
			 stmt.setString(1, uid);
			 
			 res = stmt.executeQuery();
			 
			 if(res.next()) {
				 
				 user = new User(res.getString("uid"),res.getString("name"),res.getString("email"),res.getString("password"),res.getString("personal_email"),res.getInt("age"),res.getString("blood_group"),res.getString("curr_address"),res.getString("perm_address"),res.getString("department"),res.getString("phone"),res.getString("alt_phone"),res.getString("total_salary"),res.getString("salary_amount"),res.getString("photo"));
				 
			 }
			
		}
		finally {
			
			close(conn,stmt,res);
			
		}
		
		return user;
		
	}

	public void updateUserInfo(String email,String pers_email, int age,String bg, String curr_address,String perm_address, String department, String phone,
			String alt_phone, String salary_amount, String salary_total) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "Update user_info set personal_email=?, age=?, blood_group=?, curr_address=?, perm_address=?, department=?, phone=?, alt_phone=?, salary_amount=?, total_salary=? where email=?";
			
			stmt=conn.prepareStatement(sql);
			stmt.setString(1, pers_email);
			stmt.setInt(2, age);
			stmt.setString(3, bg);
			stmt.setString(4, curr_address);
			stmt.setString(5, perm_address);
			stmt.setString(6, department);
			stmt.setString(7, phone);
			stmt.setString(8, alt_phone);
			stmt.setString(9,salary_amount);
			stmt.setString(10, salary_total);
			stmt.setString(11, email);
			
			stmt.execute();
		
		}
		finally {
			
			close(conn,stmt,null);
			
		}
		
	}

	public void updateInfoSelf(String email,String pers_email, int age, String blood_group, String curr_address, String perm_address, String phone, String alt_phone, String photo) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "update user_info set personal_email=?,age=?,blood_group=?,curr_address=?,perm_address=?,phone=?,alt_phone=?,photo=? where email=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, pers_email);
			stmt.setInt(2, age);
			stmt.setString(3, blood_group);
			stmt.setString(4, curr_address);
			stmt.setString(5, perm_address);
			stmt.setString(6, phone);
			stmt.setString(7, alt_phone);
			stmt.setString(8, photo);
			stmt.setString(9, email);
			
			stmt.execute();
			
		}
		finally {
			
			close(conn,stmt,null);
			
		}
		
	}

	@SuppressWarnings("null")
	public List<Leave> retrieveLeaves() throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet res = null;
		List<Leave> leaves = new ArrayList<>();
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "select * from user_leave inner join user on user.email=user_leave.email";
			stmt = conn.prepareStatement(sql);
			res = stmt.executeQuery();
			
			while(res.next()) {
				Leave leave = new Leave(res.getString("leave_id"), res.getString("uid"),res.getString("email"),res.getString("name"),res.getString("leave_type"),res.getString("date_of_request"),res.getString("from_date"),res.getString("to_date"),res.getString("note"),res.getString("status"));
				leaves.add(leave);
			}
			
			
		}
		finally {
			
			close(conn,stmt,res);
		
		}
		
		return leaves;
		
	}

	public void approveLeave(int leave_id) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "update user_leave set status='APPROVED' where leave_id=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, leave_id);
			
			stmt.execute();
			
		}
		finally {
			
			close(conn,stmt,null);
			
		}
		
	}

	public void rejectLeave(int leave_id) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "update user_leave set status='REJECTED' where leave_id=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, leave_id);
			
			stmt.execute();
			
		}
		finally {
			
			close(conn,stmt,null);
			
		}
		
	}

	public List<Leave> retrieveEmployeeLeaves(Date curr_date, String email) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet res = null;
		List<Leave> leaves = new ArrayList<>();
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "select * from user_leave where email=? and to_date>=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			stmt.setDate(2, curr_date);
			
			res = stmt.executeQuery();
			
			while(res.next()) {
				Leave leave = new Leave(res.getString("leave_id"),"",res.getString("email"),"",res.getString("leave_type"),res.getString("date_of_request"),res.getString("from_date"),res.getString("to_date"),res.getString("note"),res.getString("status"));
				leaves.add(leave);
			}
			
		}
		finally {
			
			close(conn,stmt,res);
			
		}
		
		return leaves;
		
	}

	public void cancel_leave(int lid) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "delete from user_leave where leave_id=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, lid);
			
			stmt.execute();
			
		}
		finally {
			
			close(conn,stmt,null);
			
		}
		
	}

	public boolean checkIsLeave(Date currDate, String email) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet res = null;
		boolean isLeave = false;
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "select * from user_leave where email=? and ?>=from_date and ?<=to_date and status='APPROVED'";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			stmt.setDate(2, currDate);
			stmt.setDate(3, currDate);
			
			res = stmt.executeQuery();
			
			if(res.next()) {
				isLeave = true;
			}
			
			
		}
		finally {
			
			close(conn,stmt,res);
			
		}
		
		return isLeave;
		
	}

	public List<Integer> retrieve_leave_types() throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet res = null;
		List<Integer> lst = new ArrayList<>();
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "select * from leave_types";
			
			stmt = conn.prepareStatement(sql);
			
			res = stmt.executeQuery();
			
			if(res.next()) {
				
				lst.add(res.getInt("PL"));
				lst.add(res.getInt("CL"));
				lst.add(res.getInt("SL"));
				lst.add(res.getInt("ML"));
				lst.add(res.getInt("MrL"));
				lst.add(res.getInt("PtL"));
				lst.add(res.getInt("COFF"));
				lst.add(res.getInt("LOP"));
				
			}
			
		}
		finally {
			
			close(conn,stmt,res);
			
		}
		
		return lst;
		
	}

	public void save_leave_types(List<Integer> selected_leave_types) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "delete from leave_types";
			
			stmt = conn.prepareStatement(sql);
			stmt.execute();
			
			sql = "insert into leave_types values(?,?,?,?,?,?,?,?)";
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, selected_leave_types.get(0));
			stmt.setInt(2, selected_leave_types.get(1));
			stmt.setInt(3, selected_leave_types.get(2));
			stmt.setInt(4, selected_leave_types.get(3));
			stmt.setInt(5, selected_leave_types.get(4));
			stmt.setInt(6, selected_leave_types.get(5));
			stmt.setInt(7, selected_leave_types.get(6));
			stmt.setInt(8, selected_leave_types.get(7));
			
			stmt.execute();
			
		}
		finally {
			
			close(conn,stmt,null);
			
		}
		
	}
	
	public void addEducationDetails(String email, Map<Integer,Map<String,String>> education_details) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = dataSource.getConnection();
			
			String insertSQL = "INSERT INTO user_education (email, education_id, school_name, location, grade, degree, from_date, to_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	        
	        // Prepare the statement
	        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
	            // Iterate through the education details map
	            for (Map.Entry<Integer, Map<String, String>> entry : education_details.entrySet()) {
	                int educationId = entry.getKey();
	                Map<String, String> details = entry.getValue();

	                pstmt.setString(1, email);
	                pstmt.setString(2, String.valueOf(educationId));
	                pstmt.setString(3, details.get("school"));
	                pstmt.setString(4, details.get("location"));
	                pstmt.setString(5, details.get("grade"));
	                pstmt.setString(6, details.get("degree"));
	                pstmt.setDate(7, java.sql.Date.valueOf(details.get("from_date")));
	                pstmt.setDate(8, java.sql.Date.valueOf(details.get("to_date")));

	                pstmt.executeUpdate();
	            }
	        } catch (Exception e) {
	            throw new Exception("Error inserting education details", e);
	        }
			
			
		}
		finally {
			
			close(conn,stmt,null);
			
		}
	}

	public void addOfficeDocs(String email, String panPath, String empPhotoPath, String marksheet12Path,
			String marksheet10Path) throws Exception {
		
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "INSERT INTO user_office_docs values(?,?,?,?,?)";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			stmt.setString(2, panPath);
			stmt.setString(3, empPhotoPath);
			stmt.setString(4, marksheet12Path);
			stmt.setString(5, marksheet10Path);
			
			stmt.execute();
			
		}
		finally {
			close(conn,stmt,null);
		}
		
		
	}

	public List<List<String>> retrieveUserEducation(String email) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet res = null;
		List<List<String>> educationDetails = new ArrayList<>();
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "SELECT education_id, school_name, location, grade, degree, from_date, to_date FROM user_education WHERE email = ?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1,email);
			
			res = stmt.executeQuery();
			
			while(res.next()) {
				List<String> educationDetail = new ArrayList<>();
	            educationDetail.add(String.valueOf(res.getInt("education_id")));
	            educationDetail.add(res.getString("school_name"));
	            educationDetail.add(res.getString("location"));
	            educationDetail.add(res.getString("grade"));
	            educationDetail.add(res.getString("degree"));
	            educationDetail.add(res.getDate("from_date").toString());
	            educationDetail.add(res.getDate("to_date").toString());

	            educationDetails.add(educationDetail);
			}
			
			
		}
		finally {
			close(conn,stmt,res);
		}
		
		return educationDetails;
		
	}

	public List<String> retrieveOfficeDocs(String email) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet res = null;
		List<String> officeDocs = new ArrayList<>();
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "SELECT pan,photo,marksheet_12,marksheet_10 from user_office_docs where email=?";
			
			stmt=conn.prepareStatement(sql);
			stmt.setString(1,email);
			
			res = stmt.executeQuery();
			
			if(res.next()) {
				officeDocs.add(res.getString("pan"));
				officeDocs.add(res.getString("photo"));
				officeDocs.add(res.getString("marksheet_12"));
				officeDocs.add(res.getString("marksheet_10"));
			}
			
		}
		finally {
			close(conn,stmt,res);
		}
		
		return officeDocs;
		
	}

	public void updateEducationDetails(String email, Map<Integer, Map<String, String>> educationDetails) throws Exception {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			
			conn = dataSource.getConnection();
			
			String deleteQuery = "DELETE FROM user_education WHERE email = ?";
            stmt = conn.prepareStatement(deleteQuery);
            stmt.setString(1, email);
            stmt.executeUpdate();
            stmt.close();
            
            String insertSQL = "INSERT INTO user_education (email, education_id, school_name, location, grade, degree, from_date, to_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	        
	        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
	     
	            for (Map.Entry<Integer, Map<String, String>> entry : educationDetails.entrySet()) {
	                int educationId = entry.getKey();
	                Map<String, String> details = entry.getValue();

	                pstmt.setString(1, email);
	                pstmt.setString(2, String.valueOf(educationId));
	                pstmt.setString(3, details.get("school"));
	                pstmt.setString(4, details.get("location"));
	                pstmt.setString(5, details.get("grade"));
	                pstmt.setString(6, details.get("degree"));
	                pstmt.setDate(7, java.sql.Date.valueOf(details.get("from_date")));
	                pstmt.setDate(8, java.sql.Date.valueOf(details.get("to_date")));

	                pstmt.executeUpdate();
	            }
	        } catch (Exception e) {
	            throw new Exception("Error inserting education details", e);
	        } 
			
			
		}
		finally {
			close(conn,stmt,null);
		}
		
	}

	public void updateOfficeDocs(String email, String panPath, String empPhotoPath, String marksheet12Path,
			String marksheet10Path) throws Exception {
		
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			
			conn = dataSource.getConnection();
			
			String sql = "UPDATE user_office_docs set pan=?, photo=?, marksheet_12=?, marksheet_10=? where email=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, panPath);
			stmt.setString(2, empPhotoPath);
			stmt.setString(3, marksheet12Path);
			stmt.setString(4, marksheet10Path);
			stmt.setString(5, email);
			
			stmt.execute();
			
		}
		finally {
			close(conn,stmt,null);
		}
		
	}

//	private static String generateSalt() {
//		
//		SecureRandom random = new SecureRandom();
//		byte[] salt = new byte[16];
//		random.nextBytes(salt);
//		return Base64.getEncoder().encodeToString(salt);
//		
//	}

}
