package com.company.seed.solr.local.core;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * SolrServer工厂
 */
public class SolrServerFactory {
	private final static Logger logger = Logger.getLogger(SolrServerFactory.class);
	private Map<String, SolrServer> solrServers = Collections.synchronizedMap(new HashMap<String, SolrServer>());
	public SolrServerFactory() {
	}
	
	/**
	 * 获取CommonsHttpSolrServer
	 * 
	 * @param solrUrl
	 * @return SolrServer
	 */
	public SolrServer getCommonsHttpSolrServer(final String solrUrl) {
		if (!solrServers.containsKey(solrUrl)) {
			try {
				SolrServer solrServer = new HttpSolrServer(solrUrl);
				solrServers.put(solrUrl, solrServer);
				logger.info("SolrServer start succefully : " + solrUrl);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return solrServers.get(solrUrl);
	}
	
	/**
	 * 获取ShardSolrServer
	 * 
	 * @param solrServerUrl 主协调Server
	 * @param shardsUrl Shard Server
	 * @param uniqueKeyField 主键名
	 * @return
	 */
	public SolrServer getShardSolrServer(String solrServerUrl, String shardsUrl, String uniqueKeyField) {
		String serverKey = solrServerUrl + ":" + shardsUrl;
		if (!solrServers.containsKey(serverKey)) {
			try {
				SolrServer solrServer = new ShardSolrServer(solrServerUrl, shardsUrl, uniqueKeyField);
				solrServers.put(serverKey, solrServer);
				logger.info("SolrServer start succefully : " + serverKey);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return solrServers.get(serverKey);
	}
	
	/**
	 * 获取CloudSolrServer
	 * @return SolrServer
	 */
	public SolrServer getCloudSolrServer(final String zkHost,final String defaultCollection,final Integer zkClientTimeout,final Integer zkConnectTimeout) {
		String key = zkHost+defaultCollection;
		if (!solrServers.containsKey(key)) {
			try {
				CloudSolrServer solrServer = new CloudSolrServer(zkHost);
				solrServer.setDefaultCollection(defaultCollection);
				solrServer.setZkClientTimeout(zkClientTimeout);
				solrServer.setZkConnectTimeout(zkConnectTimeout);			
				solrServers.put(key, solrServer);
				logger.info("SolrServer start succefully : " + zkHost);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return solrServers.get(key);
	}

}
