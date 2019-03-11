package org.yoara.framework.component.logger.monitor.mq.product.interceptor;

import javax.jms.Message;
import javax.jms.MessageProducer;

/**
 * 处理器弗雷
 * 执行顺序例如:
 * handler1 exc
 * handler2 exc
 * method exc
 * handler2 exc
 * handler1 exc
 */
public class Handler {

    public Handler(){

    }

    /**
     * 排序数值越小优先执行
     */
    protected int order(){
        return 0;
    }

    /**
     * mq发送消息前执行
     */
    public Handler beforeInvoke(MessageProducer producer, Message message){
        return this;
    }

    /**
     * mq发送消息后执行
     */
    public Handler afterInvoke(MessageProducer producer, Message message){
        return this;
    }

    /**
     * mq发送消息异常
     */
    public Handler afterInvoke(MessageProducer producer, Message message, Throwable e)  throws Throwable {
        throw e;
    }


}
