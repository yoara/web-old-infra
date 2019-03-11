package com.company.seed.basic.web.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.yoara.framework.component.web.interceptor.ZBaseInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yoara on 2016/3/2.
 */
public class CrossOriginInterceptor extends ZBaseInterceptorAdapter {
	//跨域开关
	@Value("#{bizwebProperties['switch.crossOriginInterceptor.isOpen']}")
	private String isOpen;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if("true".equals(isOpen)){//开启
			String origin=request.getHeader("Origin");
			
			if(origin!=null){
				response.setHeader("Access-Control-Allow-Origin",origin);
				response.setHeader("Access-Control-Allow-Credentials","true");
			}else{
				response.setHeader("Access-Control-Allow-Origin","*");
			}
			
			
			response.setHeader("Access-Control-Allow-Headers", "X-Requested-With,Content-Type");
			response.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
		}
		return true;
	}
}
