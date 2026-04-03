package com.raeckelboomp.learning_kafka.shared;

public class WebSocketMessage {
    private String content;
    private String topic;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

}
