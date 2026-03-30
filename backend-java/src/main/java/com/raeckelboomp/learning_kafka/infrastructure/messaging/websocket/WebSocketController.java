package com.raeckelboomp.learning_kafka.infrastructure.messaging.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import com.raeckelboomp.learning_kafka.application.order.port.CreateOrderUseCase;
import com.raeckelboomp.learning_kafka.domain.order.model.OrderEvent;

@Controller
public class WebSocketController {

    private final CreateOrderUseCase createOrderUseCase;

    public WebSocketController(CreateOrderUseCase createOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
    }

    @MessageMapping("/sendMessage")
    public void receiveMessage(String orderItem) {
        createOrderUseCase.createOrder(new OrderEvent(orderItem));
    }

}
