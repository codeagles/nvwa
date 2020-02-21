package com.codeagles.controller.center;

import com.codeagles.controller.BaseController;
import com.codeagles.enums.EnumOrderStatus;
import com.codeagles.utils.JSONResult;
import com.codeagles.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: Codeagles
 * Date: 2020/2/20
 * Time: 11:43 AM
 * <p>
 * Description:
 */
@Api(value = "用户中心-我的订单", tags = {"我的订单相关接口"})
@RestController
@RequestMapping("myorders")
public class OrderController extends BaseController {


    @ApiOperation(value = "我的订单列表", notes = "我的订单列表", httpMethod = "POST")
    @PostMapping("/query")
    public JSONResult search(
            @ApiParam(name = "userId", value = "用户id", required = true)
                    String userId,
            @ApiParam(name = "orderStatus", value = "订单状态", required = false)
                    Integer orderStatus,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
                    Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
                    Integer pageSize) {


        if(StringUtils.isBlank(userId)){
            return JSONResult.errorMsg(null);
        }
        if(page == null){
            page = 1;
        }
        if(pageSize == null){
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult pagedGridResult = myOrdersService.queryMyOrder(userId,
                orderStatus, pageSize, page);
        return JSONResult.ok(pagedGridResult);
    }


    //商家没有发货鍴，只是进行模拟发货
    @GetMapping("deliver")
    @ApiOperation(value = "模拟发货", notes = "模拟发货", httpMethod = "GET")
    @ApiImplicitParam(name = "orderId", value = "orderId", required = true)
    public JSONResult deliver(@RequestParam String orderId) {
        myOrdersService.updateOrderStatus(orderId, EnumOrderStatus.WAIT_RECEIVE.type);
        return JSONResult.ok();
    }


    @PostMapping("confirmReceive")
    @ApiOperation(value = "确认收货", notes = "确认收货", httpMethod = "POST")
    public JSONResult confirmReveive(@RequestParam String orderId, @RequestParam String userId) {

        JSONResult check = checkUserOrder(userId,orderId);
        if(check.getStatus() != HttpStatus.OK.value()){
            return check;
        }

        boolean res = myOrdersService.updateReceiveOrderStatus(orderId);
        if(!res){
            return JSONResult.errorMsg("订单确认收货失败");
        }
        return JSONResult.ok();
    }

    @PostMapping("delete")
    @ApiOperation(value = "删除订单", notes = "删除订单", httpMethod = "POST")
    public JSONResult delete(@RequestParam String orderId, @RequestParam String userId) {
        JSONResult check = checkUserOrder(userId,orderId);
        if(check.getStatus() != HttpStatus.OK.value()){
            return check;
        }

        boolean res = myOrdersService.deleteOrder(userId,orderId);
        if(!res){
            return JSONResult.errorMsg("订单删除失败");
        }
        return JSONResult.ok();
    }


}

