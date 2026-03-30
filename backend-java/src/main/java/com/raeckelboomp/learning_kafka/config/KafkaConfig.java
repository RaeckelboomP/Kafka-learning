package com.raeckelboomp.learning_kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import com.raeckelboomp.learning_kafka.shared.KafkaTopics;

@Configuration
public class KafkaConfig {
    
    @Bean
    public NewTopic orders() {
        return TopicBuilder.name(KafkaTopics.ORDERS)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic orderStatus() {
        return TopicBuilder.name(KafkaTopics.ORDER_STATUS)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
