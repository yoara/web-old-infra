package com.company.seed.web.controller.openapi;

import com.alibaba.fastjson.JSONObject;
import com.company.seed.web.AbstractControllerWithUrlTest;
import org.junit.Assert;
import org.junit.Test;

public class EchoControllerTest extends AbstractControllerWithUrlTest {
	
	@Test
	public void testEcho() {
		/********参数********/
		request.addParameter("nothing", "nothing");
		request.setRequestURI("/openapi/echo");
		JSONObject json = getJson();
		
		/********返回数据********/
		Assert.assertNotNull(json.get("result"));
	}
}
