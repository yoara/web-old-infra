package com.company.seed.module.user.mq.sender.topic;

import com.company.seed.module.user.mq.MqConstantsModuleUser;
import org.apache.activemq.command.ActiveMQTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

/**
 * topic模式demo
 * 订阅模式：所有订阅该主题的接收端，会收到订阅后发送到该topic的所有消息，
 * 当所有的接收端都接受了A消息后，A消息会被移除队列
 * @author yoara
 */
@Service
public class UserInsertTopicSender {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private JmsTemplate jmsTemplate;
	@Autowired
    public UserInsertTopicSender(JmsTemplate jmsTemplate){
        this.jmsTemplate = jmsTemplate;
    }
	
	public void send(){
		ActiveMQTopic topic = new ActiveMQTopic(MqConstantsModuleUser.USRE_INSERT_TOPIC);
		logger.info("UserInsertTopicSender [message]");
		jmsTemplate.convertAndSend(topic, "message");
	}
}
