package com.company.seed.web.controller.openapi;

import com.alibaba.fastjson.JSONObject;
import com.company.seed.module.user.model.UserModel;
import com.company.seed.module.user.service.UserService;
import com.company.seed.web.controller.WebBaseController;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yoara.framework.core.util.encrypt.MD5Util;

import javax.annotation.Resource;

/**
 * Created by yoara on 2016/3/3.
 */
@Lazy
@RestController
@RequestMapping(value = "/openapi/cache",produces = { "application/json;charset=UTF-8" })
public class DemoCacheController extends WebBaseController {
    @Resource
    private UserService userService;

    @GetMapping(value = "cache")
    public String cache() {
        UserModel model1 = new UserModel();
        model1.setName(getString("name"));
        UserModel model = userService.pickCacheAuthenticateUser(model1, MD5Util.getMD5(getString("pwd").getBytes()));
        return JSONObject.toJSONString(model);
    }

    @GetMapping(value = "remove")
    public String remove() {
        UserModel model1 = new UserModel();
        model1.setName(getString("name"));
        UserModel model = userService.loadCacheAuthenticateUser(model1, MD5Util.getMD5(getString("pwd").getBytes()));
        return JSONObject.toJSONString(model);
    }
}
