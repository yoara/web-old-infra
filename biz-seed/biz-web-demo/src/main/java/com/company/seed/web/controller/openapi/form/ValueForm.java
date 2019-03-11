package com.company.seed.web.controller.openapi.form;

import org.hibernate.validator.constraints.Email;
import org.yoara.framework.component.web.common.validation.ValidationForm;

import javax.validation.constraints.Min;

/**
 * Created by yoara on 2016/6/13.
 */
public class ValueForm implements ValidationForm {
    @Min(1)
    private int valueInt;
    @Email
    private String email;

    public int getValueInt() {
        return valueInt;
    }

    public void setValueInt(int valueInt) {
        this.valueInt = valueInt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
