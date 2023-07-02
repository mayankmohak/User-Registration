package com.highradiusIR.daoImpl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.highradiusIR.dao.UserDao;
import com.highradiusIR.dbConnections.DBConnect;

public class UserDaoImpl implements UserDao {

	Connection conn = null;

	protected Boolean checkIfExist(String email, String username) {
		PreparedStatement pst = null;
		String sqlUserCheck = "SELECT * FROM users WHERE email='" + email + "' OR username='" + username + "'";
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

	@Override
	public Map<String, Object> getUser(String username, String email, String password) {
		

		Boolean existOnDB = checkIfExist(email, username);
		PreparedStatement pst = null;
		String INSERT_SQL = "INSERT INTO users(username,email,password)values(?,?,?)";

		Map<String, Object> mp = new HashMap<>();

		if (!existOnDB) {
			try {
				conn = DBConnect.getDbConnection();
				pst = conn.prepareStatement(INSERT_SQL);
				pst.setString(1, username);
				pst.setString(2, email);
				pst.setString(3, passEncrypt(password));
				pst.executeUpdate();

				mp.put("success", true);
			} catch (Exception e) {
				e.printStackTrace();
				return mp;
			}
		} else {
			mp.put("success", false);
			mp.put("msg", "User Already present");
		}

		return mp;
	}
}
