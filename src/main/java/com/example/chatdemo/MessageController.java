package com.example.chatdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

//TODO: use only 1 endpoint but check "to" to determine if the message should be broadcasted or pm
/**
 * Messages from client should be delivered to (input destinations):
 * /chat
 *
 * server will send back messages to client at one of the 2 endpoints (output destinations)
 * /broadcast/{room} : all users in the room will see the message
 * /pm/{room}/{username} : only designated user in a designated room can see the message
 * */
@Controller
public class MessageController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private RoomService roomService;

    @GetMapping("/admin")
    public String getAdmin(Model model) {
        model.addAttribute("rooms", roomService.getAllRoom());
        return "admin";
    }

    @PostMapping("/createRoom")
    public ModelAndView createRoom(@ModelAttribute ChatRoom room, BindingResult errors, Model model) {
        roomService.addRoom(room.getName());
        return new ModelAndView("redirect:/admin");//using redirect because this is POST and /admin is GET
    }

    @DeleteMapping("/deleteRoom")
    public ModelAndView deleteRoom(@ModelAttribute ChatRoom room, BindingResult errors, Model model) {
        roomService.removeRoom(room.getName());
        return new ModelAndView("redirect:/admin");//using redirect because this is DELETE and /admin is GET
    }

    /***
     * Single input destination handles all messages, messages will be analysed and route to designated room/user
     * JOIN and LEAVE messages are broadcasted to the room only
     */
    @MessageMapping("/chat")
    public void route(Message message, SimpMessageHeaderAccessor headerAccessor) {
        String roomDestination = "/broadcast/" + message.getRoom();
        String userDestination = "/pm/" + message.getRoom() + "/" + message.getTo();

        switch (message.getType()) {
            case JOIN:
                headerAccessor.getSessionAttributes().put("username", message.getFrom());
                headerAccessor.getSessionAttributes().put("room", message.getRoom());
                //TODO: is destination valid? room and user not null? do we care if no one is listening?
                simpMessagingTemplate.convertAndSend(roomDestination, message);
                break;
            case LEAVE:
                //TODO: is destination valid? room and user not null? do we care if no one is listening?
                simpMessagingTemplate.convertAndSend(roomDestination, message);
                break;
            case CHAT:
                if (message.getTo() == null || message.getTo().isEmpty()) {//public chat
                    //TODO: is destination valid? room and user not null? do we care if no one is listening?
                    simpMessagingTemplate.convertAndSend(roomDestination, message);
                }
                else {//private chat
                    //TODO: is destination valid? room and user not null? do we care if no one is listening?
                    simpMessagingTemplate.convertAndSend(userDestination, message);
                }
        }
    }
}
