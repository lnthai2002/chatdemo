package com.example.chatdemo;

import java.util.LinkedList;
import java.util.List;

public class ChatRoom {
    private String name;
    private List<User> members;

    public ChatRoom(String name) {
        this.name = name;
        this.members = new LinkedList<>();
    }
}
