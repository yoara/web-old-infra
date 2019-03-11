package com.company.seed.web.controller.openapi;

import com.alibaba.fastjson.JSONObject;
import com.company.seed.common.LoggerConstants;
import com.company.seed.web.controller.WebBaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yoara on 2016/3/3.
 */
@Lazy
@RestController
@RequestMapping(value = "/openapi",produces = { "application/json;charset=UTF-8" })
public class EchoController extends WebBaseController {
    private Logger sqlLogger = LoggerFactory.getLogger(LoggerConstants.SQLLOGGER);
    private Logger rootLogger = LoggerFactory.getLogger(EchoController.class);

    @GetMapping(value = "echo")
    public String echo() {
        sqlLogger.info("msg",Thread.getAllStackTraces());
        rootLogger.info("msg",Thread.getAllStackTraces());

        JSONObject json = new JSONObject();
        json.put("result", "do nothing but echo");
        return json.toString();
    }

    @GetMapping(value = "echo2")
    public ModelMap echo2(ModelMap map) {
        map.put("a","b");
        return map;
    }

    @GetMapping(value = "echo3")
    public Map echo3() {
        Map<String,String> map  = new HashMap<>();
        map.put("a","b");
        return map;
    }
}
