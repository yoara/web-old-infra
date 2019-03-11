package org.yoara.framework.component.payment.support;
import org.yoara.framework.component.payment.paystrategy.PayObject;

public interface PaymentService {
	/** 向第三方支付方发送支付请求 **/
	String postPayInfo(PayObject o, AddservicePayTypeEnums payType);

	/**
	 * 向第三方支付方发送支付请求返回的是JSON字符串
	 */
	String postPayInfoForJson(PayObject payObject, AddservicePayTypeEnums payType);

}
