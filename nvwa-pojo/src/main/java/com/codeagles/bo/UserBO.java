package com.codeagles.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户BO类
 *
 * @author hcn
 * @create 2020-01-04 10:41
 **/
@ApiModel(value = "用户对象BO",description = "从前端由用户传入的数据封装在此Entiy")
@Data
public class UserBO {

    @ApiModelProperty(value = "用户名", name = "username", example = "codeagles", required = true)
    private String username;
    @ApiModelProperty(value = "密码", name = "password", example = "000000", required = true)
    private String password;
    @ApiModelProperty(value = "确认密码", name = "coxnfirmPassword", example = "000000", required = false)
    private String confirmPassword;

}
