package com.tute.sujia.controller;

import com.tute.sujia.entity.User;
import com.tute.sujia.service.UserService;
import com.tute.sujia.utils.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    UserService userService;


    @RequestMapping(value = "registry",method = RequestMethod.POST)
    @ResponseBody
    public ReturnT<?> registry(@RequestBody User user){
        return userService.doRegistry(user);
    }

    @RequestMapping(value = "login",method = RequestMethod.POST)
    @ResponseBody
    public ReturnT<?> login(@RequestBody User user){
        return userService.doLogin(user);
    }


    @RequestMapping(value = "getAll",method = RequestMethod.GET)
    @ResponseBody
    public ReturnT<?> getAll(){
        return userService.getUsers();
    }

    @RequestMapping(value = "modify",method = RequestMethod.POST)
    @ResponseBody
    public ReturnT<?> modify(@RequestBody User user){
        return userService.modifyAuth(user);
    }

    @RequestMapping(value = "delete",method = RequestMethod.POST)
    @ResponseBody
    public ReturnT<?> modify(@RequestBody String account){
        return userService.delUser(account);
    }

}
