package com.company.seed.web.interceptor;

import com.company.seed.common.ContextHolder;
import com.company.seed.common.DataSourceEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yoara.framework.component.logger.support.webaccess.ExtendedParameter;
import org.yoara.framework.component.logger.support.webaccess.RequestContextHolder;
import org.yoara.framework.component.web.interceptor.ZBaseInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 上下文拦截器，用于往上下文中放入必要的信息
 */
public class ContextInterceptor extends ZBaseInterceptorAdapter {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		try {
			ContextHolder.setDataSource(DataSourceEnum.USER);
			RequestContextHolder.setExtendedParameter(new ExtendedParameter() {
				@Override
				public String getAccessAccount() {
					return "anyone";
				}
			});
			return super.preHandle(request, response, handler);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}
	}
}
