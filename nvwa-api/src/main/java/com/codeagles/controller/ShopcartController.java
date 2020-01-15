package com.codeagles.controller;

import com.codeagles.bo.ShopcartBO;
import com.codeagles.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 购物车接口
 *
 * @author hcn
 * @create 2020-01-03 11:14
 **/
@Api(value = "购物车接口controller", tags = {"购物车的相关接口"})
@RestController
@RequestMapping(("/shopcart"))
public class ShopcartController {



    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("/add")
    public JSONResult add(
            @RequestParam String userId,
            @RequestBody ShopcartBO shopcartBO,
            HttpServletRequest request,
            HttpServletResponse response) {

        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("用户id不能为空");
        }

        System.out.println(shopcartBO);

        // TODO 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis
        return JSONResult.ok();
    }

}