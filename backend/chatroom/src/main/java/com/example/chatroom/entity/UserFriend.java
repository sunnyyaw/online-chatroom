package com.example.chatroom.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserFriend implements Serializable {
    private Long userIdFirst;
    private Long userIdSecond;
    private boolean confirmFirst;
    private boolean confirmSecond;
}
