package com.raeckelboomp.learning_kafka.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.raeckelboomp.learning_kafka.models.WebSocketMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raeckelboomp.learning_kafka.models.KafkaTopics;
import com.raeckelboomp.learning_kafka.models.OrderEvent;
import com.raeckelboomp.learning_kafka.services.KafkaProducerService;

@Controller
public class MessageController {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private ObjectMapper objectMapper;

    @MessageMapping("/sendMessage")
    public void receiveMessage(String orderItem) {
        System.out.println(String.format("Received WebSocket message: %s", orderItem));
        OrderEvent orderEvent = new OrderEvent(orderItem);
        String jsonOrderEvent;
        try {
            jsonOrderEvent = objectMapper.writeValueAsString(orderEvent);
        } catch (JsonProcessingException e) {
            System.out.println("Failed to serialize OrderEvent: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        System.out.println("Created order event: " + jsonOrderEvent);
        this.kafkaProducerService.sendMessage(KafkaTopics.ORDERS, jsonOrderEvent);
    }

}
