package org.yoara.framework.component.web.filter;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yoara.framework.component.web.common.util.CommonWebUtil;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author yoara
 * 阀值控制过滤器
 */
public class AccessThrottlingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AccessThrottlingFilter.class);
    private long lastResetTime;
    private String sessionIdName = "sid";
    private long throttlingInterval = 1000; // ms
    private int maxAccessCountPerSession = 5;
    private int maxSessionIdPerAddr = 20;
    private Map<String, Integer> accessDataMap = new HashMap<String, Integer>();
    private Map<String, Set<String>> sessionIdsMap = new HashMap<String, Set<String>>();
    private Set<String> bypassResourceSet = new HashSet<String>();

    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.lastResetTime = System.currentTimeMillis();
        if (StringUtils.isNotEmpty(filterConfig.getInitParameter("throttlingInterval"))) {
            this.throttlingInterval = Long.valueOf(filterConfig.getInitParameter("throttlingInterval"));
        }
        if (StringUtils.isNotEmpty(filterConfig.getInitParameter("maxAccessCountPerSession"))) {
            this.maxAccessCountPerSession = Integer.valueOf(filterConfig.getInitParameter("maxAccessCountPerSession"));
        }
        if (StringUtils.isNotEmpty(filterConfig.getInitParameter("sessionIdName"))) {
            this.sessionIdName = filterConfig.getInitParameter("sessionIdName");
        }
        if (StringUtils.isNotEmpty(filterConfig.getInitParameter("maxSessionIdPerAddr"))) {
            this.maxSessionIdPerAddr = Integer.valueOf(filterConfig.getInitParameter("maxSessionIdPerAddr"));
        }
        if (StringUtils.isNotEmpty(filterConfig.getInitParameter("bypassResources"))) {
            String[] bypassResources = filterConfig.getInitParameter("bypassResources").split(",");
            for (String bypassResource : bypassResources) {
                bypassResourceSet.add(bypassResource);
            }
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }
        this.resetAccessHistory();
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getServletPath();
        for(String url:bypassResourceSet){
        	if(path.startsWith(url)){
        		 chain.doFilter(httpRequest, httpResponse);
                 return;
        	}
        }
        AccessKey accessKey = this.getAccessKey(httpRequest);
        int[] accessHistory = this.statAndGetAccessHistory(accessKey);
        if (accessHistory[0] > this.maxAccessCountPerSession) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            logger.warn("来自[" + accessKey + "]的请求被拒绝访问，总访问数[" + accessHistory[0] + "]已超过设定的阀值[" + this.maxAccessCountPerSession + "]");
        } else if (accessHistory[1] > this.maxSessionIdPerAddr) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            logger.warn("来自[" + accessKey + "]的请求被拒绝访问，总Session数[" + accessHistory[1] + "]已超过设定的阀值[" + this.maxSessionIdPerAddr + "]");
        } else {
            chain.doFilter(request, response);
        }
    }

    /*
     * 如果超过指定的时间间隔则清零访问统计数据。
     */
    private synchronized void resetAccessHistory() {
        long now = System.currentTimeMillis();
        if (now >= this.lastResetTime + this.throttlingInterval) {
            this.accessDataMap.clear();
            this.sessionIdsMap.clear();
            this.lastResetTime = now;
        }
    }

    private AccessKey getAccessKey(HttpServletRequest httpRequest) {
        Cookie cookies[] = httpRequest.getCookies();
        String sid = null;
        if (cookies != null && cookies.length > 0) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (this.sessionIdName.equals(cookie.getName())) {
                    sid = cookie.getValue();
                }
            }
        }
        return new AccessKey(CommonWebUtil.getIpAddr(httpRequest)[0], sid);
    }

    private synchronized int[] statAndGetAccessHistory(AccessKey accessKey) {
        int accessCount = 0;
        String accessKeyStr = accessKey.toString();
        if (this.accessDataMap.containsKey(accessKeyStr)) {
            accessCount = this.accessDataMap.get(accessKeyStr);
        }
        this.accessDataMap.put(accessKeyStr, ++accessCount);
        String remoteAddr = accessKey.getRemoteAddr();
        Set<String> sessionIdSet = sessionIdsMap.get(remoteAddr);
        if (sessionIdSet == null) {
            sessionIdSet = new HashSet<String>();
            sessionIdsMap.put(remoteAddr, sessionIdSet);
        }
        if (StringUtils.isNotEmpty(accessKey.getSessionId())) {
            sessionIdSet.add(accessKey.getSessionId());
        }
        return new int[] { accessCount, sessionIdSet.size() };
    }

    private class AccessKey {
        private String remoteAddr;
        private String sessionId;

        private AccessKey(String remoteAddr, String sessionId) {
            super();
            this.remoteAddr = remoteAddr;
            this.sessionId = sessionId;
        }

        public String getRemoteAddr() {
            return remoteAddr;
        }

        public String getSessionId() {
            return sessionId;
        }

        public String toString() {
            if (StringUtils.isEmpty(sessionId)) {
                return remoteAddr;
            } else {
                return remoteAddr + "." + sessionId;
            }
        }
    }

    @Override
    public void destroy() {
        // Nothing to do.
    }
}