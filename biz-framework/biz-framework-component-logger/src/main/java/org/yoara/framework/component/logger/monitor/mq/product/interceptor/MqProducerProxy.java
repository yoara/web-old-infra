package org.yoara.framework.component.logger.monitor.mq.product.interceptor;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Message;
import javax.jms.MessageProducer;
import java.lang.reflect.Method;

/**
 * 代理类，将JMSTemplate进行代理，插入handler勾子
 */
public class MqProducerProxy implements
        MethodInterceptor, ApplicationContextAware, InitializingBean {

    private AbstractApplicationContext applicationContext;

    private Config config;

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

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

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(JmsTemplate.class);
        enhancer.setCallback(this);

        for(String jmsTemplateKey : factory.getBeanNamesForType(JmsTemplate.class)){

            JmsTemplate jmsTemplate = (JmsTemplate) factory.getBean(jmsTemplateKey);
            factory.destroySingleton(jmsTemplateKey);
            JmsTemplate newJmsTemplate = (JmsTemplate) enhancer.create();

            BeanUtils.copyProperties(jmsTemplate, newJmsTemplate);

            factory.registerSingleton(jmsTemplateKey, newJmsTemplate);
        }
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        if("doSend".equals(method.getName())){
            if(objects.length == 2){
                if(config==null) {
                    throw new RuntimeException("handler config can not be null!");
                }

                Object result = null;
                try {
                    for (Handler handler : config.getHandlers()) {

                        //返回自己就继续执行，否则就停止
                        if (handler.beforeInvoke((MessageProducer) objects[0], (Message) objects[1]) != handler) {
                            return result;
                        }
                    }
                    result = methodProxy.invokeSuper(o, objects);

                    for (int i = config.getHandlers().size() - 1; i >= 0; i--) {

                        Handler handler = config.getHandlers().get(i);
                        if (handler.afterInvoke((MessageProducer) objects[0], (Message) objects[1]) != handler) {
                            return result;
                        }
                    }
                }catch(Throwable e){
                    for (int i = config.getHandlers().size() - 1; i >= 0; i--) {

                        Handler handler = config.getHandlers().get(i);
                        //返回自己就继续执行，否则就停止
                        if (handler.afterInvoke((MessageProducer) objects[0], (Message) objects[1], e) != handler) {
                            return result;
                        }
                    }
                    throw e;
                }

                return result;
            }
        }

        return methodProxy.invokeSuper(o, objects);
    }

}
