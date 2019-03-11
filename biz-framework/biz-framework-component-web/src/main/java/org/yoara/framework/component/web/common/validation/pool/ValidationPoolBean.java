package org.yoara.framework.component.web.common.validation.pool;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.beans.factory.InitializingBean;
import org.yoara.framework.component.web.common.validation.ValidationErrorMessage;
import org.yoara.framework.component.web.common.validation.ValidationForm;
import org.yoara.framework.component.web.common.validation.ValidationResult;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * Created by yoara on 2016/6/13.
 * JSR-303
 * @see org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
 */
public class ValidationPoolBean implements InitializingBean{
    private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator defaultValidator = factory.getValidator();
    private GenericObjectPool<Validator> pool;
    private boolean usePool = true;
    private int maxIdle = 10;
    private int maxTotal = 50;

    public void setUsePool(boolean usePool) {
        this.usePool = usePool;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new GenericObjectPool(new ValidationPooledObjectFactory(),new ValidationPoolConfig(maxIdle,maxTotal));
    }

    /**
     * 校验传递的参数
     * @param param
     */
    public ValidationResult validationParam(ValidationForm param){
        Validator validator = null;
        try{
            //获取校验器
            validator = getValidator();
            return checkParam(param, validator);
        }finally {
            close(validator);
        }
    }

    private ValidationResult checkParam(ValidationForm param, Validator validator) {
        ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<Object>> violations = validator.validate(param);
        if(violations!=null&&violations.size()>0){
            result.setHasError(true);
            for(ConstraintViolation violation : violations){
                ValidationErrorMessage errorMessage = new ValidationErrorMessage();
                errorMessage.setMessage(violation.getMessage());
                errorMessage.setProperty(violation.getPropertyPath().toString());
                errorMessage.setClassName(violation.getRootBeanClass().getSimpleName());
                result.addErrorMessage(errorMessage);
            }
        }
        return result;
    }

    private Validator getValidator(){
        Validator validator;
        try{
            if(usePool){
                validator = pool.borrowObject();
            }else{
                validator = defaultValidator;
            }
        }catch(Exception e){
            return null;
        }
        return validator;
    }

    private void close(Validator validator){
        if(usePool && validator!=null){
            pool.returnObject(validator);
        }
    }
}
