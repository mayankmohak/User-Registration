package com.highradiusStruts.action;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.highradiusIR.dbConnections.DBConnect;

public class Action {
	String username, email, password;
	String response;

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return passEncrypt(password);
	}

	public void setPassword(String password) {
		this.password = password;
	}

	Connection conn = null;

	protected Boolean checkIfExist(String email, String username) {
		PreparedStatement pst = null;
		String sqlUserCheck = "SELECT * FROM users WHERE email='" + email + "' OR username='"+username+"'";
		ResultSet rs = null;
		Boolean flag = false;
		try {
			conn = DBConnect.getDbConnection();
			pst = conn.prepareStatement(sqlUserCheck);
			rs = pst.executeQuery();

			while (rs.next()) {
				flag = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return flag;
	}

	private String passEncrypt(String password) {
		String encryptedpassword = null;
		try {

			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(password.getBytes());
			byte[] bytes = m.digest();
			StringBuilder s = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			encryptedpassword = s.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return encryptedpassword;
	}

	public String execute() {

		Boolean existOnDB = checkIfExist(email, username);
		PreparedStatement pst = null;
		String INSERT_SQL = "INSERT INTO users(username,email,password)values(?,?,?)";

		Map<String, Object> mp = new HashMap<>();

		if (!existOnDB) {
			try {
				conn = DBConnect.getDbConnection();
				pst = conn.prepareStatement(INSERT_SQL);
				pst.setString(1, getUsername());
				pst.setString(2, getEmail());
				pst.setString(3, getPassword());
				pst.executeUpdate();

				mp.put("success", true);
			} catch (Exception e) {
				e.printStackTrace();
				return "error";
			}
		} else {
			mp.put("success", false);
			mp.put("msg", "User Already present");
		}
		Gson gson = new Gson();
		setResponse(gson.toJson(mp));
		return "success";
	}
}

/*
 * 
 * public class Action { public String a = "m";
 * 
 * 
 * public String getA() { return a; }
 * 
 * 
 * public void setA(String a) { this.a = a; }
 * 
 * 
 * public String execute() {
 * 
 * String DBUser = "root"; String DBPass = "password123."; String DBUrl =
 * "jdbc:mysql://localhost:3305/hydrabad_users"; Map<String, Object> mp = new
 * HashMap<>();
 * 
 * try { Class.forName("com.mysql.cj.jdbc.Driver"); Connection conn =
 * DriverManager.getConnection(DBUrl, DBUser, DBPass);
 * 
 * String dataQ = "select * from users"; Statement dataStatement =
 * conn.createStatement(); ResultSet dataResultSet =
 * dataStatement.executeQuery(dataQ);
 * 
 * while (dataResultSet.next()) { a = dataResultSet.getString("username");
 * System.out.println(a); } mp.put("success", true); } catch (Exception e) {
 * e.printStackTrace(); return "error"; } Gson gson = new Gson(); String res =
 * gson.toJson(mp); setA(res); return "success"; } }
 */