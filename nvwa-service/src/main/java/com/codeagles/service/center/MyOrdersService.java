package com.codeagles.service.center;

import com.codeagles.pojo.Orders;
import com.codeagles.utils.PagedGridResult;
import com.codeagles.vo.OrderStatusCountVO;

/**
 * 订单服务层
 *
 * @author hcn
 * @create 2020-01-02 17:51
 **/
public interface MyOrdersService {

    /**
     * 查询我的订单 列表
     *
     * @param userId
     * @param orderStatus
     * @param pageSize
     * @param page
     * @return
     */
    public PagedGridResult queryMyOrder(String userId,
                                        Integer orderStatus,
                                        Integer pageSize,
                                        Integer page);

    /**
     * 修改订单
     *
     * @param orderId
     * @param orderStatus
     */
    public void updateOrderStatus(String orderId, Integer orderStatus);

    /**
     * 查询我的订单
     *
     * @param userId
     * @param orderId
     * @return
     */
    public Orders queryMyOrder(String userId, String orderId);

    /**
     * 更新订单状态 确认收货
     *
     * @param orderId
     * @return
     */
    public boolean updateReceiveOrderStatus(String orderId);

    /**
     * 删除订单(逻辑删除)
     *
     * @param userId
     * @param orderId
     * @return
     */
    public boolean deleteOrder(String userId, String orderId);

    /**
     * 查询订单数
     *
     * @param userId
     * @return
     */
    public OrderStatusCountVO getOrderStatusCounts(String userId);

    /**
     * 获得分页的订单动向
     *
     * @param userId
     * @param pageSize
     * @param page
     * @return
     */
    public PagedGridResult getOrderTrend(String userId,
                                         Integer pageSize,
                                         Integer page);

}
