package com.company.seed.activemq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

/**
 * Created by yoara on 2016/6/20.
 */
@Component("JmsErrorHandler")
/**
 * JmsErrorHanlder仅负责数据恢复处理，抛异常的listener依旧会导致错误重试。
 */
public class JmsErrorHandler implements ErrorHandler{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void handleError(Throwable e) {
        logger.error(e.getMessage());
    }
}
