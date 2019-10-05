package com.example.chatdemo;

/**
 * from is required
 * room is required
 * type is required
 * missing "to" implies the message is for everyone
 * */
public class Message {
    public enum Type {
        JOIN,
        CHAT,
        LEAVE;
    }

    private String from;
    private String to;
    private String body;
    private String room;
    private Type type;

    public Message(String from, String room, Type type) {
        this.from = from;
        this.room = room;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }


    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
