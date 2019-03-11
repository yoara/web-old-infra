package com.company.seed.module.manager.model.enums;

/**
 * Created by yoara on 2016/4/25.
 * 权限注解，所有权限在此处定义
 */
public enum AuthorityAnnotationEnums{
    /** 用户 **/
    USER_EDIT("新增用户",AuthorityAnnotationNodeEnums.USER),
    USER_SELECT("查询用户",AuthorityAnnotationNodeEnums.USER),

    /** 操作人员 **/
    MANAGER_EDIT("新增用户",AuthorityAnnotationNodeEnums.MANAGER),
    MANAGER_SELECT("查询用户",AuthorityAnnotationNodeEnums.MANAGER),

    /** 系统配置 **/
    SYSTEM_EDIT("系统配置修改",AuthorityAnnotationNodeEnums.SYSTEM),
    SYSTEM_SELECT("系统配置查询",AuthorityAnnotationNodeEnums.SYSTEM),
    ;
    private String desc;
    private AuthorityAnnotationNodeEnums nodeAuth;

    AuthorityAnnotationEnums(String desc,AuthorityAnnotationNodeEnums nodeAuth) {
        this.desc = desc;
        this.nodeAuth = nodeAuth;
    }

    public String getDesc() {
        return desc;
    }

    public AuthorityAnnotationNodeEnums getNodeAuth() {
        return nodeAuth;
    }
}
