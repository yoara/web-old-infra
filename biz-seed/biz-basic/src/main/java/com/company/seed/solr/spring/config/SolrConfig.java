package com.company.seed.solr.spring.config;

import com.company.seed.common.ConfigConstants;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.yoara.framework.core.util.spring.SpringPropertyReader;

/**
 * Created by yoara on 2016/8/9.
 */
@Configuration
@EnableSolrRepositories(basePackages = ConfigConstants.SOLR_PACKAGE)
public class SolrConfig {
    @Bean
    public SolrServer solrServer() {
        HttpSolrServer solrServer = new HttpSolrServer(SpringPropertyReader.getProperty("module.solr.user.hosturl"));
        return solrServer;
    }

    @Bean
    public SolrOperations solrTemplate() {
        return new SolrTemplate(solrServer());
    }

}
