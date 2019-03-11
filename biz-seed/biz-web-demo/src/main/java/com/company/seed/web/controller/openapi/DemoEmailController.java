package com.company.seed.web.controller.openapi;

import com.alibaba.fastjson.JSONObject;
import com.company.seed.web.controller.WebBaseController;
import com.company.seed.web.email.WebDemoEmailCode;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yoara.framework.component.notify.core.mail.entity.MailTemplate;
import org.yoara.framework.component.notify.facade.MessageFacade;
import org.yoara.framework.component.notify.factory.MailTemplateFactory;

import javax.annotation.Resource;

/**
 * Created by yoara on 2016/3/3.
 */
@Lazy
@RestController
@RequestMapping(value = "/openapi/email",produces = { "application/json;charset=UTF-8" })
public class DemoEmailController extends WebBaseController {
    @Resource
    private MessageFacade messageFacade;

    static{
        MailTemplate demoTemplate = new MailTemplate(
                WebDemoEmailCode.DEMO_MAIL_TEMPLATE,	        //模板枚举值
                "commonEmailSender",					        //发送者
                "email/emailOfDemo.html",						//模板路径
                "DEMO标题");										//标题
        MailTemplateFactory.addTemplate(demoTemplate, false);
    }

    @GetMapping(value = "demo")
    public String demo() {
        JSONObject json = new JSONObject();
        messageFacade.sendMail(MailTemplateFactory.getMailTemp(
                        WebDemoEmailCode.DEMO_MAIL_TEMPLATE,new String[]{"278703880@qq.com"},null,null));
        return json.toString();
    }
}
