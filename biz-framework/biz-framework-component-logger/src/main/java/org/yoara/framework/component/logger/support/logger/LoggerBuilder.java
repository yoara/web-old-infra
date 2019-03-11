package org.yoara.framework.component.logger.support.logger;

import ch.qos.logback.classic.Level;
import org.slf4j.Logger;
import org.yoara.framework.core.util.spring.SpringPropertyReader;

/**
 * Logger构造器
 */
public class LoggerBuilder {

    public static final String LOGGER_APP_NAME = SpringPropertyReader.getProperty("logger.app", SpringPropertyReader.getProperty("dubbo.application.owner"));

    public static final String NOTIFY_LOGGER_FORMAT = "notify.logger.format";
    public static final String NOTIFY_LOGGER_SIZE = "notify.logger.size";
    public static final String NOTIFY_LOGGER_LEVEL = "notify.logger.level";
    public static final String NOTIFY_LOGGER_OFF = "notify.logger.off";

    public static final String WEB_LOGGER_FORMAT = "web.logger.format";
    public static final String WEB_LOGGER_SIZE = "web.logger.size";
    public static final String WEB_LOGGER_LEVEL = "web.logger.level";
    public static final String WEB_LOGGER_OFF = "web.logger.off";

    public static final String RPC_LOGGER_FORMAT = "rpc.logger.format";
    public static final String RPC_LOGGER_SIZE = "rpc.logger.size";
    public static final String RPC_LOGGER_LEVEL = "rpc.logger.level";
    public static final String RPC_LOGGER_OFF = "rpc.logger.off";

    
    public static final String SQL_LOGGER_FORMAT = "sql.logger.format";
    public static final String SQL_LOGGER_SIZE = "sql.logger.size";
    public static final String SQL_LOGGER_LEVEL = "sql.logger.level";
    public static final String SQL_LOGGER_OFF = "sql.logger.off";

    public static final String EXCEPTION_LOGGER_FORMAT = "exception.logger.format";
    public static final String EXCEPTION_LOGGER_SIZE = "exception.logger.size";
    public static final String EXCEPTION_LOGGER_LEVEL = "exception.logger.level";
    public static final String EXCEPTION_LOGGER_OFF = "exception.logger.off";

    private static final String MQ_LOG_BASE = (isSystemWindows() ? "c:/log/mq_logs/" : "/log/mq_logs/") + LOGGER_APP_NAME + SpringPropertyReader.getProperty("resinId","");
    private static final String WEB_LOG_BASE = (isSystemWindows() ? "c:/log/stat_logs/" : "/log/stat_logs/") + LOGGER_APP_NAME + SpringPropertyReader.getProperty("resinId","");
    private static final String RPC_LOG_BASE = (isSystemWindows() ? "c:/log/rpcstat_logs/" : "/log/rpcstat_logs/") + LOGGER_APP_NAME + SpringPropertyReader.getProperty("resinId","");
    private static final String SQL_LOG_BASE = (isSystemWindows() ? "c:/log/sqlstat_logs/" : "/log/sqlstat_logs/") + LOGGER_APP_NAME + SpringPropertyReader.getProperty("resinId","");
    private static final String EXCEPTION_LOG_BASE = (isSystemWindows() ? "c:/log/logException_logs/" : "/log/logException_logs/") + LOGGER_APP_NAME + SpringPropertyReader.getProperty("resinId","");

    private static final String DATETIME_MSG_FORMAT = "%d{yyyy-MM-dd HH:mm:ss}  %m%n";
    private static final String MSG_FORMAT = "%msg%n";

    /**
     * 构造Mq的自定义Logger
     * @param clazz
     * @return
     */
    public static Logger builderMqLogger(Class clazz){
        return LoggerUtil.getLogger(NOTIFY_LOGGER_OFF, NOTIFY_LOGGER_FORMAT, NOTIFY_LOGGER_SIZE, NOTIFY_LOGGER_LEVEL, MSG_FORMAT, MQ_LOG_BASE, "mq", clazz);
    }

    /**
     * 构造Mq的自定义Logger
     * @param clazz
     * @return
     */
    public static Logger builderLogExceptionLogger(Class clazz){
        return LoggerUtil.getLogger(EXCEPTION_LOGGER_OFF, EXCEPTION_LOGGER_FORMAT, EXCEPTION_LOGGER_SIZE, EXCEPTION_LOGGER_LEVEL, DATETIME_MSG_FORMAT, EXCEPTION_LOG_BASE, "exception", clazz);
    }

    /**
     * 构造web access的自定义Logger
     * @param clazz
     * @return
     */
    public static Logger builderWebAccessLogger(Class clazz){
        return LoggerUtil.getLogger(WEB_LOGGER_OFF, WEB_LOGGER_FORMAT, WEB_LOGGER_SIZE, WEB_LOGGER_LEVEL, MSG_FORMAT, WEB_LOG_BASE, "stat", clazz);
    }

    /**
     * 构造web access的自定义Logger
     * @param clazz
     * @return
     */
    public static Logger builderRpcAccessLogger(Class clazz){
        return LoggerUtil.getLogger(RPC_LOGGER_OFF, RPC_LOGGER_FORMAT, RPC_LOGGER_SIZE, RPC_LOGGER_LEVEL, MSG_FORMAT, RPC_LOG_BASE, "rpcstat", clazz);
    }

    
    /**
     * 构造sql stat的自定义Logger
     * @param clazz
     * @return
     */
    public static Logger builderSqlStatLogger(Class clazz){
        return LoggerUtil.getLogger(SQL_LOGGER_OFF, SQL_LOGGER_FORMAT, SQL_LOGGER_SIZE, SQL_LOGGER_LEVEL, DATETIME_MSG_FORMAT, Level.INFO.toString(), Boolean.FALSE, "500MB", SQL_LOG_BASE, "sqlstat", clazz);
    }


    /**
     * 当前应用是否部署在windows下
     */
    private static Boolean isSystemWindows() {
        if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
