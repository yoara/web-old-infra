package com.company.seed.web.controller.openapi;

import com.company.seed.module.user.server.api.UserDemoApi;
import com.company.seed.web.controller.WebBaseController;
import com.company.seed.web.hystrix.DemoDubboCommand;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * Created by yoara on 2016/3/3.
 */
@Lazy
@RestController
@RequestMapping(value = "/openapi",produces = { "application/json;charset=UTF-8" })
public class DubboApiController extends WebBaseController {
    @Resource
    private UserDemoApi userDemoApi;

    @GetMapping(value = "dubboApi")
    public String dubboApi() throws ExecutionException, InterruptedException {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        //同步模式
        Boolean s = null;
        try{
            //userDemoApi dubbo接口
            s = userDemoApi.authenticateUser("aaa","123456");
            s = new DemoDubboCommand(userDemoApi,"aaa","123456").execute();
        }finally {
            context.shutdown();
        }
        return s+"";

        //future模式
//        Future<String> s1 = new DemoDubboCommand(userDemoApi,"aaa","123456").queue();
//        return s1.get();

        //观察者模式
//        Observable<String> s2 = new DemoDubboCommand(userDemoApi,"aaa","123456").observe();
//        s2.subscribe(result -> {
//            System.out.println(result.toString());
//        });
//
//        s2.subscribe(new Observer<String>() {
//            @Override
//            public void onCompleted() {
//                  System.out.println(result.toString());
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                  System.out.println(result.toString());
//            }
//
//            @Override
//            public void onNext(String s) {
//                  System.out.println(result.toString());
//            }
//        });
    }
}
