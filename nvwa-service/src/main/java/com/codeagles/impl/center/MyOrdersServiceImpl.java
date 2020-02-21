package com.codeagles.impl.center;

import com.codeagles.enums.EnumOrderStatus;
import com.codeagles.enums.EnumYesOrNo;
import com.codeagles.mapper.OrderMapperCustom;
import com.codeagles.mapper.OrderStatusMapper;
import com.codeagles.mapper.OrdersMapper;
import com.codeagles.pojo.OrderStatus;
import com.codeagles.pojo.Orders;
import com.codeagles.service.center.MyOrdersService;
import com.codeagles.utils.PagedGridResult;
import com.codeagles.vo.MyOrdersVO;
import com.codeagles.vo.OrderStatusCountVO;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户服务实现类
 *
 * @author hcn
 * @create 2020-01-03 10:12
 **/
@Service
public class MyOrdersServiceImpl extends BaseService implements MyOrdersService {

    @Autowired
    public OrderMapperCustom orderMapperCustom;
    @Autowired
    public OrderStatusMapper orderStatusMapper;
    @Autowired
    public OrdersMapper ordersMapper;


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryMyOrder(String userId, Integer orderStatus, Integer pageSize, Integer page) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("userId", userId);
        if(orderStatus != null){
            paramsMap.put("orderStatus", orderStatus);
        }
        PageHelper.startPage(page, pageSize);
        List<MyOrdersVO> myOrdersVOS = orderMapperCustom.queryMyOrders(paramsMap);

        return this.setterPagedGrid(myOrdersVOS,page);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateOrderStatus(String orderId, Integer orderStatus) {
        OrderStatus paidStatus = new OrderStatus();
        paidStatus.setOrderId(orderId);
        paidStatus.setOrderStatus(orderStatus);
        paidStatus.setDeliverTime(new Date());

        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        criteria.andEqualTo("orderStatus", EnumOrderStatus.WAIT_DELIVER.type);

        orderStatusMapper.updateByPrimaryKeySelective(paidStatus);
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Orders queryMyOrder(String userId, String orderId) {
        Orders orders = new Orders();
        orders.setUserId(userId);
        orders.setId(orderId);
        orders.setIsDelete(EnumYesOrNo.NO.type);
        ordersMapper.selectOne(orders);
        return orders;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean updateReceiveOrderStatus(String orderId) {

        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderStatus(EnumOrderStatus.SUCCESS.type);
        orderStatus.setSuccessTime(new Date());

        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId", orderId);
        criteria.andEqualTo("orderStatus",EnumOrderStatus.WAIT_RECEIVE.type);
        int i = orderStatusMapper.updateByExampleSelective(orderStatus, example);
        return i == 1 ? true : false;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean deleteOrder(String userId, String orderId) {
        Orders updateOrder = new Orders();
        updateOrder.setIsDelete(EnumYesOrNo.YES.type);
        updateOrder.setUpdatedTime(new Date());

        Example example = new Example(Orders.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", orderId);
        criteria.andEqualTo("userId",userId);
        int i = ordersMapper.updateByExampleSelective(updateOrder, example);
        return i == 1 ? true : false;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public OrderStatusCountVO getOrderStatusCounts(String userId) {

        Map<String , Object> paramsMap = new HashMap<>();
        paramsMap.put("userId", userId);
        paramsMap.put("orderStatus", EnumOrderStatus.WAIT_PAY.type);
        int waitPayCount = orderMapperCustom.getMyOrderStatusCounts(paramsMap);

        paramsMap.put("orderStatus", EnumOrderStatus.WAIT_DELIVER.type);
        int waitDeliverCount = orderMapperCustom.getMyOrderStatusCounts(paramsMap);

        paramsMap.put("orderStatus", EnumOrderStatus.WAIT_RECEIVE.type);
        int waitReceiveCount = orderMapperCustom.getMyOrderStatusCounts(paramsMap);

        paramsMap.put("orderStatus", EnumOrderStatus.SUCCESS.type);
        paramsMap.put("isComment", EnumYesOrNo.NO.type);
        int waitCommentCount = orderMapperCustom.getMyOrderStatusCounts(paramsMap);

        OrderStatusCountVO countVO = new OrderStatusCountVO(waitPayCount,waitDeliverCount,waitReceiveCount,waitCommentCount);
        return countVO;
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult getOrderTrend(String userId, Integer pageSize, Integer page) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        PageHelper.startPage(page, pageSize);
        List<OrderStatus> myOrderTrend = orderMapperCustom.getMyOrderTrend(map);
        return setterPagedGrid(myOrderTrend, page);
    }
}
