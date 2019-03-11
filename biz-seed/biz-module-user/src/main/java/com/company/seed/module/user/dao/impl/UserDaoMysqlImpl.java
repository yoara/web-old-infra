package com.company.seed.module.user.dao.impl;

import com.company.seed.dao.basic.dataoperatelog.annotation.DataOperateLogMethod;
import com.company.seed.dao.basic.dataoperatelog.enums.DataOperateTypeEnum;
import com.company.seed.dao.basic.mybatis.BaseDao;
import com.company.seed.module.user.dao.UserDao;
import com.company.seed.module.user.model.UserModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by yoara on 2016/3/2.
 */
@Repository
public class UserDaoMysqlImpl extends BaseDao implements UserDao {
	private static String SQL_PREFIX = "com.company.seed.module.user.dao.UserDao.";

	@Override
	public void insertUser(UserModel user) {
		user.setId(UUID.randomUUID().toString());
		insert(SQL_PREFIX+"insertUser",user);
	}

	@Override
	public UserModel selectUser() {
		return (UserModel)selectOne(SQL_PREFIX+"selectUser",null);
	}

	@Override
	public UserModel authenticateUser(String userName, String passwordMD5) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userName",userName);
		params.put("passwordMD5",passwordMD5);
		return (UserModel)selectOne(SQL_PREFIX+"authenticateUser",params);
	}

	@Override
	public UserModel selectUserById(String id) {
		return (UserModel)selectOne(SQL_PREFIX+"selectUserById",id);
	}

	@Override
	@DataOperateLogMethod(DataOperateTypeEnum.UPDATE)
	public boolean updatePsw(UserModel model) {
		throw new RuntimeException("test");
		//return update(SQL_PREFIX+"updatePsw",model)>0;
	}

	@Override
	public boolean updatePsw(Map<String,Object> model) {
		UserModel modelNew = new UserModel();
		modelNew.setId(model.get("id").toString());
		modelNew.setPassword(model.get("password").toString());
		return update(SQL_PREFIX+"updatePsw",modelNew)>0;
	}
}
