package com.example.chatroom.service;

import com.example.chatroom.entity.User;
import com.example.chatroom.entity.UserFriend;
import com.example.chatroom.mapper.UserFriendMapper;
import com.example.chatroom.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserFriendService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserFriendMapper userFriendMapper;

    public UserFriend get(Long userIdFirst,Long userIdSecond) {
        return userFriendMapper.get(userIdFirst,userIdSecond);
    }
    public int add(UserFriend userFriend){return userFriendMapper.add(userFriend);}
    public int update(UserFriend userFriend){return userFriendMapper.update(userFriend);}

    public void confirmFriend(String name, String target) {
        User targetUser = userMapper.getByUsername(target);
        User self = userMapper.getByUsername(name);
        if (targetUser == null || self == null) {
            return;
        }
        UserFriend userFriend1 =
                userFriendMapper.get(self.getUserId(), targetUser.getUserId());
        UserFriend userFriend2 =
                userFriendMapper.get(targetUser.getUserId(), self.getUserId());
        if (userFriend1 == null && userFriend2 == null) {
            UserFriend newUserFriend = new UserFriend();
            newUserFriend.setUserIdFirst(self.getUserId());
            newUserFriend.setUserIdSecond(targetUser.getUserId());
            newUserFriend.setConfirmFirst(true);
            newUserFriend.setConfirmSecond(false);
            userFriendMapper.add(newUserFriend);
        } else if (userFriend1 != null ) {
            userFriend1.setConfirmFirst(true);
            userFriendMapper.update(userFriend1);
        } else {
            userFriend2.setConfirmSecond(true);
            userFriendMapper.update(userFriend2);
        }
    }
    public boolean isFriend(String name,String target) {
        User targetUser = userMapper.getByUsername(target);
        User self = userMapper.getByUsername(name);
        if (targetUser == null || self == null) {
            return false;
        }
        UserFriend userFriend1 =
                userFriendMapper.get(self.getUserId(), targetUser.getUserId());
        UserFriend userFriend2 =
                userFriendMapper.get(targetUser.getUserId(), self.getUserId());
        return userFriend1 != null && userFriend1.isConfirmFirst() && userFriend1.isConfirmSecond()
                || userFriend2 != null && userFriend2.isConfirmFirst() && userFriend2.isConfirmSecond();
    }
}
