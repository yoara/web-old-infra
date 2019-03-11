package org.yoara.framework.component.web.bean.validate;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by yoara on 2017/2/27.
 */
public class OperateActionForm extends IdForm {
    @ApiModelProperty(value = "操作指令",required = true,example = "add")
    @NotEmpty(message = "操作指令不得为空")
    private String operateAction;

    public String getOperateAction() {
        return operateAction;
    }

    public void setOperateAction(String operateAction) {
        this.operateAction = operateAction;
    }
}
