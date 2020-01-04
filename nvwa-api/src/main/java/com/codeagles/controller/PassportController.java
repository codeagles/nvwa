package com.codeagles.controller;

import com.codeagles.bo.UserBO;
import com.codeagles.service.UserSerivce;
import com.codeagles.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制层
 *
 * @author hcn
 * @create 2020-01-03 11:14
 **/
@Api(value = "注册登录", tags = {"用于注册登录的相关接口"})
@RestController
@RequestMapping(("/passport"))
public class PassportController {

    @Autowired
    private UserSerivce userSerivce;

    @ApiOperation(value = "用户名是否存在", notes = "用户名是否存在", httpMethod = "GET")
    @GetMapping("/isExistUsername")
    public JSONResult isExistUsername(@RequestParam String username){
        if(StringUtils.isBlank(username)){
            return JSONResult.errorMsg("用户名不能为空");
        }

        boolean isExist = userSerivce.queryByUserName(username);
        if(isExist){
            return JSONResult.errorMsg("用户名已经存在");
        }
        return JSONResult.ok();
    }
    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    @PostMapping("/regist")
    public JSONResult regist(@RequestBody UserBO userBO){

        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPassword = userBO.getConfirmPassword();

        //0. 判断用户名和密码必须不为空
        if(StringUtils.isBlank(username) ||
                StringUtils.isBlank(password)||
                StringUtils.isBlank(confirmPassword)){
            return JSONResult.errorMsg("用户名或者密码不能为空");
        }

        //1. 查询用户名是否存在
        boolean isExist = userSerivce.queryByUserName(username);
        if(isExist){
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
        userSerivce.createUser(userBO);
        return JSONResult.ok();

    }
}
