package com.company.seed.solr.local.core;

import com.company.seed.model.Pagination;
import com.company.seed.solr.local.AbstractSearcher;
import com.company.seed.solr.local.SearchCriteria;
import com.company.seed.solr.local.Searcher;
import com.company.seed.solr.local.core.facet.FacetCallbackHandler;
import com.company.seed.solr.local.core.group.GroupCallbackHandler;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.util.ArrayList;
import java.util.List;

/**
 * 集成Spring
 */
public class SolrTemplate<T> {
	private final static Logger logger = Logger.getLogger(SolrTemplate.class);
	private SolrServer solrServer;
	private Searcher<T> searcher;
	public SolrTemplate(AbstractSearcher<T> searcher) {
		this.searcher = searcher;
		this.solrServer = searcher.getSolrServer();
	}

	public Object execute(SolrCallback solrCallback) {
		try {
			return solrCallback.doInSolr(solrServer);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 根据SolrQuery查询
	 * @param criteria
	 * @param pagination
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Pagination<T> query(final SearchCriteria criteria, final Pagination<T> pagination) {
		return (Pagination<T>) execute(new SolrCallbackHandler<>(this, criteria, pagination));
	}

	@SuppressWarnings("unchecked")
	public Pagination<T> queryGroup(SearchCriteria criteria, Pagination<T> pagination) {
		return (Pagination<T>) execute(new GroupCallbackHandler<>(this, criteria, pagination));
	}

    @SuppressWarnings("unchecked")
    public Pagination<T> queryFacet(SearchCriteria criteria, Pagination<T> pagination) {
	    return (Pagination<T>) execute(new FacetCallbackHandler<>(this, criteria, pagination));
	}

	public List<T> solrDocumentsToEntities(SolrDocumentList solrDocumentList) throws Exception {
		List<T> entities = new ArrayList<>();
		if (null != solrDocumentList) {
			for(SolrDocument solrDocument : solrDocumentList) {
				entities.add(searcher.solrDocumentToEntity(solrDocument));
			}
		}
		return entities;
	}
	
	/**
	 * 提交数据
	 */
	@SuppressWarnings("serial")
	public void save(final T entity) {
		saveList(new ArrayList<T>() {{
			add(entity);
		}});
	}

	/**
	 * 批量保存
	 * @param entities
	 */
	public void saveList(final List<T> entities) {
		execute(solrServer -> {
			solrServer.addBeans(entities);
			solrServer.commit(false, false, true);
            logger.info("save entities successfully :" + entities.size());
            return null;
        });
	}

	/**
	 * 提交数据采用批量提交
	 */
	public void updateList(final List<T> entities) {
		saveList(entities);
	}
	
	@SuppressWarnings("serial")
	public void update(final T entity) {
		updateList(new ArrayList<T>() {{
			add(entity);
		}});
	}
	
	/**
	 * 根据ID删除索引
	 * @param id
	 */
	@SuppressWarnings("serial")
	public void deleteById(final String id) {
		deleteByIds(new ArrayList<String>() {{
			add(id);
		}});
	}
	
	/**
	 * 根据ID批量删除索引
	 */
	public void deleteByIds(final List<String> ids) {
		execute(solrServer -> {
            solrServer.deleteById(ids);
            solrServer.commit(false, false);
            logger.info("deleteByIds successfully : " + ids.size());
            return null;
        });
	}
	
	/**
	 * 根据query批量删除索引
	 */
	public void deleteByQuery(final String query) {
		execute(solrServer -> {
            solrServer.deleteByQuery(query);
            solrServer.commit(false, false);
            logger.info("deleteByQuery successfully : " + query);
            return null;
        });
	}
	
	/**
	 * 删除所有元素
	 */
	public void deleteAll() {
		execute(solrServer -> {
            solrServer.deleteByQuery("*:*");
            solrServer.commit(false, false);
            logger.info("deleteAll successfully");
            return null;
        });
	}
	
	/**
	 * 优化solr数据存储结构
	 * 
	 */
	public void optimize(final int maxSegments) {
		execute(solrServer -> {
            logger.info("Optimize solr start ...");
            long now = System.currentTimeMillis();
            solrServer.optimize(false, false, maxSegments);
            logger.info("Optimize solr end, time = " + (System.currentTimeMillis() - now));
            return null;
        });
	}
}
