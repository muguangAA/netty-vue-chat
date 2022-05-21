package com.netty.chat.controller;

import com.netty.chat.common.Result;
import com.netty.chat.service.UserService;
import com.netty.chat.entity.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @RequestMapping("/login")
    public Result<User> login(String username, String password){
        return new Result<>(userService.selectOne(username,password));
    }

    @RequestMapping("/list")
    public Result<List<User>> userList(){
        return new Result<>(userService.userList());
    }
}
