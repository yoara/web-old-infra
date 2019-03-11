package org.yoara.framework.component.payment.wxpay.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

@Configuration
@PropertySource("classpath:payment_weixin_config.properties")
public class WeixinConfig implements InitializingBean {
	@Resource
	private Environment env;
	// 这个就是自己要保管好的私有Key了（切记只能放在自己的后台代码里，不能放在任何可能被看到源代码的客户端程序中）
	// 每次自己Post数据给API的时候都要用这个key来对所有字段进行签名，生成的签名会放在Sign这个字段，API收到Post数据的时候也会用同样的签名算法对Post过来的数据进行签名和验证
	// 收到API的返回的时候也要用这个key来对返回的数据算下签名，跟API的Sign数据进行比较，如果值不一致，有可能数据被第三方给篡改

	//微信分配的公众账号ID（企业号corpid即为此appId）
	private static String app_id;
	//微信支付分配的商户号
	private static String partner_id;
	private static String partner_key;
	private static String notify_url;

	public static String getApp_id() {
		return app_id;
	}

	public static String getPartner_id() {
		return partner_id;
	}

	public static String getPartner_key() {
		return partner_key;
	}

	public static String getNotify_url() {
		return notify_url;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		app_id 		= env.getProperty("weixin.app_id");
		partner_id 	= env.getProperty("weixin.partner_id");
		partner_key = env.getProperty("weixin.partner_key");
		notify_url 	= env.getProperty("weixin.notify_url");
	}
}
