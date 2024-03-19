package com.example.chatroom.common;

import io.jsonwebtoken.Claims;
import lombok.Data;

@Data
public class ResultCode {
    private boolean success;
    private Claims claims;
}
