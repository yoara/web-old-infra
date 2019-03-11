package com.company.seed.module.user.solr.local.searcher;

import java.util.HashMap;

/**
 * Created by yoara on 2016/3/2.
 */
public class UserSearcherForZk extends UserBaseSearcher {
	private HashMap<String,String> zkHostS;
	private HashMap<String, String> solrNames;

	public void setZkHostS(HashMap<String, String> zkHostS) {
		this.zkHostS = zkHostS;
	}

	public void setSolrNames(HashMap<String, String> solrNames) {
		this.solrNames = solrNames;
	}
	
	@Override
	protected String getSolrName() {
		if(solrNames!=null && solrNames.get(getDataSource())!=null){
    		return solrNames.get(getDataSource());
    	}
		return super.defaultSolrName;
	}
	
    @Override
	protected String getZkHost() {
    	if(zkHostS!=null && zkHostS.get(getDataSource())!=null){
    		return zkHostS.get(getDataSource());
    	}
		return super.getZkHost();
	}
}
