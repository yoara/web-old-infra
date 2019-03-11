package com.company.seed.solr.local.core;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ShardParams;
import org.apache.solr.common.params.SolrParams;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

/**
 * 支持Shards CRUD, uniqueKey 默认为id
 */
public class ShardSolrServer extends HttpSolrServer {
	private static final long serialVersionUID = -7228909984319129451L;
	private Map<Integer, SolrServer> shards = Collections.synchronizedMap(new HashMap<Integer, SolrServer>());
	private String shardsUrl;
	private String uniqueKeyField = "id";
	
	public ShardSolrServer(String solrServerUrl) throws MalformedURLException {
		super(solrServerUrl);
	}
	
	public ShardSolrServer(String solrServerUrl, String shardsUrl, String uniqueKeyField) throws MalformedURLException {
		super(solrServerUrl);
		if(shardsUrl != null) {
			this.shardsUrl = shardsUrl;
			String[] shardUrls = shardsUrl.split(",");
			for(int i = 0; i < shardUrls.length; i++) {
				String shardUrl = shardUrls[i];
				if(!shardUrl.startsWith("http://")) {
					shardUrl = "http://" + shardUrl;
					HttpSolrServer shardSolrServer = new HttpSolrServer(shardUrl);
					shards.put(i, shardSolrServer);
				}
			}
		}
		if(uniqueKeyField != null) {
			this.uniqueKeyField = uniqueKeyField;
		}
	}
	
	@Override
	public UpdateResponse add(Collection<SolrInputDocument> docs) throws SolrServerException, IOException {
		Map<Integer, SolrServer> updatedSolrServers = new HashMap<Integer, SolrServer>();
		for(SolrInputDocument solrInputDocument : docs) {
			int shardId = solrInputDocument.get(uniqueKeyField).hashCode() % shards.size();
			SolrServer shardServer = shards.get(shardId);
			shardServer.add(solrInputDocument);
			updatedSolrServers.put(shardId, shardServer);
		}
		for(SolrServer solrServer : updatedSolrServers.values()) {
			solrServer.commit(false, false);
		}
		//return null for the time being;
		return null;
	}

	@Override
	public QueryResponse query(SolrParams params) throws SolrServerException {
		if(shardsUrl != null && !shardsUrl.trim().equals("")) {
			((SolrQuery)params).setParam(ShardParams.SHARDS, shardsUrl);
		}
		return super.query(params);
	}
	
	@Override
	public UpdateResponse deleteById(List<String> ids) throws SolrServerException, IOException {
		Map<Integer, SolrServer> updatedSolrServers = new HashMap<Integer, SolrServer>();
		for(String id : ids) {
			int shardId = id.hashCode() % shards.size();
			SolrServer shardServer = shards.get(shardId);
			shardServer.deleteById(id);
			updatedSolrServers.put(shardId, shardServer);
		}
		for(SolrServer solrServer : updatedSolrServers.values()) {
			solrServer.commit(false, false);
		}
		//return null for the time being;
		return null;
	}
	
	@Override
	public UpdateResponse deleteByQuery(String query) throws SolrServerException, IOException {
		for(Iterator<SolrServer> it = shards.values().iterator(); it.hasNext(); ) {
			SolrServer solrServer = it.next();
			solrServer.deleteByQuery(query);
		}
		for(SolrServer solrServer : shards.values()) {
			solrServer.commit(false, false);
		}
		//return null for the time being;
		return null;
	}
	
	@Override
	public UpdateResponse optimize(boolean waitFlush, boolean waitSearcher, int maxSegments) throws SolrServerException, IOException {
		for(Iterator<SolrServer> it = shards.values().iterator(); it.hasNext(); ) {
			SolrServer solrServer = it.next();
			solrServer.optimize(waitFlush, waitSearcher, maxSegments);
		}
		for(SolrServer solrServer : shards.values()) {
			solrServer.commit(false, false);
		}
		//return null for the time being;
		return null;
	}
	
	/**
	 * 已经在update操作中commit 
	 */
	@Override
	public UpdateResponse commit() throws SolrServerException, IOException {
		return null;
	}
	
	@Override
	public UpdateResponse commit(boolean waitFlush, boolean waitSearcher) throws SolrServerException, IOException {
		return null;
	}
}
