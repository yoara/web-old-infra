package com.company.seed.dao.basic.mybatis;

import org.apache.ibatis.session.SqlSession;

/**
 * 回调方法，用于execute方法
 * Created by yoara on 2015/12/21.
 */
public interface SqlSessionCallback {
	Object doInSqlSession(SqlSession SqlSession) throws Exception;
}
