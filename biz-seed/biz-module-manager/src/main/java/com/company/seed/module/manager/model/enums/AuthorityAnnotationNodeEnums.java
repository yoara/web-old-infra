package com.company.seed.module.manager.model.enums;

/**
 * Created by yoara on 2016/10/18.
 */
public enum AuthorityAnnotationNodeEnums {
    USER("普通用户",null),
    MANAGER("操作人员",null),
    SYSTEM("系统配置管理",null),
    ;
    private String desc;
    private AuthorityAnnotationNodeEnums parentNode;

    AuthorityAnnotationNodeEnums(String desc,AuthorityAnnotationNodeEnums parentNode) {
        this.desc = desc;
        this.parentNode = parentNode;
    }

    public String getDesc() {
        return desc;
    }

    public AuthorityAnnotationNodeEnums getParentNode() {
        return parentNode;
    }
}

