package com.company.seed.dao.basic.dataoperatelog.logger;

/**
 * Created by yoara on 2017/1/6.
 */
public class DataLoggerFactory {
    private static NormalOperateLogger normalLogger = new NormalOperateLogger();

    public static NormalOperateLogger getNormalLogger(){
        return normalLogger;
    }
}
