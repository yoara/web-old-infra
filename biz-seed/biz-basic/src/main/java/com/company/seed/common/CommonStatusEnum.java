package com.company.seed.common;

/**
 * Created by yoara on 2016/4/25.
 * 通用状态枚举
 */
public enum CommonStatusEnum {
    ENABLED("生效"),
    DISABLED("失效"),
    DELETED("删除"),
    ;
    private String desc;
    CommonStatusEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
