package org.yoara.framework.component.web.common.security.checkrequest.annotation;

import java.lang.annotation.*;

/**
 * Created by yoara on 2015/12/28.
 * <p>请求输入字符校验，目前支持可配置的校验策略</p>
 * <ui>
 *     <li>·标签过滤 {@link Policy#TAG}</li>
 *     <li>·sql语句过滤 {@link Policy#SQL}</li>
 *     <li>·敏感词过滤 {@link Policy#SENSITIVE}</li>
 *     <li>·自定义过滤 {@link Policy#CUSTOM}</li>
 * </ui>
 * <p>处理方式</p>
 * <ui>
 *     <li>·替换,请注意由于request对象的parameter不能替换，因此将
 *     数据放在Attribute，键为PREFIX+paramKey中 {@link DealType#REPLACE}</li>
 *     <li>·返回错误提示 {@link DealType#ALERT}</li>
 *     <li>·返回json信息 {@link DealType#JSON}</li>
 *     <li>·抛出异常 {@link DealType#EXCEPTION}</li>
 * </ui>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckRequestParamAnnotation {
    /**校验策略**/
    enum Policy {TAG,SQL,SENSITIVE,CUSTOM}
    /**处理方式**/
    enum DealType {REPLACE,ALERT,JSON,EXCEPTION}
    /**默认不校验任何策略**/
    Policy[] policy();
    /**自定义策略**/
    String[] customPolicys() default {};
    /**配置需要校验的输入参数，参数若为空数组表示全部匹配*/
    String[] checkParams();
    /**返回策略**/
    DealType dealType() default DealType.JSON;

}
