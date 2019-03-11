package com.company.seed.module.manager.model;

import com.company.seed.model.Entity;

/**
 * Created by yoara on 2016/4/25.
 * 岗位与权限关联表
 */
public class PositionAuthorityModel extends Entity<String> {
    private String positionId;
    private String positionName;
    private String authorityId;

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
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
