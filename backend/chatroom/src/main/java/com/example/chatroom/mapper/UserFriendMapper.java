package com.example.chatroom.mapper;

import com.example.chatroom.entity.UserFriend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserFriendMapper {
    UserFriend get(@Param("userIdFirst") Long userIdFirst,
                   @Param("userIdSecond") Long userIdSecond);
    int add(UserFriend userFriend);
    int update(UserFriend userFriend);

}
