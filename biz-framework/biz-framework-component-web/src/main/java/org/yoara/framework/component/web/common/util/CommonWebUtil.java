package org.yoara.framework.component.web.common.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.yoara.framework.component.web.common.security.checkrequest.CheckRequestParamInterceptor;
import org.yoara.framework.core.util.CommonDateUtil;
import org.yoara.framework.core.util.CommonStringUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yoara on 2016/8/4.
 */
public class CommonWebUtil {
    private static Logger log = LoggerFactory.getLogger(CommonWebUtil.class);

    public static int getInt(String name, int defaultValue) {
        String resultStr = getRequestParameter(name);
        if (resultStr != null) {
            try {
                return Integer.parseInt(resultStr);
            } catch (Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static BigDecimal getBigDecimal(String name, BigDecimal defaultValue) {
        String resultStr = getRequestParameter(name);
        if (resultStr != null) {
            try {
                return BigDecimal.valueOf(Double.parseDouble(resultStr));
            } catch (Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static String getString(String name, String defaultValue) {
        String resultStr = getRequestParameter(name);
        if (resultStr == null || "".equals(resultStr) || "null".equals(resultStr) || "undefined".equals(resultStr)) {
            return defaultValue;
        } else {
            return resultStr;
        }
    }

    public static Cookie getCookie(String cookieName) {
        if (CommonStringUtil.isEmpty(cookieName)) return null;
        Cookie[] cookies = getRequest().getCookies();
        if (cookies != null) {
            int len = cookies.length;
            for (int i = 0; i < len; i++) {
                Cookie cookie = cookies[i];
                if (cookieName.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public static String[] getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(StringUtils.isNotEmpty(ip)){
            ip = ip.replaceAll(" ","");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.split(",");
    }

    public static String getMACAddress(String ip) {
        String str;
        String macAddress = "";
        try {
            Process p = Runtime.getRuntime().exec("nbtstat -A " + ip);
            InputStreamReader ir = new InputStreamReader(p.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (int i = 1; i < 100; i++) {
                str = input.readLine();
                if (str != null) {
                    if (str.indexOf("MAC Address") > 1) {
                        macAddress = str.substring(str.indexOf("MAC Address") + 14, str.length());
                        break;
                    }
                    if (str.indexOf("MAC Address") > 1) {
                        macAddress = str.substring(str.indexOf("MAC 地址") + 14, str.length());
                        break;
                    }
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return macAddress;
    }

    public static Boolean getBoolean(String name, boolean defaultValue) {
        String resultStr = getRequestParameter(name);
        if (resultStr == null || "".equals(resultStr.trim()) || "null".equals(resultStr.toLowerCase().trim()) || "undefined".equals(resultStr.toLowerCase().trim())) {
            return defaultValue;
        } else {
            if (resultStr.trim().equals("1")||resultStr.trim().equalsIgnoreCase("true")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static double getDouble(String name, double defaultValue) {
        String resultStr = getRequestParameter(name);
        if (resultStr != null) {
            try {
                return Double.parseDouble(resultStr);
            } catch (Exception e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public static Date getDate(String date, String format) {
        String dateStr = getString(date,null);
        if (dateStr == null || "".equals(dateStr))
            return null;
        return CommonDateUtil.parseStringToDate(format, dateStr);
    }

    /**
     * 查询request中的参数
     **/
    private static String getRequestParameter(String name) {
        HttpServletRequest request = getRequest();
        if (request.getAttribute(CheckRequestParamInterceptor.PREFIX + name) != null) {
            return request.getAttribute(CheckRequestParamInterceptor.PREFIX + name).toString();
        }
        return request.getParameter(name);
    }

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static ServletContext getServletContext() {
        return ContextLoader.getCurrentWebApplicationContext().getServletContext();
    }

    public static Map<String, String> getParamterMap() {
        Map<String, String> map = new HashMap<>();
        Enumeration paramNames = getRequest().getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String[] paramValues = getRequest().getParameterValues(paramName);
            map.put(paramName, StringUtils.join(paramValues,","));
        }
        return map;
    }

    public static String getHostContext(){
        StringBuffer url = getRequest().getRequestURL();
        String uri = getRequest().getRequestURI();

        return url.delete(url.length() - uri.length(), url.length())
                .append(getRequest().getContextPath())
                .append("/").toString();
    }
}
