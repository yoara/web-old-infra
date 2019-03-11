package org.yoara.framework.component.web.common.validation;

import java.io.Serializable;

/**
 * Created by yoara on 2016/6/13.
 */
public class ValidationErrorMessage implements Serializable{
    private String message;
    private String property;
    private String className;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
