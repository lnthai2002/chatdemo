package com.example.chatdemo;

public class Message {
    public enum Type {
        JOIN,
        CHAT,
        LEAVE;
    }

    private String from;
    private String to;
    private String body;
    private String roomName;
    private Type type;
    public Type getType() {
        return type;
    }

    public Message(String from, String to, String body, String roomName, Type type) {
        this.from = from;
        this.to = to;
        this.body = body;
        this.roomName = roomName;
        this.type = type;
    }

    public void setType(Type type) {
        this.type = type;
    }


    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
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
