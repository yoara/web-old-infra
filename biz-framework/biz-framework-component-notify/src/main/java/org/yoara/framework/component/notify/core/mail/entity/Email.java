package org.yoara.framework.component.notify.core.mail.entity;

import java.io.File;

/**
 * Created by yoara on 2016/5/12.
 */
public class Email {

    /**
     * 邮件发送者
     */
    private String sender;

    /**
     * 邮件接收者
     */
    private String[] receivers;

    /**
     * 邮件抄送人
     */
    private String[] cc;

    /**
     * 邮件暗抄送人
     */
    private String[] bcc;

    /**
     * Email发送的内容
     */
    private String content;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件附件
     */
    private File[] attachFile;

    public String getSender(){
        return sender;
    }

    public void setSender(String sender)
    {
        this.sender = sender;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public File[] getAttachFile(){
        if(attachFile==null){
            return new File[]{};
        }
        return attachFile;
    }

    public void setAttachFile(File[] attachFile)
    {
        this.attachFile = attachFile;
    }

    public String[] getReceivers()
    {
        return receivers;
    }

    public void setReceivers(String[] receivers)
    {
        this.receivers = receivers;
    }

    public String[] getCc()
    {
        return cc;
    }

    public void setCc(String[] cc)
    {
        this.cc = cc;
    }

    public String[] getBcc()
    {
        return bcc;
    }

    public void setBcc(String[] bcc)
    {
        this.bcc = bcc;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
