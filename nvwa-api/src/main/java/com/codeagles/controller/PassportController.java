package com.codeagles.controller;

import com.codeagles.bo.UserBO;
import com.codeagles.service.UserSerivce;
import com.codeagles.utils.JSONResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制层
 *
 * @author hcn
 * @create 2020-01-03 11:14
 **/
@RestController
@RequestMapping(("/passport"))
public class PassportController {

    @Autowired
    private UserSerivce userSerivce;

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
