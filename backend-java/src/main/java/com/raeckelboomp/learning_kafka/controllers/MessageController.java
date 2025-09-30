package com.raeckelboomp.learning_kafka.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.raeckelboomp.learning_kafka.models.KafkaMessage;

@Controller
public class MessageController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @MessageMapping("/sendMessage")
    public void receiveMessage(KafkaMessage msg) {
        msg.setContent("Backend received: " + msg.getContent());
        this.kafkaTemplate.send(msg.getTopic(), msg.getContent());
    }

}
