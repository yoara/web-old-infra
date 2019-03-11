package org.yoara.framework.component.notify.core.mail.impl;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.yoara.framework.component.notify.core.mail.MailSender;
import org.yoara.framework.component.notify.core.mail.MailSenderPool;
import org.yoara.framework.component.notify.core.mail.MailService;
import org.yoara.framework.component.notify.core.mail.entity.Email;
import org.yoara.framework.component.notify.core.mail.entity.MailTemplate;
import org.yoara.framework.core.util.CommonDateUtil;
import org.yoara.framework.core.util.VelocityUtil;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("componentNotifyMailService")
public class MailServiceImpl implements MailService{
	private static final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);
	private static final int TRYCOUNT = 3;
	/**
	 * 发送邮件
	 * @param template 邮件模板
	 */
	public void send(MailTemplate template){
		send(template, null);
	}

	/**
	 * 发送邮件
	 * @param template 邮件模板
	 * @param params 参数模板
	 */
	public void send(MailTemplate template, Map<String, Object> params){
		if(template == null) {
			throw new RuntimeException("邮件模板不存在!");
		}
		Map<String, Object> model = new HashMap<>();
		makeModel(model,params);						//手动参数

		//渲染subject
		if(StringUtils.hasLength(template.getSubject())){
			template.setSubject(VelocityUtil.evaluate(model, template.getSubject()));
		}

		//渲染contentTemplateUrl
		if(template.getTempUrl() != null){
			template.setContent(VelocityUtil.templateMerge(template.getTempUrl(), model));
		}

		try {
			sendEmailMessageOfAttachedFileAndSimpleText(template.adapterEmail(), true);
		} catch (MessagingException e) {
			log.error("send mail fail! content="+template.getContent(), e);
		}
	}

	/**
	 * 设置模型参数
	 * @return
	 */
	private void makeModel(Map<String, Object> model , Map<String, Object> params){
		model.put("currentTime", CommonDateUtil.formatDateToyyyy_MM_dd_HH_mm_ss(new Date()));//当前时间
		if(params != null && params.size()>0){
			model.putAll(params);
		}
	}

	/**
	 * 带附件并且html格式邮件发送,HTML格式的消息
	 */
	private void sendEmailMessageOfAttachedFileAndSimpleText(
			Email email, boolean isHtmlText) throws MessagingException {
		MailSenderPool pool = MailSenderPool.getMailSenderList(email.getSender());
		//发送池size
		int poolSize = pool.getMailSenderList().size();
		//重试次数
		int tryCount = TRYCOUNT;
		//轮询种子
		int seedInt = RandomUtils.nextInt(poolSize)+tryCount;
		while(tryCount>0){
			MailSender emailSender = pool.getMailSenderList().get(seedInt%poolSize);
			try{
				MimeMessage message = getMimeMessage(emailSender,emailSender.getSenderCode(),email,isHtmlText);
				emailSender.send(message);
				return;
			}catch (MessagingException me){
				log.error(me.getMessage(),me);
				tryCount--;
				seedInt--;
			}
		}
	}

	private MimeMessage getMimeMessage(JavaMailSenderImpl emailSender,String senderCode,
				Email email, boolean isHtmlText) throws MessagingException {
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setFrom(senderCode);
		//helper.setValidateAddresses(true);
		helper.setText(email.getContent(), isHtmlText);
		helper.setSubject(email.getSubject());
		helper.setTo(email.getReceivers());
		if(ArrayUtils.isNotEmpty(email.getCc())){
			helper.setCc(email.getCc());
		}
		if(ArrayUtils.isNotEmpty(email.getBcc())){
			helper.setBcc(email.getCc());
		}
		helper.setSentDate(new Date());

		for(File file : email.getAttachFile()){
			FileSystemResource fileSystemResource = new FileSystemResource(file);
			helper.addAttachment(file.getName(), fileSystemResource);
		}
		return message;
	}
}
