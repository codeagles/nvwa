package com.codeagles.impl;

import com.codeagles.bo.UserBO;
import com.codeagles.enums.EnumSex;
import com.codeagles.mapper.UsersMapper;
import org.n3r.idworker.Sid;
import com.codeagles.pojo.Users;
import com.codeagles.service.UserSerivce;
import com.codeagles.utils.DateUtils;
import com.codeagles.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

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

    @Autowired
    private Sid sid;


    public static final String USER_FACE = "http://img.codeagles.com/5m1AgeRLf3.jpg";

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryByUserName(String name) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",name);
        Users users = usersMapper.selectOneByExample(example);

        return users == null?false:true;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users createUser(UserBO userBO) {
        String userId = sid.nextShort();

        Users user = new Users();
        user.setId(userId);
        user.setUsername(userBO.getUsername());
        try {
            user.setPassword(MD5Utils.getMD5Str(userBO.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //设置默认昵称
        user.setNickname(userBO.getUsername());
        //设置默认头像
        user.setFace(USER_FACE);
        //设置默认生日
        user.setBirthday(DateUtils.stringToDate("1970-01-01"));
        //设置默认性别为保密
        user.setSex(EnumSex.secret.type);

        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());

        usersMapper.insert(user);
        return user;
    }
}
