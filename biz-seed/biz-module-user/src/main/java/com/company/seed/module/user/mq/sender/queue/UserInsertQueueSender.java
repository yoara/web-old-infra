package com.company.seed.module.user.mq.sender.queue;

import com.alibaba.fastjson.JSONObject;
import com.company.seed.module.user.mq.MqConstantsModuleUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserInsertQueueSender {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private JmsTemplate jmsTemplate;
	
	public void send(String alert){
		try{
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("alert", alert);
			logger.info("UserInsertQueueSender ["+jsonObj.toJSONString()+"]");
			jmsTemplate.convertAndSend(MqConstantsModuleUser.USRE_INSERT_QUEUE,jsonObj.toJSONString());
		}catch(Exception e){
			logger.error(e.getMessage());
		}
	}
}
