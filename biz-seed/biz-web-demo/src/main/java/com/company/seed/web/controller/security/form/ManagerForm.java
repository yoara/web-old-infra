package com.company.seed.web.controller.security.form;

import org.hibernate.validator.constraints.NotEmpty;
import org.yoara.framework.component.web.common.validation.ValidationForm;

/**
 * Created by yoara on 2016/8/3.
 */
public class ManagerForm implements ValidationForm {
    private String id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String phoneNumber;
    private boolean status;
    @NotEmpty
    private String positionId;

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
