package com.company.seed.solr.local.core.group;

import com.company.seed.model.Pagination;
import com.company.seed.solr.local.SearchCriteria;
import com.company.seed.solr.local.core.SolrCallbackHandler;
import com.company.seed.solr.local.core.SolrTemplate;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Group查询回调处理
 */
public class GroupCallbackHandler<T> extends SolrCallbackHandler<T> {
	private Logger logger = Logger.getLogger(this.getClass());
	
    public GroupCallbackHandler(SolrTemplate<T> solrTemplate, SearchCriteria criteria, Pagination<T> pagination) {
        super(solrTemplate, criteria, pagination);
    }
    
    public List<T> parseQueryResponse(QueryResponse response) {
        if (!(pagination instanceof GroupSolrPagination)) {
            return super.parseQueryResponse(response);
        }
        GroupSolrPagination<T> page = (GroupSolrPagination<T>)pagination;
        Map<String,SolrGroupBean> groupFieldsMap = criteria.getGroupQueryInfo().getGroupFieldsMap();
        page.setGroupFields(criteria.getGroupQueryInfo().getGroupFields());

        if (response.getGroupResponse() != null) {
            List<GroupCommand> values = response.getGroupResponse().getValues();
            page.setRecordCount(values.size());
            for (GroupCommand groupCommand : values) {
                String groupName = groupCommand.getName();
                SolrGroupBean groupBean = groupFieldsMap.get(groupName);
                groupBean.setDocsCount(groupCommand.getMatches());
                groupBean.setNgCount(groupCommand.getNGroups());
                Map<String,List<T>> groupResult = new HashMap<>();
                groupBean.setGroupResult(groupResult);
                for (Group group : groupCommand.getValues()) {
                    try {
                        List<T> result = solrTemplate.solrDocumentsToEntities(group.getResult());
                        groupResult.put(group.getGroupValue(),result);
                    } catch (Exception e) {
                        logger.error(e);
                    }
                }
            }
        }
        //group分页数据需要使用单独的方法
        return null;
    }
}
