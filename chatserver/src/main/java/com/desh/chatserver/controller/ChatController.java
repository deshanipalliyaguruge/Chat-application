package com.desh.chatserver.controller;

import com.desh.chatserver.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.desh.chatserver.model.Status;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message")  // /app/message
    @SendTo("/chatroom/public")
    public Message receivePublicMessage(@Payload Message message){
        // Handle the JOIN message appropriately
        if (message.getStatus() == Status.JOIN) {
            // Only process the senderName and status for JOIN
            return new Message(message.getSenderName(), null, null, null, Status.JOIN);
        }
        return message;
    }

    @MessageMapping("/private-message")
    public void receivePrivateMessage(@Payload Message message){
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message); // /user/{username}/private
        System.out.println(message.toString());
    }
}
