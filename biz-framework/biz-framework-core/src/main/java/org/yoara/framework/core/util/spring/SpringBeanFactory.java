package org.yoara.framework.core.util.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Created by yoara on 2016/5/12.
 */
public class SpringBeanFactory implements ApplicationContextAware  {

    private static  AbstractApplicationContext applicationContext;

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

    public static <T> T getBean(Class<T> clazz){
        return applicationContext.getBean(clazz);
    }

    public static Object getBean(String name){
        return applicationContext.getBean(name);
    }


}
