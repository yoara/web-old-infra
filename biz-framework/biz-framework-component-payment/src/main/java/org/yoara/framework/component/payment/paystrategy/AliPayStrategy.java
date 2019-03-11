package org.yoara.framework.component.payment.paystrategy;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.yoara.framework.component.payment.alipay.AliPostInfo;
import org.yoara.framework.component.payment.alipay.config.AlipayConfig;
import org.yoara.framework.component.payment.alipay.util.AlipayNotify;
import org.yoara.framework.component.payment.alipay.util.AlipaySubmit;
import org.yoara.framework.component.payment.paycallback.PayCallBackResult;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component("aliPayStrategy")
public class AliPayStrategy implements PayStrategy {
	@Override
	public Object postPayInfo(Object o) throws Exception {
		Map<String, String> sParaTemp = makeParams(o);
		return AlipaySubmit.buildRequestPara(sParaTemp);
	}

	/** 生成参数列 **/
	protected Map<String, String> makeParams(Object o) {
		AliPostInfo post = (AliPostInfo)o;
		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "create_direct_pay_by_user");
        sParaTemp.put("partner", AlipayConfig.getPartner());
        sParaTemp.put("seller_email", AlipayConfig.getSeller_email());
        sParaTemp.put("it_b_pay", AlipayConfig.getIt_b_pay());
        sParaTemp.put("_input_charset", AlipayConfig.getInput_charset());
		sParaTemp.put("payment_type", post.getPayment_type());
		sParaTemp.put("notify_url", AlipayConfig.getNotify_url());
		sParaTemp.put("return_url", AlipayConfig.getReturn_url());
		sParaTemp.put("out_trade_no", post.getOut_trade_no());
		sParaTemp.put("subject", post.getSubject());
		sParaTemp.put("total_fee", post.getTotal_fee());
		sParaTemp.put("body", post.getBody());
		sParaTemp.put("anti_phishing_key", post.getAnti_phishing_key());
		sParaTemp.put("exter_invoke_ip", post.getExter_invoke_ip());
		return sParaTemp;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public PayCallBackResult payCallBack(HttpServletRequest request) throws Exception {
		Map<String,String> params = new HashMap<>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			valueStr = new String(valueStr);
			params.put(name, valueStr);
		}
		//商户订单号
		String out_trade_no = request.getParameter("out_trade_no");
		//支付宝交易号
		String trade_no = request.getParameter("trade_no");
		//交易状态
		String trade_status = request.getParameter("trade_status");
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		
		//计算得出通知验证结果
		PayCallBackResult result = new PayCallBackResult();
		result.setBackStr(new JSONObject(requestParams).toJSONString());
		result.setOrderId(out_trade_no);
		result.setPayTradeNo(trade_no);
		result.setPayDone(trade_status.equals("TRADE_FINISHED") || 
				trade_status.equals("TRADE_SUCCESS"));
		result.setSignPass(AlipayNotify.verify(params));
		return result;
	}

}
