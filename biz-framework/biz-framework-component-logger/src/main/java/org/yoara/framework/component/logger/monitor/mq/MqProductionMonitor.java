package org.yoara.framework.component.logger.monitor.mq;

import org.apache.activemq.command.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.yoara.framework.component.logger.monitor.mq.product.interceptor.Handler;
import org.yoara.framework.component.logger.support.digest.DigestLogBuilder;
import org.yoara.framework.component.logger.support.logger.LoggerBuilder;
import org.yoara.framework.component.logger.support.logger.LoggerExceptionUtil;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;

public class MqProductionMonitor extends Handler implements InitializingBean {

    protected Logger logger;

    ThreadLocal<MqProductionContext> ProductionParamsLocal = new ThreadLocal<MqProductionContext>();

    /**
     * 排序数值越小优先执行
     */
    protected int order(){
        return 100;
    }

    /**
     * mq发送消息前执行
     */
    public Handler beforeInvoke(MessageProducer producer, Message message){
        startLog(producer, message);
        return this;
    }

    /**
     * mq发送消息后执行
     */
    public Handler afterInvoke(MessageProducer producer, Message message) {
        endLog(true, "-");
        return this;
    }

    /**
     * mq发送消息异常
     */
    public Handler afterInvoke(MessageProducer producer, Message message, Throwable e)  throws Throwable {
        endLog(false, e.getMessage());
        return this;
    }

    public void startLog(MessageProducer producer, Message msg){

        Object enventObj = null;
        try {
            Destination destination = producer.getDestination();

            if (msg instanceof ActiveMQObjectMessage) {
                enventObj = ((ActiveMQObjectMessage) msg).getObject();
            } else if (msg instanceof ActiveMQTextMessage) {
                enventObj = ((ActiveMQTextMessage) msg).getText();
            } else if (msg instanceof ActiveMQMapMessage) {
                enventObj = ((ActiveMQMapMessage) msg).getContentMap();
            } else if (msg instanceof ActiveMQBlobMessage) {
                //文件和大数据不进行记录
                enventObj = null;
            } else if (msg instanceof ActiveMQBytesMessage) {
                //文件和大数据不进行记录
                enventObj = null;
            } else if (msg instanceof ActiveMQStreamMessage) {
                //文件和大数据不进行记录
                enventObj = null;
            }

            MqProductionContext params = new MqProductionContext();
            params.setStartTime(System.currentTimeMillis());
            params.setDestination(destination);
            params.setEventObj(enventObj);
            params.setMessage(msg);

            ProductionParamsLocal.set(params);
        }catch(Exception e){
            LoggerExceptionUtil.error(logger,"MqProductionMonitor logger fail!", e);
        }
    }

    public void endLog(Boolean success, String msg){
        try {
            MqProductionContext productionParams = ProductionParamsLocal.get();
            if(productionParams!=null) {
                ActiveMQDestination activeMQDestination = (ActiveMQDestination)productionParams.getDestination();
                ActiveMQMessage activeMQMessage = (ActiveMQMessage)productionParams.getMessage();
                Object eventObj = productionParams.getEventObj();
                logger.info(DigestLogBuilder.buildMqDigest(
                        activeMQDestination.getPhysicalName(), activeMQMessage.getJMSMessageID(),
                        productionParams.getStartTime(), "Production",
                        activeMQDestination.getDestinationTypeAsString(), success, msg, eventObj));
                ProductionParamsLocal.remove();
            }
        }catch(Exception ex){
            LoggerExceptionUtil.error(logger,"MqProductionMonitor logger fail!", ex);
        }
    }

    public void afterPropertiesSet() throws Exception {
        logger = LoggerBuilder.builderMqLogger(MqProductionMonitor.class);
    }
}

