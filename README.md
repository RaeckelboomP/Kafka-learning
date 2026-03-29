# kafka-learning
The goal of this project is to simulate an order handling system based on an event-driven architecture with Kafka and WebSockets.
This project use **Java 21**, **Angular 20**, **Kafka**, **DevContainer** and **Docker**.

## Architecture

- Java application (kafka producer and consumer)
- Kafka broker
- Angular application (shows Java messages)

To test Kafka's capabilities, the Angular application will have one button to create an order, and a list of orders and their status.

The backend will receive the order and create a message in kafka's orders topic, the consumer will then receive this message and create a message in kafka's order-status topic, and periodically update the status **PROCESSING → SHIPPED → IN_TRANSIT → DELIVERED**. Each order update is sent by websocket to the frontend to display it in the list.

## How to launch the application

### Environment

You can either use the [Dev container extension](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) to get an environment able to launch the application, or install the required tools :

- Java 21
- Maven 3.8.7
- Angular 20.3.2
- npm 10.8.2

### Kafka

- Open a terminal at the root of the project
- ```bash
  docker compose up -d
  ```

### Java backend

- Open a terminal
- ```
  cd backend-java
  mvn spring-boot:run
  ```

### Frontend

- Open a new terminal
- ```bash

  cd frontend
  ng serve --host 0.0.0.0 --port 4200
  ```

## How to use kafka

Use the command ```docker compose up -d```
To create the topics : 
```bash 
docker exec -it broker /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --topic topic-1 --partitions 3 --replication-factor 1

docker exec -it broker /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --topic topic-2 --partitions 3 --replication-factor 1
```
To list the topics :
```bash
docker exec -it broker /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --list
```
To delete a topic :
```bash
docker exec -it broker /opt/kafka/bin/kafka-topics.sh \
  --bootstrap-server localhost:9092 \
  --delete --topic topic-1
```
This kafka container persist data between launches, to load new configurations, use ```docker compose down -v```.

To check the configurations use ```docker exec -it broker env | grep KAFKA```.

### Notes on the configuration

In the docker-compose for kafka :
```KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://host.docker.internal:9092```
KAFKA_ADVERTISED_LISTENERS is the address advertised by kafka to the listeners.
We use ```host.docker.internal:9092``` to reach the port 9092 within the docker internal network.
Because I'm working in a devcontainer, the two containers are in different networks, so I must use the internal docker network to reach kafka.

In the java application.properties :
```spring.kafka.bootstrap-servers=host.docker.internal:9092```
We simply tell the address of kafka. We cannot use kafka's DNS "broker" because it is not known inside the devcontainer.
