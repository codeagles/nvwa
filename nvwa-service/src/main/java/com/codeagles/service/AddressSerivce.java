package com.codeagles.service;

import com.codeagles.pojo.UserAddress;
import com.codeagles.bo.AddressBO;

import java.util.List;

/**
 * 地址相关服务层
 *
 * @author hcn
 * @create 2020-01-02 17:51
 **/
public interface AddressSerivce {

    public List<UserAddress> queryAll(String userId);

    /**
     * 新增收货人地址
     * @param addressVO
     */
    public void addNewUserAddress(AddressBO addressVO);

    /**
     * 修改收货人地址
     * @param addressVO
     */
    public void updateUserAddress(AddressBO addressVO);

    /**
     * 删除地址 根据用户id与地址id
     * @param userId
     * @param addressId
     */
    public void deleteUserAddress(String userId, String addressId);

    /**
     * 根据用户id与地址id 修改地址为默认地址
     * @param userId
     * @param addressId
     */
    public void updateAddressToBeDefault(String userId, String addressId);


    public UserAddress queryUserAddress(String userId, String addressId);
}
