package org.yoara.framework.component.payment;

import org.yoara.framework.component.payment.paystrategy.PayObject;
import org.yoara.framework.component.payment.paystrategy.PayStrategy;

/**
 * Created by Administrator on 2015/11/25 0025.
 */
public abstract class DoPostPay<P extends PayStrategy, T> {
    private P payStrategy;

    public DoPostPay(P payStrategy) {
        this.payStrategy = payStrategy;
    }

    public T process(PayObject payObject) throws Exception {
        return doHandler(payStrategy,payObject);
    }

    protected abstract T doHandler(P payStrategy,PayObject payObject) throws Exception;
}
