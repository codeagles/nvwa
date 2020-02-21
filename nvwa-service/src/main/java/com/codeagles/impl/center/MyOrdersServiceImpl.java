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
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
public class MyOrdersServiceImpl implements MyOrdersService {

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


    private PagedGridResult setterPagedGrid( List<?> list,int page){
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageList.getPages());
        grid.setRecords(pageList.getTotal());
        return grid;
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
}
