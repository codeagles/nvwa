package com.codeagles.service;

import com.codeagles.pojo.UserAddress;

import java.util.List;

/**
 * 地址相关服务层
 *
 * @author hcn
 * @create 2020-01-02 17:51
 **/
public interface AddressSerivce {

    public List<UserAddress> queryAll(String userId);
}
