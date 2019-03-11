package org.yoara.framework.component.payment.alipay.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yoara.framework.component.payment.alipay.config.AlipayConfig;
import org.yoara.framework.component.payment.alipay.sign.RSA;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ExternalPartner {
    private static Logger logger = LoggerFactory.getLogger(ExternalPartner.class);

    public static String payParam(Map<String, String> requestParam) {
        String subject=requestParam.get("subject");
        String body=requestParam.get("body");
        String total_fee=requestParam.get("total_fee");
        String out_trade_no=requestParam.get("out_trade_no");
        String notify_url=requestParam.get("notify_url");
        String _input_charset="utf-8";

        StringBuilder builder = getNewOrderInfo( subject, body, total_fee,out_trade_no, notify_url ,_input_charset);
        String sign = RSA.sign(builder.toString(), AlipayConfig.getPrivate_key(), _input_charset);
        sign = encode(sign,_input_charset);
        builder.append("&sign=\"").append(sign).append("\"&").append("sign_type=\"RSA\"");
        return builder.toString();
    }

    private static StringBuilder getNewOrderInfo(String subject,String body,String total_fee,String out_trade_no, String notify_url,String _input_charset) {
        StringBuilder builder = new StringBuilder();
        builder.append("partner=\"").append(AlipayConfig.getPartner())
                .append("\"&out_trade_no=\"").append(out_trade_no)
                .append("\"&subject=\"").append(subject)
                .append("\"&body=\"").append(body)
                .append("\"&total_fee=\"").append(total_fee)
                // 网址需要做URL编码
                .append("\"&notify_url=\"").append(encode(notify_url,_input_charset))
                .append("\"&service=\"mobile.securitypay.pay")
                .append("\"&_input_charset=\"").append(_input_charset)
                .append("\"&payment_type=\"1")
                .append("\"&seller_id=\"").append(AlipayConfig.getSeller_id())
                .append("\"&it_b_pay=\"1m")
                .append("\"");
        return builder;
    }

    private static String encode(String url,String charset){
        try {
            return URLEncoder.encode(url,charset);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    public static String notify(HttpServletRequest request, AlipayNotifyCallback alipayNotifyCallback) {
        Map<String, String> params = new HashMap<String, String>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while(parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            String value = request.getParameter(name);
            params.put(name, value);
        }

        String orderNumber = request.getParameter("out_trade_no");
        String tradeStatus = request.getParameter("trade_status");
        if(logger.isInfoEnabled()) {
            logger.info("支付宝通知回调， 订单号: " + orderNumber);
        }
        if("WAIT_BUYER_PAY".equals(tradeStatus)) {
            if(logger.isInfoEnabled()) {
                logger.info("支付宝通知回调， 订单号: " + orderNumber + ", 等待买家付款");
                return "success";
            }
        }

        boolean verify = false;
        for(int i = 0; !verify && i < 3; i ++) {
            long start = System.currentTimeMillis();
            verify = AlipayNotify.verify(params);
            if (logger.isInfoEnabled()) {
                logger.info("支付宝回调， 第" + i + "次校验耗时: " + (System.currentTimeMillis() - start) + "ms");
            }
            try {
                Thread.sleep(3000 * (i + 1)); // 休息后重试
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (verify) {  // 回调校验
            String msg = alipayNotifyCallback.notify(orderNumber, request.getParameter("trade_no"), tradeStatus);
            if (logger.isInfoEnabled()) {
                logger.info("支付宝通知回调， 订单号: " + orderNumber + ", 处理结果: " + msg);
            }
            return msg;
        }
        logger.info("支付宝通知回调， 订单号: " + orderNumber + "， 回调校验失败");
        return "fail";
    }

    public static interface AlipayNotifyCallback {
        public String notify(String orderNumber, String tradeNumber, String tradeStatus);
    }
}
