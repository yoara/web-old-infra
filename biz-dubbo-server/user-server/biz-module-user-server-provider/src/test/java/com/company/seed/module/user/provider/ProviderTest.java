package com.company.seed.module.user.provider;

import com.alibaba.dubbo.container.spring.SpringContainer;
import com.company.seed.module.user.server.api.UserDemoApi;
import junit.framework.TestCase;

/**
 * Created by yoara on 2016/7/13.
 */
public class ProviderTest extends TestCase{
    public void testProvider(){
        SpringContainer container = new SpringContainer();
        container.start();
        //
        UserDemoApi userDemoApi = SpringContainer.getContext().getBean("userApi",UserDemoApi.class);
        System.out.println(userDemoApi.authenticateUser("aaa","ccc"));
    }
}
