package com.codeagles.service;

import com.codeagles.bo.SubmitOrderBo;
import com.codeagles.pojo.OrderStatus;
import com.codeagles.vo.OrderVO;

/**
 * 订单接口
 * @author codeagles
 *
 */
public interface OrderService {
    /**
     * 用于创建订单相关信息
     * @param submitOrderBo
     */
    public OrderVO createOrder(SubmitOrderBo submitOrderBo);

    /**
     * 修改订单
     * @param orderId
     * @param orderStatus
     */
    public void updateOrderStatus(String orderId, Integer orderStatus);

    /**
     * 查询订单状态
     * @param orderId
     * @return
     */
    public OrderStatus queryOrederStatusInfo(String orderId);
}
