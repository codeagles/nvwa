package com.codeagles.vo;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: Codeagles
 * Date: 2020/2/20
 * Time: 11:16 AM
 * <p>
 * Description: 嵌套的订单VO类
 */
@Data
public class MySubOrderItemVO {

    private String itemId;
    private String itemImg;
    private String itemName;
    private String itemSpecName;
    private String itemSpecId;
    private Integer buyCounts;
    private Integer price;


}
