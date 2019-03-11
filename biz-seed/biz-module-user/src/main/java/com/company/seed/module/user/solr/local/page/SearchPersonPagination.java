package com.company.seed.module.user.solr.local.page;

import com.company.seed.solr.local.core.facet.FacetSolrPagination;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by yoara on 2016/3/2.
 */
public class SearchPersonPagination<T> extends FacetSolrPagination<T> {
	
	private static final long serialVersionUID = -3672709530740337683L;
	
	private Set<Integer> personIds = new HashSet<Integer>();

    public SearchPersonPagination() {
	      super();
	 }
	 
	 public SearchPersonPagination(int pageSize, int page) {
	       super(pageSize, page);
	 }
	 
	 public void setFacet(QueryResponse response) {
	        List<FacetField> facetFields = response.getFacetFields();
	        if (facetFields != null && facetFields.size() >= 1) {
	            List<Count> counts = facetFields.get(0).getValues();
	            if(counts != null && counts.size() > 0){
	            	for (Count count : counts) {
		                personIds.add(Integer.parseInt(count.getName()));
		            }
	            }
	            
            }
	    }

	public Set<Integer> getPersonIds() {
		return personIds;
	}

	public void setPersonIds(Set<Integer> personIds) {
		this.personIds = personIds;
	}

	 
}
