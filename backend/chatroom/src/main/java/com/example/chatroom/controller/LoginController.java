package com.example.chatroom.controller;

import com.example.chatroom.common.JWTUtils;
import com.example.chatroom.common.PasswordUtils;
import com.example.chatroom.common.R;
import com.example.chatroom.entity.User;
import com.example.chatroom.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
public class LoginController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R<String> login(@RequestBody User userForm){
        String username = userForm.getUsername();
        String password = userForm.getPassword();
        User dbUser = userService.getByUsername(username);
        if (dbUser != null) {
            String dbPassword = dbUser.getPassword();
            String salt = dbUser.getSalt();
            boolean matched = PasswordUtils.matchPassword(password,dbPassword,salt);
            if (matched) {
                String token = JWTUtils.generateToken(username);
                return R.success("登录成功",token);
            } else {
                return R.error("密码不正确",null);
            }
        }
        return R.error("用户名不存在",null);
    }
    @PostMapping("/register")
    public R<String> register(@RequestBody User userForm){
        String username = userForm.getUsername();
        String password = userForm.getPassword();
        String salt = PasswordUtils.generateSalt();
        String encodedPassword = PasswordUtils.encryptPassword(password,salt);
        if (userService.getByUsername(username) == null) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(encodedPassword);
            user.setSalt(salt);
            userService.add(user);
            return R.success("注册成功",null);
        }
        return R.error("用户名已存在",null);
    }
}
