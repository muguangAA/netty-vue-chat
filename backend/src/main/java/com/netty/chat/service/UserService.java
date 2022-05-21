package com.netty.chat.service;

import com.netty.chat.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserService {

    public static final Map<String, User> USER_MAP = new HashMap<>();

    public UserService() {
        USER_MAP.put("xiaoming", new User("xiaoming", "123456", 1, null));
        USER_MAP.put("xiaogang", new User("xiaogang", "123456", 2, null));
        USER_MAP.put("xiaohong", new User("xiaohong", "123456", 3, null));
        USER_MAP.put("xiaoguang", new User("xiaoguang", "123456", 4, null));
    }

    public User selectOne(String username, String password) {
        User user = USER_MAP.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        } else {
            return null;
        }
    }

    public List<User> userList() {
        return new ArrayList<>(USER_MAP.values());
    }
}
