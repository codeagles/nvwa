package com.codeagles.service.center;

import com.codeagles.bo.center.CenterUsersBO;
import com.codeagles.pojo.Users;

/**
 * 用户服务层
 *
 * @author hcn
 * @create 2020-01-02 17:51
 **/
public interface CenterUserService {
    /**
     * 根据用户id查询用户
     * @param userId
     * @return
     */
    public Users queryUserInfo(String userId);

    /**
     * 修改用户信息
     * @param userId
     * @param centerUsersBO
     * @return
     */
    public Users updateUserInfo(String userId, CenterUsersBO centerUsersBO);
}
