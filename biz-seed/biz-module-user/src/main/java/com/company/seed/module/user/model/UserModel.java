package com.company.seed.module.user.model;

import com.company.seed.dao.basic.dataoperatelog.annotation.DataOperateLogEntity;
import com.company.seed.dao.basic.dataoperatelog.annotation.DataOperateLogField;
import com.company.seed.module.user.solr.local.searcher.UserBaseSearcher;
import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;

/**
 * Created by yoara on 2016/3/2.
 */
@DataOperateLogEntity(method = "selectUserById")
public class UserModel implements Serializable{
    //spring-data-solr 不能使用父类的字段做数据映射
    @Field(UserBaseSearcher.ID)
    private String id;
    @Field(UserBaseSearcher.NAME)
    private String name;
    private UserModel detail;

    @DataOperateLogField(field = "password",desc = "密码")
    private String password;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserModel getDetail() {
        return detail;
    }

    public void setDetail(UserModel detail) {
        this.detail = detail;
    }
}
