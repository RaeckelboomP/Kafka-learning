package com.raeckelboomp.learning_kafka.infrastructure.messaging.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.raeckelboomp.learning_kafka.application.messaging.OrderMessagingPublisher;
import com.raeckelboomp.learning_kafka.domain.order.model.OrderEvent;
import com.raeckelboomp.learning_kafka.domain.order.model.OrderStatusEvent;

@Service
public class WebSocketAdapter implements OrderMessagingPublisher {

    private final SimpMessagingTemplate template;

    public WebSocketAdapter(SimpMessagingTemplate template) {
        this.template = template;
    }

    @SuppressWarnings("null")
    @Override
    public void publishOrder(OrderEvent event) {
        System.out.println("Publishing order event to WebSocket: " + event);
        template.convertAndSend("/topic/orders", event);
    }

    @SuppressWarnings("null")
    @Override
    public void publishOrderStatus(OrderStatusEvent event) {
        template.convertAndSend("/topic/order-status", event);
    }

}
