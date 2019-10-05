package com.example.chatdemo;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This simple implementation of a RoomService keep room registry on memory
 * */
@Service
public class SimpleRoomService implements RoomService {
    private Map<String, ChatRoom> roomRegistry = new ConcurrentHashMap<>();

    @Override
    public ChatRoom getRoom(String name) {
        return roomRegistry.get(name);
    }

    @Override
    public void addRoom(String name) {
        ChatRoom room = new ChatRoom(name);
        roomRegistry.put(name, room);
    }

    @Override
    public void removeRoom(String name) {
        roomRegistry.remove(name);
    }

    @Override
    public boolean doesRoomExist(String name) {
        return roomRegistry.containsKey(name);
    }

    @Override
    public void addUser(String roomName, User user) {
        //TODO: use optional
        ChatRoom room = roomRegistry.get(roomName);
        if (room != null) {
            room.addUser(user);
        }
    }

    @Override
    public void removeUser(String roomName, String username) {
        //TODO: use optional
        ChatRoom room = roomRegistry.get(roomName);
        if (room != null) {
            room.removeUser(username);
        }
    }

    /**
     * @return true if room exists and user is present in the room, false otherwise*/
    @Override
    public boolean isUserAvailable(String roomName, String username) {
        return Optional.of(roomRegistry.get(roomName))
                .map(r -> r.isUserPresent(username))
                .orElse(false);
    }

    @Override
    public Collection<ChatRoom> getAllRoom() {
        return roomRegistry.values();
    }
}