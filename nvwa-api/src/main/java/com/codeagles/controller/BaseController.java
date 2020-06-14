package com.codeagles.controller;

import com.codeagles.pojo.Orders;
import com.codeagles.pojo.Users;
import com.codeagles.service.center.MyOrdersService;
import com.codeagles.utils.JSONResult;
import com.codeagles.utils.RedisOperator;
import com.codeagles.vo.UsersVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.util.UUID;

/**
 * @author hcn
 * @create 2020-01-11 13:58
 **/
@Controller
public class BaseController {

    @Autowired
    public MyOrdersService myOrdersService;
    @Autowired
    private RedisOperator redisOperator;


    public static final Integer COMMON_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;
    public static final String FOOD_SHOPCART = "shopcart";
    public static final String REDIS_USER_TOKEN = "redis_user_token";




    //用户上传头像的位置
    public static final String IMAGE_USER_FACE_LOCATION =
            File.separator +"Users" +
            File.separator +"codeagles" +
            File.separator +"IdeaProjects" +
            File.separator +"new" +
            File.separator +"images" +
            File.separator +"foodie" +
            File.separator +"faces";


    //用于校验用户订单关联关系
    public JSONResult checkUserOrder(String userId, String orderId){
        Orders orders = myOrdersService.queryMyOrder(userId, orderId);

        if(orders == null){
            return JSONResult.errorMsg("订单不存在");
        }
        return JSONResult.ok(orders);

    }


    public UsersVO convertUsersVO(Users users){
        //实现用户的redis会话
        String uniqueToken = UUID.randomUUID().toString().trim();
        redisOperator.set(REDIS_USER_TOKEN+":"+users.getId(),uniqueToken);

        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(users, usersVO);
        usersVO.setUserUniqueToken(uniqueToken);
        return usersVO;
    }
}
