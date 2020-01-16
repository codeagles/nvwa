package com.codeagles.controller;

import com.codeagles.pojo.UserAddress;
import com.codeagles.service.AddressSerivce;
import com.codeagles.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 订单控制层
 *
 * @author hcn
 * @create 2020-01-03 11:14
 **/
@Api(value = "地址相关", tags = {"用于地址相关接口"})
@RestController
@RequestMapping(("/address"))
public class AddressController {

    /**
     * 用户在确认订单页面，可以针对收获地址做如下操作：
     * 1. 查询用户的所有收货地址列表
     * 2. 新增收货地址
     * 3. 删除收货地址
     * 4. 修改收货地址
     * 5. 设置默认地址
     */
    @Autowired
    private AddressSerivce addressSerivce;


    @ApiOperation(value = "根据userId查询地址列表", notes = "根据userId查询地址列表", httpMethod = "POST")
    @PostMapping("/list")
    public JSONResult list(
            @ApiParam(name = "userId", value = "用户id", required = true, example = "xx-1001")
            @RequestParam(defaultValue = "userId", required = true) String userId
    ) {

        if(StringUtils.isBlank(userId)){
            return JSONResult.errorMsg("");
        }
        List<UserAddress> userAddresses = addressSerivce.queryAll(userId);
        return JSONResult.ok(userAddresses);
    }


}