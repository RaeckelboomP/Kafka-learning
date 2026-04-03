package com.raeckelboomp.learning_kafka.application.messaging;

import com.raeckelboomp.learning_kafka.domain.order.model.OrderEvent;
import com.raeckelboomp.learning_kafka.domain.order.model.OrderStatusEvent;

public interface OrderMessagingPublisher {
    void publishOrder(OrderEvent orderEvent);
    void publishOrderStatus(OrderStatusEvent orderStatusEvent);
}
