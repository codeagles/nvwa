package com.codeagles.impl.center;

import com.codeagles.bo.center.OrderItemsCommentsBO;
import com.codeagles.enums.EnumYesOrNo;
import com.codeagles.mapper.ItemsCommentsMapperCustom;
import com.codeagles.mapper.OrderItemsMapper;
import com.codeagles.mapper.OrderStatusMapper;
import com.codeagles.mapper.OrdersMapper;
import com.codeagles.pojo.OrderItems;
import com.codeagles.pojo.OrderStatus;
import com.codeagles.pojo.Orders;
import com.codeagles.service.center.MyCommentService;
import com.codeagles.utils.PagedGridResult;
import com.codeagles.vo.MyCommentVO;
import com.github.pagehelper.PageHelper;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Codeagles
 * Date: 2020/2/21
 * Time: 9:16 AM
 * <p>
 * Description:
 */
@Service
public class MyCommentServiceImpl extends BaseService implements MyCommentService {

    @Autowired
    public OrderItemsMapper orderItemsMapper;
    @Autowired
    private Sid sid;
    @Autowired
    public ItemsCommentsMapperCustom itemsCommentsMapperCustom;
    @Autowired
    public OrdersMapper ordersMapper;
    @Autowired
    public OrderStatusMapper orderStatusMapper;


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<OrderItems> queryPendingComment(String orderId) {

        OrderItems query = new OrderItems();
        query.setOrderId(orderId);

        return orderItemsMapper.select(query);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComments(String orderId, String userId, List<OrderItemsCommentsBO> commentsBOList) {
        // 1. 保存评价,入库items_comments

        for (OrderItemsCommentsBO bo : commentsBOList) {
            bo.setCommentId(sid.nextShort());
        }

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("userId", userId);
        queryMap.put("commentList", commentsBOList);
        itemsCommentsMapperCustom.saveComments(queryMap);
        // 2. 修改订单表改为已评价 orders
        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setIsComment(EnumYesOrNo.YES.type);
        ordersMapper.updateByPrimaryKeySelective(orders);

        // 3. 修改订单状态表 修改评论时间 orders_status
        OrderStatus os = new OrderStatus();
        os.setOrderId(orderId);
        os.setCommentTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(os);

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult querymyComments(String userId, int page, int pageSize) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("userId", userId);
        PageHelper.startPage(page, pageSize);
        List<MyCommentVO> myCommentVOS = itemsCommentsMapperCustom.queryComments(queryMap);

        return setterPagedGrid(myCommentVOS, page);
    }



}
