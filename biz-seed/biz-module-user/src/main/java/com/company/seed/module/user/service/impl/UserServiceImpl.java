package com.company.seed.module.user.service.impl;

import com.company.seed.cache.RedisCache;
import com.company.seed.cache.data.annotation.DataCacheAble;
import com.company.seed.cache.data.annotation.DataCacheRemove;
import com.company.seed.common.CacheTypeEnum;
import com.company.seed.dao.basic.dataoperatelog.annotation.DataOperateLogMethod;
import com.company.seed.dao.basic.dataoperatelog.enums.DataOperateTypeEnum;
import com.company.seed.model.Pagination;
import com.company.seed.module.user.dao.UserDao;
import com.company.seed.module.user.model.UserModel;
import com.company.seed.module.user.mq.sender.queue.UserInsertQueueSender;
import com.company.seed.module.user.mq.sender.topic.UserInsertTopicSender;
import com.company.seed.module.user.service.UserService;
import com.company.seed.module.user.solr.local.searcher.UserBaseSearcher;
import com.company.seed.module.user.solr.local.searcher.UserSearcherForHost;
import com.company.seed.module.user.solr.spring.UserRepositoryService;
import com.company.seed.service.BaseServiceImpl;
import com.company.seed.solr.local.SearchCriteria;
import com.company.seed.solr.local.core.group.GroupSolrPagination;
import com.company.seed.solr.local.core.group.SolrGroupBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yoara on 2016/3/3.
 */
@Service
public class UserServiceImpl extends BaseServiceImpl implements UserService {
    @Resource
    private UserDao userDaoMysql;
    @Resource
    private UserInsertQueueSender userInsertQueueSender;
    @Resource
    private UserInsertTopicSender userInsertTopicSender;
    @Resource
    private RedisCache redisCache;
    @Resource
    private UserSearcherForHost userSearcherForHost;
    @Resource
    private UserRepositoryService userRepositoryService;
    @Resource
    private SolrOperations solrTemplate;

    @Override
    public void insertUser(String userName) {
        UserModel user = new UserModel();
        user.setName(userName);
        userDaoMysql.insertUser(user);
        redisCache.put(CacheTypeEnum.USERINFO, "key", "value", 60);
        userInsertQueueSender.send("alert");
        userInsertTopicSender.send();
        userSearcherForHost.update(user);
    }

    @Override
    public UserModel selectUser(String param) {
        System.out.println(redisCache.get(CacheTypeEnum.USERINFO, "key"));
        {
            //普通solr查询
//            SearchCriteria criteria = new SearchCriteria();
//            criteria.addQuery(UserBaseSearcher.NAME,param, SearchCriteria.OpeType.AND);
//            Pagination<UserModel> userSolr = userSearcherForHost.query(criteria,new Pagination<>());
//            System.out.println(userSolr.getItems().get(0).getName());
        }

        {
            //solr group查询
            SearchCriteria criteria = new SearchCriteria();
            List<SolrGroupBean> fields = new ArrayList<>();
            SolrGroupBean<UserModel> nameEntity = new SolrGroupBean(UserBaseSearcher.NAME);
            SolrGroupBean<UserModel> idEntity = new SolrGroupBean(UserBaseSearcher.ID);
            fields.add(nameEntity);
            fields.add(idEntity);
            criteria.setGroupQueryInfo(fields,null);
            Pagination<UserModel> userSolr = userSearcherForHost.query(criteria,new GroupSolrPagination<>());
            System.out.println(userSolr.getItems().get(0).getName());
        }

        {
            //solr facet查询
//            SearchCriteria criteria = new SearchCriteria();
//            List<SolrFacetBean> facetFields = new ArrayList<>();
//            SolrFacetBean entity = new SolrFacetBean(UserBaseSearcher.NAME);
//            entity.addQuery("bbbb");
//            facetFields.add(entity);
//            criteria.setFacetQueryInfo(facetFields);
//            criteria.addQuery(UserBaseSearcher.NAME,param, SearchCriteria.OpeType.AND);
//            Pagination<UserModel> userSolr = userSearcherForHost.query(criteria,new FacetSolrPagination<>());
//            System.out.println(userSolr.getItems().get(0).getName());
        }

        {
            //spring-data-solr
//            List<UserModel> userSolrSpring = userRepositoryService.findByName(param);
//            System.out.println(userSolrSpring.get(0).getName());
//
//            SimpleHighlightQuery query = new SimpleHighlightQuery(new SimpleStringCriteria("name:"+param));
//            query.setHighlightOptions(new HighlightOptions());
//            HighlightPage<UserModel> page1 = solrTemplate.queryForHighlightPage(query, UserModel.class);

            Pageable pageRequest = new PageRequest(0,20);
            HighlightPage<UserModel> page =  userRepositoryService.findByName(param,pageRequest);
            System.out.println(page);
        }

        return userDaoMysql.selectUser();
    }

    @Override
    public UserModel authenticateUser(String userName, String passwordMD5) {
        return userDaoMysql.authenticateUser(userName,passwordMD5);
    }

    @Override
    @DataCacheAble(value=CacheTypeEnum.REDIS_CACHED_USERDEMO,condition = {"#0.name=='abc'"},key = {"#1.#this"})
    public UserModel pickCacheAuthenticateUser(UserModel user, String passwordMD5) {
        return userDaoMysql.authenticateUser(user.getName(),passwordMD5);
    }

    @Override
    @DataCacheRemove(value=CacheTypeEnum.REDIS_CACHED_USERDEMO)
    public UserModel loadCacheAuthenticateUser(UserModel user, String passwordMD5) {
        return userDaoMysql.authenticateUser(user.getName(),passwordMD5);
    }

    @Override
    public UserModel selectUserById(String id) {
        return userDaoMysql.selectUserById(id);
    }

    @Override
    public boolean updatePsw(UserModel model) {
        return userDaoMysql.updatePsw(model);
    }

    @Override
    @DataOperateLogMethod(value = DataOperateTypeEnum.UPDATE,operateEntity = UserModel.class)
    public boolean updatePsw(Map<String, Object> model) {
        return userDaoMysql.updatePsw(model);
    }
}
