package org.yoara.framework.component.web.common.security.checkrequest.policy;

/**
 * Created by yoara on 2015/12/28.
 * 执行输入判断策略的父类
 */
public abstract class BaseCheckPolicy {
    protected static final String ERR_MSG_SQL = "1";
    protected static final String ERR_MSG_TAG = "2";
    protected static final String ERR_MSG_SENSITIVE = "3";
    /**执行策略**/
    public abstract void doCheck(CheckResult result);
}
