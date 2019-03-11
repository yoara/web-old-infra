package com.company.seed.module.manager.model;


import com.company.seed.module.manager.model.enums.AuthorityAnnotationNodeEnums;

import java.io.Serializable;

/**
 * Created by yoara on 2016/4/25.
 * 权限节点
 */
public class AuthorityNodeModel implements Serializable{
    private AuthorityAnnotationNodeEnums key;
    private String name;
    private AuthorityAnnotationNodeEnums parentKey;
    private int order;

    public AuthorityAnnotationNodeEnums getKey() {
        return key;
    }

    public void setKey(AuthorityAnnotationNodeEnums key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AuthorityAnnotationNodeEnums getParentKey() {
        return parentKey;
    }

    public void setParentKey(AuthorityAnnotationNodeEnums parentKey) {
        this.parentKey = parentKey;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "AuthorityNodeModel{" +
                "key=" + key +
                ", name='" + name + '\'' +
                ", parentKey=" + parentKey +
                '}';
    }
}
