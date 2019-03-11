package org.yoara.framework.component.web.bean.validate;


import io.swagger.annotations.ApiModelProperty;
import org.yoara.framework.component.web.common.validation.ValidationForm;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by yoara on 2017/2/27.
 */
public class PageForm implements ValidationForm {
    @ApiModelProperty(value = "页容量参数",required = true,example = "20")
    @NotNull(message = "页容量参数不得为空")
    @Min(message = "页容量参数不得为空",value=1)
    private int pageSize;

    @ApiModelProperty(value = "当前页",required = true,example = "1")
    @NotNull(message = "当前页参数不得为空")
    @Min(message = "当前页参数不得为空",value=1)
    private int currentPage;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
