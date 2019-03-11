package org.yoara.framework.component.web.bean.validate.phone;

import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yoara on 2017/3/1.
 */
public class PhoneValidator implements ConstraintValidator<Phone,String> {
    @Override
    public void initialize(Phone constraintAnnotation) {

    }

    private final static Pattern p = Pattern.compile("^[1][0-9]{8}$");
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(StringUtils.isEmpty(value)){
            return false;
        }
        value = value.replaceAll("-","").replaceAll(" ","");
        Matcher m = p.matcher(value);
        return m.find();
    }
}

