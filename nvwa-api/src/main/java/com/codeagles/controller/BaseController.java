package com.codeagles.controller;

import org.springframework.stereotype.Controller;

import java.io.File;

/**
 * @author hcn
 * @create 2020-01-11 13:58
 **/
@Controller
public class BaseController {

    public static final Integer COMMON_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;
    public static final String FOOD_SHOPCART = "shopcart";

    //支付中心的调用地址
    String paymentUrl = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";
    //微信支付成功-> 支付中心 -> 现在商户平台
    //回调通知URL
//    String payReturnUrl = "http://localhost:8080/orders/notifyMerchantOrderPaid";
    String payReturnUrl = "http://7y76uq.natappfree.cc/orders/notifyMerchantOrderPaid";


    //用户上传头像的位置
    public static final String IMAGE_USER_FACE_LOCATION =
            File.separator +"Users" +
            File.separator +"codeagles" +
            File.separator +"IdeaProjects" +
            File.separator +"new" +
            File.separator +"images" +
            File.separator +"foodie" +
            File.separator +"faces";

}
