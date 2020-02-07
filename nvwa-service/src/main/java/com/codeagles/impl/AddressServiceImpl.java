package com.codeagles.impl;

import com.codeagles.bo.AddressBO;
import com.codeagles.enums.EnumYesOrNo;
import com.codeagles.mapper.UserAddressMapper;
import com.codeagles.pojo.UserAddress;
import com.codeagles.service.AddressSerivce;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> queryAll(String userId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        return userAddressMapper.select(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addNewUserAddress(AddressBO addressBO) {
        Integer isDefault = 0;
        //1.判断当前用户是否存在地址，如果没有，则新增为'默认地址'
        List<UserAddress> userAddresses = this.queryAll(addressBO.getUserId());
        if(userAddresses == null || userAddresses.isEmpty() || userAddresses.size() == 0){
            isDefault =1;
        }
        //2. 保存数据导数据库
        String addressId = sid.nextShort();
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO, userAddress);
        userAddress.setId(addressId);
        userAddress.setIsDefault(isDefault);
        userAddress.setCreatedTime(new Date());
        userAddress.setUpdatedTime(new Date());
        userAddressMapper.insert(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddress(AddressBO addressBO) {
        //1. 保存数据导数据库
        String addressId = addressBO.getAddressId();
        UserAddress updateAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO, updateAddress);

        updateAddress.setId(addressId);
        updateAddress.setUpdatedTime(new Date());
        userAddressMapper.updateByPrimaryKeySelective(updateAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteUserAddress(String userId, String addressId) {
        UserAddress address = new UserAddress();
        address.setUserId(userId);
        address.setId(addressId);
        userAddressMapper.delete(address);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateAddressToBeDefault(String userId, String addressId) {
        //1.查找默认地址
        UserAddress queryAddress = new UserAddress();
        queryAddress.setUserId(userId);
        queryAddress.setIsDefault(EnumYesOrNo.YES.type);
        List<UserAddress> userAddressList = userAddressMapper.select(queryAddress);
        for (UserAddress address : userAddressList) {
            address.setIsDefault(EnumYesOrNo.NO.type);
            userAddressMapper.updateByPrimaryKeySelective(address);
        }

        //2.修改为默认地址
        UserAddress address = new UserAddress();
        address.setUserId(userId);
        address.setId(addressId);
        address.setIsDefault(EnumYesOrNo.YES.type);
        userAddressMapper.updateByPrimaryKeySelective(address);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public UserAddress queryUserAddress(String userId, String addressId) {

        UserAddress singleAddress = new UserAddress();
        singleAddress.setUserId(userId);
        singleAddress.setId(addressId);
        return userAddressMapper.selectOne(singleAddress);

    }
}
