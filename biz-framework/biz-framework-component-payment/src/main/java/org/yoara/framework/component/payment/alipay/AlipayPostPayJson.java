package org.yoara.framework.component.payment.alipay;

import com.alibaba.fastjson.JSONObject;
import org.yoara.framework.component.payment.alipay.util.ExternalPartner;
import org.yoara.framework.component.payment.paystrategy.AliPayStrategy;

import java.util.Map;

/**
 * Created by Administrator on 2015/11/26 0026.
 */
public class AlipayPostPayJson extends AlipayPostPay<String> {
    public AlipayPostPayJson(AliPayStrategy payStrategy) {
        super(payStrategy);
    }

    @Override
    protected String processByReturnType(Map<String, String> requestParam) throws Exception {
        String queryParam=ExternalPartner.payParam(requestParam);
        JSONObject result = new JSONObject();
        result.put("queryParam",queryParam);
        return JSONObject.toJSONString(result);
    }

}
