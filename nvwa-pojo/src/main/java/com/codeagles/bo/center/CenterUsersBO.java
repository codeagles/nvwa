package com.codeagles.bo.center;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Codeagles
 * Date: 2020/2/19
 * Time: 11:24 AM
 * <p>
 * Description:
 */
@ApiModel(value = "用户对象", description = "从客户端，由用户传入的数据封装在此处的entity中")
@Data
public class CenterUsersBO {

    @ApiModelProperty(value = "用户名", name = "username", example = "codeagles", required = true)
    private String username;
    @ApiModelProperty(value = "密码", name = "password", example = "000000", required = true)
    private String password;
    @ApiModelProperty(value = "确认密码", name = "coxnfirmPassword", example = "000000", required = false)
    private String confirmPassword;

    @NotBlank(message = "昵称不能为空")
    @Length(max = 12, message = "昵称不能超过12位")
    @ApiModelProperty(value = "用户昵称", name = "nickname", example = "杰森", required = false)
    private String nickname;

    @NotBlank(message = "姓名不能为空")
    @Length(max = 12, message = "昵称不能超过12位")
    @ApiModelProperty(value = "真实姓名", name = "realname", example = "张三", required = false)
    private String realname;
    //    @ApiModelProperty(value = "", name = "coxnfirmPassword", example = "000000", required = false)
//    private String face;
    @Pattern(regexp = "^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\\d{8})$", message = "电话号码格式不正确")
    @ApiModelProperty(value = "手机号码", name = "mobile", example = "13911111111", required = false)
    private String mobile;

    @Email
    @ApiModelProperty(value = "电子邮件", name = "email", example = "qq@qq.com", required = false)
    private String email;

    @Min(value = 0, message = "性别选择不正确")
    @Max(value = 2, message = "性别选择不正确")
    @ApiModelProperty(value = "性别", name = "sex", example = "0:女 1:男 2:保密", required = false)
    private Integer sex;
    @ApiModelProperty(value = "生日", name = "birthday", example = "1990-01-01", required = false)
    private Date birthday;
}
