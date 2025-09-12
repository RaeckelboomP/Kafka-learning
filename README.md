# kafka-learning
The goal of this project is to learn Kafka by doing.
I will create a very simple notification application to use kafka's features.

## Architecture

- Java application (kafka producer and consumer)
- Kafka broker
- Angular application (shows Java messages)

To test Kafka's capabilities, the Angular application will have an input and two buttons :
- Send to topic 1
- Send to topic 2

A click on each of these buttons will send the message to Java via WebSocket, which store them in their respective topics in Kafka.
The Java application will listen to Kafka as a consumer, decorate the message and send it back to the angular application through the WebSocket.

## How to launch the application

### Only the java backend

- Open a terminal
- ```bash
  cd learning-kafka-java
  run spring-boot:run
  ```