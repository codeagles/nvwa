package com.codeagles.controller.center;

import com.codeagles.bo.center.CenterUsersBO;
import com.codeagles.pojo.Users;
import com.codeagles.service.center.CenterUserService;
import com.codeagles.utils.CookieUtils;
import com.codeagles.utils.JSONResult;
import com.codeagles.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Codeagles
 * Date: 2020/2/19
 * Time: 11:21 AM
 * <p>
 * Description: 用户中心，用户相关接口
 */
@Api(value = "用户信息接口", tags = {"用户信息相关接口"})
@RestController
@RequestMapping("userInfo")
public class CenterUserController {

    @Autowired
    private CenterUserService centerUserService;

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", httpMethod = "POST")
    @PostMapping("update")
    public JSONResult updateUserInfo(
            @ApiParam(value = "用户id", name = "userId", required = true)
            @RequestParam String userId,
            @RequestBody @Valid CenterUsersBO centerUsersBO,
            BindingResult bindingResult,
            HttpServletRequest request, HttpServletResponse response

            ){
        //判断BindingResult是否有保存错误信息，如果有，直接return
        if(bindingResult.hasErrors()){
            Map<String, String> errors = getErrors(bindingResult);
            return JSONResult.errorMap(errors);
        }

        Users users = centerUserService.updateUserInfo(userId,centerUsersBO);
        users = setNullProperty(users);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(users), true);
        //TODO 后续会增加令牌token，整合redis，分布式会话
        return JSONResult.ok();
    }

    private Map<String, String> getErrors(BindingResult bindingResult){

        Map<String, String> errorMap = new HashMap<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            //发生验证错误的某个属性
            String field = fieldError.getField();
            //验证错误的信息
            String defaultMessage = fieldError.getDefaultMessage();
            errorMap.put(field, defaultMessage);

        }
        return errorMap;
    }

    private Users setNullProperty(Users users) {
        users.setPassword(null);
        users.setMobile(null);
        users.setCreateTime(null);
        users.setUpdateTime(null);
        users.setBirthday(null);
        users.setEmail(null);
        return users;
    }

}
