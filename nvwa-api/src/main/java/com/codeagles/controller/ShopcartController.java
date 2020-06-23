package com.codeagles.controller;

import com.codeagles.bo.ShopcartBO;
import com.codeagles.utils.JSONResult;
import com.codeagles.utils.JsonUtils;
import com.codeagles.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车接口
 *
 * @author hcn
 * @create 2020-01-03 11:14
 **/
@Api(value = "购物车接口controller", tags = {"购物车的相关接口"})
@RestController
@RequestMapping(("/shopcart"))
public class ShopcartController extends BaseController {

    @Autowired
    private RedisOperator redisOperator;

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

        // 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis
        // 需要判断当前购物车中的商品包含已经存在的商品，如果存在则累加购买数量
        String shopcartJson = redisOperator.get(FOOD_SHOPCART + ":" + userId);
        List<ShopcartBO> shopcartBOList = null;
        if (StringUtils.isNotBlank(shopcartJson)) {
            //redis中已经有购物车了
            shopcartBOList = JsonUtils.jsonToList(shopcartJson, ShopcartBO.class);
            //判断购物车中商品是否已经存在，如果有的话counts累加
            boolean isHaving = false;
            for (ShopcartBO bo : shopcartBOList) {
                String temSpecId = bo.getSpecId();
                if (temSpecId.equals(shopcartBO.getSpecId())) {
                    bo.setBuyCounts(bo.getBuyCounts() + shopcartBO.getBuyCounts());
                    isHaving = true;
                }
            }
            if (!isHaving) {
                shopcartBOList.add(shopcartBO);
            }
        } else {
            //没有购物车 创建一个
            shopcartBOList = new ArrayList<>();
            //将商品直接添加进购物车
            shopcartBOList.add(shopcartBO);
        }
        redisOperator.set(FOOD_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopcartBOList));

        return JSONResult.ok();
    }


    @ApiOperation(value = "删除购物车中指定商品", notes = "删除购物车中指定商品", httpMethod = "POST")
    @PostMapping("/del")
    public JSONResult del(
            @RequestParam String userId,
            @RequestParam String itemSpecId,
            HttpServletRequest request,
            HttpServletResponse response) {

        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("用户id不能为空");
        }

        // 前端用户在登录的情况下，删除购物车中数据，会到redis中同时在后端同步删除redis购物车中商品
        String shopcartJson = redisOperator.get(FOOD_SHOPCART + ":" + userId);
        if (StringUtils.isNotBlank(shopcartJson)) {
            //redis中已经有购物车了
            List<ShopcartBO> shopcartBOList = JsonUtils.jsonToList(shopcartJson, ShopcartBO.class);
            for (ShopcartBO shopcartBO : shopcartBOList) {
                String temSpectId = shopcartBO.getSpecId();
                if (temSpectId.equals(itemSpecId)) {
                    shopcartBOList.remove(shopcartBO);
                    break;
                }
            }
            //覆盖现有的redis购物车
            redisOperator.set(FOOD_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopcartBOList));
        }
        return JSONResult.ok();
    }
}