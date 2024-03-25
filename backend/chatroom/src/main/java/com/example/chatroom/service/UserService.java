package com.example.chatroom.service;

import com.example.chatroom.entity.User;
import com.example.chatroom.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    public User getByUsername(String username) {return userMapper.getByUsername(username);}
    public List<User> searchByUsername(String username){return userMapper.searchByUsername(username);}
    public List<User> all(){
        return userMapper.all();
    }
    public int add(User user){
        return userMapper.add(user);
    }
    public int update(User user){return userMapper.update(user);}
    public int remove(Long userId){
        return userMapper.remove(userId);
    }
}
