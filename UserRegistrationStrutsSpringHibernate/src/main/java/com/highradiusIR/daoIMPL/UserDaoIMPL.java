package com.highradiusIR.daoIMPL;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.highradiusIR.dao.UserDao;
import com.highradiusIR.dbConfiguration.DBConfiguration;
import com.highradiusIR.modal.UserData;

public class UserDaoIMPL implements UserDao {

	protected Boolean checkIfExist(String email, String username) {
		Boolean flag = false;
		
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		SessionFactory sf = cfg.buildSessionFactory();
		Session s = sf.openSession();
		Transaction t=null;
		
		try {
			t = s.beginTransaction();
			String HQL = "FROM UserData U WHERE U.username='"+username+"' OR U.email='"+email+"'";
			Query q = s.createQuery(HQL);
			List <UserData> lud = q.list();
			if(lud.size() > 0)
				flag =  true;
		} catch (Exception e1) {
			if(t!=null) {
				t.rollback();
				e1.printStackTrace();
			}
		}finally {
			s.close();
		}	
		
		return flag;
	}

	@Override
	public Map<String, Object> getUser(String username, String email, String password) {
		
		Boolean existOnDB = checkIfExist(email, username);
		Map<String, Object> mp = new HashMap<>();
		if(!existOnDB) {
			Configuration cfg = new Configuration();
			cfg.configure("hibernate.cfg.xml");
			SessionFactory sf = cfg.buildSessionFactory();
			Session s = sf.openSession();
			Transaction t=null;

			try {
				t = s.beginTransaction();
				try {
					UserData user = new UserData();
					user.setUsername(username);
					user.setEmail(email);
					user.setPassword(password);
					s.save(user);
				} catch (Exception e) {
					e.printStackTrace();
				}
				t.commit();
				mp.put("success", true);
			} catch (Exception e1) {
				if(t!=null) {
					t.rollback();
					e1.printStackTrace();
					mp.put("success", false);
					mp.put("msg", "Failure on Server : "+ e1);
				}
			}finally {
				s.close();
			}
		} else {
			mp.put("success", false);
			mp.put("msg", "User Already present");
		}

		return mp;
	}
}
