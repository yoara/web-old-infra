package org.yoara.framework.component.logger.support.logger;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import org.slf4j.LoggerFactory;
import org.yoara.framework.core.util.spring.SpringPropertyReader;

public class LoggerUtil {
    /**
     * 默认级别为INFO
     * 默认日志开关打开
     * 默认大小50M
     */
    public static org.slf4j.Logger getLogger(String onOffKey, final String formatKey, final String sizeKey, final String levelKey, final String defFormat, final String baseDir, final String logName, Class<?> clazz){
        return getLogger(onOffKey, formatKey, sizeKey, levelKey, defFormat, Level.INFO.toString(), Boolean.FALSE, "50000KB", baseDir, logName, clazz);
    }

    /**
     * 自定义logger生成器，
     * @param onOffKey 开关key
     * @param formatKey 日志格式key
     * @param sizeKey 日志大小key
     * @param levelKey 日志级别key
     * @param defFormat 默认日志格式
     * @param defLevel 默认日志级别
     * @param defOnOff 默认自定义开关
     * @param baseDir 日志基本目录
     * @param logName 日志文件名称
     * @param clazz 日志的clazz
     * @return 日志对象
     */
    public static org.slf4j.Logger getLogger(String onOffKey, final String formatKey, final String sizeKey, final String levelKey, final String defFormat, final String defLevel, final Boolean defOnOff, final String defSize, final String baseDir, final String logName, Class<?> clazz) {

        Logger logger = null;

        if("false".equalsIgnoreCase(SpringPropertyReader.getProperty(onOffKey, defOnOff.toString()))){

            logger = new LoggerContext().getLogger(clazz);

            final LoggerContext loggerContext = logger.getLoggerContext();
            loggerContext.start();
            //loggerContext.reset();

            RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<ILoggingEvent>(){{
                setContext(loggerContext);
                setFile(baseDir + "/" + logName + ".log");

                PatternLayoutEncoder encoder = new PatternLayoutEncoder(){{
                    setContext(loggerContext);
                    setPattern(SpringPropertyReader.getProperty(formatKey, defFormat));
                }};
                encoder.start();

                TimeBasedRollingPolicy timeBasedRollingPolicy = new TimeBasedRollingPolicy(){{
                    setContext(loggerContext);
                    setFileNamePattern(baseDir + "/" + logName + "-%d{yyyyMMdd}.%i.log");

                    setTimeBasedFileNamingAndTriggeringPolicy(new SizeAndTimeBasedFNATP() {{
                        setContext(loggerContext);
                        setMaxFileSize(SpringPropertyReader.getProperty(sizeKey, defSize));
                    }});
                }};
                timeBasedRollingPolicy.setParent(this);
                timeBasedRollingPolicy.start();

                setRollingPolicy(timeBasedRollingPolicy);
                setEncoder(encoder);
            }};
            appender.start();


            logger.setAdditive(false);
            logger.setLevel(Level.valueOf(SpringPropertyReader.getProperty(levelKey, defLevel)));
            logger.addAppender(appender);
        }else{
            logger = (Logger) LoggerFactory.getLogger(clazz);
        }

        return logger;
    }
    private LoggerUtil(){}
}
