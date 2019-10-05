package com.example.chatdemo;

import java.util.LinkedList;
import java.util.List;

public class ChatRoom {
    private String name;
    private List<User> members = new LinkedList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChatRoom(String name) {
        this.name = name;
        this.members = new LinkedList<>();
    }

    public void addUser(User user) {
        members.add(user);
    }

    public boolean isUserPresent(String name) {
        return members.stream()
                .anyMatch(m -> m.getUsername().equals(name));
    }

    public void removeUser(String name) {
        members.removeIf(m->m.getUsername().equals(name));
    }
}
