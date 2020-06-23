package com.codeagles.controller.center;

import com.codeagles.pojo.Users;
import com.codeagles.service.center.CenterUserService;
import com.codeagles.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 * User: Codeagles
 * Date: 2020/2/19
 * Time: 10:25 AM
 * <p>
 * Description:用户中心
 */
@Api(value = "center - 用户中心", tags = {"用户中心展示的相关接口"})
@RestController
@RequestMapping("center")
public class CenterController {

    @Autowired
    private CenterUserService centerUserService;


    @ApiOperation(value = "获取用户信息", notes = "获取用户信息", httpMethod = "GET")
    @GetMapping("userInfo")
    public JSONResult userInfo(
            @ApiParam(value = "用户id", name = "userId", required = true)
            @RequestParam String userId) {
        Users users = centerUserService.queryUserInfo(userId);
        return JSONResult.ok(users);
    }


}

