package org.yoara.framework.component.notify.core.mail;

import org.yoara.framework.component.notify.core.mail.entity.MailTemplate;

import java.util.Map;


public interface MailService {
	
	/**
	 * 发送邮件
	 * @param temp 邮件模板
	 * @param params 参数模板
	 */
	public void send(MailTemplate temp, Map<String, Object> params);
	
	/**
	 * 发送邮件
	 * @param temp 邮件模板
	 */
	public void send(MailTemplate temp);
	
}
