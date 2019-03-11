package com.company.seed.web.email;

import org.yoara.framework.component.notify.core.mail.enums.MailTemplateCode;

/**
 * Created by yoara on 2016/5/13.
 */
public enum WebDemoEmailCode implements MailTemplateCode {
    DEMO_MAIL_TEMPLATE;
    @Override
    public String getTemplateCode() {
        return this.name();
    }
}
