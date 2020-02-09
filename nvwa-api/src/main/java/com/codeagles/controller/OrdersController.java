package com.codeagles.controller;

import com.codeagles.bo.SubmitOrderBo;
import com.codeagles.enums.EnumOrderStatus;
import com.codeagles.enums.EnumPayMethod;
import com.codeagles.service.OrderService;
import com.codeagles.utils.JSONResult;
import com.codeagles.vo.MerchantOrdersVO;
import com.codeagles.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 订单控制层
 *
 * @author hcn
 * @create 2020-01-03 11:14
 **/
@Api(value = "订单相关", tags = {"用于订单相关接口"})
@RestController
@RequestMapping(("/orders"))
public class OrdersController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestTemplate restTemplate;

    @ApiOperation(value = "根据userId查询地址列表", notes = "根据userId查询地址列表", httpMethod = "POST")
    @PostMapping("/create")
    public JSONResult create(@RequestBody SubmitOrderBo submitOrderBo, HttpServletRequest request, HttpServletResponse response) {

        System.out.println(submitOrderBo.toString());

        if (!submitOrderBo.getPayMethod().equals(EnumPayMethod.WEIXIN.type) &&
            !submitOrderBo.getPayMethod().equals(EnumPayMethod.ALIPAY.type)) {
            return JSONResult.errorMsg("支付方式不支持");
        }
        //1.创建订单
        OrderVO order = orderService.createOrder(submitOrderBo);
        String orderId = order.getOrderId();
        //2.创建订单以后，移除购物车中已结算(已提交)的商品
        /**
         * 1001
         * 2002 ===>用户已购买
         * 3003 ===>用户已购买
         * 4004
         */
        //TODO 整合redis之后，完善购物车中的已结算商品，并且需要同步到前端的cookie
//        CookieUtils.setCookie(request, response, FOOD_SHOPCART, "", true);

        //3.向支付中心发送当前订单，用于保存支付中心的订单信息
        MerchantOrdersVO merchantOrdersVO = order.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(payReturnUrl);
        //为了方便测试购买，所有的支付金额都统一改为1分钱
        merchantOrdersVO.setAmount(1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("imoocUserId", "tmp2020");
        headers.add("password", "tmp2020");

        HttpEntity<MerchantOrdersVO> entity = new HttpEntity<>(merchantOrdersVO,headers);
        ResponseEntity<JSONResult> responseEntity = restTemplate.postForEntity(paymentUrl, entity, JSONResult.class);
        JSONResult paymentResult = responseEntity.getBody();
        if (paymentResult.getStatus() != 200) {
            return JSONResult.errorMsg("支付中心订单创建失败");
        }

        return JSONResult.ok(orderId);
    }

    @PostMapping("notifyMerchantOrderPaid")
    @ApiOperation(value = "支付回调地址", notes = "支付回调地址", httpMethod = "POST")
    @ApiImplicitParam(name = "merchantOrderId", value = "merchantOrderId" , required = true)
    public int notifyMerchantOrderPaid(
           @RequestParam String merchantOrderId){

        orderService.updateOrderStatus(merchantOrderId, EnumOrderStatus.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }
}