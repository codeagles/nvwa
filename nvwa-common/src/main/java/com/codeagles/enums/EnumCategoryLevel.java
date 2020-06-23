package com.codeagles.enums;

/**
 * 性别枚举类
 *
 * @author hcn
 * @create 2020-01-04 11:08
 **/
public enum EnumCategoryLevel {

    ONE(1, "一级"),
    TWO(2, "二级"),
    THREE(3, "三级");

    public final Integer type;
    public final String value;


    EnumCategoryLevel(Integer type, String value) {
        this.type = type;
        this.value = value;
    }


}
