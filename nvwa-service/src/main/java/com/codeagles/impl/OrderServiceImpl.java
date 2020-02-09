package com.codeagles.impl;

import com.codeagles.bo.SubmitOrderBo;
import com.codeagles.enums.EnumOrderStatus;
import com.codeagles.enums.EnumYesOrNo;
import com.codeagles.mapper.OrderItemsMapper;
import com.codeagles.mapper.OrderStatusMapper;
import com.codeagles.mapper.OrdersMapper;
import com.codeagles.pojo.*;
import com.codeagles.service.AddressSerivce;
import com.codeagles.service.ItemService;
import com.codeagles.service.OrderService;
import com.codeagles.vo.MerchantOrdersVO;
import com.codeagles.vo.OrderVO;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * order实现接口
 *
 * @author hcn
 * @create 2020-01-08 19:20
 **/
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private AddressSerivce addressSerivce;
    @Autowired
    private ItemService itemService;
    @Autowired
    private Sid sid;
    @Autowired
    private OrderItemsMapper orderItemsMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public OrderVO createOrder(SubmitOrderBo submitOrderBo) {
        String userId = submitOrderBo.getUserId();
        String addressId = submitOrderBo.getAddressId();
        String itemSpecIds = submitOrderBo.getItemSpecIds();
        Integer payMethod = submitOrderBo.getPayMethod();
        String leftMsg = submitOrderBo.getLeftMsg();
        //包邮邮费设置为0
        Integer postAomount = 0;

        //1.新订单数据保存
        String orderId = sid.nextShort();
        Orders newOrder = new Orders();
        newOrder.setId(orderId);
        newOrder.setUserId(userId);

        UserAddress userAddress = addressSerivce.queryUserAddress(userId, addressId);
        newOrder.setReceiverName(userAddress.getReceiver());
        newOrder.setReceiverMobile(userAddress.getMobile());
        newOrder.setReceiverAddress(userAddress.getProvince()+" "
                                    + userAddress.getCity()+" "
                                    + userAddress.getDistrict()+" "
                                    + userAddress.getDetail());

        newOrder.setPostAmount(postAomount);
        newOrder.setPayMethod(payMethod);
        newOrder.setLeftMsg(leftMsg);

        newOrder.setIsComment(EnumYesOrNo.NO.type);
        newOrder.setIsDelete(EnumYesOrNo.NO.type);
        newOrder.setCreatedTime(new Date());
        newOrder.setUpdatedTime(new Date());


        //2.循环根据itemSpecIds保存订单商品信息表
        String[] itemSpecIdArr = itemSpecIds.split(",");
        Integer totalAmount = 0;//商品原价
        Integer realPayAmount = 0;//实际支付价格

        for (String itemSpecId : itemSpecIdArr) {
            //2.1 根据规格id，查询具体信息
            ItemsSpec itemsSpec = itemService.queryItemSpecById(itemSpecId);
            //TODO 整合redis后，商品购买数量重新从redis的购物车中获取
            int buyCounts = 1;
            totalAmount += itemsSpec.getPriceNormal() * buyCounts;//获取购物车商品数量
            realPayAmount += itemsSpec.getPriceDiscount() * buyCounts;
            //2.2 根据规格id 获得商品信息以及商品图片
            String itemId = itemsSpec.getItemId();
            Items item = itemService.queryItemById(itemId);
            String imgUrl = itemService.queryItemMainImgById(itemId);
            //2.3 循环保存子订单数据到数据库
            String subOrderId = sid.nextShort();
            OrderItems subOrderItem = new OrderItems();
            subOrderItem.setId(subOrderId);
            subOrderItem.setOrderId(orderId);
            subOrderItem.setItemId(itemId);
            subOrderItem.setItemImg(imgUrl);
            subOrderItem.setItemName(item.getItemName());
            subOrderItem.setItemSpecId(itemSpecId);
            subOrderItem.setItemSpecName(itemsSpec.getName());
            subOrderItem.setPrice(itemsSpec.getPriceDiscount());
            subOrderItem.setBuyCounts(buyCounts);
            orderItemsMapper.insert(subOrderItem);
            //2.4 用户提交订单以后，规格表中扣除库存
            itemService.decreaseItemSpecStock(itemSpecId, buyCounts);
        }
        newOrder.setTotalAmount(totalAmount);
        newOrder.setRealPayAmount(realPayAmount);
        ordersMapper.insert(newOrder);
        //3.保存订单状态表
        OrderStatus waitPayOrderStatus = new OrderStatus();
        waitPayOrderStatus.setOrderId(orderId);
        waitPayOrderStatus.setOrderStatus(EnumOrderStatus.WAIT_PAY.type);
        waitPayOrderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(waitPayOrderStatus);

        //4. 构建商户订单，用于传给支付中心
        MerchantOrdersVO merchantOrdersVO = new MerchantOrdersVO();
        merchantOrdersVO.setMerchantOrderId(orderId);
        merchantOrdersVO.setMerchantUserId(userId);
        merchantOrdersVO.setAmount(realPayAmount+postAomount);
        merchantOrdersVO.setPayMethod(payMethod);
        //5. 构建自定义orderVO
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setMerchantOrdersVO(merchantOrdersVO);
        return orderVO;

    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateOrderStatus(String orderId, Integer orderStatus) {
       OrderStatus paidStatus = new OrderStatus();
       paidStatus.setOrderId(orderId);
       paidStatus.setOrderStatus(orderStatus);
       paidStatus.setPayTime(new Date());

       orderStatusMapper.updateByPrimaryKeySelective(paidStatus);
    }
}
