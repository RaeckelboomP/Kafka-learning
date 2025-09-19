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

## How to use kafka

Use the command ```bash docker compose up -d```
To create the topics : 
```bash 
docker exec -it broker /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --topic topic-1 --partitions 3 --replication-factor 1

docker exec -it broker /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --topic topic-2 --partitions 3 --replication-factor 1
```
To list the topics :
```bash
docker exec -it broker /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --list
```