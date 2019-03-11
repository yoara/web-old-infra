package org.yoara.framework.component.notify.core.mail;

import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Created by yoara on 2016/7/11.
 */
public class MailSender extends JavaMailSenderImpl{
    //发送者定义值
    private String senderCode;

    public String getSenderCode() {
        return senderCode;
    }

    public void setSenderCode(String senderCode) {
        this.senderCode = senderCode;
    }
}
