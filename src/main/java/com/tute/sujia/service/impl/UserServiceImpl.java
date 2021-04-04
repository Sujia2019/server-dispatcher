package com.tute.sujia.service.impl;

import com.tute.sujia.dao.UserMapper;
import com.tute.sujia.entity.User;
import com.tute.sujia.service.UserService;
import com.tute.sujia.utils.Constants;
import com.tute.sujia.utils.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Override
    public ReturnT<?> doLogin(User user) {
        if (userMapper.getUserByAccountAndPwd(user) != null) {
            return new ReturnT<>(Constants.SUCCESS);
        } else {
            return new ReturnT<>(Constants.FAIL, "用户名或密码错误");
        }
    }

    @Override
    public ReturnT<?> doRegistry(User user) {
        if (userMapper.getByAccount(user.getUser_account()) != null) {
            return new ReturnT<>(Constants.FAIL, "此账号已存在");
        }
        if (userMapper.getUserByName(user.getUser_name()) != null) {
            return new ReturnT<>(Constants.FAIL, "此名字已存在");
        }
        return new ReturnT<>(Constants.SUCCESS, userMapper.insert(user));
    }

    @Override
    public ReturnT<?> modifyAuth(User user) {
        return new ReturnT<>(Constants.SUCCESS, userMapper.modify(user));
    }

    @Override
    public ReturnT<?> delUser(String userAccount) {
        return new ReturnT<>(Constants.SUCCESS, userMapper.delete(userAccount));
    }

    @Override
    public ReturnT<?> getUsers() {
        return new ReturnT<>(Constants.SUCCESS, userMapper.getAll());
    }
}
