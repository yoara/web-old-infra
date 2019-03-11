package org.yoara.framework.component.logger.support.logger;

import org.slf4j.Logger;
import org.yoara.framework.component.logger.access.WebAccessLoggerInterceptor;
import org.yoara.framework.component.logger.support.digest.DigestLogBuilder;
import org.yoara.framework.component.logger.support.webaccess.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yoara on 2016/5/27.
 */
public class LoggerOut {
    public static final String PARAM_KEY = "debug";

    public static void info(HttpServletRequest request, HttpServletResponse response) {
        try {
            RequestContextHolder.init(request.getParameter(LoggerOut.PARAM_KEY) != null);
            String loggerMessage = DigestLogBuilder.buildWebAccessDigest(request, response, true, null);
            if(loggerMessage!=null){
                getLogger().info(loggerMessage);
            }
            RequestContextHolder.clear();
        } catch (Exception e) {}
    }

    private static Logger getLogger() {
        Logger logger = WebAccessLoggerInterceptor.getLogger();
        if(logger==null){
            WebAccessLoggerInterceptor.setLogger();
            logger = WebAccessLoggerInterceptor.getLogger();
        }
        return logger;
    }
}
