package com.example.chatroom.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private Long userId;

    private String avatar;
    private String username;
    private String password;
    private String salt;
}
