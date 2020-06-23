package com.codeagles.controller;

import com.codeagles.bo.ShopcartBO;
import com.codeagles.bo.UserBO;
import com.codeagles.pojo.Users;
import com.codeagles.service.UserSerivce;
import com.codeagles.utils.*;
import com.codeagles.vo.UsersVO;
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
 * 用户控制层
 *
 * @author hcn
 * @create 2020-01-03 11:14
 **/
@Api(value = "注册登录", tags = {"用于注册登录的相关接口"})
@RestController
@RequestMapping(("/passport"))
public class PassportController extends BaseController {

    @Autowired
    private UserSerivce userSerivce;
    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "用户名是否存在", notes = "用户名是否存在", httpMethod = "GET")
    @GetMapping("/isExistUsername")
    public JSONResult isExistUsername(@RequestParam String username) {
        if (StringUtils.isBlank(username)) {
            return JSONResult.errorMsg("用户名不能为空");
        }

        boolean isExist = userSerivce.queryByUserName(username);
        if (isExist) {
            return JSONResult.errorMsg("用户名已经存在");
        }
        return JSONResult.ok();
    }

    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    @PostMapping("/regist")
    public JSONResult regist(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response) {

        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPassword = userBO.getConfirmPassword();

        //0. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password) ||
                StringUtils.isBlank(confirmPassword)) {
            return JSONResult.errorMsg("用户名或者密码不能为空");
        }

        //1. 查询用户名是否存在
        boolean isExist = userSerivce.queryByUserName(username);
        if (isExist) {
            return JSONResult.errorMsg("用户名已经存在");
        }
        //2. 密码长度不能少于6位
        if (password.length() < 6) {
            return JSONResult.errorMsg("密码长度不能少于6位");
        }
        //3. 判断两次密码是否一致
        if (!password.equals(confirmPassword)) {
            return JSONResult.errorMsg("两次密码不一致");
        }
        //4. 实现注册
        Users users = userSerivce.createUser(userBO);

//        users = setNullProperty(users);
        UsersVO usersVO = convertUsersVO(users);

        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(usersVO), true);
        // 同步购物车数据
        synchShopcarData(users.getId(), request, response);

        return JSONResult.ok();

    }

    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public JSONResult login(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String username = userBO.getUsername();
        String password = userBO.getPassword();

        //0. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password)) {
            return JSONResult.errorMsg("用户名或者密码不能为空");
        }

        //1. 实现注册
        Users users = userSerivce.queryUserForLogin(username, MD5Utils.getMD5Str(password));
        if (users == null) {
            return JSONResult.errorMsg("用户名或者密码不正确");
        }

//        users = setNullProperty(users);
        UsersVO usersVO = convertUsersVO(users);

        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(usersVO), true);

        // 同步购物车数据
        synchShopcarData(users.getId(), request, response);
        return JSONResult.ok(users);
    }

    //注册登录成功后，同步cookie和redis的购物车数据
    private void synchShopcarData(String userId, HttpServletRequest request, HttpServletResponse response) {
        /**
         *  1、redis中无数据，如果cookie中的购物车为空，那么这个时候不作任何处理；
         *                  如果cookie中的购物车不为空，此时直接放入redis中
         *  2、redis中有数据，如果cookie中的购物车为空，那么直接把redis的购物车覆盖本地的cookie
         *                  如果cookie中的购物车不为空，如果coolie中的某个商品redis中存在，
         *                      则以cookie为主，删除redis中的，把cookie中的商品直接覆盖redis中(参考京东，而淘宝是需要登录)
         *
         *  3、同步到redis中去了以后，覆盖本地cookie购物车的数据，保证本地购物车的数据是同步最新的
         */

        //从redis中获取购物车
        String shopcartJsonRedis = redisOperator.get(FOOD_SHOPCART + ":" + userId);

        //从cookie中获取购物车
        String shopcartStrCookie = CookieUtils.getCookieValue(request, FOOD_SHOPCART, true);
        if (StringUtils.isBlank(shopcartJsonRedis)) {
            //redis为空，cookie不为空，直接把cookie中的数据放入redis
            if (StringUtils.isNotBlank(shopcartStrCookie)) {
                redisOperator.set(FOOD_SHOPCART + ":" + userId, shopcartStrCookie);
            }
        } else {
            //redis不为空，cookie不为空，合并cookie和redis中购物车种的数据，若同一商品则覆盖redis
            if (StringUtils.isNotBlank(shopcartStrCookie)) {
                /**
                 * 1. 已经存在的，把cookie中对应的数量，覆盖redis(参考京东)
                 * 2. 该项商品应标标记为待删除，统一放入一个待删除list中
                 * 3. 从cookie中清理所有的待删除list
                 * 4. 合并redis和cookie中的数据
                 * 5. 更新到redis和cookie中，保证一致且最新的数据
                 */
                List<ShopcartBO> shopcartsReids = JsonUtils.jsonToList(shopcartJsonRedis, ShopcartBO.class);
                List<ShopcartBO> shopcartsCookie = JsonUtils.jsonToList(shopcartStrCookie, ShopcartBO.class);

                List<ShopcartBO> pendingDeleteList = new ArrayList<>();
                for (ShopcartBO redisShopcart : shopcartsReids) {
                    String redisSpecId = redisShopcart.getSpecId();

                    for (ShopcartBO cookieShopcart : shopcartsCookie) {
                        String cookieSpectId = cookieShopcart.getSpecId();
                        if (cookieSpectId.equals(redisSpecId)) {
                            //覆盖购买数量，不累加
                            redisShopcart.setBuyCounts(cookieShopcart.getBuyCounts());
                            //把cookieShopcart放入待删除列表，用于最后的删除与合并
                            pendingDeleteList.add(cookieShopcart);
                        }
                    }
                }
                //从现有cookie中删除对应的覆盖过的商品数据
                shopcartsCookie.removeAll(pendingDeleteList);
                //合并两个list
                shopcartsReids.addAll(shopcartsCookie);
                //更新到redis和cookie
                CookieUtils.setCookie(request, response, FOOD_SHOPCART, JsonUtils.objectToJson(shopcartsReids), true);
                redisOperator.set(FOOD_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopcartsReids));

            } else {
                //redis不为空，cookie为空，直接把redis覆盖cookie
                CookieUtils.setCookie(request, response, FOOD_SHOPCART, shopcartJsonRedis, true);
            }
        }


    }
//    //新增UserVO类转换此方法飞废弃
//    private Users setNullProperty(Users users) {
//        users.setPassword(null);
//        users.setMobile(null);
//        users.setCreateTime(null);
//        users.setUpdateTime(null);
//        users.setBirthday(null);
//        users.setEmail(null);
//        return users;
//    }


    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public JSONResult logout(String userId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //清楚用户相关cookie
        CookieUtils.deleteCookie(request, response, "user");

        //用户退出登录，需要清空购物车
        CookieUtils.deleteCookie(request, response, FOOD_SHOPCART);

        //分布式会话中需要清除用户数据
        redisOperator.del(REDIS_USER_TOKEN + ":" + userId);
        return JSONResult.ok();
    }
}