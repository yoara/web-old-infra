package com.company.seed.web.controller.security.form;

import org.hibernate.validator.constraints.NotEmpty;
import org.yoara.framework.component.web.common.validation.ValidationForm;

/**
 * Created by yoara on 2016/8/3.
 */
public class PaModifyForm implements ValidationForm {
    private boolean check;
    @NotEmpty
    private String positionId;
    @NotEmpty
    private String authorityId;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(String authorityId) {
        this.authorityId = authorityId;
    }
}
