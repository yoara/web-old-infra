package com.company.seed.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.company.seed.common.CommonConstants;
import com.company.seed.module.user.model.UserModel;
import com.company.seed.module.user.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yoara.framework.component.web.bean.validate.LoginForm;
import org.yoara.framework.core.util.encrypt.MD5Util;

import javax.annotation.Resource;

/**
 * Created by yoara on 2016/3/3.
 */
@Lazy
@RestController
@RequestMapping(value = "/login/user",produces = { "application/json;charset=UTF-8" })
public class UserLoginController extends WebBaseController {
    @Resource
    private UserService userService;

    @GetMapping(value = "loginin")
    public String loginIn(LoginForm form) {
        JSONObject json = new JSONObject();
        String passwordMD5 = MD5Util.getMD5(form.getPassword().getBytes());
        UserModel user = userService.authenticateUser(form.getUserName(),passwordMD5);
        if(user==null){
            json.put("result", "fail");
            json.put("msg", "wrong username/password");
            return json.toString();
        }
        getRequest().getSession().setAttribute(CommonConstants.CURRENT_LOGIN_USER, user);
        json.put("result", "ok");
        return json.toString();
    }

    @GetMapping(value = "loginout")
    public String loginOut() {
        getRequest().getSession().removeAttribute(CommonConstants.CURRENT_LOGIN_USER);
        JSONObject json = new JSONObject();
        json.put("result", "ok");
        return json.toString();
    }
}
