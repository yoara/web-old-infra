package com.company.seed.activemq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;

/**
 * 通用的错误监听器
 * Created by yoara on 2016/3/3.
 */
@Component("jmsExceptionListener")
public class JmsExceptionListener implements ExceptionListener{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
    public void onException( final JMSException e ){
    	logger.error(e.getMessage());
    }
}
