package org.yoara.framework.component.payment.wxpay;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Consts;
import org.yoara.framework.component.payment.DoPostPay;
import org.yoara.framework.component.payment.paystrategy.PayObject;
import org.yoara.framework.component.payment.paystrategy.WeiXinPayStrategy;
import org.yoara.framework.component.payment.wxpay.config.WeixinConfig;
import org.yoara.framework.component.payment.wxpay.util.MD5;
import org.yoara.framework.component.payment.wxpay.util.Util;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2015/11/25 0025.
 */
public class WeiXinpayAppPay extends DoPostPay<WeiXinPayStrategy, String> {
    public WeiXinpayAppPay(WeiXinPayStrategy payStrategy) {
        super(payStrategy);
    }

    @Override
    protected String doHandler(WeiXinPayStrategy payStrategy, PayObject payObject) throws Exception {
        WeiXinPostInfo wxPost = new WeiXinPostInfo();
        wxPost.setAppid(WeixinConfig.getApp_id());
        wxPost.setMch_id(WeixinConfig.getPartner_id());
        wxPost.setTrade_type("APP");
        //微信的单位是一分
        wxPost.setTotal_fee(payObject.getPayPrice().multiply(new BigDecimal(100)).intValue());
        wxPost.setSpbill_create_ip(payObject.getUserIp());
        wxPost.setBody(payObject.getSubject());
        wxPost.setAttach(payObject.getAttach());
        wxPost.setProduct_id(payObject.getProductId());
        wxPost.setOut_trade_no(payObject.getOrderId());

        JSONObject result=new JSONObject();
        PayResData resData = (PayResData) payStrategy.postPayInfo(wxPost);
        if (resData != null &&
                PayResData.SUCCESS.equals(resData.getReturn_code()) &&
                PayResData.SUCCESS.equals(resData.getResult_code())) {
            String timeStampStr=String.valueOf(Util.genTimeStamp());
            result.put("appid", WeixinConfig.getApp_id());
            result.put("partnerid", WeixinConfig.getPartner_id());
            result.put("timestamp",timeStampStr);
            result.put("prepayid",resData.getPrepay_id());
            result.put("noncestr",resData.getNonce_str());
            result.put("package","Sign=WXPay");
            return toStringWithSign(result);
        }
        result.put("return_code",resData.getReturn_code());
        result.put("result_code",resData.getResult_code());
        return result.toJSONString();
    }

    private String toStringWithSign(JSONObject prepayIdInfo) {
        StringBuilder builder = new StringBuilder();
        builder.append("appid").append('=').append(WeixinConfig.getApp_id()).append('&')
                .append("noncestr").append('=').append(prepayIdInfo.getString("noncestr")).append('&')
                .append("package").append('=').append(prepayIdInfo.getString("package")).append('&')
                .append("partnerid").append('=').append(WeixinConfig.getPartner_id()).append('&')
                .append("prepayid").append('=').append(prepayIdInfo.getString("prepayid")).append('&')
                .append("timestamp").append('=').append(prepayIdInfo.getString("timestamp"));
        prepayIdInfo.put("sign", sign(builder));
        return prepayIdInfo.toJSONString();
    }

    private String sign(StringBuilder builder){
        if(builder.charAt(builder.length()-1)!='&'){
            builder.append('&');
        }
        builder.append("key=").append(WeixinConfig.getPartner_key());
        return MD5.getMessageDigest(builder.toString().getBytes(Consts.UTF_8)).toUpperCase();
    }
}
