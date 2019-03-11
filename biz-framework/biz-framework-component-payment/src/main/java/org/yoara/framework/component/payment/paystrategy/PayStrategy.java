package org.yoara.framework.component.payment.paystrategy;

import org.yoara.framework.component.payment.paycallback.PayCallBackResult;

import javax.servlet.http.HttpServletRequest;

public interface PayStrategy {
	/** 传递支付信息到第三方网站 **/
	Object postPayInfo(Object o) throws Exception;
	
	/** 回调接口 **/
	PayCallBackResult payCallBack(HttpServletRequest request) throws Exception;
}
