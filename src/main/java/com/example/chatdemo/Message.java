package com.example.chatdemo;

public class Message {
    private String from;
    private String to;
    private String body;
    private String roomName;

    public Message(String from, String to, String body, String roomName) {
        this.from = from;
        this.to = to;
        this.body = body;
        this.roomName = roomName;
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
