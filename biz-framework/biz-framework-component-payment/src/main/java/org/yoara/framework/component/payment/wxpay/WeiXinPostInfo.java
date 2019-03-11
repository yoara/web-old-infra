package org.yoara.framework.component.payment.wxpay;

import org.yoara.framework.component.payment.wxpay.config.WeixinConfig;
import org.yoara.framework.core.util.CommonDateUtil;

import java.util.Date;
import java.util.UUID;

/**
 * 传递给微信接口的封装数据
 * @author yoara
 */
public class WeiXinPostInfo {
	//微信分配的公众账号ID（企业号corpid即为此appId）
	private String appid = WeixinConfig.getApp_id();
	//微信支付分配的商户号
	private String mch_id= WeixinConfig.getPartner_id();
	//随机字符串，不长于32位。推荐随机数生成算法
	private String nonce_str = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
	//货币类型
	private String fee_type = "CNY";
	//交易起始时间,订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010
	private String time_start = CommonDateUtil.formatDateToyyyyMMddHHmmss(new Date());
	//交易结束时间,最短失效时间间隔必须大于5分钟
	private String time_expire = CommonDateUtil.formatDateToyyyyMMddHHmmss(
			CommonDateUtil.addHour(new Date(),2));
	//交易类型
	private String trade_type ="NATIVE";
	
	//接收微信支付异步通知回调地址
	private String notify_url;
	//订单总金额，只能为整数，详见支付金额
	private int total_fee;
	//8.8.8.8	APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
	private String spbill_create_ip;
	//商品或支付单简要描述(32)
	private String body;
	//非必填(127) 附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
	private String attach;
	//此参数必传。此id为二维码中包含的商品ID，商户自行定义。
	private String product_id;
	//商户订单号,商户系统内部的订单号,32个字符内、可包含字母
	private String out_trade_no;
	//签名，详见签名生成算法
	private String sign;
	
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getMch_id() {
		return mch_id;
	}
	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}
	public String getNonce_str() {
		return nonce_str;
	}
	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getFee_type() {
		return fee_type;
	}
	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}
	public int getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}
	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}
	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}
	public String getTime_start() {
		return time_start;
	}
	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}
	public String getTime_expire() {
		return time_expire;
	}
	public void setTime_expire(String time_expire) {
		this.time_expire = time_expire;
	}
	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	public String getTrade_type() {
		return trade_type;
	}
	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
}
