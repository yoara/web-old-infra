package org.yoara.framework.component.payment.wxpay;

import org.yoara.framework.component.payment.DoPostPay;
import org.yoara.framework.component.payment.paystrategy.PayObject;
import org.yoara.framework.component.payment.paystrategy.WeiXinPayStrategy;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2015/11/25 0025.
 */
public class WeiXinpayPostPay extends DoPostPay<WeiXinPayStrategy, String> {
    public WeiXinpayPostPay(WeiXinPayStrategy payStrategy) {
        super(payStrategy);
    }

    @Override
    protected String doHandler(WeiXinPayStrategy payStrategy,PayObject payObject) throws Exception {
        WeiXinPostInfo wxPost = new WeiXinPostInfo();
        //微信的单位是一分
        wxPost.setTotal_fee(payObject.getPayPrice().multiply(
                new BigDecimal(100)).intValue());
        wxPost.setSpbill_create_ip(payObject.getUserIp());
        wxPost.setBody(payObject.getSubject());
        wxPost.setAttach(payObject.getAttach());
        wxPost.setProduct_id(payObject.getProductId());
        wxPost.setOut_trade_no(payObject.getOrderId());

        PayResData resData = (PayResData) payStrategy.postPayInfo(wxPost);
        if (resData != null &&
                PayResData.SUCCESS.equals(resData.getReturn_code()) &&
                PayResData.SUCCESS.equals(resData.getResult_code())) {
            return resData.getCode_url();
        }
        return null;
    }
}
