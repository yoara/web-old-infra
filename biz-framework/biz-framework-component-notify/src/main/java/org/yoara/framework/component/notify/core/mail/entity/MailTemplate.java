package org.yoara.framework.component.notify.core.mail.entity;

import org.yoara.framework.component.notify.core.SendLevel;
import org.yoara.framework.component.notify.core.mail.enums.MailTemplateCode;

import java.io.Serializable;

/**
 * 邮件模板
 *
 * @author yoara
 */
public class MailTemplate implements Serializable, Cloneable {

    private static final long serialVersionUID = 5740811485505326431L;

    /**
     * 模板编码
     */
    private MailTemplateCode templateCode;

    /**
     * 邮件内容模板
     */
    private String tempUrl;

    /**
     * 消息来源(仅对邮件消息可用,为发件人地址,服务端会检验这个地址在EDM的可用性)
     */
    private String sender;

    /**
     * 消息主题
     */
    private String subject;

    /**
     * 副标题
     */
    private String subhead;

    /**
     * 内容
     */
    private String content;

    /**
     * 接收人
     */
    private String[] receivers;
    /**
     * 抄送人
     */
    private String[] cc;
    /**
     * 暗抄送人
     */
    private String[] bcc;

    /**
     * 发送级别 //未实现
     */
    private SendLevel level = SendLevel.LOW;


    /**
     *
     * @param templateCode 模板枚举
     * @param sender 参看spring邮件配置
     * @param tempUrl 模板资源路径
     * @param subject 主标题
     */
    public MailTemplate(MailTemplateCode templateCode,
                        String sender, String tempUrl, String subject) {
        this.templateCode = templateCode;
        this.tempUrl = tempUrl;
        this.sender = sender;
        this.subject = subject;
    }

    public MailTemplate(MailTemplateCode templateCode,
                        String sender, String tempUrl, String subject, SendLevel level) {
        this(templateCode, sender, tempUrl, subject);
        this.level = level;
    }

    public MailTemplateCode getTemplateCode() {
        return templateCode;
    }

    public String getSubject() {
        return subject;
    }

    public MailTemplate setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getSubhead() {
        return subhead;
    }

    public MailTemplate setSubhead(String subhead) {
        this.subhead = subhead;
        return this;
    }

    public String getContent() {
        return content;
    }

    public MailTemplate setContent(String content) {
        this.content = content;
        return this;
    }

    public SendLevel getLevel() {
        return level;
    }

    public String[] getReceivers() {
        return receivers;
    }

    public MailTemplate setReceivers(String[] receivers) {
        this.receivers = receivers;
        return this;
    }

    public String getTempUrl() {
        return tempUrl;
    }

    public String[] getCc() {
        return cc;
    }

    public void setCc(String[] cc) {
        this.cc = cc;
    }

    public String[] getBcc() {
        return bcc;
    }

    public void setBcc(String[] bcc) {
        this.bcc = bcc;
    }

    /**
     * 获取邮件发送基础组装对象
     * @return
     */
    public Email adapterEmail() {
        Email mail = new Email();
        mail.setSender(sender);
        mail.setSubject(subject);
        mail.setContent(content);
        mail.setReceivers(receivers);
        mail.setCc(cc);
        mail.setBcc(bcc);
        return mail;
    }

    @Override
    public MailTemplate clone() {
        try {
            MailTemplate cloneTemp = (MailTemplate) super.clone();
            return cloneTemp;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
