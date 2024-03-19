package com.example.chatroom.common;

import org.springframework.util.DigestUtils;

import java.util.UUID;

public class PasswordUtils {
    public static String generateSalt() {
        String salt = UUID.randomUUID().toString().replace("-","");
        return salt;
    }
    public static String encryptPassword(String password,String salt) {
        String encodedPassword =
                DigestUtils.md5DigestAsHex((salt + password).getBytes());
        return encodedPassword;
    }
    public static boolean matchPassword(String password,String dbPassword,String salt) {
        String encodedPassword = encryptPassword(password,salt);
        return encodedPassword.equals(dbPassword);
    }
}
