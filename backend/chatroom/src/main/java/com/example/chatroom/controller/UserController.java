package com.example.chatroom.controller;

import com.example.chatroom.common.R;
import com.example.chatroom.entity.User;
import com.example.chatroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public R<List<User>> getUser(@RequestParam String username) {
        List<User> users = userService.searchByUsername(username);
        return R.success("查询成功",users);
    }
}
