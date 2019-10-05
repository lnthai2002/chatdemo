package com.example.chatdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

//TODO: use only 1 endpoint but check "to" to determine if the message should be broadcasted or pm
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
        //simpMessagingTemplate.convertAndSend("/topic/fleet/" + fleetId, new Simple(fleetId, driverId));
        return null;//TODO: how to not broadcast
    }

    @MessageMapping("/chat/{room}/{username}")
    @SendTo("/pm/{room}/{username}")
    public Message send(Message message) throws Exception {
        Thread.sleep(1000); // simulated delay
        if (message.getTo() != null && !message.getTo().isEmpty()) {
            return message;
        }
        //simpMessagingTemplate.convertAndSend("/topic/fleet/" + fleetId, new Simple(fleetId, driverId));
        return null;//TODO: how to not broadcast
    }
}
