package com.company.seed.web.controller.security;

import com.alibaba.fastjson.JSONObject;
import com.company.seed.web.AbstractAuthControllerWithUrlTest;
import org.junit.Assert;
import org.junit.Test;

public class UserControllerTest extends AbstractAuthControllerWithUrlTest {
	
	@Test
	public void testSelect() {
		loginManager("admin","admin");
		/********参数********/
		request.setRequestURI("/security/user/select");
		JSONObject json = getJson();
		
		/********返回数据********/
		Assert.assertNotNull(json.get("result"));
	}
}
