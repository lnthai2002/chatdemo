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

//TODO: use only 1 endpoint but check "to" to determine if the message should be broadcast or pm
/**
 * Messages from client should be delivered to (input destinations):
 * /chat
 * <br/>
 * Server will send back messages to client at one of the 2 endpoints (output destinations)
 * /broadcast/{room} : all users in the room will see the message
 * /pm/{room}/{nickname} : only designated user in a designated room can see the message
 * */
@Controller
public class MessageController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private RoomService roomService;

    /**
     * Show page where user select room and pick a nickname*/
    @GetMapping("/")
    public String getRoot(Model model) {
        model.addAttribute("rooms", roomService.getAllRoom());
        return "selectRoom";
    }

    /**
     * Create a user in the selected room and show the chat page
     * If the nickname user selected is conflicting with another user, a random nickname will be assigned
     * If room has been deleted right after user selected, return user to the room selection page*/
    @PostMapping("/join")
    public String createUser(@ModelAttribute Participation participation, BindingResult errors, Model model) {
        ChatRoom room = roomService.getRoom(participation.getRoom());
        if (room != null) {
            User user = new User(participation.getNickname());;
            roomService.addUser(room.getName(), user);

            model.addAttribute(WsSessionAttributes.ROOM, room.getName());
            model.addAttribute(WsSessionAttributes.NICKNAME, user.getNickname());
            return "index";
        }
        model.addAttribute("rooms", roomService.getAllRoom());
        return "selectRoom";
    }

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
     * JOIN and LEAVE messages are broadcast to the room only
     * Message will be dropped if recipient (room/user) is not available
     * If recipient (room/user) is deleted while message being process we can't do anything about it
     */
    @MessageMapping("/chat")
    public void route(Message message, SimpMessageHeaderAccessor headerAccessor) {
        ChatRoom room = roomService.getRoom(message.getRoom());
        if(room != null) {
            String roomDestination = "/broadcast/" + message.getRoom();
            String userDestination = "/pm/" + message.getRoom() + "/" + message.getTo();
            switch (message.getType()) {
                case JOIN:
                    //associate room and nickname to ws session so I broadcast one last message when ws disconnected
                    headerAccessor.getSessionAttributes().put("nickname", message.getFrom());
                    headerAccessor.getSessionAttributes().put("room", message.getRoom());


                    simpMessagingTemplate.convertAndSend(roomDestination, message);
                    break;
                case LEAVE:
                    roomService.removeUser(room.getName(), message.getFrom());

                    simpMessagingTemplate.convertAndSend(roomDestination, message);
                    break;
                case CHAT:
                    if (message.getTo() == null || message.getTo().isEmpty()) {//public chat
                        simpMessagingTemplate.convertAndSend(roomDestination, message);
                    }
                    else {//private chat
                        if (room.isUserPresent(message.getTo())){
                            simpMessagingTemplate.convertAndSend(userDestination, message);
                        }
                    }
                    break;
            }
        }
        //dont route messages if room doesnt exist
    }
}
