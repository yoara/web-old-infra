package com.company.seed.common;

/**
 * Created by yoara on 2016/3/2.
 */
public enum DataSourceEnum {
    USER;

    public static DataSourceEnum getDataSource(String dataSource) {
        try {
            return DataSourceEnum.valueOf(dataSource.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

}
