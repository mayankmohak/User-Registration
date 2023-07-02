package com.highradiusStruts.action;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.gson.Gson;
import com.highradiusIR.manager.UserManager;

public class Action {
	private String username, email, password;
	String response;
	
	ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
	UserManager userManager = context.getBean("userManager", UserManager.class);
	
	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

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
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String addUser () {
		Map<String, Object>  res = userManager.getUser(username,email,password);
		Gson gson = new Gson();
		setResponse(gson.toJson(res));
		return "success";
	}
}