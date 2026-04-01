package com.raeckelboomp.learning_kafka;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import com.raeckelboomp.learning_kafka.application.order.port.OrderStatusEventPublisher;
import com.raeckelboomp.learning_kafka.domain.order.model.OrderEvent;
import com.raeckelboomp.learning_kafka.domain.order.service.OrderStatusService;

public class OrderStatusServiceTest {
    @Test
    void should_publish_some_statuses() {
        OrderStatusEventPublisher publisher = mock(OrderStatusEventPublisher.class);

        OrderStatusService service = new OrderStatusService(publisher);

        service.processOrderStatus(new OrderEvent("pizza"));

        verify(publisher, atLeastOnce()).publishOrderStatusEvent(any());
    }
}
