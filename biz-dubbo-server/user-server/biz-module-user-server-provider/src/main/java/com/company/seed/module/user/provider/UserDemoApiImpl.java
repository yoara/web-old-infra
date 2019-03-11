package com.company.seed.module.user.provider;

import com.company.seed.module.user.server.api.UserDemoApi;
import com.company.seed.module.user.service.UserService;

import javax.annotation.Resource;

public class UserDemoApiImpl implements UserDemoApi {
    @Resource
    private UserService userService;
    @Override
    public boolean authenticateUser(String userName, String passwordMD5) {
        if(userService.authenticateUser(userName,passwordMD5)!=null){
            return true;
        }
        return false;
    }
}