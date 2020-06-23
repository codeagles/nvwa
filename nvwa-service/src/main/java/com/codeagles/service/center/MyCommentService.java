package com.codeagles.service.center;

import com.codeagles.bo.center.OrderItemsCommentsBO;
import com.codeagles.pojo.OrderItems;
import com.codeagles.utils.PagedGridResult;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Codeagles
 * Date: 2020/2/21
 * Time: 9:16 AM
 * <p>
 * Description:
 */
public interface MyCommentService {

    /**
     * 根据订单id查询关联的商品
     *
     * @param orderId
     * @return
     */
    public List<OrderItems> queryPendingComment(String orderId);

    /**
     * 保存用户的评论
     *
     * @param orderId
     * @param userId
     * @param commentsBOList
     */
    public void saveComments(String orderId, String userId, List<OrderItemsCommentsBO> commentsBOList);


    public PagedGridResult querymyComments(String userId, int page, int pageSize);
}
