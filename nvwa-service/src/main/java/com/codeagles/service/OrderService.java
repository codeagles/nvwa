package com.codeagles.service;

import com.codeagles.bo.SubmitOrderBo;

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
    public void createOrder(SubmitOrderBo submitOrderBo);
}
