package com.company.seed.dao.basic.mybatis.mapper;

import com.company.seed.dao.basic.mybatis.BaseDao;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.Proxy;

import static org.springframework.util.Assert.notNull;

/**
 *
 * @param <T>
 */
public class AutoMapperBean<T> implements InitializingBean,FactoryBean<T> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Class<T> mapperInterface;
    private BaseDao baseDao;

    public AutoMapperBean(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public AutoMapperBean() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T getObject() throws Exception {
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(),
                new Class[] { mapperInterface },
                new AutoMapper<T>(baseDao,mapperInterface));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<T> getObjectType() {
        return this.mapperInterface;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSingleton() {
        return true;
    }


    public void setMapperInterface(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        notNull(this.mapperInterface, "Property 'mapperInterface' is required");
        notNull(this.baseDao, "Property 'baseDao' is required");

        Configuration configuration = baseDao.getSqlSession().getConfiguration();
        if (!configuration.hasMapper(this.mapperInterface)) {
            try {
                configuration.addMapper(this.mapperInterface);
            } catch (Exception e) {
                logger.error("Error while adding the mapper '" + this.mapperInterface + "' to configuration.", e);
                throw new IllegalArgumentException(e);
            } finally {
                ErrorContext.instance().reset();
            }
        }
    }
}
