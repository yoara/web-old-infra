package org.yoara.framework.component.payment.paystrategy;

import java.math.BigDecimal;

public class PayObject {
	//支付端订单id
	private String orderId;
	//产品id（微信支付必须）
	private String productId;
	//用户IP
	private String userIp;
	//订单标题
	private String subject;
	//订单描述（支付宝必须）
	private String description;
	//订单价格
	private BigDecimal payPrice;
	//附加信息
	private String attach;

	/**
	 * 获得aliPay所需要的数据组装对象
	 * @param orderId 订单id
	 * @param subject 订单标题
	 * @param description 订单描述
	 * @param payPrice 支付额
     * @return
     */
	public static PayObject aliPayObject(String orderId, String subject, String description, BigDecimal payPrice) {
		PayObject payObject = new PayObject();
		payObject.orderId = orderId;
		payObject.subject = subject;
		payObject.description = description;
		payObject.payPrice = payPrice;
		return payObject;
	}

	private PayObject() {
	}

	/**
	 * 获得weixin所需要的数据组装对象
	 * @param orderId 订单id
	 * @param productId 产品ID
	 * @param subject 订单标题
	 * @param payPrice 支付额
     * @return
     */
	public static PayObject weixinPayObject(String orderId, String productId, String subject, BigDecimal payPrice) {
		PayObject payObject = new PayObject();
		payObject.orderId = orderId;
		payObject.productId = productId;
		payObject.subject = subject;
		payObject.payPrice = payPrice;
		return payObject;
	}

	public String getUserIp() {
		return userIp;
	}
	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(BigDecimal payPrice) {
		this.payPrice = payPrice;
	}
}
