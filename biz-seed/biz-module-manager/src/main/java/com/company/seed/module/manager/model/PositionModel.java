package com.company.seed.module.manager.model;

import com.company.seed.common.CommonStatusEnum;
import com.company.seed.model.Entity;

/**
 * 岗位Model类
 * Created by Administrator on 2016-08-01
 */
public class PositionModel extends Entity<String> {
	// 岗位名
	private String name;
	// 状态(ENABLED,DISABLED)
	private CommonStatusEnum status;
	// parentId
	private String parentId;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setStatus(CommonStatusEnum status){
		this.status = status;
	}

	public CommonStatusEnum getStatus(){
		return status;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}
