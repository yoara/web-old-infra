package org.yoara.framework.component.payment.alipay;

import org.yoara.framework.component.payment.DoPostPay;
import org.yoara.framework.component.payment.paystrategy.AliPayStrategy;
import org.yoara.framework.component.payment.paystrategy.PayObject;

import java.util.Map;

/**
 * Created by Administrator on 2015/11/25 0025.
 */
public abstract class AlipayPostPay<T> extends DoPostPay<AliPayStrategy, T> {
    public AlipayPostPay(AliPayStrategy payStrategy) {
        super(payStrategy);
    }

    protected Map<String, String> getRequestParam(AliPayStrategy payStrategy,PayObject payObject) throws Exception {
        AliPostInfo aliPost = new AliPostInfo();
        aliPost.setBody(payObject.getDescription());
        aliPost.setOut_trade_no(payObject.getOrderId());
        aliPost.setTotal_fee(String.valueOf(payObject.getPayPrice().doubleValue()));
        aliPost.setSubject(payObject.getSubject());
        return (Map<String, String>) payStrategy.postPayInfo(aliPost);
    }

    @Override
    protected T doHandler(AliPayStrategy payStrategy,PayObject payObject) throws Exception {
        return processByReturnType(getRequestParam(payStrategy, payObject));
    }

    protected abstract T processByReturnType(Map<String, String> requestParam) throws Exception;
}

