package com.codeagles.controller;

import com.codeagles.service.UserSerivce;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public int isExistUsername(@RequestParam String username){
        if(StringUtils.isBlank(username)){
            return 500;
        }

        boolean isExist = userSerivce.queryByUserName(username);
        if(isExist){
            return 500;
        }
        return 200;
    }
}
