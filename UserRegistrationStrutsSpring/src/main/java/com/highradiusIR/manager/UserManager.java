package com.highradiusIR.manager;

import java.util.Map;

public interface UserManager {
	public Map<String, Object>  getUser(String username, String email, String password);
}
