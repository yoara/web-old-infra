package org.yoara.framework.component.web.bean.validate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.yoara.framework.component.web.common.validation.ValidationForm;

/**
 * Created by yoara on 2017/2/27.
 */
@ApiModel
public class LoginForm implements ValidationForm {
    @ApiModelProperty(value = "用户名",required = true,example = "user")
    @NotEmpty(message="用户名不得为空")
    private String userName;
    @ApiModelProperty(value = "密码",required = true,example = "password")
    @NotEmpty(message="用户密码不得为空")
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
