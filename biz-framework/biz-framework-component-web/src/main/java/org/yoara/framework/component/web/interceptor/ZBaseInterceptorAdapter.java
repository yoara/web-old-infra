package org.yoara.framework.component.web.interceptor;

import com.alibaba.fastjson.JSON;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.yoara.framework.component.web.bean.JsonCommonCodeEnum;
import org.yoara.framework.component.web.common.util.CommonWebUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/1.
 */
public class ZBaseInterceptorAdapter extends HandlerInterceptorAdapter {
    public String[] getIpAddr(HttpServletRequest request) {
        return CommonWebUtil.getIpAddr(request);
    }
    public String getString(String name) {
        return getString(name, null);
    }
    public String getString(String name, String defaultValue) {
        return CommonWebUtil.getString(name,defaultValue);
    }
    /**
     * 如果name是1 就返回true,否则返回false
     *
     * @param name
     * @param defaultValue
     * @return
     */
    public Boolean getBoolean(String name, boolean defaultValue) {
        return CommonWebUtil.getBoolean(name,defaultValue);
    }

    /**
     * 输出提示json
     * @param response
     */
    public void printJsonMsg(HttpServletResponse response,ResponseBody body) {
        try {
            response.setContentType("application/json");
            response.setStatus(body.getStatus());//401状态
            response.setCharacterEncoding("UTF-8");
            Map<String, Object> result = new HashMap<>();
            result.put("status", body.getCode());
            result.put("message", body.getMessage());
            result.put("result", body.getLoginUrl());//返回登录url
            String resultMsg = JSON.toJSONString(result);
            response.getWriter().write(resultMsg);
        } catch (IOException e) {
            //log.error(e);
        }
    }
    /**提示提醒**/
    protected void doDealAlert(HttpServletResponse response,String message) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println("<script>alert('"+message+"');window.history.go(-1)</script>");
        out.flush();
        out.close();
    }

    /**
     * 获得所需要的注解
     * @param handler 方法handler
     * @param clazz 枚举类型
     * @param methodFirst 是否以方法注解为优先
     * @return
     */
    protected <T extends Annotation> T getAnnotation(
            Object handler, Class<T> clazz, boolean methodFirst){
        //仅校验方法级别的Handler
        if(!(handler instanceof HandlerMethod)){
            return null;
        }
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        T annotationMethod = null;
        if(methodFirst){
            annotationMethod = handlerMethod.getMethod().getAnnotation(clazz);
        }
        if(annotationMethod!=null){
            return annotationMethod;
        }
        T annotationClazz = handlerMethod.getMethod().getDeclaringClass().getAnnotation(clazz);
        return annotationClazz;
    }
    /**
     * 获得所需要的注解，默认方法级枚举优先
     * @param handler 方法handler
     * @param clazz 枚举类型
     * @return
     */
    protected <T extends Annotation> T getAnnotation(Object handler,Class<T> clazz){
        return getAnnotation(handler,clazz,true);
    }

    public static class ResponseBody{
        private int status = 401;
        private String code;
        private String message;
        private String loginUrl;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
        public void setCode(JsonCommonCodeEnum code) {
            this.code = code.name();
            this.message = code.getMessage();
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getLoginUrl() {
            return loginUrl;
        }

        public void setLoginUrl(String loginUrl) {
            this.loginUrl = loginUrl;
        }
    }
}
