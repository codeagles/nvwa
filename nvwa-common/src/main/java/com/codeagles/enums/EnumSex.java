package com.codeagles.enums;

/**
 * 性别枚举类
 *
 * @author hcn
 * @create 2020-01-04 11:08
 **/
public enum  EnumSex {

    woman(0, "女"),
    man(1, "男"),
    secret(2, "保密");

    public final Integer type;
    public final String value;


    EnumSex(Integer type, String value) {
        this.type = type;
        this.value = value;
    }




}
