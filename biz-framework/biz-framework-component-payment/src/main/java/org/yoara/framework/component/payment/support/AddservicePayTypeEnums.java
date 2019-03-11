package org.yoara.framework.component.payment.support;

public enum AddservicePayTypeEnums {
    ALIPAY("支付宝"),
    WECHAT("微信");

    public final String desc;

    AddservicePayTypeEnums(String desc) {
        this.desc = desc;
    }

}
