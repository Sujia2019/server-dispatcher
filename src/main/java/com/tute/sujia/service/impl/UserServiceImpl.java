package com.tute.sujia.service.impl;

import com.tute.sujia.entity.User;
import com.tute.sujia.service.UserService;
import com.tute.sujia.utils.ReturnT;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public ReturnT<?> doLogin(User user) {
        return null;
    }

    @Override
    public ReturnT<?> doRegistry(User user) {
        return null;
    }

    @Override
    public ReturnT<?> modifyAuth(User user) {
        return null;
    }

    @Override
    public ReturnT<?> delUser(String userAccount) {
        return null;
    }

    @Override
    public ReturnT<?> getUsers() {
        return null;
    }
}
