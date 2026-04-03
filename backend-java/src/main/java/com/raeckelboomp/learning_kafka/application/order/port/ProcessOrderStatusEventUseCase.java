package com.raeckelboomp.learning_kafka.application.order.port;

import com.raeckelboomp.learning_kafka.domain.order.model.OrderStatusEvent;

public interface ProcessOrderStatusEventUseCase {
    void processOrderStatusEvent(OrderStatusEvent orderStatusEvent);
}
