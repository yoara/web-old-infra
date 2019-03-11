package com.company.seed.web;

import com.alibaba.fastjson.JSONObject;
import com.company.seed.web.interceptor.ContextInterceptor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:biz-web-test.xml",
})
public class AbstractControllerWithUrlTest {
	@Autowired
	private RequestMappingHandlerMapping requestMappingHandlerMapping;
	@Autowired
	private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

	protected MockHttpServletRequest request;
	protected MockHttpServletResponse response;

	public String getResponseContext(){
		try {
			return response.getContentAsString();
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	protected JSONObject getJson(){
		//String viewName = excuteAction(request, response).getViewName();
		excuteAction(request, response);
		return JSONObject.parseObject(getResponseContext());
	}

	@Before
	public void setUp(){
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		request.setMethod("post");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		ServletRequestAttributes attributes = new ServletRequestAttributes(request);
		RequestContextHolder.setRequestAttributes(attributes);
	}
	/**
	 * 执行request请求
	 *
	 * @throws Exception
	 */
	public ModelAndView excuteAction(HttpServletRequest request,
									 HttpServletResponse response) {
		ContextInterceptor interceptor = new ContextInterceptor();
		ModelAndView model = null;
		try {
			interceptor.preHandle(request, response, null);
			// 这里需要声明request的实际类型，否则会报错
			request.setAttribute(HandlerMapping.INTROSPECT_TYPE_LEVEL_MAPPING,
					true);
			HandlerExecutionChain chain = requestMappingHandlerMapping
					.getHandler(request);
			model = requestMappingHandlerAdapter.handle(request, response,
					chain.getHandler());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}

	protected void assertBasicStructure(JSONObject json) {
		//状态码
		Assert.assertNotNull(json.get("status"));
		//描述
		Assert.assertNotNull(json.get("message"));
		//result
		Assert.assertNotNull(json.get("result"));
	}

	protected void addParams(String... paramPairs) {

		if( paramPairs == null ) {
			return;
		}
		int size = paramPairs.length % 2 == 0 ? paramPairs.length : paramPairs.length - 1;

		for (int i = 0; i < size; i += 2) {
			request.addParameter(paramPairs[i], paramPairs[i + 1]);
		}
	}
}
