package com.codeagles.enums;

/**
 * 性别枚举类
 *
 * @author hcn
 * @create 2020-01-04 11:08
 **/
public enum EnumYesOrNo {

    NO(0, "否"),
    YES(1, "是");


    public final Integer type;
    public final String value;


    EnumYesOrNo(Integer type, String value) {
        this.type = type;
        this.value = value;
    }




}
