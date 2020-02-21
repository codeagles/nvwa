package com.codeagles.controller.center;

import com.codeagles.bo.center.OrderItemsCommentsBO;
import com.codeagles.controller.BaseController;
import com.codeagles.enums.EnumYesOrNo;
import com.codeagles.pojo.OrderItems;
import com.codeagles.pojo.Orders;
import com.codeagles.service.center.MyCommentService;
import com.codeagles.utils.JSONResult;
import com.codeagles.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Codeagles
 * Date: 2020/2/21
 * Time: 9:14 AM
 * <p>
 * Description:
 */
@Api(value = "我的评价", tags = {"订单评价相关接口"})
@RestController
@RequestMapping("mycomments")
public class CommentController extends BaseController {


    @Autowired
    public MyCommentService myCommentService;

    @ApiOperation(value = "查询订单评价", notes = "查询订单评价", httpMethod = "POST")
    @PostMapping("pending")
    public JSONResult pending(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId
    ){
        //判断用户是否和订单关联
        JSONResult check = checkUserOrder(userId,orderId);
        if(check.getStatus() != HttpStatus.OK.value()){
            return check;
        }
        //判断订单是否已经评价
        Orders data = (Orders) check.getData();
        if (EnumYesOrNo.YES.type.equals(data.getIsComment())) {
            return JSONResult.errorMsg("该笔订单已经评价");
        }

        List<OrderItems> orderItems = myCommentService.queryPendingComment(orderId);
        return JSONResult.ok(orderItems);

    }

    @ApiOperation(value = "保存评论列表", notes = "保存评论列表", httpMethod = "POST")
    @PostMapping("saveList")
    public JSONResult saveList(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId,
            @ApiParam(name = "commentList", value = "评论列表", required = true)
            @RequestBody List<OrderItemsCommentsBO> commentList
    ){
        //判断用户是否和订单关联
        JSONResult check = checkUserOrder(userId,orderId);
        if(check.getStatus() != HttpStatus.OK.value()){
            return check;
        }
        //判断评论内容list不能为空
        if (commentList == null || commentList.isEmpty() || commentList.size()==0) {
            return JSONResult.errorMsg("评论内容不能为空");
        }
        myCommentService.saveComments(orderId,userId,commentList);
        return JSONResult.ok();

    }


    @ApiOperation(value = "我的评价列表", notes = "我的评价列表", httpMethod = "POST")
    @PostMapping("query")
    public JSONResult query(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = true)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "每页的条数", required = true)
            @RequestParam Integer pageSize
    ){
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg(null);
        }
        if(page == null){
            page = 1;
        }
        if (pageSize == null){
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult pagedGridResult = myCommentService.querymyComments(userId, page, pageSize);

        return JSONResult.ok(pagedGridResult);

    }
}
