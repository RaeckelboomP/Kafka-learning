package com.raeckelboomp.learning_kafka.application.order.port;

import com.raeckelboomp.learning_kafka.domain.order.model.OrderEvent;

public interface ProcessOrderEventUseCase {
    void processOrderEvent(OrderEvent orderEvent);
}
