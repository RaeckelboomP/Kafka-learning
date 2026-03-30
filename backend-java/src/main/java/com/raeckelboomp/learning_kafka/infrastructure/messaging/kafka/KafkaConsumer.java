package com.raeckelboomp.learning_kafka.infrastructure.messaging.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raeckelboomp.learning_kafka.application.order.port.ProcessOrderEventUseCase;
import com.raeckelboomp.learning_kafka.application.order.port.ProcessOrderStatusEventUseCase;
import com.raeckelboomp.learning_kafka.domain.order.model.OrderEvent;
import com.raeckelboomp.learning_kafka.domain.order.model.OrderStatusEvent;
import com.raeckelboomp.learning_kafka.shared.KafkaTopics;

@Service
public class KafkaConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    private final ProcessOrderEventUseCase processOrderEventUseCase;
    private final ProcessOrderStatusEventUseCase processOrderStatusEventUseCase;

    public KafkaConsumer(ProcessOrderEventUseCase processOrderEventUseCase, ProcessOrderStatusEventUseCase processOrderStatusEventUseCase) {
        this.processOrderEventUseCase = processOrderEventUseCase;
        this.processOrderStatusEventUseCase = processOrderStatusEventUseCase;
    }

    @KafkaListener(id = "orders-listener", topics = KafkaTopics.ORDERS, concurrency = "3")
    public void listenOrdersTopic(String orderEventJson) {
        OrderEvent orderEvent;
        try {
            orderEvent = objectMapper.readValue(orderEventJson, OrderEvent.class);
            processOrderEventUseCase.processOrderEvent(orderEvent);
        } catch (Exception e) {
            System.out.println("Failed to deserialize OrderEvent: " + e.getMessage());
            e.printStackTrace();
            return;
        }
    }

    @KafkaListener(id = "order-status-listener", topics = KafkaTopics.ORDER_STATUS, concurrency = "3")
    public void listenOrderStatusTopic(String kafkaOrderStatusEvent) {
        OrderStatusEvent orderStatusEvent;
        try {
            orderStatusEvent = objectMapper.readValue(kafkaOrderStatusEvent, OrderStatusEvent.class);
            processOrderStatusEventUseCase.processOrderStatusEvent(orderStatusEvent);
        } catch (Exception e) {
            System.out.println("Failed to deserialize OrderStatusEvent: " + e.getMessage());
            e.printStackTrace();
            return;
        }
    }

}
