package com.company.seed.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.company.seed.common.CommonConstants;
import com.company.seed.module.manager.model.ManagerModel;
import com.company.seed.module.manager.model.enums.AuthorityAnnotationEnums;
import com.company.seed.module.manager.service.AuthorityService;
import com.company.seed.module.manager.service.ManagerService;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yoara.framework.component.web.bean.validate.LoginForm;
import org.yoara.framework.core.util.encrypt.MD5Util;

import javax.annotation.Resource;
import java.util.Set;

/**
 * Created by yoara on 2016/3/3.
 */
@Lazy
@RestController
@RequestMapping(value = "/login/manager",produces = { "application/json;charset=UTF-8" })
public class ManagerLoginController extends WebBaseController {
    @Resource
    private ManagerService managerService;
    @Resource
    private AuthorityService authorityService;

    @GetMapping(value = "loginin")
    public String loginIn(LoginForm form) {
        JSONObject json = new JSONObject();
        String passwordMD5 = MD5Util.getMD5(form.getPassword().getBytes());
        ManagerModel manager = managerService.authenticateManager(form.getUserName(),passwordMD5);
        if(manager==null){
            json.put("result", "fail");
            json.put("msg", "wrong username/password");
            return json.toString();
        }
        Set<AuthorityAnnotationEnums> authSet = authorityService.queryAuthLeafsByManagerId(manager.getId());
        manager.setAuthSet(authSet);
        getRequest().getSession().setAttribute(CommonConstants.CURRENT_LOGIN_MANAGER, manager);

        json.put("result", "ok");
        return json.toString();
    }

    @GetMapping(value = "loginout")
    public String loginOut() {
        getRequest().getSession().removeAttribute(CommonConstants.CURRENT_LOGIN_MANAGER);
        JSONObject json = new JSONObject();
        json.put("result", "ok");
        return json.toString();
    }
}
