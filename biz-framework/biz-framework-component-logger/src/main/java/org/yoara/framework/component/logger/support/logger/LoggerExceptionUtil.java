package org.yoara.framework.component.logger.support.logger;

import org.slf4j.Logger;

public class LoggerExceptionUtil{
    public static void error(Logger logger,String msg, Throwable e){
        if(logger != null) {
            logger.error(msg, e);
        }
    }

    public static void error(Logger logger,String msg){
        if(logger != null) {
            logger.error(msg);
        }
    }
}
