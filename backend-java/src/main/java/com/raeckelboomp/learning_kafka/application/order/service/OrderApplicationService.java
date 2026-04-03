package com.raeckelboomp.learning_kafka.application.order.service;

import org.springframework.stereotype.Service;
import com.raeckelboomp.learning_kafka.application.messaging.OrderMessagingPublisher;
import com.raeckelboomp.learning_kafka.application.order.port.OrderEventPublisher;
import com.raeckelboomp.learning_kafka.application.order.port.ProcessOrderEventUseCase;
import com.raeckelboomp.learning_kafka.application.order.port.ProcessOrderStatusEventUseCase;
import com.raeckelboomp.learning_kafka.application.order.port.CreateOrderUseCase;
import com.raeckelboomp.learning_kafka.domain.order.model.OrderEvent;
import com.raeckelboomp.learning_kafka.domain.order.model.OrderStatusEvent;
import com.raeckelboomp.learning_kafka.domain.order.service.OrderStatusService;

@Service
public class OrderApplicationService implements CreateOrderUseCase, ProcessOrderEventUseCase, ProcessOrderStatusEventUseCase {

    private final OrderStatusService orderStatusService;
    private final OrderMessagingPublisher orderMessagingPublisher;
    private final OrderEventPublisher orderEventPublisher;

    public OrderApplicationService(OrderStatusService orderStatusService, OrderMessagingPublisher orderMessagingPublisher, OrderEventPublisher orderEventPublisher) {
        this.orderStatusService = orderStatusService;
        this.orderMessagingPublisher = orderMessagingPublisher;
        this.orderEventPublisher = orderEventPublisher;
    }

    @Override
    public void processOrderEvent(OrderEvent orderEvent) {
        orderMessagingPublisher.publishOrder(orderEvent);
        orderStatusService.processOrderStatus(orderEvent);
    }

    @Override
    public void processOrderStatusEvent(OrderStatusEvent orderStatusEvent) {
        orderMessagingPublisher.publishOrderStatus(orderStatusEvent);
    }

    @Override
    public void createOrder(OrderEvent orderEvent) {
        orderEventPublisher.publishOrderEvent(orderEvent);
    }

}
