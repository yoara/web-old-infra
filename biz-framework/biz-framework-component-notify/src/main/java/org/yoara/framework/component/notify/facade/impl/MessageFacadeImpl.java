package org.yoara.framework.component.notify.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yoara.framework.component.notify.core.mail.MailService;
import org.yoara.framework.component.notify.core.mail.entity.MailTemplate;
import org.yoara.framework.component.notify.facade.MessageFacade;

import java.util.Map;

@Service("componentNotifyMessageFacade")
public class MessageFacadeImpl implements MessageFacade {
	@Autowired
	private MailService mailService;

	@Override
	public void sendMail(MailTemplate template, Map<String, Object> params) {
		mailService.send(template, params);
	}

	@Override
	public void sendMail(MailTemplate template) {
		mailService.send(template);
	}
}
