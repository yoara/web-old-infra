package com.company.seed.solr.local.core.group;

import java.util.List;
import java.util.Map;

public class SolrGroupBean<T> {
    private int docsCount; //query 查询的结果数
    private int ngCount; //query 查询的组数
    private String groupField;
    private Map<String,List<T>> groupResult;

    public SolrGroupBean(String groupField) {
        this.groupField = groupField;
    }

    public String getGroupField() {
        return groupField;
    }

    public int getNgCount() {
        return ngCount;
    }

    public void setNgCount(int ngCount) {
        this.ngCount = ngCount;
    }

    public Map<String, List<T>> getGroupResult() {
        return groupResult;
    }

    public void setGroupResult(Map<String, List<T>> groupResult) {
        this.groupResult = groupResult;
    }

    public int getDocsCount() {
        return docsCount;
    }

    public void setDocsCount(int docsCount) {
        this.docsCount = docsCount;
    }
}
