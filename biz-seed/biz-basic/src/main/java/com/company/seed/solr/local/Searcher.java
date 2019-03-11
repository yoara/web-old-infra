package com.company.seed.solr.local;


import org.apache.solr.common.SolrDocument;

import java.util.List;

/**
 *
 * @param <T>
 */
public interface Searcher<T> {
    //void reIndexAll();
    void save(T entity);

    void saveList(List<T> entityList);

    void update(T entity);

    void updateList(List<T> entityList);

    void deleteById(String id);

    void deleteByIds(List<String> ids);

    void deleteByQuery(final String query);

    void optimize(int maxSegments);

    void deleteAll();

    T solrDocumentToEntity(SolrDocument solrDocument) throws Exception;
}
