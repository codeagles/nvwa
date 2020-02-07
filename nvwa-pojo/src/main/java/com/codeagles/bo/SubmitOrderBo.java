package com.codeagles.bo;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: Codeagles
 * Date: 2020/2/4
 * Time: 10:14 AM
 * <p>
 * Description: 提交订单BO类
 */
@Data
public class SubmitOrderBo {

    private String userId;
    private String itemSpecIds;
    private String addressId;
    private Integer payMethod;
    private String leftMsg;


}
