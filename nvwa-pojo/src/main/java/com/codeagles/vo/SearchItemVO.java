package com.codeagles.vo;

import lombok.Data;

/**
 * 用于商品评价的VO
 *
 * @author hcn
 * @create 2020-01-10 9:25
 **/
@Data
public class SearchItemVO {


    private String itemId;

    private String itemName;

    private int sellCounts;

    private String imgUrl;

    private int price;


}
