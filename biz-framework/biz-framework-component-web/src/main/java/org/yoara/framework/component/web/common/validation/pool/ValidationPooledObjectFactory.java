package org.yoara.framework.component.web.common.validation.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * Created by yoara on 2016/6/13.
 */
public class ValidationPooledObjectFactory extends BasePooledObjectFactory<Validator> {
    private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    @Override
    public Validator create() throws Exception {
        return factory.getValidator();
    }

    @Override
    public PooledObject wrap(Validator obj) {
        return new DefaultPooledObject<>(obj);
    }
}
