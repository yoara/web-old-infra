package com.company.seed.solr.local.core;

import org.apache.solr.client.solrj.SolrServer;


/**
 * SolrServer回调
 */
public interface SolrCallback {
	Object doInSolr(SolrServer solrServer) throws Exception;
}
