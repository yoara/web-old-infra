package org.yoara.framework.component.web.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author yoara
 */
public class HttpServletRequestWrapper extends
		javax.servlet.http.HttpServletRequestWrapper {
	/** sessionid **/
	private String sid = "";

	public HttpServletRequestWrapper(String sid, HttpServletRequest request) {
		super(request);
		this.sid = sid;
	}

	public HttpSession getSession(boolean create) {
		return new HttpSessionSidWrapper(this.sid, super.getSession(create));
	}

	public HttpSession getSession() {
		return new HttpSessionSidWrapper(this.sid, super.getSession());
	}

}
