package com.example.chatroom.mapper;

import com.example.chatroom.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    User getByUsername(String username);
    List<User> searchByUsername(String username);
    List<User> all();
    int add(User user);
    int remove(Long userId);
}
