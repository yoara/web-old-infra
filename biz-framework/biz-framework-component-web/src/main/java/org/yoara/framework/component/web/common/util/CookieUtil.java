package org.yoara.framework.component.web.common.util;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * cookie操作相关的工具类
 * Created by yoara on 2016/3/3.
 */
public class CookieUtil {

    public static Cookie getCookie(HttpServletRequest request, String cookieName){
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies){
                if (cookieName.equals(cookie.getName())){
                   return cookie;
                }
            }
        }
        return null;
    }
    
    public static Cookie addCookie(HttpServletResponse resp
            ,String cookieName,String cookieValue,int second){
        return addCookie(resp,cookieName,cookieValue,null,null,second);
    }

    public static Cookie addCookie(HttpServletResponse resp
            ,String cookieName,String cookieValue,String domain,int second){
        return addCookie(resp,cookieName,cookieValue,domain,null,second);
    }

    public static Cookie addCookie(HttpServletResponse resp
            ,String cookieName,String cookieValue,String domain,String path,int second){
        Cookie ck = new Cookie(cookieName,cookieValue);
        ck.setMaxAge(second);
        if(StringUtils.isNotEmpty(domain)){
            ck.setDomain(domain);
        }
        if(StringUtils.isNotEmpty(path)){
            ck.setPath(path);
        }
        resp.addCookie(ck);
        return ck;
    }
}
