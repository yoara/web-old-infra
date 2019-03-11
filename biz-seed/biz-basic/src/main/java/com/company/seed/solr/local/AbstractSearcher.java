package com.company.seed.solr.local;

import com.company.seed.common.ContextHolder;
import com.company.seed.model.Pagination;
import com.company.seed.solr.local.core.SolrServerFactory;
import com.company.seed.solr.local.core.SolrTemplate;
import com.company.seed.solr.local.core.facet.FacetSolrPagination;
import com.company.seed.solr.local.core.group.GroupSolrPagination;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.beans.Field;
import org.apache.solr.common.SolrDocument;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public abstract class AbstractSearcher<T> implements Searcher<T> {
    protected SolrServerFactory solrServerFactory;
    protected String solrServerUrl;
    protected String defaultSolrName;//默认查询solr对象名称
    protected Integer zkClientTimeout;//请求超时时间
    protected Integer zkConnectTimeout;//链接超时时间
    protected String defaultZkHost;//默认的zookkeep地址

    protected SolrTemplate solrTemplate;

    protected void initSolrTemplate(){
        solrTemplate = new SolrTemplate(this);
    }

    public SolrServer getSolrServer(){
        SolrServer solrServer = null;
        if(getZkHost()!=null){//当使用zkhost的时候 就视为使用了solrcloud
            solrServer = solrServerFactory.getCloudSolrServer(
                    getZkHost(), getSolrName(), zkClientTimeout, zkConnectTimeout);
        }else{
            if(solrServerUrl == null) {
                throw new RuntimeException("solrServerUrl must be specified");
            }
            solrServer = solrServerFactory.getCommonsHttpSolrServer(solrServerUrl);
        }
        return solrServer;
    }

    public void setSolrServerFactory(SolrServerFactory solrServerFactory) {
        this.solrServerFactory = solrServerFactory;
    }

    public void setSolrServerUrl(String solrServerUrl) {
        this.solrServerUrl = solrServerUrl;
    }

    public void setDefaultSolrName(String defaultSolrName) {
        this.defaultSolrName = defaultSolrName;
    }

    public void setZkClientTimeout(Integer zkClientTimeout) {
        this.zkClientTimeout = zkClientTimeout;
    }

    public void setZkConnectTimeout(Integer zkConnectTimeout) {
        this.zkConnectTimeout = zkConnectTimeout;
    }

    public void setDefaultZkHost(String defaultZkHost) {
        this.defaultZkHost = defaultZkHost;
    }

    protected String getDataSource(){
        return ContextHolder.getDataSource().name().toLowerCase();
    }
    public Pagination<T> query(SearchCriteria criteria, Pagination<T> pagination) {
        if (criteria.getGroupQueryInfo()!=null){
            if(!(pagination instanceof GroupSolrPagination)){
                throw new RuntimeException("wrong pagination ,group query need GroupSolrPagination");
            }
            return solrTemplate.queryGroup(criteria, pagination);
        }
        if (criteria.getFacetQueryInfo()!=null) {
            if(!(pagination instanceof FacetSolrPagination)){
                throw new RuntimeException("wrong pagination ,facet query need FacetSolrPagination");
            }
            return solrTemplate.queryFacet(criteria,pagination);
        }
        Pagination<T> page = solrTemplate.query(criteria, pagination);
        return page;
    }

    @Override
    public void save(T entity) {
        solrTemplate.save(entity);
    }

    @Override
    public void saveList(List<T> entityList) {
        solrTemplate.saveList(entityList);
    }

    @Override
    public void update(T entity) {
        solrTemplate.update(entity);
    }

    @Override
    public void updateList(List<T> entityList) {
        solrTemplate.updateList(entityList);
    }

    @Override
    public void deleteById(String id) {
        solrTemplate.deleteById(id);
    }

    @Override
    public void deleteByIds(List<String> ids) {
        solrTemplate.deleteByIds(ids);
    }

    @Override
    public void deleteByQuery(String query) {
        solrTemplate.deleteByQuery(query);
    }

    @Override
    public void optimize(int maxSegments) {
        solrTemplate.optimize(maxSegments);
    }

    @Override
    public void deleteAll() {
        solrTemplate.deleteAll();
    }

    protected String getZkHost(){
        return defaultZkHost;
    }

    protected String getSolrName(){
        return defaultSolrName;
    }

    @Override
    public T solrDocumentToEntity(SolrDocument solrDocument) throws Exception {
        if(solrDocument == null){
            return null;
        }

        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType)((Class) genType).getGenericSuperclass()).getActualTypeArguments();
        T entity = (T) ((Class) params[0]).newInstance();
        java.lang.reflect.Field[] fields = ((Class) params[0]).getDeclaredFields();
        if(fields!=null){
            for(java.lang.reflect.Field field:fields){
                Annotation annotation = field.getAnnotation(Field.class);
                if(annotation!=null){
                    try {
                        field.setAccessible(true);
                        field.set(entity,solrDocument.getFieldValue(((Field)annotation).value()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return entity;
    }
}
