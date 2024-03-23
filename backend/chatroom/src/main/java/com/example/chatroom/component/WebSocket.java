package com.example.chatroom.component;

import com.example.chatroom.common.JWTUtils;
import com.example.chatroom.common.ResultCode;
import com.example.chatroom.service.UserFriendService;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
@Component
@ServerEndpoint("/websocket/{token}")
public class WebSocket {
    private static UserFriendService userFriendService;
    @Autowired
    public void setUserFriendService(UserFriendService userFriendService) {
        WebSocket.userFriendService = userFriendService;
    }
    private Session session;
    private String name;
    private static ConcurrentHashMap<String,WebSocket> webSocketSet = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, ArrayList<String>> bufferSet = new ConcurrentHashMap<>();
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

        if (bufferSet.get(name) != null) {
            for (String message : bufferSet.get(name)) {
                sendMessage(name,message);
            }
        }
        bufferSet.put(name,new ArrayList<>());

        for ( String other : webSocketSet.keySet()) {
            sendMessage(name,"L/" + other);
        }
        sendAllMessage("L/" + name);
        log.info("[WebSocket] 连接成功，当前连接人数为: {}",webSocketSet.size());
    }
    @OnClose
    public void onClose(){
        if (this.name == null) return;
        webSocketSet.remove(this.name);
        sendAllMessage("E/" + name);
        log.info("[WebSocket] 退出成功，当前连接人数为: {}",webSocketSet.size());
    }
    @OnMessage
    public void onMessage(String message){
        log.info("[WebSocket] 收到消息: {}",message);
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (message.startsWith("G/")) {
            String pureMessage = message.substring(message.indexOf('#') + 1);
            sendAllMessage("M/" + name + "/" + time + "#" + pureMessage);
        } else if(message.startsWith("Q/")) {
            String target = message.substring(message.indexOf('/') + 1);
            boolean online = webSocketSet.get(target) != null;
            if(online) {
                sendMessage(name,"L/" + target);
            } else {
                sendMessage(name,"E/" + target);
            }
        } else if (message.startsWith("F/")) {
            String target = message.substring(message.indexOf('/') + 1);
            userFriendService.confirmFriend(name,target);
            boolean online = webSocketSet.get(target) != null;
            if(online) {
                sendMessage(target,"F/" + name);
            } else {
                bufferSet.computeIfAbsent(target, k -> new ArrayList<>());
                bufferSet.get(target).add("F/" + name);
            }
        } else if (message.startsWith("R/")) {
            String target = message.substring(message.indexOf('/') + 1);
            userFriendService.confirmFriend(name,target);
        } else if (message.equals("ping")) {
            sendMessage(name,"pong");
        } else {
            String pureMessage = message.substring(message.indexOf('#') + 1);
            String to = message.substring(message.indexOf('/') + 1,message.indexOf('#'));
            if (webSocketSet.get(to) == null) {
                bufferSet.computeIfAbsent(to, k -> new ArrayList<>());
                sendMessage(name,"P/" + to + "/" + name + "/" + time + "#" + pureMessage);
                bufferSet.get(to).add("P/" + name + "/" + name + "/" + time + "#" + pureMessage);
            } else {
                sendMessage(name,"P/" + to + "/" + name + "/" + time + "#" + pureMessage);
                sendMessage(to,"P/" + name + "/" + name + "/" + time + "#" + pureMessage);
            }
        }
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
        log.info("[WebSocket] 单点消息: {}",message);
        try {
            webSocketSet.get(name).session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
