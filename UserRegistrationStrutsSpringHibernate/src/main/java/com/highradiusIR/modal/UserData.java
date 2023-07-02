package com.highradiusIR.modal;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserData {
	
	int id;
	String username;
	String email;
	String password;
	
//	public UserData(int id, String username, String email, String password) {
//		super();
//		this.id = id;
//		this.username = username;
//		this.email = email;
//		this.password = password;
//	}
	
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
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
		return password;
	}

	public void setPassword(String password) {
		this.password = passEncrypt(password);
	}

	@Override
	public String toString() {
		return "UserData [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password + "]";
	}

}
