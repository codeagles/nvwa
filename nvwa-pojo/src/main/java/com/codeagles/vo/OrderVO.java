package com.codeagles.vo;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: Codeagles
 * Date: 2020/2/9
 * Time: 9:12 PM
 * <p>
 * Description:
 */
@Data
public class OrderVO {

    private String orderId;

    private MerchantOrdersVO merchantOrdersVO;
}
