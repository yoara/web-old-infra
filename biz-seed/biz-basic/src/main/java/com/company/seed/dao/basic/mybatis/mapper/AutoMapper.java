package com.company.seed.dao.basic.mybatis.mapper;

import com.company.seed.dao.basic.mybatis.BaseDao;
import com.company.seed.dao.basic.mybatis.mapper.annotation.CountSql;
import com.company.seed.dao.basic.mybatis.mapper.exception.AutoMapperException;
import com.company.seed.model.Pagination;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 映射mapper查询，自动实现dao层接口类
 */
public class AutoMapper<T> implements InvocationHandler {
    private final Class<T> mapperInterface;
    private BaseDao baseDao;

    public AutoMapper(BaseDao baseDao,Class<T> mapperInterface) {
        this.baseDao = baseDao;
        this.mapperInterface = mapperInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodCommand command = new MethodCommand(method);
        command.makeParam(method,args);
        switch (command.getMethodType()){
            case DELETE:
                return baseDao.delete(command.getStatementName(),command.getParam());
            case INSERT:
                return baseDao.insert(command.getStatementName(),command.getParam());
            case UPDATE:
                return baseDao.update(command.getStatementName(),command.getParam());
            case SELECT:
                if(command.isPageAble()){
                    baseDao.selectList(command.getStatementName(),command.getCountSql(),
                            command.getParam(),command.getPage());
                    if(List.class.isAssignableFrom(method.getReturnType())){
                        return command.getPage().getItems();
                    }else if(Pagination.class.isAssignableFrom(method.getReturnType())){
                        return command.getPage();
                    }
                }else if(List.class.isAssignableFrom(method.getReturnType())){
                    return baseDao.selectList(command.getStatementName(),command.getParam());
                }else{
                    return baseDao.selectOne(command.getStatementName(),command.getParam());
                }
        }
        return null;
    }

    private class MethodCommand{
        private Pagination page;
        private Map<String,Object> param;
        private boolean pageAble;
        private SqlCommandType methodType;
        private String statementName;
        private String countSql;
        private Class parameterType;

        public Pagination getPage() {
            return page;
        }

        public boolean isPageAble() {
            return pageAble;
        }

        public String getCountSql() {
            return countSql;
        }

        public SqlCommandType getMethodType() {
            return methodType;
        }

        public String getStatementName() {
            return statementName;
        }

        public MethodCommand(Method method) {
            Configuration configuration = baseDao.getSqlSession().getConfiguration();
            //设置主查询映射
            statementName = mapperInterface.getName() + "." + method.getName();
            CountSql annotation = method.getAnnotation(CountSql.class);
            //设置页总量查询映射
            if(annotation!=null){
                countSql = StringUtils.isEmpty(annotation.value())
                        ?statementName:mapperInterface.getName()+ "." + annotation.value();
            }else{
                countSql = statementName;
            }
            MappedStatement ms = configuration.getMappedStatement(statementName);
            if(ms==null){
                throw new AutoMapperException("mapper配置未定义该方法");
            }
            methodType = ms.getSqlCommandType();
            parameterType = ms.getParameterMap().getType();
        }

        public void makeParam(Method method,Object[] args) {
            if(args!=null){
                Parameter[] params = method.getParameters();
                param = new HashMap<>();
                for(int i=0;i<args.length;i++){
                    Object arg = args[i];
                    Parameter parameter = params[i];
                    if(arg instanceof Pagination){
                        page = (Pagination)arg;
                        pageAble = true;
                    }else{
                        param.put(parameter.getName(),arg);
                    }
                }
            }
        }

        public Object getParam() {
            if(param!=null&&param.size()==1){
                Object onlyParam = param.values().toArray()[0];
                if (parameterType.equals(Map.class)) {
                    param = (Map) onlyParam;
                } else {
                    return onlyParam;
                }
            }
            return param;
        }

        @Override
        public String toString() {
            return "MethodCommand{" +
                    "pageAble=" + pageAble +
                    ", methodType=" + methodType +
                    ", statementName='" + statementName + '\'' +
                    ", countSql='" + countSql + '\'' +
                    '}';
        }
    }
}
