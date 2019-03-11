package org.yoara.framework.component.payment.alipay.config;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

@Configuration
@PropertySource("classpath:payment_alipay_config.properties")
public class AlipayConfig implements InitializingBean{
	@Resource
	private Environment env;
	//服务器异步通知页面路径
	//需http://格式的完整路径，不能加?id=123这类自定义参数
	private static String notify_url;
	//同步接口
	private static String return_url;
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	private static String partner;
	// 收款支付宝账号
	private static String seller_email;
	// 卖家支付宝账号对应的支付宝唯一用户号。以2088开头的纯16位数字。
	private static String seller_id;
	// 商户的私钥
	private static String key;
	// 商户的私钥
	private static String private_key;
	// 支付宝的公钥，无需修改该值
	private static String ali_public_key;
	//设置未付款交易的超时时间
	private static String it_b_pay;
	// 字符编码格式 目前支持 gbk 或 utf-8
	private static String input_charset = "utf-8";
	// 签名方式 不需修改
	private static String sign_type = "MD5";

	public static String getNotify_url() {
		return notify_url;
	}

	public static String getReturn_url() {
		return return_url;
	}

	public static String getPartner() {
		return partner;
	}

	public static String getSeller_email() {
		return seller_email;
	}

	public static String getSeller_id() {
		return seller_id;
	}

	public static String getKey() {
		return key;
	}

	public static String getPrivate_key() {
		return private_key;
	}

	public static String getAli_public_key() {
		return ali_public_key;
	}

	public static String getIt_b_pay() {
		return it_b_pay;
	}

	public static String getInput_charset() {
		return input_charset;
	}

	public static String getSign_type() {
		return sign_type;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		notify_url 		= env.getProperty("alipay.notify_url");
		return_url 		= env.getProperty("alipay.return_url");
		partner 		= env.getProperty("alipay.partner");
		seller_email 	= env.getProperty("alipay.seller_email");
		seller_id 		= env.getProperty("alipay.seller_id");
		key 			= env.getProperty("alipay.key");
		private_key 	= env.getProperty("alipay.private_key");
		ali_public_key 	= env.getProperty("alipay.ali_public_key");
		it_b_pay 		= env.getProperty("alipay.it_b_pay","2h");
		input_charset 	= env.getProperty("alipay.input_charset","utf-8");
		sign_type 		= env.getProperty("alipay.sign_type","MD5");
	}
}
