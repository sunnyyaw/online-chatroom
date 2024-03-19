package com.example.chatroom.component;

import com.example.chatroom.common.JWTUtils;
import com.example.chatroom.common.ResultCode;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
@Component
@ServerEndpoint("/websocket/{token}")
public class WebSocket {
    private Session session;
    private String name;
    private static ConcurrentHashMap<String,WebSocket> webSocketSet = new ConcurrentHashMap<>();
    @OnOpen
    public void onOpen(Session session, @PathParam(value="token")String token){
        ResultCode verified = JWTUtils.verifyToken(token);
        if (!verified.isSuccess()) {
            log.info("[WebSocketFilter] 验证失败");
            try {
                session.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        log.info("[WebSocketFilter] 验证成功");
        this.session = session;
        this.name = verified.getClaims().getSubject();
        webSocketSet.put(name,this);
        sendAllMessage("L " + name);
        log.info("[WebSocket] 连接成功，当前连接人数为: {}",webSocketSet.size());
    }
    @OnClose
    public void onClose(){
        if (this.name == null) return;
        webSocketSet.remove(this.name);
        sendAllMessage("E " + name);
        log.info("[WebSocket] 退出成功，当前连接人数为: {}",webSocketSet.size());
    }
    @OnMessage
    public void onMessage(String message){
        log.info("[WebSocket] 收到消息: {}",message);
        sendAllMessage("M " + name + ":" + message);
    }
    @OnError
    public void onError(Session session,Throwable t) {
        log.info("[WebSocket] 出现未知错误");
        t.printStackTrace();
    }
    public void sendAllMessage(String message) {
        log.info("[WebSocket] 广播消息: {}",message);
        webSocketSet.values().forEach(webSocket ->
            webSocket.session.getAsyncRemote().sendText(message));
    }
    public void sendMessage(String name, String message) {
        try {
            webSocketSet.get(name).session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
