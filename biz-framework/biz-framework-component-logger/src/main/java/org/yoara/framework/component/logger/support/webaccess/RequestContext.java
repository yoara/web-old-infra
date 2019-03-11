/**
 * Qfang.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package org.yoara.framework.component.logger.support.webaccess;

import java.util.UUID;

/**
 * 
 * @author Administrator
 * @version $Id: DigestContext.java, v 0.1 2015年4月30日 上午9:56:28 Administrator Exp $
 */
public class RequestContext {

    private String sessionId;
    private String requestId;
    private long timeKey;
    private boolean debug;
    private String loginAccount;
    private String clientAppName;


    public RequestContext() {
        requestId = UUID.randomUUID().toString();
        timeKey = System.currentTimeMillis();
        debug = false;
    }


    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String request){
        this.requestId = request;
    }

    public long getTimeKey() {
        return timeKey;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getClientAppName() {
        return clientAppName;
    }

    public void setClientAppName(String clientAppName) {
        this.clientAppName = clientAppName;
    }
}