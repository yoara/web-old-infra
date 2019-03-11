package com.company.seed.web.hystrix;

import com.company.seed.module.user.server.api.UserDemoApi;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;

/**
 * Created by yoara on 2016/9/20.
 */
public class DemoDubboCommand extends HystrixCommand<Boolean> {
    private String name;
    private String psw;
    private UserDemoApi userDemoApi;

    public DemoDubboCommand(UserDemoApi userDemoApi, String name, String psw) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        .withCoreSize(1))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(10000)));
        this.name = name;
        this.psw = psw;
        this.userDemoApi = userDemoApi;
    }
    @Override
    protected Boolean run() throws Exception {
        return userDemoApi.authenticateUser(name,psw);
    }
}
