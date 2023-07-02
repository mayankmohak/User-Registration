package com.highradiusIR.dbConnections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
	public static Connection con;

	public static Connection getDbConnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String DBUser = "root";
		String DBPass = "password123.";
		String DBUrl = "jdbc:mysql://localhost:3305/hydrabad_users";
		try {
		con = DriverManager.getConnection(DBUrl, DBUser, DBPass);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return con;
	}
}
