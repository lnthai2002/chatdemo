package com.example.chatdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

//TODO: use only 1 endpoint but check "to" to determine if the message should be broadcasted or pm
/**
 * Messages from client should be delivered to one of the 3 endpoint(input destinations):
 * /chat/{room} : message target the whole room
 * /chat/{room}/{username} : message designated to a user in a room
 * /chat/addUser/{room} : message means to announce a user has join room
 *
 * server will send back messages to client at one of the 2 endpoints (output destinations)
 * /broadcast/{room} : all users in the room will see the message
 * /pm/{room}/{username} : only designated user in a designated room can see the message
 * */
@Controller
public class MessageController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat/{room}")
    @SendTo("/broadcast/{room}")
    public Message broadcast(@DestinationVariable String room,  Message message, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        if (room.equals(message.getRoom())) {
            if (message.getTo() == null || message.getTo().isEmpty()) {
                if (message.getType() == Message.Type.JOIN) {
                    headerAccessor.getSessionAttributes().put("username", message.getFrom());
                    headerAccessor.getSessionAttributes().put("room", message.getRoom());
                }
                return message;
            }
        }
        return null;//TODO: how to not broadcast
    }

    @MessageMapping("/chat/{room}/{username}")
    @SendTo("/pm/{room}/{username}")
    public Message send(@DestinationVariable String room, @DestinationVariable String username, Message message) throws Exception {
        if (room.equals(message.getRoom()) && username.equals(message.getTo())) {
            return message;
        }
        return null;//TODO: how to not broadcast
    }
}
