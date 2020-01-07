package com.codeagles.service;

import com.codeagles.bo.UserBO;
import com.codeagles.pojo.Users;

/**
 * 用户服务层
 *
 * @author hcn
 * @create 2020-01-02 17:51
 **/
public interface UserSerivce {

    public boolean queryByUserName(String name);

    public Users createUser(UserBO userBO);

    public Users queryUserForLogin(String username, String password);
}
