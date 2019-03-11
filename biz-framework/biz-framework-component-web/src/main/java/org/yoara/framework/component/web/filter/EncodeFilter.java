package org.yoara.framework.component.web.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by yoara on 2016/3/2.
 */
public class EncodeFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
						 FilterChain chain) throws IOException, ServletException {
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		chain.doFilter(request, response);
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
	}

	@Override
	public void destroy() {
	}

}
