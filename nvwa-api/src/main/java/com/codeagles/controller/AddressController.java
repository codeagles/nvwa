package com.codeagles.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单控制层
 *
 * @author hcn
 * @create 2020-01-03 11:14
 **/
@Api(value = "地址相关", tags = {"用于地址相关接口"})
@RestController
@RequestMapping(("/index"))
public class AddressController {

    /**
     * 用户在确认订单页面，可以针对收获地址做如下操作：
     * 1. 查询用户的所有收货地址列表
     * 2. 新增收货地址
     * 3. 删除收货地址
     * 4. 修改收货地址
     * 5. 设置默认地址
     */
}