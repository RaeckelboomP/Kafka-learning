package com.raeckelboomp.learning_kafka.domain.order.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderEvent(String orderItem, LocalDateTime dateTime, UUID orderId) {
    public OrderEvent(String orderItem) {
        this(orderItem, LocalDateTime.now(), UUID.randomUUID());
    }
}
