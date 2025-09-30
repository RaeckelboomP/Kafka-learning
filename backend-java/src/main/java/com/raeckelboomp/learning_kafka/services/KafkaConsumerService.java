package com.raeckelboomp.learning_kafka.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.raeckelboomp.learning_kafka.models.KafkaMessage;

@Service
public class KafkaConsumerService {

    @Autowired
    private SimpMessagingTemplate template;

    @KafkaListener(id = "listener1", topics = "topic-1")
    public void listenTopic1(String message) {
        KafkaMessage msg = new KafkaMessage();
        msg.setTopic("topic-1");
        msg.setContent("Received from kafka's topic-1: " + message);
        template.convertAndSend("/topic/messages", msg);
    }

    @KafkaListener(id = "listener2", topics = "topic-2")
    public void listenTopic2(String message) {
        KafkaMessage msg = new KafkaMessage();
        msg.setTopic("topic-2");
        msg.setContent("Received from kafka's topic-2: " + message);
        template.convertAndSend("/topic/messages", msg);
    }

}
