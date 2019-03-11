package org.yoara.framework.component.payment.paycallback;

import org.springframework.stereotype.Service;
import org.yoara.framework.component.payment.paystrategy.AliPayStrategy;
import org.yoara.framework.component.payment.paystrategy.WeiXinPayStrategy;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 服务购买支付网关回调接口
 */
@Service
public class PaymentCallback{
	@Resource
	private AliPayStrategy aliPayStrategy;
	@Resource
	private WeiXinPayStrategy weiXinPayStrategy;

	/**
	 * ali同步回调接口
	 */
	public PayCallBackResult alipaynotify(HttpServletRequest request) throws Exception {
		return doAliCallback(request);
	}
	/**
	 * ali异步回调接口
	 */
	public PayCallBackResult alipayreturn(HttpServletRequest request) throws Exception {
		return doAliCallback(request);
	}
	/**调用支付同步数据**/
	private PayCallBackResult doAliCallback(HttpServletRequest request) throws Exception {
		return aliPayStrategy.payCallBack(request);
	}

	/**
	 * weixin同步回调接口
	 */
	public PayCallBackResult weixinPayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PayCallBackResult result = weiXinPayStrategy.payCallBack(request);
		if(!result.isSignPass()){
			return result;
		}
		//微信需返回确认返回确认
		if(result.isPayDone()){
			result.setResponseStr("<xml><return_code><![CDATA[SUCCESS]]>" +
					"</return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
		}
		return result;
	}
}
