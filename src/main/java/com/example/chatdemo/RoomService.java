package com.example.chatdemo;

import java.util.Collection;

public interface RoomService {
    ChatRoom getRoom(String name);
    void addRoom(String name);
    void removeRoom(String name);
    boolean doesRoomExist(String name);
    String addUser(String roomName, User user);
    void removeUser(String roomName, String username);
    boolean isUserAvailable(String roomName, String username);
    Collection<ChatRoom> getAllRoom();
}
