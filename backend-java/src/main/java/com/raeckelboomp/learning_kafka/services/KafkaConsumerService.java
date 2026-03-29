package com.raeckelboomp.learning_kafka.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raeckelboomp.learning_kafka.models.KafkaTopics;
import com.raeckelboomp.learning_kafka.models.OrderEvent;

@Service
public class KafkaConsumerService {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private OrderStatusService orderStatusService;

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("null")
    @KafkaListener(id = "orders-listener", topics = KafkaTopics.ORDERS, concurrency = "3")
    public void listenOrdersTopic(String orderEventJson) {
        OrderEvent orderEvent;
        try {
            orderEvent = objectMapper.readValue(orderEventJson, OrderEvent.class);
            orderStatusService.processOrderStatus(orderEvent);
            template.convertAndSend("/topic/orders", orderEventJson);
        } catch (Exception e) {
            System.out.println("Failed to deserialize OrderEvent: " + e.getMessage());
            e.printStackTrace();
            return;
        }
    }

    @SuppressWarnings("null")
    @KafkaListener(id = "order-status-listener", topics = KafkaTopics.ORDER_STATUS, concurrency = "3")
    public void listenOrderStatusTopic(String kafkaOrderStatusEvent) {
        template.convertAndSend("/topic/order-status", kafkaOrderStatusEvent);
    }

}
