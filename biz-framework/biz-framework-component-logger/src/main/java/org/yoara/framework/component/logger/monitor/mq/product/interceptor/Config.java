package org.yoara.framework.component.logger.monitor.mq.product.interceptor;


import java.util.ArrayList;
import java.util.List;

/**
 * 简易监听者模式定义处理器
 */
public abstract class Config {

    private List<Handler> handlers = new ArrayList<Handler>();

    public List<Handler> getHandlers(){
        return handlers;
    }

    /**
     * 获得handler
     */
    public Handler getHandler(Class<Handler> clazz){

        if(clazz == null)   { return null;}

        for(Handler handler : handlers){

            if(clazz == handler.getClass()) { return handler; }
        }

        return null;
    }


    public void register(Handler handler){
        handlers.add(handler);
    }
}


