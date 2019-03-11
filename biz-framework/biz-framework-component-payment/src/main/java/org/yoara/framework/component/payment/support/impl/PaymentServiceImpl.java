package org.yoara.framework.component.payment.support.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.yoara.framework.component.payment.DoPostPay;
import org.yoara.framework.component.payment.alipay.AlipayPostPayHtml;
import org.yoara.framework.component.payment.alipay.AlipayPostPayJson;
import org.yoara.framework.component.payment.paystrategy.AliPayStrategy;
import org.yoara.framework.component.payment.paystrategy.PayObject;
import org.yoara.framework.component.payment.paystrategy.PayStrategy;
import org.yoara.framework.component.payment.paystrategy.WeiXinPayStrategy;
import org.yoara.framework.component.payment.support.AddservicePayTypeEnums;
import org.yoara.framework.component.payment.support.PaymentService;
import org.yoara.framework.component.payment.wxpay.WeiXinpayAppPay;
import org.yoara.framework.component.payment.wxpay.WeiXinpayPostPay;

import javax.annotation.Resource;

@Service("paymentService")
public class PaymentServiceImpl implements PaymentService {
	private Logger logger  = LoggerFactory.getLogger(this.getClass());

	@Resource
	private WeiXinPayStrategy weiXinPayStrategy;

	@Resource
	private AliPayStrategy aliPayStrategy;

	@Override
	public String postPayInfo(PayObject o, AddservicePayTypeEnums payType) {
		DoPostPay<? extends PayStrategy, String> doPostPay = null;
		switch (payType) {
			case ALIPAY:
				doPostPay = new AlipayPostPayHtml(aliPayStrategy);
				break;
			case WECHAT:
				doPostPay = new WeiXinpayPostPay(weiXinPayStrategy);
				break;
			default:
				break;
		}
		return doPostPayInfo(doPostPay, o);
	}

	@Override
	public String postPayInfoForJson(PayObject payObject, AddservicePayTypeEnums payType) {
		DoPostPay<? extends PayStrategy, String> doPostPay = null;
		switch (payType) {
			case ALIPAY:
				doPostPay = new AlipayPostPayJson(aliPayStrategy);
				break;
			case WECHAT:
				doPostPay = new WeiXinpayAppPay(weiXinPayStrategy);
				break;
			default:
				break;
		}
		return doPostPayInfo(doPostPay, payObject);
	}

	private String doPostPayInfo(DoPostPay<? extends PayStrategy, String> doPostPay, PayObject payObject) {
		if (doPostPay == null) {
			return null;
		}
		try {
			return doPostPay.process(payObject);
		} catch (Exception e) {
			logger.error("[pay]" + e);
		}
		return null;
	}
}
