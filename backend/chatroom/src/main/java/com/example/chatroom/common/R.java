package com.example.chatroom.common;

import lombok.Data;

@Data
public class R<T>{
    private int code;
    private String message;
    private T data;
    public R(int code,String message,T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public static <T> R<T> success(String message, T data){
        return new R<>(200,message,data);
    }
    public static <T> R<T> error(String message, T data){
        return new R<>(500,message,data);
    }
}
