package com.highradiusIR.dao;

import java.util.Map;

public interface UserDao {
	public Map<String, Object> getUser(String username, String email, String password);
}
