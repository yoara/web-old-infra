package org.yoara.framework.component.logger.access;

import org.yoara.framework.component.logger.support.digest.DigestLogBuilder;
import org.yoara.framework.component.logger.support.logger.LoggerBuilder;
import org.yoara.framework.component.logger.support.logger.LoggerExceptionUtil;
import org.yoara.framework.component.logger.support.logger.LoggerOut;
import org.yoara.framework.component.logger.support.webaccess.Diagnosis;
import org.yoara.framework.component.logger.support.webaccess.RequestContext;
import org.yoara.framework.component.logger.support.webaccess.RequestContextHolder;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * web访问拦截器
 */
public class WebAccessLoggerInterceptor extends HandlerInterceptorAdapter implements InitializingBean {

    private static Logger logger;

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger() {
        logger = LoggerBuilder.builderWebAccessLogger(WebAccessLoggerInterceptor.class);
    }

    public void afterPropertiesSet() throws Exception {
        logger = LoggerBuilder.builderWebAccessLogger(WebAccessLoggerInterceptor.class);
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        RequestContextHolder.init(request.getParameter(LoggerOut.PARAM_KEY) != null);
        if(RequestContextHolder.getDigestContext().isDebug()) {
            Diagnosis.start(DigestLogBuilder.buildStartDiagnosisDigest(request));
        }
        try{
            RequestContextHolder.getDigestContext().setSessionId(request.getSession().getId());
        }catch (Exception e){
            RequestContextHolder.getDigestContext().setSessionId("-");
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        if(modelAndView !=null && modelAndView.getModel() != null){
            model.putAll(modelAndView.getModel());

            Set<String> delKeys = new HashSet<String>();

            for(String key : model.keySet()){
                if(key.indexOf("org.springframework.validation.BindingResult") != -1){
                    delKeys.add(key);
                }
            }

            for(String key : delKeys){
                model.remove(key);
            }
        }

        //响应结果数据比较多，先暂时不打印
        //logger.info(DigestLogBuilder.buildWebAccessDigest(request, response, true, model.toString()));
        try {
            String loggerMessage = DigestLogBuilder.buildWebAccessDigest(request, response, true, null);
            if(loggerMessage!=null){
                logger.info(loggerMessage);
            }
        }catch (Exception e){
            LoggerExceptionUtil.error(logger,"WebAccessLoggerIntercepter logger fail!", e);
        }

        diagnosisEnd(response);
    }

    /**
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (ex != null) {
            try {
                String loggerMessage = DigestLogBuilder.buildWebAccessDigest(request, response, false, ex.getMessage());
                if(loggerMessage!=null){
                    logger.info(loggerMessage);
                }
            }catch(Exception e){
                LoggerExceptionUtil.error(logger,"WebAccessLoggerIntercepter logger fail!", e);
            }
            diagnosisEnd(response);
        }

    }

    public void diagnosisEnd(HttpServletResponse response) throws IOException {
        RequestContext rc = RequestContextHolder.getDigestContext();
        if(rc==null){
            return;
        }
        if(rc.isDebug()) {
            Diagnosis.release();
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(Diagnosis.dump().replaceAll("\r\n", "<br />").replaceAll("\r", "<br />").replaceAll("\n", "<br />"));
            response.flushBuffer();
            Diagnosis.clear();
        }

        RequestContextHolder.clear();
    }

}
