# biz-framework
默认JDK1.8+，与biz-seed配合使用，提供常用的工具组件支持

## 目前实现的通用组件如下
 - 1.core，包含通用的工具方法，特别的：SpringBeanFactory/SpringPropertyReader
 - 2.logger，访问日志打包组件：
    - 1.基于spring-mvc拦截器的webAccess
    - 2.基于aspect和cglib实现的mq日志拦截输出
 - 3.notify，消息通知组件
    - 1.实现email
 - 4.payment，支付组件
    - 1.实现基于微信，需要在classpath中配置payment_weixin_config.properties
    - 2.实现基于支付宝，需要在classpath中配置payment_alipay_config.properties
 - 5.web，web应用层组件
    - 1.定义通用返回类型及解析方式
    - 2.定义验证组件validate及相应注解
    - 3.定义桥接cache
    - 4.定义安全验证组件:param check/csrf/encrypt/
    - 5.定义通用filter：access/encode
    - 6.定义分布式session
    - 7.定义基类interceptor