package org.yoara.framework.component.payment.paycallback;

public class PayCallBackResult{
	private boolean isSignPass;
	private String orderId;
	private String payTradeNo;
	private String backStr;
	private boolean isPayDone;

	private String responseStr;

	public boolean isPayDone() {
		return isPayDone;
	}
	public void setPayDone(boolean isPayDone) {
		this.isPayDone = isPayDone;
	}
	public boolean isSignPass() {
		return isSignPass;
	}
	public void setSignPass(boolean isSignPass) {
		this.isSignPass = isSignPass;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getPayTradeNo() {
		return payTradeNo;
	}
	public void setPayTradeNo(String payTradeNo) {
		this.payTradeNo = payTradeNo;
	}
	public String getBackStr() {
		return backStr;
	}
	public void setBackStr(String backStr) {
		this.backStr = backStr;
	}
	public String getResponseStr() {
		return responseStr;
	}
	public void setResponseStr(String responseStr) {
		this.responseStr = responseStr;
	}
}
