package org.yoara.framework.component.logger.support.digest;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public abstract class WebUtil {

    public static final String COOKIE_ID = "cookieId";
    public static final String USER_AGENT_NAME = "user-agent";

    public static Map<String, String> getParamterMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            map.put(paramName, StringUtils.join(paramValues,","));
        }
        return map;
    }

    public static String getCookieId(HttpServletRequest request){
        if(request == null){
            return null;
        }
        if(request.getCookies() == null){
            return null;
        }
        for(Cookie cookie : request.getCookies()){
            if(StringUtils.equals(cookie.getName(), COOKIE_ID)){
                return cookie.getValue();
            }
        }
        return null;
    }

    public static String getString(HttpServletRequest request, String name) {
        String resultStr = request.getParameter(name);
        if (resultStr == null || "".equals(resultStr) || "null".equals(resultStr) || "undefined".equals(resultStr)) {
            return null;
        } else {
            return resultStr;
        }
    }

    public static String getHostContext(HttpServletRequest request){
        StringBuffer url = request.getRequestURL();
        String uri = request.getRequestURI();

        return url.delete(url.length() - uri.length(), url.length())
                .append(request.getContextPath())
                .append("/").toString();
    }


    public static String[] getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){
                //根据网卡取本机配置的IP
                InetAddress inet=null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress= inet.getHostAddress();
            }
        }
        return ipAddress.split(",");
    }
}
