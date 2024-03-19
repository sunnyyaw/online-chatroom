package com.example.chatroom.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

public class JWTUtils {
    public static long EXPIRE_SECONDS = 3600 * 24;
    private static String SECRET_KEY = "ERJOIT";
    public static String generateToken(String username) {
        String baseToken = UUID.randomUUID().toString();
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        LocalDateTime localDateTimeExpire = localDateTimeNow.plusSeconds(EXPIRE_SECONDS);
        Date from = Date.from(localDateTimeNow.atZone(ZoneId.systemDefault()).toInstant());
        Date expire = Date.from(localDateTimeExpire.atZone(ZoneId.systemDefault()).toInstant());
        String token = Jwts.builder()
                .setId(baseToken)
                .setSubject(username)
                .setNotBefore(from)
                .setExpiration(expire)
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
                .compact();
        return token;
    }
    public static ResultCode verifyToken(String token) {
        ResultCode resultCode = new ResultCode();
        try {
            Claims claims = (Claims) Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parse(token)
                    .getBody();
            resultCode.setSuccess(true);
            resultCode.setClaims(claims);
        }catch (Exception e){
            resultCode.setSuccess(false);
        }
        return resultCode;
    }
}
