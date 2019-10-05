package com.example.chatdemo;

import org.apache.commons.lang3.RandomStringUtils;

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

    /**
     * This method mutates input*/
    public String addUser(User user) {
        final String[] nickname = {user.getNickname()};//work around non-final variable use in lambda
        boolean retry = true;
        while(retry) {
            if (members.stream().anyMatch(m -> m.getNickname().equals(nickname[0]))) {
                nickname[0] = generateNickname();
            }
            else {
                user.setNickname(nickname[0]);
                members.add(user);
                retry = false;
            }
        }
        return user.getNickname();
    }

    public boolean isUserPresent(String name) {
        return members.stream()
                .anyMatch(m -> m.getNickname().equals(name));
    }

    public void removeUser(String name) {
        members.removeIf(m -> m.getNickname().equals(name));
    }

    private String generateNickname() {
        int length = 10;
        boolean useLetters = true;
        boolean useNumbers = false;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }
}
