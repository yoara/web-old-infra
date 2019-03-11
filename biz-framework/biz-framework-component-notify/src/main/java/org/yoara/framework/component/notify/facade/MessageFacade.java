package org.yoara.framework.component.notify.facade;

import org.yoara.framework.component.notify.core.mail.entity.MailTemplate;

import java.util.Map;


public interface MessageFacade {
	/**
	 * 发送邮件
	 * @param temp 邮件模板
	 * @param params 参数模板
	 */
	void sendMail(MailTemplate temp, Map<String, Object> params);
	
	/**
	 * 发送邮件
	 * @param temp 邮件模板
	 */
	void sendMail(MailTemplate temp);
}
