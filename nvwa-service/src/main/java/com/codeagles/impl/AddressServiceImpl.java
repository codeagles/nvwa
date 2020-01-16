package com.codeagles.impl;

import com.codeagles.mapper.UserAddressMapper;
import com.codeagles.pojo.UserAddress;
import com.codeagles.service.AddressSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 地址服务实现类
 *
 * @author hcn
 * @create 2020-01-03 10:12
 **/
@Service
public class AddressServiceImpl implements AddressSerivce {

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> queryAll(String userId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        return userAddressMapper.select(userAddress);
    }
}
