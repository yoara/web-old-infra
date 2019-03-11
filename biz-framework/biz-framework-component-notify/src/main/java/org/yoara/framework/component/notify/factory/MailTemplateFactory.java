package org.yoara.framework.component.notify.factory;


import org.yoara.framework.component.notify.core.mail.demo.CommonMailTemplateCode;
import org.yoara.framework.component.notify.core.mail.entity.MailTemplate;
import org.yoara.framework.component.notify.core.mail.enums.MailTemplateCode;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息模板工厂
 * @author yoara
 */
public class MailTemplateFactory {
	
	private final static Map<MailTemplateCode, MailTemplate> mailTemplates = new HashMap();

	//邮件模板定义
	static{
		//通用邮件模板
		MailTemplate commonTemplate = new MailTemplate(
				CommonMailTemplateCode.MAIL_COMMON_TEMPLATE,	//模板枚举值
				"commonEmailSender",							//发送者
				"email/emailOfCommon.html",						//模板路径
				"通用标题");										//标题
		addTemplate(commonTemplate,false);
	}

	/**
	 *
	 * @param templateCode 模板枚举值
	 * @param receivers 接受者 不可为null
	 * @param cc 抄送者 可为null
	 * @param bcc 暗抄送者 可为null
     * @return 邮件模板对象
     */
	public static MailTemplate getMailTemp(MailTemplateCode templateCode,
					String[] receivers,String[] cc,String[] bcc){
		if(mailTemplates.get(templateCode)!=null){
			if(receivers==null){
				throw new IllegalArgumentException("mail receivers cannot be null");
			}
			MailTemplate temp = mailTemplates.get(templateCode).clone();
			temp.setReceivers(receivers);
			temp.setCc(cc);
			temp.setBcc(bcc);
			return temp;
		}
		return null;
	}

	/**
	 * @param template 		邮件模板对象
	 * @param force			是否强制更新
	 */
	public static void addTemplate(MailTemplate template,boolean force){
		if(mailTemplates.get(template.getTemplateCode())==null || force){
			mailTemplates.put(template.getTemplateCode(), template);
		}
	}

	/**
	 * @param templateCode 邮件模板枚举值
	 * @param sender		邮件发送者,参看spring邮件配置
	 * @param templateUrl	模板路径
	 * @param subject		标题
	 * @param force			是否强制更新
	 */
	public static void addTemplate(MailTemplateCode templateCode,
								   String sender, String templateUrl, String subject, boolean force){
		MailTemplate template = new MailTemplate(
				templateCode,	//模板枚举值
				sender,			//发送者
				templateUrl,	//模板路径
				subject);		//标题
		addTemplate(template,force);
	}
}
