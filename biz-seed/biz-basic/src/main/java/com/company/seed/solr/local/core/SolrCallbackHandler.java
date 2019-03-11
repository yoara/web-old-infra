package com.company.seed.solr.local.core;

import com.company.seed.model.Pagination;
import com.company.seed.solr.local.SearchCriteria;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import java.util.List;
import java.util.Map;

/**
 * Group查询回调处理
 */
public class SolrCallbackHandler<T> implements SolrCallback {

    protected Pagination<T> pagination;
    protected SolrQuery solrQuery;
    protected SolrTemplate<T> solrTemplate;
    protected SearchCriteria criteria;

    public SolrCallbackHandler(SolrTemplate<T> solrTemplate, SearchCriteria criteria, Pagination<T> pagination){
        this.solrTemplate = solrTemplate;
		this.criteria = criteria;
        this.solrQuery = criteria.getSolrQuery();
        this.pagination = pagination;
    }

    public Object doInSolr(SolrServer solrServer) throws Exception {
        if(pagination != null && pagination.getSortField() != null) {
            solrQuery.addSort(pagination.getSortField(), pagination.getSort().equals(Pagination.Sort.DESC) ? ORDER.desc : ORDER.asc);
        }
        solrQuery.setStart(pagination.getCurrentPageStartIndex());
        solrQuery.setRows(pagination.getPageSize());
        QueryResponse response = solrServer.query(solrQuery,METHOD.POST);
        List<T> items = parseQueryResponse(response);
        pagination.setItems(items);
        return pagination;
    }

	public List<T> parseQueryResponse(QueryResponse response) {
		SolrDocumentList solrDocumentList = response.getResults();
		setHighlighting(solrDocumentList, response.getHighlighting());
		List<T> items = null;
		try {
			//当前还不能解析出泛型的具体类型
			//items = response.getBeans((Class)getClass().getGenericSuperclass());
			items = solrTemplate.solrDocumentsToEntities(solrDocumentList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		pagination.setRecordCount(Long.valueOf(response.getResults().getNumFound()).intValue());
		return items;
	}
	
	private void setHighlighting(SolrDocumentList solrDocumentList, Map<String, Map<String, List<String>>> highlightMap) {
		if (highlightMap == null) {
			return;
		}
		try {
			List<String> fieldList = criteria.getHighlightFields();
			if(fieldList==null||fieldList.size()==0){
				return;
			}
			solrDocumentList.stream().filter(doc -> doc.containsKey("id")).forEach(doc -> {
				String id = doc.getFieldValue("id").toString();
				Map<String, List<String>> resultMap = highlightMap.get(id);
				if (resultMap != null && resultMap.size() > 0) {
					fieldList.stream().filter(field -> doc.containsKey(field)).forEach(field -> {
						List<String> list = resultMap.get(field);
						if (list != null && list.size() > 0) {
							doc.setField(field, list.get(0));
						}
					});
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
