package org.yoara.framework.component.logger.monitor.mq.product.interceptor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class DefaultConfig extends Config implements ApplicationContextAware, InitializingBean {

    private AbstractApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        if (applicationContext instanceof AbstractApplicationContext) {
            this.applicationContext = (AbstractApplicationContext) applicationContext;
        } else {
            throw new RuntimeException(
                    "applicationContext is not instantce of AbstractApplicationContext!");
        }
    }


    public void afterPropertiesSet() throws Exception {

        DefaultListableBeanFactory factory = (DefaultListableBeanFactory)applicationContext.getBeanFactory();
        Map<String, Handler> handlerMap = factory.getBeansOfType(Handler.class);
        for(Handler handler : handlerMap.values()){
            register(handler);
        }
        Collections.sort(getHandlers(), new SortByOrder());
    }

    class SortByOrder implements Comparator<Handler> {
        public int compare(Handler o1, Handler o2) {
            if (o1.order() > o2.order())
                return 1;
            return 0;
        }
    }
}
