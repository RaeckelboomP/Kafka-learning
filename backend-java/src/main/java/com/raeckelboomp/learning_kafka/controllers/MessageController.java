package com.raeckelboomp.learning_kafka.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.raeckelboomp.learning_kafka.models.KafkaMessage;

@Controller
public class MessageController {

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/sendMessage") // corresponds to /app/sendMessage
    public void receiveMessage(KafkaMessage msg) {
        msg.setContent("Backend received: " + msg.getContent());
        template.convertAndSend("/topic/messages", msg);
    }

}
