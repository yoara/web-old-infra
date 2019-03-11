package com.company.seed.module.user.server.api;

public interface UserDemoApi {
	boolean authenticateUser(String userName, String passwordMD5);
}