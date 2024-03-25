package com.example.chatroom.controller;

import com.example.chatroom.common.JWTUtils;
import com.example.chatroom.common.R;
import com.example.chatroom.entity.User;
import com.example.chatroom.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public R<List<User>> searchUser(@RequestParam String username) {
        List<User> users = userService.searchByUsername(username);
        return R.success("查询成功",users);
    }

    @GetMapping("/user/{username}")
    public R<User> getUser(@PathVariable String username) {
        User user = userService.getByUsername(username);
        return R.success("查询成功",user);
    }

    @PostMapping("/user/avatar")
    public R<String> uploadAvatar(@RequestParam("avatar") MultipartFile file, HttpServletRequest request) throws IOException {
        log.info("文件上传");

        String token = request.getHeader("token");
        Claims claims = JWTUtils.verifyToken(token).getClaims();
        User user = userService.getByUsername(claims.getSubject());

        String filename = file.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));

        String newFilename = user.getAvatar() == null ?
                UUID.randomUUID().toString().replace("-","") + suffix :
                user.getAvatar();
        File destFile = new File(System.getProperty("user.dir") +
                "/assets/images/" + newFilename);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        file.transferTo(destFile);
        String url = "http://localhost:8080/images/" + newFilename;

        user.setAvatar(newFilename);
        userService.update(user);
        return R.success("上传成功",url);
    }
}
