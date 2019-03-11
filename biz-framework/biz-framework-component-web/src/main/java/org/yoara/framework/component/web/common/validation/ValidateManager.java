package org.yoara.framework.component.web.common.validation;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yoara.framework.component.web.bean.JsonCommonCodeEnum;
import org.yoara.framework.component.web.common.validation.pool.ValidationPoolBean;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 缓存切面,拦截标记Cache注解的方法
 * Created by yoara on 2017/1/3.
 */
@Aspect
@Component
public class ValidateManager {
    @Resource
    protected ValidationPoolBean validationPoolBean;

    @Around(value = "@annotation(rm)")
    public Object validate(ProceedingJoinPoint joinPoint, RequestMapping rm) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if(args==null||args.length==0){
            return joinPoint.proceed();
        }
        for(Object arg:args){
            if(arg instanceof ValidationForm){
                ValidationResult result = validationPoolBean.validationParam((ValidationForm)arg);
                if(result.hasError()){
                    Map<String, Object> jsonResult = new HashMap<>();
                    jsonResult.put("status", JsonCommonCodeEnum.E0002.getStatus());
                    jsonResult.put("message", result.getErrorMessages().get(0).getMessage());
                    return JSON.toJSONString(jsonResult);
                }
            }
        }
        return joinPoint.proceed();
    }
}
