package com.codeagles.enums;

/**
 * 性别枚举类
 *
 * @author hcn
 * @create 2020-01-04 11:08
 **/
public enum EnumCommentLevel {

    GOOD(1, "好评"),
    NORMAL(2, "正常"),
    BAD(3, "差评");


    public final Integer type;
    public final String value;


    EnumCommentLevel(Integer type, String value) {
        this.type = type;
        this.value = value;
    }


}
