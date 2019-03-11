package com.company.seed.solr.local.core.facet;

import org.apache.solr.client.solrj.response.FacetField;

import java.util.List;

public class SolrFacetBean{
    private int queryCount; //query 查询的结果数
    List<FacetField.Count> fieldCount;
    private String facetField;
    private String facetFiledValue;


    public String getFacetField() {
        return facetField;
    }

    public SolrFacetBean(String facetField) {
        this.facetField = facetField;
    }

    /**
     * 增加facet查询项，关联字段为构造行数中传入的field
     * @param value
     */
    public void addQuery(String value){
        facetFiledValue = value;
    }

    /**
     * 拼装facet查询语句
     * @return
     */
    public String createFacetQuery() {
        if(facetFiledValue==null){
            return null;
        }
        StringBuilder facetQuery = new StringBuilder();
        facetQuery.append(facetField);
        facetQuery.append(":");
        facetQuery.append(facetFiledValue);
        return facetQuery.toString();
    }

    public List<FacetField.Count> getFieldCount() {
        return fieldCount;
    }

    protected void setFieldCount(List<FacetField.Count> fieldCount) {
        this.fieldCount = fieldCount;
    }

    public int getQueryCount() {
        return queryCount;
    }

    public void setQueryCount(int queryCount) {
        this.queryCount = queryCount;
    }
}
