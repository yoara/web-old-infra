package com.company.seed.solr.local;

import com.company.seed.solr.local.core.facet.SolrFacetBean;
import com.company.seed.solr.local.core.group.SolrGroupBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.DisMaxParams;
import org.apache.solr.common.params.GroupParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询条件
 */
public class SearchCriteria{
	private String queryType;// 查询类型
	private String defType;// 默认查询类型
	private String fl;// 返回字段列表
	private String pf;// edismax属性，匹配字段
	private String qf;// edismax属性，查询字段（同于df）以及设置权重
	private String bf;// edismax属性，函数权重

	//group查询
	private GroupQueryInfo groupQueryInfo;

	//facet查询
	private FacetQueryInfo facetQueryInfo;

	//高亮字段
	private List<String> highlightFields;

	//拼装q查询参数的StringBuffer
	private StringBuffer query = new StringBuffer();
	public enum OpeType{
		AND,OR,NOT
	}
	/** 新增q 查询参数 **/
	public void addQuery(String field,String value,OpeType opeType){
		addQuery(field + ":" + value,opeType);
	}
	/** 新增q 查询参数 **/
	public void addQuery(String qStr,OpeType opeType){
		if(query.length()!=0){
			query.append(" ").append(opeType).append(" ");
		}
		query.append(qStr);
	}

	//拼装fq查询参数的List
	private List<String> filterQuery = new ArrayList<>();
	/** 新增fq 查询参数 **/
	public void addFilterQuery(String field,String value){
		addFilterQuery(field + ":" + value);
	}
	/** 新增fq 查询参数 **/
	public void addFilterQuery(String fqStr){
		filterQuery.add(fqStr);
	}

	//拼装sort的List
	private List<SolrQuery.SortClause> sortList = new ArrayList<>();
	/** 新增sort 参数 **/
	public void addSort(String field,SolrQuery.ORDER order){
		sortList.add(SolrQuery.SortClause.create(field,order));
	}

	public SolrQuery getSolrQuery() {
		SolrQuery solrQuery = new SolrQuery();

		solrQuery.setQuery(query.length()>0?query.toString():"*:*");
		filterQuery.forEach(solrQuery::addFilterQuery);
		sortList.forEach(solrQuery::addSort);

		if (null != defType) {
			solrQuery.setParam("defType", defType);
		}
		if (null != queryType) {
			solrQuery.setRequestHandler(queryType);
		}

		if (null != pf) {
			solrQuery.setParam(DisMaxParams.PF, pf);
		}

		if (null != qf) {
			solrQuery.setParam(DisMaxParams.QF, qf);
		}

		if (null != bf) {
			solrQuery.setParam(DisMaxParams.BF, bf);
		}

		if (null != fl) {
			solrQuery.setParam(CommonParams.FL, fl);
		}

		if (facetQueryInfo != null) {
			facetQueryInfo.makeFacetIndex(solrQuery);
		}
		if (groupQueryInfo!=null) {
			groupQueryInfo.makeGroupIndex(solrQuery);
		}

		return solrQuery;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getDefType() {
		return defType;
	}

	public void setDefType(String defType) {
		this.defType = defType;
	}

	public String getFl() {
		return fl;
	}

	public void setFl(String fl) {
		this.fl = fl;
	}

	public String getQf() {
		return qf;
	}

	public void setQf(String qf) {
		this.qf = qf;
	}

	public String getPf() {
		return pf;
	}

	public void setPf(String pf) {
		this.pf = pf;
	}

	public String getBf() {
		return bf;
	}

	public void setBf(String bf) {
		this.bf = bf;
	}

	public GroupQueryInfo getGroupQueryInfo() {
		return groupQueryInfo;
	}

	public FacetQueryInfo getFacetQueryInfo() {
		return facetQueryInfo;
	}

	/**
	 * 生成组查询参数
	 * @param groupFields group分组字段，无分组字段则传入null
	 * @param groupLimit group每组返回个数，默认返回1个
	 */
	public void setGroupQueryInfo(List<SolrGroupBean> groupFields, Integer groupLimit) {
		if(groupFields==null){
			return ;
		}
		this.groupQueryInfo = new GroupQueryInfo(groupFields,groupLimit);
	}

	/**
	 * 生成面查询参数
	 * @param facetFields 面聚合字段，无分组字段则传入null
	 */
	public void setFacetQueryInfo(List<SolrFacetBean> facetFields) {
		if(facetFields==null){
			return;
		}
		this.facetQueryInfo = new FacetQueryInfo(facetFields);
	}

	public class GroupQueryInfo{
		private List<SolrGroupBean> groupFields;// 多个group查询
		private Integer groupLimit;// 每组group返回个数限制
		private boolean groupNGroups;

		private GroupQueryInfo(List<SolrGroupBean> groupFields, Integer groupLimit) {
			this.groupFields = groupFields;
			this.groupLimit = groupLimit;
			this.groupNGroups = true;
		}

		public Map<String,SolrGroupBean> getGroupFieldsMap() {
			if(groupFields==null){
				return null;
			}
			Map<String,SolrGroupBean> beanMap = new HashMap<>();
			for(SolrGroupBean bean:groupFields){
				beanMap.put(bean.getGroupField(),bean);
			}
			return beanMap;
		}

		public List<SolrGroupBean> getGroupFields() {
			return groupFields;
		}

		public Integer getGroupLimit() {
			return groupLimit;
		}

		public boolean isGroupNGroups() {
			return groupNGroups;
		}

		public void setGroupNGroups(boolean groupNGroups) {
			this.groupNGroups = groupNGroups;
		}

		private void makeGroupIndex(SolrQuery solrQuery) {
			solrQuery.add(GroupParams.GROUP, "true");
			if (groupFields != null) {
				for (SolrGroupBean groupField : groupFields) {
					solrQuery.add(GroupParams.GROUP_FIELD, groupField.getGroupField());
				}
			}
			if(groupLimit!=null && groupLimit>0){
				solrQuery.add(GroupParams.GROUP_LIMIT, String.valueOf(groupQueryInfo.getGroupLimit()));
			}
			if(groupNGroups){
				solrQuery.add(GroupParams.GROUP_TOTAL_COUNT, String.valueOf(groupNGroups));
			}
		}
	}

	public class FacetQueryInfo{
		private List<SolrFacetBean> facetFields;
		/**
		 *
		 * @param facetFields 面查询的字段
         */
		private FacetQueryInfo(List<SolrFacetBean> facetFields) {
			this.facetFields = facetFields;
		}

		private void makeFacetIndex(SolrQuery solrQuery) {
			solrQuery.setFacet(true);
			for (SolrFacetBean facetField : facetFields) {
				solrQuery.addFacetField(facetField.getFacetField());
				String fq = facetField.createFacetQuery();
				if(StringUtils.isNotEmpty(fq)){
					solrQuery.addFacetQuery(facetField.createFacetQuery());
				}
			}
		}

		public List<SolrFacetBean> getFacetFields() {
			return facetFields;
		}
	}

	public List<String> getHighlightFields() {
		return highlightFields;
	}

	public void setHighlightFields(List<String> highlightFields) {
		this.highlightFields = highlightFields;
	}
}
