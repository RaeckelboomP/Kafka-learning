package com.raeckelboomp.learning_kafka.models;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderStatusEvent(String status, String orderItem, LocalDateTime updatedAt, UUID orderId) {

    public OrderStatusEvent(OrderEvent orderEvent, String status) {
        this(status, orderEvent.orderItem(), LocalDateTime.now(), orderEvent.orderId());
    }
}
