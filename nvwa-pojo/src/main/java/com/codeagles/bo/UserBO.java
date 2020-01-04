package com.codeagles.bo;

import lombok.Data;

/**
 * 用户BO类
 *
 * @author hcn
 * @create 2020-01-04 10:41
 **/
@Data
public class UserBO {
    private String username;

    private String password;

    private String confirmPassword;

}
