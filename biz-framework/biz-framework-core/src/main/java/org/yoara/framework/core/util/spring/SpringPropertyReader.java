package org.yoara.framework.core.util.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.support.PropertiesLoaderSupport;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Properties;

/**
 * Created by yoara on 2016/5/12.
 */
public class SpringPropertyReader implements ApplicationContextAware, BeanFactoryPostProcessor {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private static Properties properties=new Properties();

    public static void setProperties(String key, String value){
        properties.put(key, value);
    }

    public static String getProperty(String propertyName){
        return getProperty(propertyName, null);
    }

    public static String getProperty(String propertyName, String defValue){
        String val = properties.getProperty(propertyName);
        if(StringUtils.hasText(val)){
            return val;
        }
        return defValue;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        try {
            // get the names of BeanFactoryPostProcessor
            String[] postProcessorNames = beanFactory
                    .getBeanNamesForType(PropertiesLoaderSupport.class, true, false);

            for (String ppName : postProcessorNames) {
                // get the specified PropertiesLoaderSupport
                PropertiesLoaderSupport beanProcessor =
                        beanFactory.getBean(ppName, PropertiesLoaderSupport.class);

                // get the method mergeProperties
                // in class PropertiesLoaderSupport
                Method mergeProperties = PropertiesLoaderSupport.class.
                        getDeclaredMethod("mergeProperties");
                // get the props
                mergeProperties.setAccessible(true);
                Properties props = (Properties) mergeProperties.invoke(beanProcessor);

                // get the method convertProperties
                // in class PropertyResourceConfigurer
                if(beanProcessor instanceof PropertyResourceConfigurer){
                    Method convertProperties = PropertyResourceConfigurer.class.
                            getDeclaredMethod("convertProperties", Properties.class);
                    // convert properties
                    convertProperties.setAccessible(true);
                    convertProperties.invoke(beanProcessor, props);
                }
                properties.putAll(System.getProperties());
                properties.putAll(props);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}