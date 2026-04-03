package com.raeckelboomp.learning_kafka;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.Test;
import com.raeckelboomp.learning_kafka.application.messaging.OrderMessagingPublisher;
import com.raeckelboomp.learning_kafka.application.order.port.OrderEventPublisher;
import com.raeckelboomp.learning_kafka.application.order.service.OrderApplicationService;
import com.raeckelboomp.learning_kafka.domain.order.model.OrderEvent;
import com.raeckelboomp.learning_kafka.domain.order.model.OrderStatusEvent;
import com.raeckelboomp.learning_kafka.domain.order.service.OrderStatusService;

public class OrderApplicationServiceTest {
    @Test
    void should_publish_order_and_trigger_status_processing() {
        // given
        OrderStatusService statusService = mock(OrderStatusService.class);
        OrderMessagingPublisher messaging = mock(OrderMessagingPublisher.class);
        OrderEventPublisher publisher = mock(OrderEventPublisher.class);

        OrderApplicationService service = new OrderApplicationService(statusService, messaging, publisher);

        OrderEvent event = new OrderEvent("pizza");

        // when
        service.processOrderEvent(event);

        // then
        verify(messaging).publishOrder(event);
        verify(statusService).processOrderStatus(event);
    }

    @Test
    void should_publish_order_status() {
        // given
        OrderStatusService statusService = mock(OrderStatusService.class);
        OrderMessagingPublisher messaging = mock(OrderMessagingPublisher.class);
        OrderEventPublisher publisher = mock(OrderEventPublisher.class);
        OrderApplicationService service = new OrderApplicationService(statusService, messaging, publisher);

        OrderEvent orderEvent = new OrderEvent("pizza");
        OrderStatusEvent statusEvent = new OrderStatusEvent(orderEvent, "PROCESSING");
        // when
        service.processOrderStatusEvent(statusEvent);
        // then
        verify(messaging).publishOrderStatus(statusEvent);

    }

    @Test
    void should_publish_order() {
        // given
        OrderStatusService statusService = mock(OrderStatusService.class);
        OrderMessagingPublisher messaging = mock(OrderMessagingPublisher.class);
        OrderEventPublisher publisher = mock(OrderEventPublisher.class);
        OrderApplicationService service = new OrderApplicationService(statusService, messaging, publisher);

        OrderEvent orderEvent = new OrderEvent("pizza");
        // when
        service.createOrder(orderEvent);
        ;
        // then
        verify(publisher).publishOrderEvent(orderEvent);
    }

}
