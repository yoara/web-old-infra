package org.yoara.framework.component.logger.monitor.mq;

import javax.jms.Destination;
import javax.jms.Message;

class MqProductionContext {

    private Long startTime;

    private Destination destination;

    private Message message;

    private Object eventObj;

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public Object getEventObj() {
        return eventObj;
    }

    public void setEventObj(Object eventObj) {
        this.eventObj = eventObj;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
