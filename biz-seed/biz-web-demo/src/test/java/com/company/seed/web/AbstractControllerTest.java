package com.company.seed.web;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:biz-web-test.xml"
})
public class AbstractControllerTest {
	private MockHttpServletRequest request;

	@Resource
	private RequestMappingHandlerMapping requestMappingHandlerMapping;

	public MockHttpServletRequest getRequest() {
		return request;
	}

	@Before
	public void setUp(){
		request = new MockHttpServletRequest();
		ServletRequestAttributes attributes = new ServletRequestAttributes(request);
		RequestContextHolder.setRequestAttributes(attributes);
	}

	public boolean checkUrl(String url){
		HandlerExecutionChain chain;
		try {
			chain = requestMappingHandlerMapping.getHandler(request);
			if(chain==null){
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}
