package org.yoara.framework.component.web.bean.validate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.yoara.framework.component.web.common.validation.ValidationForm;

/**
 * Created by yoara on 2017/2/27.
 */
@ApiModel
public class IdForm implements ValidationForm {
    @ApiModelProperty(value = "主键id",required = true)
    @NotEmpty(message = "ID不得为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
