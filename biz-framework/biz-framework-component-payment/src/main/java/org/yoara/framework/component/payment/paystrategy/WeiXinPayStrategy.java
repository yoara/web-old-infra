package org.yoara.framework.component.payment.paystrategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yoara.framework.component.payment.paycallback.PayCallBackResult;
import org.yoara.framework.component.payment.wxpay.PayCallBackData;
import org.yoara.framework.component.payment.wxpay.PayResData;
import org.yoara.framework.component.payment.wxpay.WeiXinPostInfo;
import org.yoara.framework.component.payment.wxpay.config.WeixinConfig;
import org.yoara.framework.component.payment.wxpay.util.HttpsRequest;
import org.yoara.framework.component.payment.wxpay.util.Signature;
import org.yoara.framework.component.payment.wxpay.util.Util;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component("weiXinPayStrategy")
public class WeiXinPayStrategy implements PayStrategy {
	private Logger logger  = LoggerFactory.getLogger(this.getClass());
	private final static String NOTIFY_URL = ".qfang.com/broker/paycallback/" +
		"addservice/weixinpaynotify";
	private final static String WEIXIN_URL 
		= "https://api.mch.weixin.qq.com/pay/unifiedorder";
	@Override
	public Object postPayInfo(Object o) throws Exception {
		//微信同一笔交易不能多次提交
		WeiXinPostInfo post = (WeiXinPostInfo)o;
		post.setNotify_url(WeixinConfig.getNotify_url());
		setPostSign(post);
		HttpsRequest request = new HttpsRequest();
		String result = request.sendPost(WEIXIN_URL, post);
		logger.info("[pay][weixin]扫码返回信息："+result);
		return Util.getObjectFromXML(result, PayResData.class);
	}
	
	/** 生成微信支付签名 **/
	private void setPostSign(WeiXinPostInfo post) throws IllegalAccessException {
		String sign = Signature.getSign(post);
		post.setSign(sign);
	}

	@Override
	public PayCallBackResult payCallBack(HttpServletRequest request) throws Exception {
		StringBuffer sb = new StringBuffer() ; 
		InputStreamReader isr = new InputStreamReader(request.getInputStream());   
		BufferedReader br = new BufferedReader(isr); 
		String s = "" ; 
		while((s=br.readLine())!=null){ 
			sb.append(s) ; 
		} 
		String str =sb.toString(); 
		
		PayCallBackData data = (PayCallBackData)Util.getObjectFromXML(
				str, PayCallBackData.class);

		PayCallBackResult result = new PayCallBackResult();
		result.setBackStr(str);
		result.setOrderId(data.getOut_trade_no());
		result.setPayTradeNo(data.getTransaction_id());
		result.setPayDone("SUCCESS".equals(data.getReturn_code())&&
				"SUCCESS".equals(data.getResult_code()));
		//验证签名
		String sign = data.getSign();
		data.setSign("");		//不用于计算签名的字段置空
		String sign_local = Signature.getSign(data);
		data.setSign(sign);		
		
		result.setSignPass(sign!=null&&sign.equals(sign_local));
		return result;
	}
}
