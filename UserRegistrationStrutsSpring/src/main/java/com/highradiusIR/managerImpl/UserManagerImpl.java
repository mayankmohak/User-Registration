package com.highradiusIR.managerImpl;

import java.util.Map;
import com.highradiusIR.dao.UserDao;
import com.highradiusIR.manager.UserManager;

public class UserManagerImpl implements UserManager {
	
	UserDao userDao;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public Map<String, Object>  getUser(String username, String email, String password) {
		return userDao.getUser(username,email,password);
	}
	
}
