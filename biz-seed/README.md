# biz-seed
默认JDK1.8+，基于springmvc+spring+mybatis的种子工程。集成常用中间件。


## 通用组件定义全部在biz-basic模块中
### 一、通用的配置文件
### 二、通用组件</h1>
 - 1.msyql组件，支持分页、数据源切换
 - 2.redis组件
 - 3.activemq
 - 4.solr

## 设计理念
 - 1.biz-basic配置各类中间件服务所需要的信息，并提供初始的工具类。
 - 2.不同的module用不同的maven模块。
 - 3.上层的web层可集合所需的module打包项目。
 - 4.单独的module亦可通过SOA平台注册服务。继承biz-basic-server

## 其他说明
 - 1.暂无