package com.company.seed.module.user.mq.listener.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by yoara on 2016/3/10.
 */
@Service("userInsertQueueListener")
public class UserInsertQueueListener implements MessageListener {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onMessage(Message message) {
        if (!(message instanceof TextMessage)) {
            logger.error("jsm pictureWaterCheckListener send msessage not objectMessage msg:" + message.toString());
            return;
        }
        try {
            System.out.println(((TextMessage)message).getText());
        } catch (JMSException e) {
            logger.error("userInsertListener error:" + e.getMessage() + "\tmsg:" + message.toString());
        }
    }
}
