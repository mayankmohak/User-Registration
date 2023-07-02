package com.highradiusIR.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.highradiusIR.dbConnections.DBConnect;
import com.highradiusIR.model.UserData;

@WebServlet({ "/Servlet", "/ServletAdd" })
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Servlet() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();
		switch (action) {
		case "/Servlet":
			showData(request, response);
			break;
		case "/ServletAdd":
			response.sendRedirect(request.getContextPath() + "/addUser.jsp");
			break;
		default:
			System.out.println("default reached");
			break;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		addData(request, response);
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected static void showData(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection conn = null;
		System.out.println("Success Uncaught");
		PreparedStatement pst = null;
		// String id = request.getAttribute("id").toString();
		String sql = "select * from users";
		ArrayList<UserData> users = new ArrayList<UserData>();
		ResultSet rs = null;
		try {
			conn = DBConnect.getDbConnection();
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				users.add(new UserData(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
			}
			System.out.println(users);
			request.setAttribute("Users", users);
			RequestDispatcher reqDisp = request.getRequestDispatcher("index.jsp");
			reqDisp.forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected static Boolean checkIfExist(String email, String username) {
		Connection conn = null;
		PreparedStatement pst = null;
		String sqlUserCheck = "SELECT * FROM users WHERE email='" + email + "' username='"+username+"'";
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
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
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

	private void addData(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Connection conn = null;
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String password = passEncrypt(request.getParameter("password"));

		Boolean existOnDB = checkIfExist(email, username);
		PreparedStatement pst = null;
		String sql = "INSERT INTO users(username,email,password)values(?,?,?)";

		PrintWriter pw = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		Map<String, Object> mp = new HashMap<>();
		if (!existOnDB) {
			try {
				conn = DBConnect.getDbConnection();
				pst = conn.prepareStatement(sql);
				pst.setString(1, username);
				pst.setString(2, email);
				pst.setString(3, password);
				pst.executeUpdate();
				mp.put("success", true);
			} catch (Exception e) {
				e.printStackTrace();
				mp.put("success", false);
			}
		} else {
			mp.put("success", false);
			mp.put("msg", "User Already present");
		}

		Gson gson = new Gson();
		String res = gson.toJson(mp);
		pw.print(res);
		pw.flush();
	}
}
