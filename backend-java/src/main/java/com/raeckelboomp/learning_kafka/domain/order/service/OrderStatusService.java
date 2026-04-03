package com.raeckelboomp.learning_kafka.domain.order.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.raeckelboomp.learning_kafka.application.order.port.OrderStatusEventPublisher;
import com.raeckelboomp.learning_kafka.domain.order.model.OrderEvent;
import com.raeckelboomp.learning_kafka.domain.order.model.OrderStatusEvent;

@Service
public class OrderStatusService {

    private final OrderStatusEventPublisher orderStatusPublisher;
    private final String[] VALID_STATUSES = { "PROCESSING", "SHIPPING", "TRANSITTING", "DELIVERED" };
    private final String ERROR_STATUS = "ERROR";

    public OrderStatusService(OrderStatusEventPublisher publisher) {
        this.orderStatusPublisher = publisher;
    }

    @Async
    public void processOrderStatus(OrderEvent orderEvent) {
        for (String baseStatus : VALID_STATUSES) {
            try {
                publish(orderEvent, baseStatus);
                if (baseStatus.equals("DELIVERED")) break;
                sleepRandom();
                if (hasError()) {
                    baseStatus += "-" + ERROR_STATUS;
                    publish(orderEvent, baseStatus);
                    break;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("OrderStatusService was interrupted: " + e.getMessage());
                break;
            }
        }
    }

    private void publish(OrderEvent orderEvent, String status) {
        OrderStatusEvent event = new OrderStatusEvent(orderEvent, status);
        orderStatusPublisher.publishOrderStatusEvent(event);
    }

    private boolean hasError() {
        return Math.random() * 20 < 1;
    }

    private void sleepRandom() throws InterruptedException {
        double delayMs = Math.random() * 5000;
        Thread.sleep((long) delayMs);
    }
}
