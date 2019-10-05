package com.example.chatdemo;

/**
 * An instance of this class represents a presence of a person under an alias (nickname)*/
public class User {
    private String nickname;

    public User(String nickname) {
        this.nickname = nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
