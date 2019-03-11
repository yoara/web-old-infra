package org.yoara.framework.component.logger.monitor.mq;

import org.apache.activemq.command.*;
import org.apache.commons.lang.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;
import org.yoara.framework.component.logger.support.digest.DigestLogBuilder;
import org.yoara.framework.component.logger.support.logger.LoggerBuilder;
import org.yoara.framework.component.logger.support.logger.LoggerExceptionUtil;
import org.yoara.framework.component.logger.support.webaccess.RequestContextHolder;

@Aspect
public class MqConsumerMonitor implements InitializingBean, Ordered {

    protected Logger logger;

    @Override
    public void afterPropertiesSet() throws Exception {
        logger = LoggerBuilder.builderMqLogger(MqConsumerMonitor.class);
    }

    @Pointcut("this(javax.jms.MessageListener)")
    public void receive(){
    }

    @Around("receive()")
    public Object monitor(ProceedingJoinPoint pjp) throws Throwable {
        if(ArrayUtils.isEmpty(pjp.getArgs())){  //参数为空，则直接跳过
            return pjp.proceed();
        }
        //框架方法，跳过
        if(pjp.getSignature().getDeclaringType().getName().contains("org.springframework")){
            return pjp.proceed();
        }
        return normalMq(pjp);   //常规消息
    }

    private Object normalMq(ProceedingJoinPoint pjp) throws Throwable {
        Object eventObj = null;
        ActiveMQMessage msg;
        try {
            msg = (ActiveMQMessage) pjp.getArgs()[0];
            if (msg instanceof ActiveMQObjectMessage) {
                eventObj = ((ActiveMQObjectMessage) pjp.getArgs()[0]).getObject();
            } else if (msg instanceof ActiveMQTextMessage) {
                eventObj = ((ActiveMQTextMessage) pjp.getArgs()[0]).getText();
            } else if (msg instanceof ActiveMQMapMessage) {
                eventObj = ((ActiveMQMapMessage) pjp.getArgs()[0]).getContentMap();
            } else if (msg instanceof ActiveMQBlobMessage) {
                //文件和大数据不进行记录
            } else if (msg instanceof ActiveMQBytesMessage) {
                //文件和大数据不进行记录
            } else if (msg instanceof ActiveMQStreamMessage) {
                //文件和大数据不进行记录
            }
        }catch(Exception e){
            LoggerExceptionUtil.error(logger,"MqConsumerMonitor logger fail!", e);
            return pjp.proceed();
        }

        RequestContextHolder.init();
        RequestContextHolder.getDigestContext().setRequestId(msg.getJMSMessageID());
        msg.getDestination().getDestinationTypeAsString();
        return log(pjp, msg.getDestination().getPhysicalName(),
                msg.getJMSMessageID(), msg.getDestination().getDestinationTypeAsString(), eventObj);
    }

    private Object log(ProceedingJoinPoint pjp, String mqKey, String msgId, String type, Object param) throws Throwable {
        Long startTime = System.currentTimeMillis();
        Object result;
        try {
            result = pjp.proceed();
            try {
                logger.info(DigestLogBuilder.buildMqDigest(mqKey, msgId, startTime, "Consumer", type, true, "-", param));
            }catch(Exception e){
                LoggerExceptionUtil.error(logger,"MqConsumerMonitor logger fail!", e);
            }
        }catch (Throwable e){
            try{
                logger.info(DigestLogBuilder.buildMqDigest(mqKey, msgId, startTime, "Consumer", type, false, e.toString(), param));
            }catch(Exception ex){
                LoggerExceptionUtil.error(logger,"MqConsumerMonitor logger fail!", ex);
            }
            throw e;
        }
        return result;
    }


    @Override
    public int getOrder() {
        return 100;
    }


}
