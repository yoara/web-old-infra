package org.yoara.framework.component.payment.alipay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yoara.framework.component.payment.alipay.util.AlipaySubmit;
import org.yoara.framework.component.payment.paystrategy.AliPayStrategy;

import java.util.Map;

/**
 * Created by Administrator on 2015/11/25 0025.
 */
public class AlipayPostPayHtml extends AlipayPostPay<String> {
    private Logger logger = LoggerFactory.getLogger(AlipayPostPayHtml.class);

    public AlipayPostPayHtml(AliPayStrategy payStrategy) {
        super(payStrategy);
    }

    @Override
    protected String processByReturnType(Map<String, String> requestParam) throws Exception {
        //建立请求
        String sHtmlText = AlipaySubmit.buildRequest(requestParam, "get", "确认");
        logger.info("[pay:alipay][post]");
        return sHtmlText;
    }

}
