package com.company.seed.web.controller.openapi;

import com.alibaba.fastjson.JSONObject;
import com.company.seed.web.controller.WebBaseController;
import com.company.seed.web.controller.openapi.form.ValueForm;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yoara.framework.component.web.common.validation.ValidationResult;

/**
 * Created by yoara on 2016/3/3.
 */
@Lazy
@RestController
@RequestMapping(value = "/openapi",produces = { "application/json;charset=UTF-8" })
public class ValidationDemoController extends WebBaseController {
    @GetMapping(value = "validationdemo")
    public String echo(ValueForm form) {
        ValidationResult result = validationParam(form);
        JSONObject json = new JSONObject();
        json.put("result", result);
        return json.toString();
    }
}
