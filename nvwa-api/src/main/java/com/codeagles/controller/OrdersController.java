package com.codeagles.controller;

import com.codeagles.bo.SubmitOrderBo;
import com.codeagles.enums.EnumPayMethod;
import com.codeagles.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单控制层
 *
 * @author hcn
 * @create 2020-01-03 11:14
 **/
@Api(value = "订单相关", tags = {"用于订单相关接口"})
@RestController
@RequestMapping(("/orders"))
public class OrdersController {




    @ApiOperation(value = "根据userId查询地址列表", notes = "根据userId查询地址列表", httpMethod = "POST")
    @PostMapping("/create")
    public JSONResult create(@RequestBody SubmitOrderBo submitOrderBo) {

        System.out.println(submitOrderBo.toString());

        if (!submitOrderBo.getPayMethod().equals(EnumPayMethod.WEIXIN.type) ||
            !submitOrderBo.getPayMethod().equals(EnumPayMethod.ALIPAY.type)) {
            return JSONResult.errorMsg("支付方式不支持");
        }
        //1.创建订单
        //2.创建订单以后，移除购物车中已结算(已提交)的商品
        //3.向支付中心发送当前订单，用于保存支付中心的订单信息
        return JSONResult.ok(submitOrderBo);
    }


}