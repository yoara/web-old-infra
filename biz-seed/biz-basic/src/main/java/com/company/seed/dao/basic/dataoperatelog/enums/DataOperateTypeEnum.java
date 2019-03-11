package com.company.seed.dao.basic.dataoperatelog.enums;

/**
 * 操作类型
 * Created by yoara on 2017/01/06.
 */
public enum DataOperateTypeEnum {

	ADD("新增", "ADD"), UPDATE("修改", "UPDATE"), 
	VIEW("查看", "VIEW"), DEL("删除", "DEL");

	DataOperateTypeEnum(String alias, String value) {
		this.alias = alias;
		this.value = value;
	}

	private String alias; // 别名
	private String value; // 值

	public String getAlias() {
		return alias;
	}

	public String getValue() {
		return value;
	}
}
