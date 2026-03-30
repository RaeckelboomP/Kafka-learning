package com.raeckelboomp.learning_kafka.infrastructure.messaging.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raeckelboomp.learning_kafka.application.order.port.OrderEventPublisher;
import com.raeckelboomp.learning_kafka.application.order.port.OrderStatusEventPublisher;
import com.raeckelboomp.learning_kafka.domain.order.model.OrderEvent;
import com.raeckelboomp.learning_kafka.domain.order.model.OrderStatusEvent;
import com.raeckelboomp.learning_kafka.shared.KafkaTopics;

@Service
public class KafkaProducerAdapter implements OrderStatusEventPublisher, OrderEventPublisher {

    @Autowired
    ObjectMapper objectMapper;
    
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerAdapter(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishOrderStatusEvent(OrderStatusEvent orderStatusEvent) {
        try {
            String jsonOrderStatusEvent = objectMapper.writeValueAsString(orderStatusEvent);
            kafkaTemplate.send(KafkaTopics.ORDER_STATUS, jsonOrderStatusEvent);
        } catch (Exception e) {
            System.out.println("Failed to serialize OrderStatusEvent: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void publishOrderEvent(OrderEvent orderEvent) {
        try {
            String jsonOrderEvent = objectMapper.writeValueAsString(orderEvent);
            kafkaTemplate.send(KafkaTopics.ORDERS, jsonOrderEvent);
        } catch (Exception e) {
            System.out.println("Failed to serialize OrderEvent: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
