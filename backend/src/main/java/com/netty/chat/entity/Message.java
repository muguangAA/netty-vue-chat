package com.netty.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String event;
    private String from;
    private String to;
    private String username;
    private String type;
    private String data;
}
