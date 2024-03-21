package com.example.chatroom.interceptor;

import com.example.chatroom.common.JWTUtils;
import com.example.chatroom.common.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) {
        log.info("拦截请求{}",request);
        String token = request.getHeader("token");
        log.info("请求token为{}",token);
        ResultCode resultCode = JWTUtils.verifyToken(token);
        if (resultCode.isSuccess()) {
            log.info("放行请求");
            return true;
        }
        log.info("token验证失败");
        return false;
    }
}
