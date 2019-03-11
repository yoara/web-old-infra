package com.company.seed.cache.data.aspect;

import com.alibaba.fastjson.JSON;
import com.company.seed.cache.RedisCache;
import com.company.seed.cache.data.annotation.DataCacheAble;
import com.company.seed.cache.data.annotation.DataCacheRemove;
import com.company.seed.threads.ThreadCachePool;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import org.yoara.framework.core.util.encrypt.MD5Util;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 缓存切面,拦截标记Cache注解的方法
 * Created by yoara on 2017/1/3.
 */
@Aspect
@Component
public class DataCacheManager {
    private static Logger logger = LoggerFactory.getLogger(DataCacheManager.class);
    //下标匹配器
    Pattern indexPattern = Pattern.compile("#\\d+\\.");

    @Resource(name="redisCache")
    private RedisCache redisCache;

    @Around(value = "@annotation(dataCacheAble)")
    public Object caching(ProceedingJoinPoint joinPoint, DataCacheAble dataCacheAble) throws Throwable {
        try{
            //判断是否有条件
            if(!checkCondition(joinPoint,dataCacheAble.condition())){
                return joinPoint.proceed();
            }
            String key = MD5Util.getMD5(makeKey(joinPoint,dataCacheAble.key()));
            Object cacheObject = redisCache.get(dataCacheAble.value(), key);
            //ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();
            //String[] parameterNames = pnd.getParameterNames(((MethodSignature) joinPoint.getSignature()).getMethod());
            if (cacheObject == null) {
                cacheObject = joinPoint.proceed();
                if(cacheObject==null){
                    return cacheObject;
                }
                if (dataCacheAble.time() > 0) {
                    int lifecycle = calculateLifecycle(dataCacheAble);
                    redisCache.put(dataCacheAble.value(), key, cacheObject, lifecycle);
                } else {
                    redisCache.put(dataCacheAble.value(), key, cacheObject);
                }
            }
            return cacheObject;
        }catch (Exception e){
            logger.error(e.getMessage());
            return joinPoint.proceed();
        }
    }

    @Around(value = "@annotation(dataCacheRemove)")
    public Object removing(ProceedingJoinPoint joinPoint, DataCacheRemove dataCacheRemove) throws Throwable {
        ThreadCachePool.execute(() -> {
            try{
                if(dataCacheRemove.allEntries()){
                    redisCache.removeAll(dataCacheRemove.value());
                }else{
                    String key = MD5Util.getMD5(makeKey(joinPoint,dataCacheRemove.key()));
                    redisCache.remove(dataCacheRemove.value(),key);
                }
            }catch (Exception e){
                logger.error(e.getMessage());
            }
        });
        return joinPoint.proceed();
    }

    /**
     * 校验条件匹配
     * @return
     */
    private boolean checkCondition(ProceedingJoinPoint joinPoint, String[] conditions) {
        Object[] args = joinPoint.getArgs();
        if(args==null||args.length==0){
            return true;
        }
        if(conditions==null||conditions.length==0){
            return true;
        }
        for (String condition : conditions) {
            if(StringUtils.isEmpty(condition)){
                continue;
            }
            Matcher matcher = indexPattern.matcher(condition);
            if(!matcher.find()||matcher.start()!=0){
                logger.warn("wrong condition expression set['"+condition+"']");
            }else{
                int matcherEnd = matcher.end();
                int index = Integer.parseInt(condition.substring(1,matcherEnd-1));
                Expression exp = getExpression(condition, matcherEnd);
                if(!exp.getValue(args[index],Boolean.class)){
                    return false;
                }
            }
        }
        return true;
    }

    private Expression getExpression(String matcher, int matcherEnd) {
        //计算参数
        String expression = matcher.substring(matcherEnd);
        //解析表达式
        ExpressionParser parser = new SpelExpressionParser();
        return parser.parseExpression(expression);
    }

    /**
     * 生成用于缓存的key
     */
    private byte[] makeKey(ProceedingJoinPoint joinPoint, String[] keys) {
        Object[] args = joinPoint.getArgs();
        if(args==null||args.length==0){
            return argsToString(args).getBytes();
        }
        if(keys!=null&&keys.length>0){
            Object[] keyArgs = new Object[keys.length];
            for (int i=0;i<keys.length;i++){
                String key = keys[i];
                if(StringUtils.isEmpty(key)){
                    continue;
                }
                Matcher matcher = indexPattern.matcher(key);
                if(!matcher.find()||matcher.start()!=0){
                    logger.warn("wrong key expression set['"+key+"']");
                }else{
                    int matcherEnd = matcher.end();
                    int index = Integer.parseInt(key.substring(1,matcherEnd-1));
                    keyArgs[i] = getExpression(key,matcherEnd).getValue(args[index]);
                }
            }
            return argsToString(keyArgs).getBytes();
        }
        return argsToString(args).getBytes();
    }

    /**
     * 将参数转换成string
     */
    private String argsToString(Object[] args) {
        if (null == args || args.length == 0) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(toJsonString(args[0]));
        for (int i = 1; i < args.length; i++) {
            builder.append("#").append(toJsonString(args[i]));
        }
        return builder.toString();
    }

    private String toJsonString(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof Number || obj instanceof String) {
            return obj.toString();
        } else {
            return JSON.toJSONString(obj);
        }
    }
    /**
     * 换算秒
     * @return 缓存生命周期
     */
    private int calculateLifecycle(DataCacheAble dataCacheAble) {
        int lifecycle = 0;
        if (dataCacheAble.time() > 0) {
            switch (dataCacheAble.timeUnit()) {
                case second:
                    lifecycle = dataCacheAble.time();
                    break;
                case minute:
                    lifecycle = dataCacheAble.time() * 60;
                    break;
                case hour:
                    lifecycle = dataCacheAble.time() * 60 * 60;
                    break;
                default:
                    break;
            }
        }
        if (lifecycle >= 24 * 60 * 60) {
            return 24 * 60 * 60;
        }
        return lifecycle;
    }

}
