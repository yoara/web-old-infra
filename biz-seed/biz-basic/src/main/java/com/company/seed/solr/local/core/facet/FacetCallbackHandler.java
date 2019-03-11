package com.company.seed.solr.local.core.facet;

import com.company.seed.model.Pagination;
import com.company.seed.solr.local.SearchCriteria;
import com.company.seed.solr.local.core.SolrCallbackHandler;
import com.company.seed.solr.local.core.SolrTemplate;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.util.List;
import java.util.Map;

/**
 * Facet查询回调处理
 */
public class FacetCallbackHandler<T> extends SolrCallbackHandler<T> {

    public FacetCallbackHandler(SolrTemplate<T> solrTemplate, SearchCriteria criteria, Pagination<T> pagination) {
        super(solrTemplate, criteria, pagination);
    }
    
    public List<T> parseQueryResponse(QueryResponse response) {
        if (pagination instanceof FacetSolrPagination) {
            FacetSolrPagination<T> page = (FacetSolrPagination<T>)pagination;

            List<SolrFacetBean> facetFields = criteria.getFacetQueryInfo().getFacetFields();
            if (null != facetFields) {
                //facetQuery结果集
                Map<String, Integer> facetQuery = response.getFacetQuery();
                for (SolrFacetBean solrEnum : facetFields) {
                    Integer count = facetQuery.get(solrEnum.createFacetQuery());
                    solrEnum.setQueryCount(count == null ? 0 : count);
                    //facetField 结果集
                    FacetField ff = response.getFacetField(solrEnum.getFacetField());
                    solrEnum.setFieldCount(ff.getValues());
                }
            }
            page.setFacetFields(facetFields);
        }
        return super.parseQueryResponse(response);
    }

}
