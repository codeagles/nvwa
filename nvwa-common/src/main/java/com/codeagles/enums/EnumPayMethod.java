package com.codeagles.enums;

/**
 * 支付枚举类
 *
 * @author hcn
 * @create 2020-01-04 11:08
 **/
public enum EnumPayMethod {

    WEIXIN(1, "微信"),
    ALIPAY(2, "支付宝");

    public final Integer type;
    public final String value;


    EnumPayMethod(Integer type, String value) {
        this.type = type;
        this.value = value;
    }




}
