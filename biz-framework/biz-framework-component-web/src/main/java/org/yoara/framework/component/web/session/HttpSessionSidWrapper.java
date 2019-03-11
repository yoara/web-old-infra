package org.yoara.framework.component.web.session;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.Map;

/**
 * @author yoara
 */
public class HttpSessionSidWrapper extends HttpSessionWrapper {
    /** sessionid **/
    private String sid = "";
    /** 本地session数据 **/
    private Map<String, Object> map = null;

    public HttpSessionSidWrapper(String sid, HttpSession session) {
        super(session);
        this.sid = sid;
        this.map = SessionService.getInstance().getSession(sid);
    }

    public Object getAttribute(String arg0) {
        return this.map.get(arg0);
    }

    public Enumeration getAttributeNames() {
        return (new Enumerator(this.map.keySet(), true));
    }

    public void invalidate() {
        this.map.clear();
        SessionService.getInstance().removeSession(this.sid);
    }

    public void removeAttribute(String arg0) {
        this.map.remove(arg0);
        SessionService.getInstance().saveSession(this.sid, this.map);
    }

    public void setAttribute(String arg0, Object arg1) {
        this.map.put(arg0, arg1);
        SessionService.getInstance().saveSession(this.sid, this.map);
    }

    public String getId() {
        return this.sid;
    }
}
