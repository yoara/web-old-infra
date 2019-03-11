package com.company.seed.module.user.mq;

/**
 * Created by yoara on 2016/3/4.
 */
public class MqConstantsModuleUser {
    private static final String Q_PREFIX = "BIZ_MODULE_USER_";
    //插入User新记录的queue消息队列
    public static final String USRE_INSERT_QUEUE = Q_PREFIX + "USRE_INSERT_QUEUE";

    //================================================================================

    //插入User新记录的topic订阅队列
    public static final String USRE_INSERT_TOPIC = Q_PREFIX + "USRE_INSERT_TOPIC";
}
