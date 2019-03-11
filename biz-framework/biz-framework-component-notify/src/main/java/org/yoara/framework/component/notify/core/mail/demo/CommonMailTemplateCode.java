package org.yoara.framework.component.notify.core.mail.demo;

import org.yoara.framework.component.notify.core.mail.enums.MailTemplateCode;

/**
 * Created by yoara on 2016/5/12.
 */
public enum CommonMailTemplateCode implements MailTemplateCode {
    //通用邮件模板
    MAIL_COMMON_TEMPLATE;
    @Override
    public String getTemplateCode() {
        return this.name();
    }
}
