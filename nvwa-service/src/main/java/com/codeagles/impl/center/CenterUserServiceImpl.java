package com.codeagles.impl.center;

import com.codeagles.bo.center.CenterUsersBO;
import com.codeagles.mapper.UsersMapper;
import com.codeagles.pojo.Users;
import com.codeagles.service.center.CenterUserService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 用户服务实现类
 *
 * @author hcn
 * @create 2020-01-03 10:12
 **/
@Service
public class CenterUserServiceImpl implements CenterUserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;


    public static final String USER_FACE = "http://img.codeagles.com/5m1AgeRLf3.jpg";

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfo(String userId) {
        Users queryUser = usersMapper.selectByPrimaryKey(userId);
        queryUser.setPassword(null);
        return queryUser;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserInfo(String userId, CenterUsersBO centerUsersBO) {
        Users updateUser = new Users();
        BeanUtils.copyProperties(centerUsersBO, updateUser);
        updateUser.setId(userId);
        updateUser.setUpdateTime(new Date());
        usersMapper.updateByPrimaryKeySelective(updateUser);
        return queryUserInfo(userId);
    }
}
