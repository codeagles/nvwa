package com.codeagles.vo;

import lombok.Data;

/**
 * 购物车BO类
 *
 * @author hcn
 * @create 2020-01-15 9:58
 **/
@Data
public class ShopcartVO {
    private String itemId;
    private String itemImgUrl;
    private String itemName;
    private String specId;
    private String specName;
    private String priceDiscount;
    private String priceNormal;

}
