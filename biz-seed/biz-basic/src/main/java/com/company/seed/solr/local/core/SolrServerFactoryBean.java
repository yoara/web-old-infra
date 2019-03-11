package com.company.seed.solr.local.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * 集成Spring
 */
public class SolrServerFactoryBean implements FactoryBean<SolrServerFactory>, InitializingBean, ApplicationListener<ApplicationEvent> {
	private final static Logger logger = LoggerFactory.getLogger(SolrServerFactoryBean.class);
	private SolrServerFactory solrServerFactory;

	public void onApplicationEvent(ApplicationEvent event) {
	}

	public void afterPropertiesSet() throws Exception {
		solrServerFactory = new SolrServerFactory();
		logger.info("******** SolrServerFactory init finished ***********");
	}

	public SolrServerFactory getObject() throws Exception {
		if (solrServerFactory == null) {
			afterPropertiesSet();
		}
		return this.solrServerFactory;
	}

	public Class<?> getObjectType() {
		return this.solrServerFactory == null ? SolrServerFactory.class : this.solrServerFactory.getClass();
	}

	public boolean isSingleton() {
		return true;
	}
}
