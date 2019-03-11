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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yoara on 2016/3/3.
 */
@Lazy
@RestController
@RequestMapping(value = "/openapi/operate",produces = { "application/json;charset=UTF-8" })
public class DemoOperateController extends WebBaseController {
    @Resource
    private UserService userService;

    @GetMapping(value = "object")
    public String operateObject() {
        UserModel model = new UserModel();
        model.setId(getString("id"));
        model.setPassword( MD5Util.getMD5(getString("psw").getBytes()));
        userService.updatePsw(model);
        return JSONObject.toJSONString(model);
    }

    @GetMapping(value = "map")
    public String operateMap() {
        Map<String,Object> model = new HashMap<>();
        model.put("id",getString("id"));
        model.put("password",MD5Util.getMD5(getString("psw").getBytes()));
        userService.updatePsw(model);
        return JSONObject.toJSONString(model);
    }
}
