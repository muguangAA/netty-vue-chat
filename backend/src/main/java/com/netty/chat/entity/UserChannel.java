package com.netty.chat.entity;

import io.netty.channel.Channel;

import java.util.HashMap;

public class UserChannel {

    private static final HashMap<String, Channel> USER_CHANNEL = new HashMap<>();

    public static void put(String userId, Channel channel) {
        USER_CHANNEL.put(userId, channel);
    }

    public static Channel get(String userId) {
        return  USER_CHANNEL.get(userId);
    }
}
