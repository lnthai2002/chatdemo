package com.example.chatdemo;

import java.util.Collection;

public interface RoomService {
    ChatRoom getRoom(String name);
    void addRoom(String name);
    void removeRoom(String name);
    boolean doesRoomExist(String name);
    /**
     * Add user to room, if room does not exist, throw runtime exception
     * The given user may be modified before adding
     * @return the user added*/
    User addUser(String roomName, User user);
    void removeUser(String roomName, String nickname);
    boolean isUserAvailable(String roomName, String nickname);
    Collection<ChatRoom> getAllRoom();
}
