package org.yoara.framework.component.payment.alipay;

public class AliPostInfo {
	//支付类型
	private String payment_type = "1";
	
	//商户订单号，商户网站订单系统中唯一订单号，必填
	private String out_trade_no;

	//订单名称
	private String subject;

	//付款金额
	private String total_fee;

	//订单描述
	private String body;

	//防钓鱼时间戳,若要使用请调用类文件submit中的query_timestamp函数
	private String anti_phishing_key = "";

	//客户端的IP地址,防钓鱼使用，//非局域网的外网IP地址，如：221.0.0.1
	private String exter_invoke_ip = "";

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getAnti_phishing_key() {
		return anti_phishing_key;
	}

	public void setAnti_phishing_key(String anti_phishing_key) {
		this.anti_phishing_key = anti_phishing_key;
	}

	public String getExter_invoke_ip() {
		return exter_invoke_ip;
	}

	public void setExter_invoke_ip(String exter_invoke_ip) {
		this.exter_invoke_ip = exter_invoke_ip;
	}

	public String getPayment_type() {
		return payment_type;
	}
}
