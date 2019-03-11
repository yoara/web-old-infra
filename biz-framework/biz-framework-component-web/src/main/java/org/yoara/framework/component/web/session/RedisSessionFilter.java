package org.yoara.framework.component.web.session;

import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yoara
 * 统一session过滤器
 */
public class RedisSessionFilter extends HttpServlet implements Filter {
	private static final long serialVersionUID = -365105405910803550L;

	private static String sessionId = "sid";
	private static String cookieDomain = "";
	private static String cookiePath = "/";

	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		if (request.getServletPath().contains(".")) {
			filterChain.doFilter(request, response);
			return;
		}
		Cookie cookies[] = request.getCookies();
		String sid = getId(cookies, sessionId);
		//若sid不存在，则依照uuid规则新生成一条sid
		if (StringUtils.isEmpty(sid)) {
			sid = java.util.UUID.randomUUID().toString();
			setSidInCookie(response, sid);
		}
		request.setAttribute(sessionId, sid);
		//做session延时操作
		SessionService.getInstance().expireSession(sid);
		filterChain.doFilter(new HttpServletRequestWrapper(sid, request), servletResponse);
	}
	
	private String getId(Cookie[] cookies, String key) {
		if(cookies!=null && cookies.length>0){
			for(Cookie sCookie:cookies){
				if (sCookie.getName().equals(key)) {
					return sCookie.getValue();
				}
			}
		}
		return null;
	}

	private static void setSidInCookie(HttpServletResponse response, String sid) {
		Cookie sidCookies = new Cookie(sessionId, sid);
		sidCookies.setMaxAge(-1);
		if (StringUtils.isNotEmpty(cookieDomain)) {
			sidCookies.setDomain(cookieDomain);
		}
		sidCookies.setPath(cookiePath);
		response.addCookie(sidCookies);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		String filterSessionId = filterConfig.getInitParameter("sessionId");
		if (StringUtils.isNotBlank(filterSessionId)) {
			this.sessionId = filterSessionId;
		}
		String filterDomain = filterConfig.getInitParameter("cookieDomain");
		if (StringUtils.isNotBlank(filterDomain)) {
			this.cookieDomain = filterDomain;
		}

		String filterCookiePath = filterConfig.getInitParameter("cookiePath");
		if (StringUtils.isNotBlank(filterCookiePath)) {
			this.cookiePath = filterCookiePath;
		}
	}

	public static void removeSidInCookie(HttpServletResponse response){
		setSidInCookie(response,null);
	}
}
