package com.company.seed.module.user.dao;

import com.company.seed.module.user.model.UserModel;

import java.util.Map;

/**
 * Created by yoara on 2016/3/2.
 */
public interface UserDao {
	void insertUser(UserModel user);
	UserModel selectUser();
	/**
	 * @param userName 用户名
	 * @param passwordMD5 MD5加密后的密文
	 * @return
	 */
	UserModel authenticateUser(String userName,String passwordMD5);

	UserModel selectUserById(String id);

	boolean updatePsw(UserModel model);
	boolean updatePsw(Map<String,Object> model);
}
