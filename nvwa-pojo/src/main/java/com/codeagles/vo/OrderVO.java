package com.codeagles.vo;

import com.codeagles.bo.ShopcartBO;
import lombok.Data;

import java.util.List;

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

    private List<ShopcartBO> toBeRemovedShopcatdList;
}
