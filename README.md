# kafka-learning
The goal of this project is to learn Kafka by doing.
I will create a very simple application to use kafka's event feature.

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
