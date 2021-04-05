package com.tute.sujia.service;


import com.tute.sujia.entity.User;
import com.tute.sujia.utils.ReturnT;
import org.springframework.stereotype.Service;


public interface UserService {

    public ReturnT<?> doLogin(User user);
    public ReturnT<?> doRegistry(User user);

    public ReturnT<?> modifyAuth(User user);
    public ReturnT<?> delUser(String userAccount);

    public ReturnT<?> getUsers();

    public ReturnT<?> getInfo(String user_account);
}
