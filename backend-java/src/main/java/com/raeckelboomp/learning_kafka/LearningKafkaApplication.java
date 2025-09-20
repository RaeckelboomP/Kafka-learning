package com.raeckelboomp.learning_kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;

@SpringBootApplication
public class LearningKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearningKafkaApplication.class, args);
    }

    @Bean
    public NewTopic topic1() {
        return TopicBuilder.name("topic-1")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topic2() {
        return TopicBuilder.name("topic-2")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @KafkaListener(id = "listener1", topics = "topic-1")
    public void listenTopic1(String message) {
        System.out.println("Received from topic-1: " + message);
        // do something specific for topic-1
    }

    @KafkaListener(id = "listener2", topics = "topic-2")
    public void listenTopic2(String message) {
        System.out.println("Received from topic-2: " + message);
        // do something specific for topic-2
    }

}
