package com.codeagles.impl;

import com.codeagles.mapper.UsersMapper;
import com.codeagles.pojo.Users;
import com.codeagles.service.UserSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

/**
 * 用户服务实现类
 *
 * @author hcn
 * @create 2020-01-03 10:12
 **/
@Service
public class UserServiceImpl implements UserSerivce {

    @Autowired
    private UsersMapper usersMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryByUserName(String name) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",name);
        Users users = usersMapper.selectOneByExample(example);

        return users == null?false:true;
    }
}
