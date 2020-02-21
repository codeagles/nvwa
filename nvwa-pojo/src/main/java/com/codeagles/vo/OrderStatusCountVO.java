package com.codeagles.vo;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: Codeagles
 * Date: 2020/2/21
 * Time: 6:57 PM
 * <p>
 * Description: 订单数量VO
 */
@Data
public class OrderStatusCountVO {

    private Integer waitPayCounts;
    private Integer waitDeliverCounts;
    private Integer waitReceiveCounts;
    private Integer waitCommentCounts;

    public OrderStatusCountVO(Integer waitPayCounts, Integer waitDeliverCount, Integer waitReceiveCounts, Integer waitCommentCounts) {
        this.waitPayCounts = waitPayCounts;
        this.waitDeliverCounts = waitDeliverCount;
        this.waitReceiveCounts = waitReceiveCounts;
        this.waitCommentCounts = waitCommentCounts;
    }
}
