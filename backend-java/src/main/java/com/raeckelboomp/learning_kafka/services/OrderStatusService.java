package com.raeckelboomp.learning_kafka.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raeckelboomp.learning_kafka.models.KafkaTopics;
import com.raeckelboomp.learning_kafka.models.OrderEvent;
import com.raeckelboomp.learning_kafka.models.OrderStatusEvent;

@Service
public class OrderStatusService {
    @Autowired
    KafkaProducerService producer;

    @Autowired
    ObjectMapper objectMapper;

    String[] validStatuses = {"PROCESSING", "SHIPPED", "IN_TRANSIT", "DELIVERED"};
    String errorStatus = "ERROR";
    
    @Async
    public void processOrderStatus(OrderEvent orderEvent) {
        for (String status : validStatuses) {
            int randomInt = (int) (Math.random() * 20);
            if (randomInt < 2) {
                status = errorStatus;
            }
            OrderStatusEvent orderStatusEvent = new OrderStatusEvent(orderEvent, status);
            try {
                double delaySeconds = (Math.random() * 5) * 1000.0;
                Thread.sleep((long) delaySeconds);
                String jsonOrderStatusEvent = objectMapper.writeValueAsString(orderStatusEvent);
                producer.sendMessage(KafkaTopics.ORDER_STATUS, jsonOrderStatusEvent);
                if (status.equals(errorStatus)) break;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("OrderStatusService was interrupted: " + e.getMessage());
                break;
            } catch (JsonProcessingException e) {
                System.out.println("Failed to serialize OrderStatusEvent: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
