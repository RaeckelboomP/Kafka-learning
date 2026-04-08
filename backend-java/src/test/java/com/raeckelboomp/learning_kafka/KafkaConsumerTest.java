package com.raeckelboomp.learning_kafka;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raeckelboomp.learning_kafka.application.order.port.ProcessOrderEventUseCase;
import com.raeckelboomp.learning_kafka.application.order.port.ProcessOrderStatusEventUseCase;
import com.raeckelboomp.learning_kafka.domain.order.model.OrderEvent;
import com.raeckelboomp.learning_kafka.domain.order.model.OrderStatusEvent;
import com.raeckelboomp.learning_kafka.infrastructure.messaging.kafka.KafkaConsumer;

public class KafkaConsumerTest {
    @Test
    void should_process_order_use_case() {
        // Given
        ObjectMapper objectMapper = new ObjectMapper()
                .findAndRegisterModules();
        ProcessOrderEventUseCase processOrderEventUseCase = mock(ProcessOrderEventUseCase.class);
        ProcessOrderStatusEventUseCase processOrderStatusEventUseCase = mock(ProcessOrderStatusEventUseCase.class);
        KafkaConsumer kafkaConsumer = new KafkaConsumer(objectMapper, processOrderEventUseCase,
                processOrderStatusEventUseCase);
        UUID orderId = UUID.randomUUID();
        String orderEventJson = """
                    {
                        "orderItem":"pizza",
                        "dateTime":"2023-10-01T12:00:00",
                        "orderId":"%s"
                    }
                """.formatted(orderId);

        // When
        kafkaConsumer.listenOrdersTopic(orderEventJson);

        // Then
        ArgumentCaptor<OrderEvent> captor = ArgumentCaptor.forClass(OrderEvent.class);
        verify(processOrderEventUseCase).processOrderEvent(captor.capture());

        OrderEvent captured = captor.getValue();

        assertEquals("pizza", captured.orderItem());
        assertEquals(orderId, captured.orderId());
    }

    @Test
    void should_process_order_status_use_case() {
        // Given
        ObjectMapper objectMapper = new ObjectMapper()
                .findAndRegisterModules();
        ProcessOrderEventUseCase processOrderEventUseCase = mock(ProcessOrderEventUseCase.class);
        ProcessOrderStatusEventUseCase processOrderStatusEventUseCase = mock(ProcessOrderStatusEventUseCase.class);
        KafkaConsumer kafkaConsumer = new KafkaConsumer(objectMapper, processOrderEventUseCase,
                processOrderStatusEventUseCase);
        UUID orderId = UUID.randomUUID();
        String orderStatusEventJson = """
                    {
                        "status":"PROCESSING",
                        "orderItem":"pizza",
                        "updatedAt":"2023-10-01T12:00:00",
                        "orderId":"%s"
                    }
                """.formatted(orderId);

        // When
        kafkaConsumer.listenOrderStatusTopic(orderStatusEventJson);

        // Then
        ArgumentCaptor<OrderStatusEvent> captor = ArgumentCaptor
                .forClass(OrderStatusEvent.class);
        verify(processOrderStatusEventUseCase).processOrderStatusEvent(captor.capture());
        OrderStatusEvent captured = captor.getValue();
        assertEquals("PROCESSING", captured.status());
        assertEquals("pizza", captured.orderItem());
        assertEquals(orderId, captured.orderId());
    }
}
